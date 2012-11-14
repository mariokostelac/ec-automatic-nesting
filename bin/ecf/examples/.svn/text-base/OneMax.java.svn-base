package ecf.examples;

import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMax;
import ecf.fitness.IEvaluate;
import ecf.genotype.bitstring.BitString;

/**
 * Testni razred.
 * Sve što korisnik mora napraviti nalazi
 * se u ovom razredu. Korisnik mora definirati
 * metodu za evaluaciju fitnessa i pokrenuti
 * algoritam. Parametri se čitaju iz datoteke
 * Parametri.xml.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class OneMax implements IEvaluate {

    @Override
    public void evaluate(Fitness fitness) {
        BitString bitstring = (BitString) fitness.getIndividual().get(0);
        double value = 0.0;

        //onemax problem - brojim jedinice u kromosomu
        for (int i = 0; i < bitstring.size(); i++) {
            if (bitstring.get(i) == 1) {
                value++;
            }
        }
        fitness.setValue(value);
    }
    
    
    @Override
	public Fitness createFitness() {
		return new FitnessMax();
	}
    
    
    /**
     * Metoda koja pokreće genetski algoritam.
     * @param args Argumenti iz komandne linije
     */
    public void pokreni(String[] args){
        State state = new State(this);
        state.initialize(args);
        state.run();
    }

    
    /**
     * Metoda koja se poziva prilikom pokretanja programa
     * @param args Argumenti iz komandne linije, ne koriste se
     */
    public static void main(String[] args) {
        OneMax test = new OneMax();
        test.pokreni(args);
    }

    public void registerParameters() {
    }

    public void initialize() {
    }
}
