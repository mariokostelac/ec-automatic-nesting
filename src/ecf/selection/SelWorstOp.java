package ecf.selection;

import java.util.Vector;
import ecf.Individual;

/**
 * Razred koji predstavlja implementaciju operatora
 * selekcije. Uvijek se odabire najlošija jedinka.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class SelWorstOp extends SelectionOperator{
	
	public void initialize(){ }
	
	
	public Individual select(Vector<Individual> pool){
		Individual najgori = pool.get(0);
		for(int i=1;i<pool.size();i++){
			Individual jedinka = pool.get(i);
			if(jedinka.getFitness().compareTo(najgori.getFitness())<0){
				najgori = jedinka;
			}
		}
		return najgori;
	}
}
