package ecf.examples;

import java.util.Vector;

import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.tree.PrimitiveSet;
import ecf.genotype.tree.Tree;


/**
 * Razred koji demonstrira uporabu ECF-a za rješavanje
 * problema dvije kutije (Two Boxes Problem) simboličkom
 * regresijom uz korištenje korisnički definiranih funkcija. 
 * 
 * Problem dvije kutije ima šest neovisnih varijabli (L0, W0, H0
 * i L1, W1 i H1) i jednu varijablu (D) koja ovisi o prethodnih
 * šest. Rješenje je D = L0*W0*H0 - L1*W1*H1, a interpretira se
 * kao razlika volumena dviju kutija čiji su parametri duljina (L),
 * širina (W) i visina (H).
 * 
 * Pri simboličkoj regresiji se nastoji pronaći analitički oblik 
 * nepoznate funkcije koja je zadana određenim brojem parova 
 * ulaz-izlaz.
 * 
 * Da bi koristio ECF, korisnik mora implementirati sučelje 
 * IEvaluate pri čemu mora napisati vlastitu funkciju za
 * računanje fitnessa (dobrote) jedinki.
 * 
 * Sve korisnički definirane funkcije potrebno je dodati u skup
 * primitiva prije inicijalizacije razreda State (može i prije
 * stvaranja razreda State).
 * 
 * @author Marko Pielić
 */
public class TwoBoxesProblemUserDefinedFunction implements IEvaluate {

	/**Vrijednosti L0, W0, H0, L1, W1, H1*/
	private Vector<double[]> domain = new Vector<double[]>();
	
	/**Poznate vrijednosti varijable D*/
	private Vector<Double> codomain = new Vector<Double>();
	
	/**Stanje algoritma, glavni objekt ECF-a*/
	private State state;
	
	
	/**
	 * Metoda koja se poziva prilikom pokretanja programa.
	 * 
	 * @param args Argumenti iz komandne linije, koriste se za inicijalizaciju ECF-a
	 */
	public static void main(String[] args) {
		TwoBoxesProblemUserDefinedFunction tbp = new TwoBoxesProblemUserDefinedFunction();
		tbp.pokreni(args);

	}

	
	/**
	 * Metoda kojom se pokreće ECF.
	 * Prije standardnog pokretanja algoritma, 
	 * potrebno je sve korisnički definirane
	 * funkcije dodati u skup primitiva.
	 * 
	 * @param args Argumenti iz komandne linije, koriste se za inicijalizaciju ECF-a
	 */
	private void pokreni(String[] args) {
		PrimitiveSet.addUserDefinedFunction(new UserDefinedFunction<Number>());
		
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
			tree.setTerminalValue("L0", domain.get(i)[0]);
			tree.setTerminalValue("W0", domain.get(i)[1]);
			tree.setTerminalValue("H0", domain.get(i)[2]);
			tree.setTerminalValue("L1", domain.get(i)[3]);
			tree.setTerminalValue("W1", domain.get(i)[4]);
			tree.setTerminalValue("H1", domain.get(i)[5]);
			
			double result = ((Number)tree.execute()).doubleValue();
			value += Math.abs(codomain.get(i) - result);
		}
		
		fitness.setValue(value);
	}

	@Override
	public void initialize() {
		String domena = state.getRegistry().getEntry("domain");
		String kodomena = state.getRegistry().getEntry("codomain");
		
		String[] domenaPolje = domena.split("\\|");
		String[] kodomenaPolje = kodomena.split("\\s+");
		
		if(domenaPolje.length!=kodomenaPolje.length){
			throw new IllegalArgumentException("Error: Evaluation operator initialization failed!");
		}
		for(int i=0;i<kodomenaPolje.length;i++){
			codomain.add(Double.valueOf(kodomenaPolje[i]));
			
			String[] polje = domenaPolje[i].trim().split("\\s+");
			double[] doubleovi = new double[polje.length];
			for(int j=0;j<doubleovi.length;j++){
				doubleovi[j] = Double.valueOf(polje[j]);
			}
			domain.add(doubleovi);
		}
	}

	@Override
	public void registerParameters() {
		state.getRegistry().registerEntry("domain", "");
		state.getRegistry().registerEntry("codomain", "");
	}
}
