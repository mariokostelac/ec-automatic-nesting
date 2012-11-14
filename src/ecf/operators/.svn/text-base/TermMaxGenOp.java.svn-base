package ecf.operators;
import org.w3c.dom.Element;
import ecf.State;


/**
 * Razred koji predstavlja operator zaustavljanja koji
 * zaustavlja algoritam nakon određenog broja generacija.
 *
 * @author Rene Huić
 */
public class TermMaxGenOp extends Operator{

    private State state;
    protected int nGenerations;

    public TermMaxGenOp(State state){
        this.state = state;
    }

    @Override
    public boolean initialize() {
        if(!state.getRegistry().isModified("term.maxgen"))
            return false;

        nGenerations = Integer.parseInt(state.getRegistry().getEntry("term.maxgen"));
        return true;
    }

    @Override
    public void registerParameters() {
        state.getRegistry().registerEntry("term.maxgen", "10"); //hardkodirano na 10 generacija
    }

    @Override
    public void write(Element elem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void operate() {
        if(state.getGenerationNo() >= nGenerations){
            state.setTerminateCond();
            state.getLogger().log(1, "Termination: max generation reached");
        }
    }

    @Override
    public void read(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Metoda za dohvacanje maksimalnog broja generacija.
     *
     * @return max broj generacija
     */
    public int getMaxGeneration(){
        return nGenerations;
    }

}
