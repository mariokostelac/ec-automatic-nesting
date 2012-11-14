package ecf.selection;

import ecf.Individual;
import java.util.Vector;

/**
 * Razred koji predstavlja implementaciju operatora
 * selekcije. Uvijek se odabire najbolja jedinka.
 *
 * @author Rene Huić
 */
public class SelBestOp extends SelectionOperator {

    @Override
    public void initialize() {
    }

    @Override
    public Individual select(Vector<Individual> pool) {
        Individual najbolji = pool.get(0);
        for (int i = 1; i < pool.size(); i++) {
            Individual jedinka = pool.get(i);
            if (jedinka.getFitness().compareTo(najbolji.getFitness()) > 0) {
                najbolji = jedinka;
            }
        }
        
        return najbolji;
    }

}
