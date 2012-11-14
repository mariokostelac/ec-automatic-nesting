package ecf.operators;
import org.w3c.dom.Element;
import ecf.State;

/**
 * Razred koji predstavlja operator zaustavljanja koji
 * zaustavlja algoritam nakon određenog broja sekundi.
 *
 * @author Rene Huić
 */
public class TermMaxTimeOp extends Operator{

    private State state;
    protected long maxTime;

    public TermMaxTimeOp(State state){
        this.state = state;
    }

    @Override
    public boolean initialize() {
        if(!state.getRegistry().isModified("term.maxtime"))
            return false;

        maxTime = Long.parseLong(state.getRegistry().getEntry("term.maxtime"))*1000;
        return true;
    }

    @Override
    public void registerParameters() {
        state.getRegistry().registerEntry("term.maxtime", Long.toString(maxTime));
    }

    @Override
    public void write(Element elem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void operate() {
        if(state.getElapsedTime() > maxTime){
            state.setTerminateCond();
            state.getLogger().log(1, "Termination: designated time elapsed.");
        }
    }

    @Override
    public void read(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
