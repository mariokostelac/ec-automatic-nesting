package ecf.examples;

import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.permutation.Permutation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred koji demonstrira uporabu ECF-a za rješavanje
 * problema trgovačkog putnika. Problem se sastoji od
 * pronalaska najkraćeg povratnog puta između N gradova.
 *
 * Da bi koristio ECF, korisnik mora implementirati sučelje
 * IEvaluate pri čemu mora napisati vlastitu funkciju za
 * računanje fitnessa (dobrote) jedinki.
 *
 * @author Rene Huić
 */
public class TSP implements IEvaluate {
    List<List<Integer>> matricaPovezanosti;

   @Override
    public void evaluate(Fitness fitness) {
        Permutation perm = (Permutation) fitness.getIndividual().getGenotype(0);
        int value = 0;
        for(int i = 0; i < perm.getSize() - 1; i++){
            value += matricaPovezanosti.get(perm.getElement(i)).get(perm.getElement(i+1));
        }
        value += matricaPovezanosti.get(perm.getElement(perm.getSize() - 1)).get(perm.getElement(0));
        fitness.setValue(value);
    }

    @Override
    public Fitness createFitness() {
        return new FitnessMin();
    }

    /**
     * Metoda koja pokreće genetski algoritam.
     * @param args Argumenti iz komandne linije
     */
    public void pokreni(String[] args) {
        readFile();
        State state = new State(this);
        state.initialize(args);
        state.run();
    }

    /**
     * Metoda koja čita matricu povezanosti između gradova
     * iz datoteke.
     *
     * U datoeci se mora nalaziti matrica u obliku da
     * jedan red matrice predstavlja jedan red u datoteci
     * i da svaki element u istom redu mora biti odvojen
     * sa jednim praznim mjestom.
     */
    private void readFile(){
        matricaPovezanosti = new LinkedList<List<Integer>>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("gradovi.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String linija = "";
        while (true) {
            try {
                linija = in.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (linija == null || linija.equals("") || linija.startsWith("Rjesenje")) {
                break;
            }
            else {
                String[] elementi = linija.split(" ");
                if(elementi.length <= 0)
                    continue;
                List<Integer> lista = new LinkedList<Integer>();
                for(int i = 0; i < elementi.length; i++){
                    lista.add(Integer.parseInt(elementi[i].trim()));
                }
                matricaPovezanosti.add(lista);
            }
        }
    }

    /**
     * Metoda koja se poziva prilikom pokretanja programa
     * @param args Argumenti iz komandne linije, ne koriste se
     */
    public static void main(String[] args) {
        TSP test = new TSP();
        test.pokreni(args);
    }

    @Override
    public void registerParameters() {
    }

    @Override
    public void initialize() {
    }
}
