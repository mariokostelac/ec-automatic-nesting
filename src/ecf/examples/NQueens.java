package ecf.examples;

import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMax;
import ecf.fitness.IEvaluate;
import ecf.genotype.permutation.Permutation;

/**
 * Razred koji demonstrira uporabu ECF-a za rješavanje
 * problema N kraljica. Problem se svodi na postavljanje
 * N kraljica na šahovsku ploču tako da nijedna kraljica
 * ne može napasti neku drugu kraljicu.
 *
 * Da bi koristio ECF, korisnik mora implementirati sučelje
 * IEvaluate pri čemu mora napisati vlastitu funkciju za
 * računanje fitnessa (dobrote) jedinki.
 * 
 * @author Rene Huić
 */
public class NQueens implements IEvaluate {

    @Override
    public void evaluate(Fitness fitness) {
        Permutation perm = (Permutation) fitness.getIndividual().getGenotype(0);
        int temp_fitnes = perm.getSize();
        int kolizije = 0, temp = 0;
        int brojac = 0;

        //provjera kolizija u desnim dijagonalama
        for (int i = 0; i < 2 * perm.getSize() - 3; i++) {
            if (i >= (perm.getSize() - 1)) {
                if (i == perm.getSize() - 1) {
                    brojac = perm.getSize() + 1;
                }
                brojac--;
                temp = 0;
                for (int j = 0; j < brojac; j++) {
                    if (perm.getElement(perm.getSize() - brojac + j) == j) {
                        temp++;
                    }
                }
                if (temp > 1) {
                    kolizije += temp-1;
                }

            } else {
                brojac++;
                temp = 0;
                for (int j = 0; j < brojac; j++) {
                    if (perm.getElement(j) == (perm.getSize() - brojac + j)) {
                        temp++;
                    }
                }
                if (temp > 1) {
                    kolizije += temp-1;
                }
            }
        }
        
        brojac = 0;
        //provjera kolizija u lijevim dijagonalama
        for (int i = 0; i < 2 * perm.getSize() - 3; i++) {
            if (i >= (perm.getSize() - 1)) {
                if (i == perm.getSize()-1) {
                    brojac = perm.getSize() + 1;
                }
                brojac--;
                temp = 0;
                for (int j = 0; j < brojac; j++) {
                    if (perm.getElement(perm.getSize() - j - 1) == perm.getSize() - brojac + j) {
                        temp++;
                    }
                }
                if(temp > 1)
                    kolizije += temp-1;

            } else {
                brojac++;
                temp = 0;
                for (int j = 0; j < brojac; j++) {
                    if (perm.getElement(brojac - j - 1) == j) {
                        temp++;
                    }
                }
                if(temp > 1)
                    kolizije += temp-1;

            }
        }
        fitness.setValue(temp_fitnes - kolizije);
    }

    @Override
    public Fitness createFitness() {
        return new FitnessMax();
    }

    /**
     * Metoda koja pokreće genetski algoritam.
     * @param args Argumenti iz komandne linije
     */
    public void pokreni(String[] args) {
        State state = new State(this);
        state.initialize(args);
        state.run();
    }

    /**
     * Metoda koja se poziva prilikom pokretanja programa
     * @param args Argumenti iz komandne linije, ne koriste se
     */
    public static void main(String[] args) {
        NQueens test = new NQueens();
        test.pokreni(args);
    }

    @Override
    public void registerParameters() {
    }

    @Override
    public void initialize() {
    }
}
