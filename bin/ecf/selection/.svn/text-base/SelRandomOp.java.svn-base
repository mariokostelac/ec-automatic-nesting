package ecf.selection;

import java.util.Random;
import java.util.Vector;
import ecf.Individual;
import ecf.State;


/**
 * Razred koji predstavlja implementaciju operatora
 * selekcije. Uvijek se odabire jedinka dobivena
 * slučajnim odabirom.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class SelRandomOp extends SelectionOperator {

	/**Objekt za kreiranje slučajnih brojeva*/
    private Random random;

    private State state;
    
    public SelRandomOp(State state) { this.state = state; }
    
    public void initialize() {
        random = state.getRandomizer();
    }

    public Individual select(Vector<Individual> pool) {
        return pool.get(random.nextInt(pool.size()));
    }
}