package ecf.examples;

import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.binary.Binary;
import ecf.genotype.floatingpoint.FloatingPoint;

/**
 * Razred koji demonstrira uporabu ECF-a za rjesavanje
 * problema minimizacije funkcija. Potrebno je pronaci
 * minimum zadane funkcije.
 *
 * Implementacije funkcija postoje za binarni prikaz i
 * za floating point prikaz.
 *
 * Da bi koristio ECF, korisnik mora implementirati sucelje
 * IEvaluate pri cemu mora napisati vlastitu funkciju za
 * racunanje fitnessa (dobrote) jedinki.
 *
 * @author Rene Huic
 */
public class MinFunction {

    // <editor-fold defaultstate="collapsed" desc="De Jong's function">
    private class FunctionFP1 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < fp.getnDimension(); i++) {
                double value = fp.getNumber(i);
                fitnes += value * value;
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin1 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < binary.getnDimension(); i++) {
                double value = binary.getRealValue(i);
                fitnes += value * value;
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rastrigin's function">
    private class FunctionFP3 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 10 * fp.getnDimension();
            for (int i = 0; i < fp.getnDimension(); i++) {
                double value = fp.getNumber(i);
                fitnes += value * value - 10 * Math.cos(2 * Math.PI * value);
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin3 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 10 * binary.getnDimension();
            for (int i = 0; i < binary.getnDimension(); i++) {
                double value = binary.getRealValue(i);
                fitnes += value * value - 10 * Math.cos(2 * Math.PI * value);
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Schwefel's function">
    private class FunctionFP4 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < fp.getnDimension(); i++) {
                double value = fp.getNumber(i);
                fitnes += (0 - value) * Math.sin(Math.sqrt(Math.abs(value)));
            }
            fitness.setValue(Math.abs(-fp.getnDimension() * 418.9829 - Math.abs(fitnes)));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin4 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < binary.getnDimension(); i++) {
                double value = binary.getRealValue(i);
                fitnes += (0 - value) * Math.sin(Math.sqrt(Math.abs(value)));
            }
            fitness.setValue(Math.abs((0 - binary.getnDimension()) * 418.9829 - Math.abs(fitnes)));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ackley's Path function">
    private class FunctionFP5 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;

            int a = 20;
            double b = 0.2;
            double c = 2 * Math.PI;
            double sum1 = 0, sum2 = 0;

            for (int i = 0; i < fp.getnDimension(); i++) {
                double value = fp.getNumber(i);
                sum1 += value * value;
                sum2 += Math.cos(c * value);
            }
            fitnes = -a * Math.exp(-b * Math.sqrt(sum1 / fp.getnDimension())) - Math.exp(sum2 / fp.getnDimension()) + a + Math.E;
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin5 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            int a = 20;
            double b = 0.2;
            double c = 2 * Math.PI;
            double sum1 = 0, sum2 = 0;

            for (int i = 0; i < binary.getnDimension(); i++) {
                double value = binary.getRealValue(i);
                sum1 += value * value;
                sum2 += Math.cos(c * value);
            }
            fitnes = -a * Math.exp(-b * Math.sqrt(sum1 / binary.getnDimension())) - Math.exp(sum2 / binary.getnDimension()) + a + Math.E;
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Goldstein-Price's function">
    @SuppressWarnings("unused")
	private class FunctionFP6 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            double value1 = fp.getNumber(0);
            double value2 = fp.getNumber(1);

            fitnes = (1 + Math.pow(value1 + value2 + 1, 2) * (19 - 14 * value1 + 3 * value1 * value1
                    - 14 * value2 + 6 * value1 * value2 + 3 * value2 * value2))
                    * (30 + Math.pow(2 * value1 - 3 * value2, 2) * (18 - 32 * value1 + 12 * value1 * value1
                    + 48 * value2 - 36 * value1 * value2 + 27 * value2 * value2));

            fitness.setValue(Math.abs(3 - Math.abs(fitnes)));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin6 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            double value1 = binary.getRealValue(0);
            double value2 = binary.getRealValue(1);

            fitnes = (1 + Math.pow(value1 + value2 + 1, 2) * (19 - 14 * value1 + 3 * value1 * value1
                    - 14 * value2 + 6 * value1 * value2 + 3 * value2 * value2))
                    * (30 + Math.pow(2 * value1 - 3 * value2, 2) * (18 - 32 * value1 + 12 * value1 * value1
                    + 48 * value2 - 36 * value1 * value2 + 27 * value2 * value2));

            fitness.setValue(Math.abs(3 - Math.abs(fitnes)));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Function 7">
    private class FunctionFP7 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            FloatingPoint fp = (FloatingPoint) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < fp.getnDimension(); i++) {
                double value = fp.getNumber(i);
                fitnes += value * value;
            }
            fitness.setValue(Math.abs(1 - Math.abs(0.5 - (Math.pow(Math.sin(Math.sqrt(fitnes)), 2) - 0.5)
                    / Math.pow(1 + 0.001 * fitnes, 2))));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
            state.initialize(args);
            state.run();
        }
    }

    private class FunctionBin7 implements IEvaluate {

        @Override
        public void evaluate(Fitness fitness) {
            Binary binary = (Binary) fitness.getIndividual().getGenotype(0);
            double fitnes = 0;
            for (int i = 0; i < binary.getnDimension(); i++) {
                double value = binary.getRealValue(i);
                fitnes += value * value;
            }
            fitness.setValue(Math.abs(1 - Math.abs(0.5 - (Math.pow(Math.sin(Math.sqrt(fitnes)), 2) - 0.5)
                    / Math.pow(1 + 0.001 * fitnes, 2))));
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
         * Metoda koja pokrece genetski algoritam.
         * @param args Argumenti iz komandne linije
         */
        public void pokreni(String[] args) {
            State state = new State(this);
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
      //  test.pokreni1(args);
        test.pokreni2(args);
//        test.pokreni3(args);
//        test.pokreni4(args);
//        test.pokreni5(args);
//        test.pokreni6(args);
//        test.pokreni7(args);
    }

    public void pokreni1(String[] args) {
        FunctionFP1 test1 = new FunctionFP1();
        test1.pokreni(args);
        FunctionBin1 test2 = new FunctionBin1();
        test2.pokreni(args);
    }

    public void pokreni2(String[] args) {
//        FunctionFP2 test1 = new FunctionFP2();
//        test1.pokreni(args);
        FunctionBin2 test2 = new FunctionBin2();
        test2.pokreni(args);
    }

    public void pokreni3(String[] args) {
        FunctionFP3 test1 = new FunctionFP3();
        test1.pokreni(args);
        FunctionBin3 test2 = new FunctionBin3();
        test2.pokreni(args);
    }

    public void pokreni4(String[] args) {
        FunctionFP4 test1 = new FunctionFP4();
        test1.pokreni(args);
        FunctionBin4 test2 = new FunctionBin4();
        test2.pokreni(args);
    }

    public void pokreni5(String[] args) {
        FunctionFP5 test1 = new FunctionFP5();
        test1.pokreni(args);
        FunctionBin5 test2 = new FunctionBin5();
        test2.pokreni(args);
    }

    public void pokreni6(String[] args) {
        FunctionFP6 test1 = new FunctionFP6();
        test1.pokreni(args);
        FunctionBin6 test2 = new FunctionBin6();
        test2.pokreni(args);
    }

    public void pokreni7(String[] args) {
        FunctionFP7 test1 = new FunctionFP7();
        test1.pokreni(args);
        FunctionBin7 test2 = new FunctionBin7();
        test2.pokreni(args);
    }
}
