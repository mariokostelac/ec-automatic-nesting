package ecf;

import ecf.selection.SelBestOp;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 *
 * @author Rene Huić
 */
public class HallOfFame {

    protected boolean empty;
    protected State state;
    protected int size;
    protected int lastChangeGen;
    protected Vector<Individual> bestIndividuals;
    protected Vector<Integer> bestGenerations;
    protected SelBestOp selectBest;

    public HallOfFame() {
        selectBest = new SelBestOp();
        size = 1;
        bestIndividuals = new Vector<Individual>();
        bestIndividuals.setSize(size);
        bestGenerations = new Vector<Integer>();
        bestGenerations.setSize(size);
        empty = true;
        lastChangeGen = 0;
    }

    /**
     * Metoda koja inicijalizira objekt state koji predstavlja
     * trenutno stanje algoritma
     *
     * @param state stanje algoritma
     */
    public void initialize(State state) {
        this.state = state;
    }

    /**
     * Metoda koja obavlja operate metodu nad svakom
     * deme koja postoji
     *
     * @param state trenutno stanje algoritma
     */
    public void operate(State state) {
        Population pop = state.getPopulation();
        for (int i = 0; i < pop.size(); i++) {
            operate(pop.get(i));
        }
    }

    /**
     * Metoda koja uspoređuje najbolju jedinku dosad i najboljih
     * iz trenutne generacije. Pa u slucaju da je nova jedinka bolja
     * onda ju stavlja u vektor najboljih jedinki.
     * 
     * @param individuals Vektor najboljih jedinki iz trenutne generacije
     */
    public void operate(Vector<Individual> individuals) {
        int ind = 0;
        Individual best;
        while (ind < size) {
            best = selectBest.select(individuals);
            if (empty || (best.getFitness().compareTo(bestIndividuals.get(ind).getFitness()) > 0)) {
                // copy individual to HoF
                Individual novi = best.copy();

                bestIndividuals.set(ind, novi);
                bestGenerations.set(ind, state.getGenerationNo());
                empty = false;
                lastChangeGen = state.getGenerationNo();
            }
            ind++;
        }
    }

    /**
     * Metoda za zapisivanje stanja u datoteku.
     * @param elem xml cvor u koji zapisuje stanje
     */
    public void write(Element elem) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            elem.setAttribute("size", Integer.toString(size));
            for (int i = 0; i < size; i++) {
                Element elem2 = doc.createElement("Individual" + (i + 1));
                bestIndividuals.get(i).write(elem2);
                elem2.setAttribute("gen" + (i + 1), Integer.toString(bestGenerations.get(i)));
                elem.appendChild(elem2);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(HallOfFame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za ucitavanje stanja.
     * @param elem xml cvor iz kojeg cita stanje
     */
    public void read(Element elem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Metoda kojom se dohvaćaju najbolje jedinke.
     * 
     * @return Najbolje jedinke
     */
    public Vector<Individual> getBest() {
        return bestIndividuals;
    }

    /**
     * Metoda kojom se dohvaća zadnja
     * generacija u kojoj je pronađena najbolja
     * jedinka
     *
     * @return Vrijednost zadnje generacije
     */
    public int getLastChange() {
        return lastChangeGen;
    }
}
