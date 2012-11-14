package ecf.selection;

import java.util.Vector;
import ecf.Individual;

/**
 * Apstraktni razred koji predstavlja operator 
 * selekcije. Svaka implementacija selekcije mora 
 * naslijediti ovaj razred.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class SelectionOperator {

	/**
	 * Metoda koja inicijalizira operator selekcije.
	 */
	public abstract void initialize();
	
	/**
	 * Metoda koja odabire jednu jedinku iz grupe.
	 * 
	 * @param pool Grupa jedinki
	 * @return Odabrana jedinka
	 */
	public abstract Individual select(Vector<Individual> pool);
}
