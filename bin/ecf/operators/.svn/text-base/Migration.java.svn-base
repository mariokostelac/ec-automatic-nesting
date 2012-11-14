package ecf.operators;

import ecf.Communicator;
import java.util.Vector;
import org.w3c.dom.Element;
import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.selection.SelBestOp;
import ecf.selection.SelRandomOp;
import ecf.selection.SelectionOperator;

/**
 * Razred koji predstavlja implementaciju migracije.
 * Nasljeđuje razred Operator.
 *
 * @author Rene Huić
 */
public class Migration extends Operator{

    protected State state;
    protected int migFrequency;
    protected int nEmigrants;
    protected Vector<SelectionOperator> selOp;
    
    protected static int BEST = 0;
    protected static int RANDOM = 1;

    public Migration(State state){
        this.state = state;
        selOp = new Vector<SelectionOperator>();
    }

    @Override
    public boolean initialize() {
        migFrequency = Integer.parseInt(state.getRegistry().getEntry("migration.freq"));
        nEmigrants = Integer.parseInt(state.getRegistry().getEntry("migration.number"));

        selOp.add(new SelBestOp());
        selOp.add(new SelRandomOp(state));

        selOp.get(BEST).initialize();
        selOp.get(RANDOM).initialize();

        if(migFrequency == 0 && state.getPopulation().getNoDemes() > 1){
            state.getLogger().log(1, "Warning: migration operator is not configured " +
            		"(migration will not occur between demes).");
            return false;
        }
        if(migFrequency == 0) return false;

        if(nEmigrants > state.getPopulation().getLocalDeme().getSize()){
            state.getLogger().log(1, "Error: number of emmigrants greater than deme size!");
            throw new IllegalArgumentException("Error: number of emmigrants greater than deme size!");
        }

        if(state.getPopulation().getNoDemes() == 1){
            state.getLogger().log(1, "Warning: migration operator not applicable with a single deme population!");
            migFrequency = 0;
            return false;
        }
        return true;
    }

    @Override
    public void registerParameters() {
        state.getRegistry().registerEntry("migration.freq", "0");
        state.getRegistry().registerEntry("migration.number", "1");
    }

    @Override
    public void write(Element elem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void operateSequential() {
        Vector<Vector<Individual>> outPool = new Vector<Vector<Individual>>();
        Vector<Vector<Individual>> inPool = new Vector<Vector<Individual>>();
        inPool.setSize(state.getPopulation().getNoDemes());

        for(int i = 0; i < state.getPopulation().getNoDemes(); i++){
            Deme myDeme = state.getPopulation().get(i);
            Vector<Individual> emigrants = new Vector<Individual>();
            Individual myBest = selOp.get(BEST).select(myDeme);
            emigrants.add(myBest);

            for(int j = 1; j < nEmigrants; j++){
                emigrants.add(selOp.get(RANDOM).select(myDeme));
            }

            outPool.add(emigrants);
        }

        for(int i = 0; i < outPool.size(); i++){
            for(int j = 0; j < outPool.get(i).size(); j++){
                Individual originalni = outPool.get(i).get(j);
            	Individual kopija = originalni.copy();
            	outPool.get(i).set(j, kopija);
            }
        }

        for(int i = 0; i < state.getPopulation().getNoDemes(); i++){
            inPool.add(i, new Vector<Individual>());
        }

        //topologija: prsten
        for(int i = 0; i < state.getPopulation().getNoDemes(); i++){
            int destDeme = (i+1) % state.getPopulation().getNoDemes();
            inPool.set(destDeme, outPool.get(i));
        }


        //ubaci, ali sačuvaj najbolju
        for(int i = 0; i < state.getPopulation().getNoDemes(); i++){
            Deme myDeme = state.getPopulation().get(i);
            Vector<Individual> immigrants = inPool.get(i);
            Individual myBest = selOp.get(BEST).select(myDeme);
            state.getLogger().log(4, "Received inds fitness: " + immigrants.get(0).getFitness().getValue());

            for(int j = immigrants.size() - 1; j >= 0; j--){
                Individual victim;
                do {
                    victim = selOp.get(RANDOM).select(myDeme);
                }
                while(victim == myBest);
                state.getAlgorithm().replaceWith(victim, immigrants.get(j));
            }
        }
    }
    
    private void operateParallel(Communicator communicator) {
        
        Deme myDeme = state.getPopulation().get(0);
        
        Vector<Individual> emigrants = new Vector<Individual>(nEmigrants);
        Individual bestIndividual = selOp.get(BEST).select(myDeme);
        emigrants.add(bestIndividual);
        for (int i = 1; i < nEmigrants; i++) {
            emigrants.add(selOp.get(RANDOM).select(myDeme));
        }
        
        int destinationProcess = communicator.getDemeMaster(
            (state.getPopulation().getMyDemeIndex() + 1) % state.getPopulation().getNoDemes()
        );
        
        communicator.sendIndividualsGlobal(emigrants, destinationProcess);
        
        Vector<Individual> imigrants = communicator.receiveIndividualsGlobal();
     
        state.getLogger().log(4, "Received inds fitness: {0}", new Object[] { imigrants.get(0).getFitness().getValue() });
        
        for (Individual imigrant : imigrants) {
            Individual victim;
            do {
                victim = selOp.get(RANDOM).select(myDeme);
            } while (victim.getIndex() == bestIndividual.getIndex());
            state.getAlgorithm().replaceWith(victim, imigrant);
        }
    }
    
    @Override
    public void operate() {
        if(migFrequency == 0 || state.getGenerationNo() % migFrequency != 0){
            return;
        }

        Communicator communicator = state.getCommunicator();
        
        if (communicator == null) { operateSequential(); }
        else { operateParallel(communicator); }
        
    }

    @Override
    public void read(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
