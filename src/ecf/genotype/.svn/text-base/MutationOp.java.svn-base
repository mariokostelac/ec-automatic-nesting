package ecf.genotype;

import java.util.Random;
import ecf.State;

/**
 * Apstraktni razred koji predstavlja operator 
 * mutacije. Svaka implementacija mutacije mora 
 * naslijediti ovaj razred.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class MutationOp {
	
	/**Objekt sa stanjem algoritma*/
	protected State state;
	
	/**vjerojatnost uporabe ovog operatora mutacije*/
	protected double probability;	
	
	/**Genotip ovog operatora mutacije*/
	protected Genotype myGenotype;
	
	
	/**
	 * Konstruktor koji prima referencu na razred state.
	 * 
	 * @param state Razred sa stanjem algoritma
	 */
	public MutationOp(State state){
		this.state = state;
		this.probability = 0;
	}
	
	
	/**
	 * Inicijalizacija operatora mutacije.
	 * Poziva se prije prvog mutiranja.
	 */
	public abstract void initialize();

	
	/**
	 * Metoda za registraciju parametara.
	 * Poziva se prije inicijalizacije.
	 */
	public abstract void registerParameters();
	
	
	/**
	 * Metoda koja mutira jedinku.
	 * 
	 * @param ind Genotip jedinke koja se mutira
	 */
	public abstract void mutate(Genotype ind);
	
	
	/**
	 * Metoda koja vraća vjerojatnost mutacije
	 * operatora mutacije.
	 * 
	 * @return Vjerojatnost mutacije
	 */
	public double getProbability() {
		return probability;
	}


	/**
	 * Metoda koja vraća genotip operatora mutacije.
	 * 
	 * @return Genotip operatora mutacije
	 */
	public Genotype getMyGenotype() {
		return myGenotype;
	}


	/**
	 * Metoda koja postavlja genotip operatora mutacije.
	 * 
	 * @param myGenotype Genotip operatora mutacije
	 */
	public void setMyGenotype(Genotype myGenotype) {
		this.myGenotype = myGenotype;
	}
}
