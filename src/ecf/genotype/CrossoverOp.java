package ecf.genotype;

import java.util.Random;
import ecf.State;

/**
 * Apstraktni razred koji predstavlja operator 
 * križanja. Svaka implementacija križanja mora 
 * naslijediti ovaj razred.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class CrossoverOp {

	/**Objekt sa stanjem algoritma*/
	protected State state;
	
	/**vjerojatnost uporabe ovog operatora križanja*/
	protected double probability;	
	
	/**Genotip ovog operatora križanja*/
	protected Genotype myGenotype;
	
	
	/**
	 * Konstruktor koji prima referencu na razred state.
	 * 
	 * @param state Razred sa stanjem algoritma
	 */
	public CrossoverOp(State state) {
		this.state = state;;
		this.probability = 0;
	}

	
	/**
	 * Inicijalizacija operatora križanja.
	 * Poziva se prije prvog križanja.
	 */
	public abstract void initialize();

	
	/**
	 * Metoda za registraciju parametara.
	 * Poziva se prije inicijalizacije.
	 */
	public abstract void registerParameters();


	/**
	 * Metoda koja križa dvije jedinke.
	 * 
	 * @param ind1 Prva jedinka koja se križa
	 * @param ind2 Druga jedinka koja se križa
	 * @return Dijete dobiveno križanjem
	 */
	public abstract Genotype mate(Genotype ind1, Genotype ind2);
	
	
	/**
	 * Metoda koja vraća vjerojatnost križanja
	 * operatora križanja.
	 * 
	 * @return Vjerojatnost križanja
	 */
	public double getProbability() {
		return probability;
	}


	/**
	 * Metoda koja vraća genotip operatora križanja.
	 * 
	 * @return Genotip operatora križanja
	 */
	public Genotype getMyGenotype() {
		return myGenotype;
	}


	/**
	 * Metoda koja postavlja genotip operatora križanja.
	 * 
	 * @param myGenotype Genotip operatora križanja
	 */
	public void setMyGenotype(Genotype myGenotype) {
		this.myGenotype = myGenotype;
	}
}
