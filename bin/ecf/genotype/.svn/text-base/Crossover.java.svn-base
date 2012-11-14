package ecf.genotype;

import java.util.Random;
import java.util.Vector;
import ecf.Individual;
import ecf.State;

/**
 * Razred koji sadrži vektor operatora križanja
 * i rukuje križanjem jedinki.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Crossover {

    /**Vektor operatora križanja za svaki genotip*/
    private Vector<Vector<CrossoverOp>> operators;
    /**Vjerojatnosti križanja operatora križanja*/
    private Vector<Vector<Double>> opProb;
    /**Stanje algoritma*/
    private State state;
    /**Označava koji se genotipi križaju*/
    private String crxGenotypes;

    /**
     * Konstruktor.
     *
     * @param state State objekt
     */
    public Crossover(State state) {
        this.state = state;
        operators = new Vector<Vector<CrossoverOp>>();
        opProb = new Vector<Vector<Double>>();
    }

    /**
     * Metoda za inicijalizaciju križanja.
     */
    public void initialize() {
        crxGenotypes = state.getRegistry().getEntry("crossover.genotypes");
        if (!crxGenotypes.equalsIgnoreCase("random") && !crxGenotypes.equalsIgnoreCase("all")) {
            state.getLogger().log(1, "Warning: invalid parameter value (key: crossover.genotypes)");
            crxGenotypes = "random";		//pretpostavljena vrijednost
        }

        for (int genotip = 0; genotip < operators.size(); genotip++) {
            int brojOperatora = operators.get(genotip).size();
            for (int i = 0; i < brojOperatora; i++) {
                operators.get(genotip).get(i).initialize();	//state referencu je dobio u konstruktoru
            }
            Vector<Double> probs = new Vector<Double>(brojOperatora);

            probs.add(operators.get(genotip).get(0).getProbability());
            for (int i = 1; i < brojOperatora; i++) {
                probs.add(probs.get(i - 1) + operators.get(genotip).get(i).getProbability());
            }

            if (probs.get(brojOperatora - 1) == 0) {
                probs.set(0, -1.);
            } else if (probs.get(brojOperatora - 1) != 1) {
                double normal = probs.get(brojOperatora - 1);
                state.getLogger().log(1, "Warning: " +
                        operators.get(genotip).get(0).getMyGenotype().getName() +
                        " crossover operators: cumulative probability not equal to 1 (sum = " +
                        +normal + ")");
                for (int i = 0; i < probs.size(); i++) {
                    probs.set(i, probs.get(i) / normal);
                }
            }
            opProb.add(probs);
        }
    }

    /**
     * Metoda za registraciju parametara.
     * Poziva se prije metode za inicijalizaciju,
     * a jedini parametar koji koristi označava
     * koji se genotipovi križaju (default: random).
     */
    public void registerParameters() {
        state.getRegistry().registerEntry("crossover.genotypes", "random");
    }

    /**
     * Metoda koja križa dvije jedinke.
     *
     * @param ind1 Prva jedinka koja se križa
     * @param ind2 Druga jedinka koja se križa
     * @param child Dijete dobiveno križanjem
     */
    public void mate(Individual ind1, Individual ind2, Individual child) {
        //child.set(0, operators.get(0).mate(ind1.get(0), ind2.get(0)));	//stara verzija

        child.getFitness().setValid(false);

        if (ind1 == child) {
            ind1 = ind1.copy();	//provjera ako je neki roditelj po referenci isti kao dijete    
        }
        if (ind2 == child) {
            ind2 = ind2.copy();
        }
        
        Random random = state.getRandomizer();

        if (crxGenotypes.equalsIgnoreCase("all")) {
            for (int iGenotype = 0; iGenotype < ind1.size(); iGenotype++) {
                // choose operator
                int iOperator;
                if (opProb.get(iGenotype).get(0) < 0) {
                    iOperator = random.nextInt(operators.get(iGenotype).size());
                } else {
                    double slucajni = random.nextDouble();
                    iOperator = 0;
                    while (opProb.get(iGenotype).get(iOperator) < slucajni) {
                        iOperator++;
                    }
                }
              
                Genotype g = operators.get(iGenotype).get(iOperator).mate(ind1.get(iGenotype),
                        ind2.get(iGenotype));
                child.set(iGenotype, g);
                for(Genotype gen:child){
                	gen.setIndividual(child);
                }
            }
        } else if (crxGenotypes.equalsIgnoreCase("random")) {
            int iGenotype = random.nextInt(ind1.size());

            // copy unchanged genotypes from parents
            for (int i = 0; i < iGenotype; i++) {
                child.set(i, ind1.get(i).copy());
            }
            for (int i = iGenotype + 1; i < child.size(); i++) {
                child.set(i, ind2.get(i).copy());
            }

            // choose operator
            int iOperator;
            if (opProb.get(iGenotype).get(0) < 0) {
                iOperator = random.nextInt(operators.get(iGenotype).size());
            } else {
                double slucajni = random.nextDouble();
                iOperator = 0;
                while (opProb.get(iGenotype).get(iOperator) < slucajni) {
                    iOperator++;
                }
            }
            
            Genotype g = operators.get(iGenotype).get(iOperator).mate(ind1.get(iGenotype),
                    ind2.get(iGenotype));
            child.set(iGenotype, g);
            for(Genotype gen:child){
            	gen.setIndividual(child);
            }
        }
    }

    /**
     * Metoda koja vraća sve definirane operatore
     * križanja.
     *
     * @return Popis operatora križanja
     */
    public Vector<Vector<CrossoverOp>> getOperators() {
        return operators;
    }

    /**
     * Metoda koja dodaje operator križanja.
     * 
     * @param operator Operator križanja
     * @param genotypeID ID genotipa čiji se operator križanja dodaje
     */
    public void addOperator(CrossoverOp operator, int genotypeID) {	
    	if(operators.size()==genotypeID){			//ako je ovo prvi operator novog genotipa
    		Vector<CrossoverOp> krizanja = new Vector<CrossoverOp>();
    		krizanja.add(operator);
    		operators.add(krizanja);
    	}
    	else{										//ako nije prvi operator
    		operators.get(genotypeID).add(operator);
    	}
    }
    	
}
