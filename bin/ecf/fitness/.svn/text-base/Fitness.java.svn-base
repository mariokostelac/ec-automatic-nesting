package ecf.fitness;

import ecf.Individual;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja fitness.
 * Svaka implementacija fitnessa mora
 * naslijediti ovaj razred.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class Fitness implements Comparable<Fitness> {
	
	/**Zastavica sa oznakom je li fitness ispravan*/
	private boolean valid = true;
	
	/**Vrijednost fitnessa*/
	private double value;
	
	/**Jedinka kojoj je pridružen fitness*/
	private Individual individual;
	
	
	/**
	 * Metoda za stvaranje kopije fitnessa.
	 * 
	 * @return Kopija fitnessa
	 */
	public abstract Fitness copy();

	/**
	 * Metoda za zapisivanje stanja u datoteku.
	 * @param element XML čvor u koji zapisuje stanje
	 */
	public abstract void write(Element element);

	/**
	 * Metoda za učitavanje stanja.
	 * @param element XML čvor iz kojeg čita stanje
	 */
	public abstract void read(Element element);

	/**
	 * Metoda za evaluaciju fitnessa.
	 * Definira ju korisnik, a potrebno je
	 * postaviti vrijednosti valid i value.
	 * 
	 * @param ievaluate Sučelje sa metodom za evaulaciju fitnessa
	 */
	public void evaluate(IEvaluate ievaluate){	
		ievaluate.evaluate(this);
	}

	/**
	 * Metoda koja vraća oznaku je li
	 * fitness updatean.
	 * 
	 * @return True ako je fitness updatean, inače false
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Metoda koja postavlja oznaku je li
	 * fitness updatean.
	 * 
	 * @param valid Nova oznaka updateanosti fitnessa
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Metoda koja vraća vrijednost fitnessa.
	 * 
	 * @return Vrijednost fitnessa
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Metoda koja postavlja vrijednost fitnessa.
	 * 
	 * @param value Nova vrijednost fitnessa
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Metoda koja vraća jedinku kojoj je fitness
	 * pridružen.
	 * 
	 * @return Tražena jedinka
	 */
	public Individual getIndividual() {
		return individual;
	}

	/**
	 * Metoda koja postavlja jedinku kojoj je fitness
	 * pridružen.
	 * 
	 * @param individual Nova jedinka kojoj je fitness pridružen
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}
}
