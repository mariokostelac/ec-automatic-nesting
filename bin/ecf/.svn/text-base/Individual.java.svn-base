package ecf;

import ecf.fitness.Fitness;
import ecf.fitness.IEvaluate;
import ecf.genotype.Genotype;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja jednu jedinku.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Individual extends Vector<Genotype>{

	private static final long serialVersionUID = 1L;

	/**Fitness jedinke*/
	private Fitness fitness;

	/**Indeks jedinke u deme*/
	private int index;

	/**Sučelje sa metodom evaluate*/
	private IEvaluate ievaluate;

	/**Stanje algoritma*/
	private State state;


	/**
	 * Konstruktor koji stvara novu jedinku
	 * i poziva metodu za inicijalizaciju te
	 * jedinke.
	 * 
	 * @param state Stanje algoritma
	 */
	public Individual(IEvaluate ievaluate, State state) {
		this.state = state;
		this.ievaluate = ievaluate;
	}


	/**
	 * Metoda koja inicijalizira jedinku.
	 */
	public void initialize(){
		for(int i=0;i<state.getGenotypes().size();i++){		//svi genotipi se kopiraju iz State objekta
			Genotype gen = state.getGenotypes().get(i).copy();
			gen.setIndividual(this);	
			gen.initialize();
			add(gen);

		}
               fitness = state.ievaluate.createFitness();
	}


	/**
	 * Metoda koja kopira jedinku.
	 * 
	 * @return novi jedinke
	 */
	public Individual copy(){
		Individual novi = new Individual(ievaluate, state);

		if(fitness!=null) {
			novi.fitness = fitness.copy();
			novi.fitness.setIndividual(novi);
		}
                //kopiranje genotipova
		for(int i=0;i<size();i++){
			novi.add(get(i).copy());
			novi.get(i).setIndividual(novi);
		}
		novi.evaluate();
		return novi;
	}

	/**
	 * Metoda koja vraća genotip na zadanom indexu.
	 * 
	 * @param index Index genotipa koji se traži
	 * @return Traženi genotip
	 */
	public Genotype getGenotype(int index){
		return get(index);
	}

	/**
	 * Metoda za evaluaciju jedinke. Poziva 
	 * evaluaciju pridruženog fitness objekta.
	 */
	public void evaluate(){
		fitness.evaluate(ievaluate);
	}


	/**
	 * Metoda koja vraća fitness jedinke.
	 * 
	 * @return Fitness jedinke
	 */
	public Fitness getFitness() {
		return fitness;
	}


	/**
	 * Metoda koja postavlja fitness jedinke.
	 * 
	 * @param fitness Fitness jedinke
	 */
	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}


	/**
	 * Metoda koja vraća indeks jedinke u deme.
	 * 
	 * @return Traženi indeks jedinke
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * Metoda koja postavlja indeks jedinke.
	 * 
	 * @param index Indeks jedinke
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Metoda za zapisivanje stanja u datoteku.
	 * @param elem xml cvor u koji zapisuje stanje
	 */
	public void write(Element elem){
	
            elem.setAttribute("size", Integer.toString(this.size()));
            Document doc = elem.getOwnerDocument();

            Element elem2 = doc.createElement("Fitness");

            if(fitness == null){
                    elem.appendChild(elem2);
            }
            else {
                    fitness.write(elem2);
                    elem.appendChild(elem2);
            }

            for(int i = 0; i < this.size(); i++){
                    elem2 = doc.createElement(get(i).getName() + "" + (i+1));
                    get(i).write(elem2);
                    elem.appendChild(elem2);
            }

	}

	/**
	 * Metoda za ucitavanje stanja.
	 * @param elem xml cvor iz kojeg cita stanje
	 */
	public void read(Element elem){
		Element xFit = (Element)elem.getChildNodes().item(0);

		if(fitness.getValue() != 0){
			fitness = state.getFitness().copy();
		}
		fitness.read(xFit);

		for(int i = 0; i < size(); i++){
			this.get(i).read((Element)elem.getChildNodes().item(i + 1));
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Genotype gen:this){
			s.append(gen.toString()).append(" \t");
		}
		if(fitness!=null) s.append("fitness: ").append(fitness.getValue());
		return s.toString();
	}
}
