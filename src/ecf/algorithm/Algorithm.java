package ecf.algorithm;

import ecf.Communicator;
import java.util.Vector;
import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.genotype.Crossover;
import ecf.genotype.Mutation;
import ecf.selection.SelectionOperator;

/**
 * Razred koji predstavlja genetski algoritam.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class Algorithm {

    /**Lista operatora odabira*/
    protected Vector<SelectionOperator> selectionOp;
    
    /**Operator križanja*/
    protected Crossover crossover;
    
    /**Operator mutacije*/
    protected Mutation mutation;

    /**Stanje algoritma*/
    protected State state;

    /**Trenutno aktivan deme*/
    protected Deme activeDeme;

    /**Ime (vrsta) algoritma*/
    protected String name;
	
    /**
     * Konstruktor koji kreira novi algoritam.
     */
    public Algorithm(State state, String name) {
    	this.state = state;
    	this.name = name;
    	selectionOp = new Vector<SelectionOperator>();
        registerParameters();
    }
    
    /**
     * Metoda koja inicijalizira algoritam.
     */
    public abstract void initialize();
    
    
    /**
     * Metoda koja evaluira inicijalnu populaciju algoritma.
     */
    public void initializePopulation(){
    	for(int iDeme=0;iDeme<state.getPopulation().size();iDeme++){
    		Deme deme = state.getPopulation().get(iDeme);
    		for(int iInd=0;iInd<deme.size();iInd++){
    			evaluate(deme.get(iInd));
    		}
    	}
    }
    
    
    /**
     * Metoda koja registrira sve parametre algoritma.
     */
    public abstract void registerParameters();
    
    /**
     * Broadcasts terminate message.
     */
    public void broadCastTermination() {
        Communicator communicator = state.getCommunicator();
        if(communicator.getCommRank() == 0) {
            for(int i = 1; i < communicator.getCommSize(); i++) {
                communicator.sendControlMessage(i, state.getTeminateCond() ? 1 : 0);
            }
	}
	else {
            if(communicator.receiveControlMessage(0) == 1) {
                state.setTerminateCond();
            }
	}
    }
    
    
	/**
	 * Helper metoda za registraciju jednog parametra.
	 * 
	 * @param kljuc Ključ parametra
	 * @param vrijednost Vrijednost parametra
	 */
    public void registerParameter(String kljuc, String vrijednost){
        kljuc = name + "." + kljuc;
        if (state.getRegistry().isRegistered(kljuc)) { state.getRegistry().modifyEntry(kljuc, vrijednost); }
        else { state.getRegistry().registerEntry(kljuc, vrijednost); }
    }
    
    
    /**
	 * Helper metoda za dohvat vrijednosti jednog
	 * registriranog parametra.
	 * 
	 * @param kljuc Ključ parametra koji se dohvaća
	 * @return Traženi parametar
	 */
	public String getParameterValue(String kljuc){
		return state.getRegistry().getEntry(name + "." + kljuc);
	}

	
    /**
     * Metoda kojom cijela populacija algoritma
     * prelazi u sljedeću generaciju.
     * Svaki deme napreduje za jednu generaciju.
     */
    public void advanceGeneration(){
		for(int iDeme = 0; iDeme < state.getPopulation().size(); iDeme++) {
			activeDeme = state.getPopulation().get(iDeme);
			advanceGeneration(activeDeme);
		}
    }

    
    /**
     * Metoda kojom deme prelazi u sljedeću generaciju.
     * 
     * @param deme Deme koji prelazi u sljedeću generaciju
     */
    public abstract void advanceGeneration(Deme deme);


    /**
     * Helper metoda za evaluaciju jedinke.
     * 
     * @param ind Jedinka koja se evaluira
     */
    protected void evaluate(Individual ind) {
    	ind.setFitness(state.getFitness().copy());
    	ind.getFitness().setIndividual(ind);	
		ind.evaluate();
	}

    
    /**
     * Helper metoda koja šalje vektor jedinki
     * na mutiranje.
     * 
     * @param pool Vektor jedinki koje treba mutirati
     * @return Broj mutiranih jedinki
     */
    protected int mutate(Vector<Individual> pool){
    	return mutation.mutation(pool);
    }
    
    
    /**
     * Helper metoda koja šalje jedinku na mutiranje.
     * 
     * @param ind Jedinka koja se šalje na mutiranje
     * @return Broj mutiranih jedinki
     */
	protected int mutate(Individual ind){
		Vector<Individual> pool = new Vector<Individual>(1);
		pool.add(ind);
		return mutate(pool);
	}
	
	
	/**
	 * Helper metoda koja zamjenjuje staru jedinku novom.
	 * Zamjena se vrši u aktivnom demeu.
	 * 
	 * @param oldInd Jedinka koja se zamjenjuje
	 * @param newInd Nova jedinka
	 */
	public void replaceWith(Individual oldInd, Individual newInd){
		activeDeme.replace(oldInd.getIndex(), newInd);
	}
	
	
	/**
	 * Helper metoda koja križa dvije jedinke.
	 * 
	 * @param ind1 Prvi roditelj
	 * @param ind2 Drugi roditelj
	 * @param child Dobiveno dijete
	 */
	protected void mate(Individual ind1, Individual ind2, Individual child){
		crossover.mate(ind1, ind2, child);
	}
	
	
	/**
	 * Helper metoda koja kopira jedinku.
	 * 
	 * @param source Jedinka koja se kopira
	 * @return Kopija jedinke
	 */
	protected Individual copy(Individual source){
		return source.copy();
	}
	
	
	/**
	 * Helper metoda koja briše jedinku iz vektora jedinki.
	 * 
	 * @param victim Jedinka koja se briše
	 * @param pool Vektor jedinki iz kojeg se jedinka briše
	 * @return True ako je jedinka obrisana, inače false
	 */
	protected boolean removeFrom(Individual victim, Vector<Individual> pool){
		int index = 0;
		while(index<pool.size() && pool.get(index)!=victim){
			index++;
		}
		if(index==pool.size()) return false;
		pool.remove(index);
		return true;
	}
	
	
	/**
	 * Helper metoda koja provjerava nalazi li se jedinka
	 * u vektoru jedinki.
	 * 
	 * @param single Jedinka za koju se provjerava pripadnost
	 * @param pool Bazen jedinki
	 * @return True ako je jedinka član vektora, inače false
	 */
	protected boolean isMember(Individual single, Vector<Individual> pool){
		int index = 0;
		while(index<pool.size() && pool.get(index)!=single){
			index++;
		}
		if(index==pool.size()) return false;
		return true;
	}
	
    
    /**
     * Metoda koja vraća operator križanja
     * 
     * @return Operator križanja
     */
    public Crossover getCrossover() {
        return crossover;
    }
    
    
    /**
     * Metoda koja postavlja operator križanja
     * 
     * @param crossover Novi operator križanja
     */
    public void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    
    /**
     * Metoda koja vraća operator mutacije
     * 
     * @return Operator mutacije
     */
    public Mutation getMutation() {
        return mutation;
    }

    
    /**
     * Metoda koja postavlja operator mutacije
     * 
     * @param mutation Novi operator mutacije
     */
    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    
    /**
     * Metoda koja dohvaća listu koja sadrži korištene
     * operatore selekcije.
     * 
     * @return Lista operatora selekcije
     */
    public Vector<SelectionOperator> getSelectionOp() {
        return selectionOp;
    }

    
    /**
     * Metoda koja postavlja listu koja sadrži korištene
     * operatore selekcije.
     * 
     * @param selectionOp Nova lista s operatorima selekcije
     */
    public void setSelectionOp(Vector<SelectionOperator> selectionOp) {
        this.selectionOp = selectionOp;
    }


	/**
	 * Metoda koja vraća ime korištenog algoritma.
	 * 
	 * @return Ime algoritma
	 */
	public String getName() {
		return name;
	}

    /**
     * Returns true if this algorithm is parallel, false if not
     * 
     * @return true if this algorithm is parallel, false if not
     */
    public boolean isParallel() { return false; }
}
