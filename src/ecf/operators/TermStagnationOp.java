package ecf.operators;
import org.w3c.dom.Element;
import ecf.State;


/**
 * Razred koji predstavlja implementaciju operatora
 * zaustavljanja koji prati da li se fitnes najbolje
 * jadinke promijenio kroz zadani broj generacija.
 * Ako nije tada zaustavlja algoritam.
 *
 * @author Rene Huić
 */
public class TermStagnationOp extends Operator{

    private State state;
    protected int termStagnation;

    public TermStagnationOp(State state){
        this.state = state;
    }

    @Override
    public boolean initialize() {
         if(!state.getRegistry().isModified("term.stagnation"))
             return false;

        termStagnation = Integer.parseInt(state.getRegistry().getEntry("term.stagnation"));
        if(termStagnation == 0){
            int demeSize = Integer.parseInt(state.getRegistry().getEntry("population.size"));
            termStagnation = 5000 / demeSize;

            if(termStagnation < 10){
                termStagnation = 5;
            }
            if(termStagnation > 200){
                termStagnation = 200;
            }
        }
        return true;
    }

    @Override
    public void registerParameters() {
        state.getRegistry().registerEntry("term.stagnation", "0");
    }

    @Override
    public void operate() {
        int currentGen = state.getGenerationNo();
        if(currentGen - state.getPopulation().getHof().getLastChange() > termStagnation){
            state.setTerminateCond();
            state.getLogger().log(1, "Termination: maximum number of generations without improvement ("
			+ termStagnation + ") reached");
        }
    }

    @Override
    public void write(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
