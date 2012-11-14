package ecf.examples.parallel;

import ecf.examples.*;
import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.binary.Binary;
import ecf.genotype.floatingpoint.FloatingPoint;

/**
 * Razred koji demonstrira uporabu ECF-a za rješavanje
 * problema minimizacije funkcija. Potrebno je pronaći
 * minimum zadane funkcije.
 *
 * Implementacije funkcija postoje za binarni prikaz i
 * za floating point prikaz.
 *
 * Da bi koristio ECF, korisnik mora implementirati sučelje
 * IEvaluate pri čemu mora napisati vlastitu funkciju za
 * računanje fitnessa (dobrote) jedinki.
 *
 * @author Rene Huić
 */
public class MinFunction {

    // <editor-fold defaultstate="collapsed" desc="Rosenbrock's valley">
    private class FunctionFP2 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < fp.getnDimension() - 1; i++) {
                double value1 = fp.getNumber(i);
                double value2 = fp.getNumber(i + 1);
                fitnes += 100 * Math.pow((value2 - value1 * value1), 2) + Math.pow(1 - value1 * value1, 2);
            }
            fitness.setValue(Math.abs(fitnes));
        }

        @Override
        public Fitness createFitness() {
            return new FitnessMin();
        }

        @Override
        public void registerParameters() {
        }

        @Override
        public void initialize() {
        }

        /**
         * Metoda koja pokreće genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            args = state.enableMPI(args);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin2 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < binary.getnDimension() - 1; i++) {
                double value1 = binary.getRealValue(i);
                double value2 = binary.getRealValue(i + 1);
                fitnes += 100 * Math.pow((value2 - value1 * value1), 2) + Math.pow(1 - value1 * value1, 2);
            }
            fitness.setValue(Math.abs(fitnes));
        }

        @Override
        public Fitness createFitness() {
            return new FitnessMin();
        }

        @Override
        public void registerParameters() {
        }

        @Override
        public void initialize() {
        }

        /**
         * Metoda koja pokreće genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            args = state.enableMPI(args);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>


    /**
     * Metoda koja se poziva prilikom pokretanja programa
     * @param args Argumenti iz komandne linije, ne koriste se
     */
    public static void main(String[] args) {
        MinFunction test = new MinFunction();
        test.pokreni2(args);
    }

    public void pokreni2(String[] args) {
//        FunctionFP2 test1 = new FunctionFP2();
//        test1.pokreni(args);
        FunctionBin2 test2 = new FunctionBin2();
        test2.pokreni(args);
    }

}
