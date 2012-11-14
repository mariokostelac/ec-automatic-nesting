package ecf.genotype;

import java.util.Random;
import java.util.Vector;
import ecf.Individual;
import ecf.State;

/**
 * Razred koji sadrži vektor operatora mutacije
 * i rukuje mutacijom jedinki.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Mutation {

    /**Vektor operatora mutacije za svaki genotip*/
    private Vector<Vector<MutationOp>> operators;
    
    /**Vjerojatnosti mutacije operatora mutacije*/
	private Vector<Vector<Double>> opProb;
	
	/**Stanje algoritma*/
	private State state;
	
	/**Označava koji se genotipi križaju*/
	private String mutGenotypes;

	/**Vjerojatnost mutiranja jedinke*/
	private double indMutProb;	
	
	/**Vjerojatnost mutiranja gena jedinke*/
	private double geneMutProb;
	
	/**
	 * Konstruktor.
	 * 
	 * @param state State objekt
	 */
	public Mutation(State state) {
		this.state = state;
		operators = new Vector<Vector<MutationOp>>();
		opProb = new Vector<Vector<Double>>();
	}
	
	
	/**
     * Metoda koja inicijalizira mutaciju.
     */
    public void initialize(){
    	indMutProb = Double.parseDouble(state.getRegistry().getEntry("mutation.indprob"));
    	geneMutProb = Double.parseDouble(state.getRegistry().getEntry("mutation.geneprob"));
    	if(!state.getRegistry().isModified("mutation.geneprob")) geneMutProb = 0;
    	
    	mutGenotypes = state.getRegistry().getEntry("mutation.genotypes");
    	if(!mutGenotypes.equalsIgnoreCase("random") && !mutGenotypes.equalsIgnoreCase("all")){
    		state.getLogger().log(1, "Warning: invalid parameter value (key: mutation.genotypes)");
    	}
        
    	// inicijaliziraj operatore za sve genotipove
    	for(int genotip=0;genotip<operators.size();genotip++){
    		int brojOperatora = operators.get(genotip).size();
    		
    		for(int i=0;i<brojOperatora; i++) {
    			operators.get(genotip).get(i).initialize();	//state referencu je dobio u konstruktoru
    		}
    		
    		// izračunaj kumulativne operatore vjerojatnosti
    		Vector<Double> probs = new Vector<Double>(brojOperatora);
    		probs.add(operators.get(genotip).get(0).getProbability());
    		
    		for(int i=1;i<brojOperatora;i++){
    			//probs.set(i, probs.get(i-1) + operators.get(genotip).get(i).getProbability());
    			probs.add(probs.get(i-1) + operators.get(genotip).get(i).getProbability());
    		}
    		
    		if(probs.get(brojOperatora-1)==0){
    			probs.set(0, -1.);	//da bude kao u križanju, u C++ verziji je ovo malo drukčije
    		}
    		else{
    			if(probs.get(brojOperatora-1)!=1){
    				double normal = probs.get(brojOperatora-1);
    				state.getLogger().log(1, "Warning: " + 
    						operators.get(genotip).get(0).myGenotype.getName() +
    						" mutation operators: cumulative probability not equal to 1 " +
    						"(sum = " + normal + ")");
    				for(int i=0;i<probs.size();i++){
    					probs.set(i, probs.get(i)/normal);
    				}
    			}
    		}
                opProb.add(probs);
    	}
    }
	
    
    /**
     * Izvodi mutaciju nad bazenom jedinki.
     * Vjerojatnost mutacije je definirana sa 
     * parametrima geneMutProb i indMutProb:
     * ako je geneMutProb definiran u konfiguracijskoj
     * datoteci upotrebljava njega, a inače indMutProb.
     * 
     * @return Broj mutiranih jedinki
     */
    public int mutation(Vector<Individual> pool){
    	int mutated = 0;
        Random random = state.getRandomizer();
    	for(int i=0;i<pool.size();i++){
    		//dodati koristenje vjerojatnosti mutacije gena (geneMutProb)
    		if(random.nextDouble() <= indMutProb) {
    			mutated++;
    			mutate(pool.get(i));
    		}
    	}

    	return mutated;
    }
    
    
    /**
     * Metoda koja mutira jedinku.
     * 
     * @param ind Jedinka koja se mutira
     */
    public void mutate(Individual ind) {
    	ind.getFitness().setValid(false);
    	
        Random random = state.getRandomizer();
        
    	if(mutGenotypes.equalsIgnoreCase("random")) {
    		int iGenotype = random.nextInt(ind.size());
    		// choose operator
    		int iOperator;
    		
    		if(opProb.get(iGenotype).get(0)<0){
    			iOperator = random.nextInt(operators.get(iGenotype).size());
    		}
    		else{
    			double slucajni = random.nextDouble();
    			iOperator = 0;
    			while(opProb.get(iGenotype).get(iOperator)<slucajni){
    				iOperator++;
    			}
    		}
    		operators.get(iGenotype).get(iOperator).mutate(ind.get(iGenotype));
    	}
    	else if(mutGenotypes.equalsIgnoreCase("all")) {
    		for(int iGenotype=0; iGenotype<ind.size();iGenotype++) {
    			// choose operator
    			int iOperator;
    			if(opProb.get(iGenotype).get(0)<0){
    				iOperator = random.nextInt(operators.get(iGenotype).size());
    			}
    			else{
    				double slucajni = random.nextDouble();
    				iOperator = 0;
    				while(opProb.get(iGenotype).get(iOperator)<slucajni){
    					iOperator++;
    				}
    			}
    			operators.get(iGenotype).get(0).mutate(ind.get(iGenotype));
    		}
    	}
    }

    
    /**
     * Metoda koja registrira sve potrebne parametre
     * za mutaciju.
     */
    public void registerParameters(){
    	state.getRegistry().registerEntry("mutation.indprob", "0.3");
    	state.getRegistry().registerEntry("mutation.geneprob", "0.01");
    	state.getRegistry().registerEntry("mutation.genotypes", "random");
    }
    
    
    /**
     * Metoda koja vraća sve definirane operatore
     * mutacije.
     * 
     * @return Popis operatora mutacije
     */
    public Vector<Vector<MutationOp>> getOperators() {
        return operators;
    }

    
    /**
     * Metoda koja dodaje operator mutacije
     * 
     * @param operator Operator križanja
     * @param genotypeID ID genotipa čiji se operator križanja dodaje
     */
    public void addOperator(MutationOp operator, int genotypeID){
    	if(operators.size()==genotypeID){			//ako je ovo prvi operator novog genotipa
    		Vector<MutationOp> mutacije = new Vector<MutationOp>();
    		mutacije.add(operator);
    		operators.add(mutacije);
    	}
    	else{										//ako nije prvi operator
    		operators.get(genotypeID).add(operator);
    	}
    }


	/**
	 * Metoda koja vraća vjerojatnost mutiranja gena.
	 * 
	 * @return Vjerojatnost mutiranja gena
	 */
	public double getGeneMutProb() {
		return geneMutProb;
	}
}
