package ecf;

import java.util.Vector;
import ecf.fitness.IEvaluate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Razred koji predstavlja deme.
 * Deme je skup (otok) jedinki.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Deme extends Vector<Individual> {

	private static final long serialVersionUID = 1L;

	/**Sučelje sa metodom evaluate*/
	private IEvaluate ievaluate;

	/**Najbolje jedinke demea*/
	private HallOfFame hof;

	/**Broj jedinki u deme*/
	protected int nIndividuals = 1;

	/**
	 * Konstruktor koji kreira novi deme.
	 * 
	 * @param ievaluate Sučelje sa metodom za evaulaciju fitnessa
	 */
	public Deme(IEvaluate ievaluate) {
		this.ievaluate = ievaluate;
		hof = new HallOfFame();
	}


	/**
	 * Konstruktor koji prima broj jedinki demea.
	 * 
	 * @param ievaluate Sučelje sa metodom za evaluaciju fitnessa
	 * @param brojJedinki Broj jedinki demea
	 */
	public Deme(IEvaluate ievaluate, int brojJedinki) {
		this.ievaluate = ievaluate;
		this.nIndividuals = brojJedinki;
		hof = new HallOfFame();
	}


	/**
	 * Metoda koja inicijalizira deme.
	 * 
	 * @param state Stanje algoritma
	 */
	public void initialize(State state){
		hof.initialize(state);
		for(int i=0;i<nIndividuals;i++){
                    Individual ind = new Individual(ievaluate, state);
                    ind.setIndex(size());
                    ind.initialize();
                    add(ind);
		}
                hof.operate(this);
	}


	/**
	 * Metoda koja zamjenjuje staru jedinku sa novom.
	 * 
	 * @param index Index stare jedinke
	 * @param newInd Nova jedinka
	 */
	public void replace(int index, Individual newInd){
		newInd.setIndex(index);
		set(index, newInd);
	}

	/**
	 * Metoda za zapisivanje stanja u datoteku.
	 * @param elem xml cvor u koji zapisuje stanje
	 */
	public void write(Element elem){
		try {
			elem.setAttribute("size", Integer.toString(nIndividuals));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element elem2 = doc.createElement("HallOfFame");
			hof.write(elem2);
			elem.appendChild(elem2);

			for(int i = 0; i < this.size(); i++){
				elem2 = doc.createElement("Individual" + (i+1));
				get(i).write(elem2);
				elem.appendChild(elem2);
			}

		} catch (ParserConfigurationException ex) {
			Logger.getLogger(Deme.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Metoda za ucitavanje stanja.
	 * @param elem xml cvor iz kojeg cita stanje
	 */
	public void read(Element elem){
		NodeList childs = elem.getChildNodes();
		for(int i = 0; i < childs.getLength(); i++){
			if(i < this.size())
				this.get(i).read((Element)childs.item(i));
		}
	}


	/**
	 * Metoda koja vraća broj jedinki u demeu.
	 * 
	 * @return Broj jedinki demea
	 */
	public int getSize(){
		return nIndividuals;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i=0;i<size();i++) {
			s.append("Jedinka " + i + ": " + get(i).toString() + "\n");
		}
		return s.toString();
	}


	/**
	 * Metoda koja dohvaća najbolje jedinke.
	 * 
	 * @return Najbolje jedinke
	 */
	public HallOfFame getHof() {
		return hof;
	}


	/**
	 * Metoda koja postavlja najbolje jedinke.
	 * 
	 * @param hof Najbolje jedinke
	 */
	public void setHof(HallOfFame hof) {
		this.hof = hof;
	}
}
