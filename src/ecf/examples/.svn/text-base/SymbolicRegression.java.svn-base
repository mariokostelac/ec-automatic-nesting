package ecf.examples;

import java.util.Vector;
import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.tree.Tree;


/**
 * Razred koji demonstrira uporabu ECF-a za rješavanje
 * problema simboličke regresije. Pri simboličkoj regresiji
 * se nastoji pronaći analitički oblik nepoznate funkcije
 * koja je zadana određenim brojem parova ulaz-izlaz.
 * 
 * Da bi koristio ECF, korisnik mora implementirati sučelje 
 * IEvaluate pri čemu mora napisati vlastitu funkciju za
 * računanje fitnessa (dobrote) jedinki.
 * 
 * @author Marko Pielić
 */
public class SymbolicRegression implements IEvaluate {

	/**Točke funkcije za koje se računaju njeni izlazi*/
	private Vector<Double> domain = new Vector<Double>();
	
	/**Izlazne vrijednosti funkcije u nekim točkama*/
	private Vector<Double> codomain = new Vector<Double>();
	
	/**Stanje algoritma, glavni objekt ECF-a*/
	private State state;
	
	
	/**
	 * Metoda koja se poziva prilikom pokretanja programa.
	 * 
	 * @param args Argumenti iz komandne linije, koriste se za inicijalizaciju ECF-a
	 */
	public static void main(String[] args) {
		SymbolicRegression symb = new SymbolicRegression();
		symb.pokreni(args);
	}

	
	/**
	 * Metoda kojom se pokreće ECF.
	 * 
	 * @param args Argumenti iz komandne linije, koriste se za inicijalizaciju ECF-a
	 */
	private void pokreni(String[] args) {
		state = new State(this);
		state.initialize(args);
		state.run();
	}
	

	@Override
	public Fitness createFitness() {
		return new FitnessMin();
	}

	@Override
	public void evaluate(Fitness fitness) {
		Tree tree = (Tree)fitness.getIndividual().get(0);
		double value = 0;
		
		for(int i=0;i<domain.size();i++){
			tree.setTerminalValue("X", domain.get(i));
			double result = ((Number)tree.execute()).doubleValue();
			value += Math.abs(codomain.get(i) - result);
		}
		
		fitness.setValue(value);
	}

	@Override
	public void initialize() {
		String domena = state.getRegistry().getEntry("domain").trim();
		String kodomena = state.getRegistry().getEntry("codomain").trim();
		
		String[] domenaPolje = domena.split("\\s+");
		String[] kodomenaPolje = kodomena.split("\\s+");
		
		if(domenaPolje.length!=kodomenaPolje.length){
			throw new IllegalArgumentException("Error: Evaluation operator initialization failed!");
		}
		for(int i=0;i<domenaPolje.length;i++){
			domain.add(Double.valueOf(domenaPolje[i]));
			codomain.add(Double.valueOf(kodomenaPolje[i]));
		}
	}

	@Override
	public void registerParameters() {
		state.getRegistry().registerEntry("domain", "");
		state.getRegistry().registerEntry("codomain", "");
	}
}
