package ecf.fitness;


/**
 * Sučelje koje mora implementirati korisnik da 
 * bi omogućio evaluaciju fitnessa.
 * 
 * @author Marko Pielić/Rene Huić
 */
public interface IEvaluate {

	
	/**
	 * Metoda za evaluaciju fitnessa.
	 * Definira ju korisnik.
	 * 
	 * @param fitness Fitness čiji se evaluate poziva
	 */
	public void evaluate(Fitness fitness);
	
	
	/**
	 * Metoda koja kreira Fitness objekt.
	 * Definira ju korisnik.
	 * Fitness objekt se koristi za usporedbu
	 * jedinki u algoritmu.
	 * 
	 * @return Kreirani Fitness objekt
	 */
	public Fitness createFitness();
	
	
	/**
	 * Metoda za registriranje parametara. Ukoliko parametri
	 * nisu potrebni, implementacija ove metode može ostati
	 * prazna.
	 */
	public void registerParameters();
	
	
	/**
	 * Metoda za inicijalizaciju. Ukoliko inicijalizacija nije
	 * potrebna, implementacija ove metode može ostati prazna.
	 */
	public abstract void initialize();
}
