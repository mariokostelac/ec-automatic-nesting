package ecf.algorithm;

import java.util.Vector;
import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.selection.SelRandomOp;
import ecf.selection.SelWorstOp;
import ecf.selection.SelectionOperator;

/**
 * Razred koji implementira steady-state turnirski odabir jedinki
 * 
 * @author Marko Pielić/Rene Huić
 */
public class SteadyStateTournament extends Algorithm {

    /**Veličina turnira pri selekciji*/
    private int nTournament;
    
    /**Operatori odabira*/
    private SelectionOperator selRandomOp, selWorstOp;

    /**
     * Konstruktor koji kreira novi turnir.
     * Dodaje sve operatore odabira koji se 
     * koriste pri ovom algoritmu u listu.
     * 
     * @param state Stanje algoritma
     */
    public SteadyStateTournament(State state) {
        super(state, "SteadyStateTournament");
        selRandomOp = new SelRandomOp(state);
        selWorstOp = new SelWorstOp();
        selectionOp.add(selRandomOp);
        selectionOp.add(selWorstOp);
    }

    
    @Override
    public void initialize() {
        nTournament = Integer.valueOf(getParameterValue("tsize"));
        if(nTournament<3){
        	String poruka = "Error: SteadyStateTournament algorithm requires minimum tournament size of 3!";
    		state.getLogger().log(1, poruka);
    		throw new IllegalArgumentException(poruka);
    	}

        for(int i = 0; i < selectionOp.size(); i++){	//inicijalizacija operatora odabira
            selectionOp.get(i).initialize();
        }
    }

    
    @Override
    public void registerParameters() {
    	registerParameter("tsize", "3");	//default: turnir veličine 3
    }
    

    public void advanceGeneration(Deme deme) {
    	state.getLogger().log(5, "Individuals in tournament: ");
        Vector<Individual> tournament = new Vector<Individual>();
   
        for (int i = 0; i < deme.size(); i++) {		//jedna generacija algoritma
        	tournament.clear();
        	
            //odabire se nasumično n jedinki za turnir
            for (int j = 0; j < nTournament; j++) {
                tournament.add(selRandomOp.select(deme));
                state.getLogger().log(5, " " + tournament.get(j).getFitness().getValue());
            }

            Individual worst = selWorstOp.select(tournament); 	//najgora jedinka od n odabranih za turnir
            state.getLogger().log(5, "The worst from the tournament: " + worst.getFitness().getValue());
            removeFrom(worst, tournament);		
            mate(tournament.get(0), tournament.get(1), worst);	//križaj preostale dvije
            mutate(worst);										//mutiraj novu jedinku
            worst.evaluate();									//i odredi joj fitness 
            
            state.getLogger().log(5, "New individual: " + worst.getFitness().getValue());
        }

        for (int i = 0; i < deme.size(); i++) {					//logiraj novo stanje dema
            state.getLogger().log(5, "deme[" + i + "]: " + deme.get(i).getFitness().getValue() +
                    "\t" + deme.get(i).getIndex());
        }
    }
}
