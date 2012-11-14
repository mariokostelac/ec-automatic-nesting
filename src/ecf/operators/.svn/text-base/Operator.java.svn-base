package ecf.operators;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja ECF operator.
 * Sve implementacije operatora moraju
 * naslijediti ovaj razred.
 * 
 * @author Rene Huić
 */
public abstract class Operator {

    /**
     * Metoda koja inicijalizira operator.
     */
    public abstract boolean initialize();

    /**
     * Metoda koja registrira sve parametre operatora.
     */
    public abstract void registerParameters();

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
     * Metoda kojom se obavlja zadana operacija.
     */
    public abstract void operate();
}
