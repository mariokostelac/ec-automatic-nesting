/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecf.selection;

import ecf.Individual;
import ecf.State;
import java.util.Random;
import java.util.Vector;


/**
 *
 * @author Marijan
 */
public class SelFitnessProportionalOp extends SelectionOperator {

    private State state;
    
    private double selectionPressure = 2;
    
    private boolean inverseSelect = false;
    
    private Random randomizer;
    
    public SelFitnessProportionalOp(State state) { this.state = state; }
    
    public void setInverseProportional() { inverseSelect = true; }
    
    public void setFitnessProportional() { inverseSelect = false; }
    
    public boolean setSelPressure(double selPressure) {
        if (selPressure > 1) {
            selectionPressure = selPressure;
            return true;
        }
        return false;
    }

    @Override
    public void initialize() { 
        randomizer = state.getRandomizer();
    }

    @Override
    public Individual select(Vector<Individual> pool) {
        return selectMany(pool, 1).get(0);
    }
    
    public Vector<Individual> selectMany(Vector<Individual> pool, int numberOfRepeats) {
        
        if (pool.isEmpty()) { return null; }
        
        Individual best = new SelBestOp().select(pool);
        Individual worst = new SelWorstOp().select(pool);
        
        double bestValue = best.getFitness().getValue();
        double worstValue = worst.getFitness().getValue();
        
        Vector<Double> howFit = new Vector<Double>(pool.size());
        Vector<Individual> selected = new Vector<Individual>(numberOfRepeats);
        
        if (inverseSelect) {
            howFit.add(1 + (selectionPressure - 1) * (bestValue - pool.get(0).getFitness().getValue()) / (bestValue - worstValue));
            for(int i = 1; i < pool.size(); i++) {
                    double temp = 1 + (selectionPressure - 1) * (bestValue - pool.get(i).getFitness().getValue()) / (bestValue - worstValue);
                    howFit.add(temp + howFit.get(i - 1));
            }
        } else {
            howFit.add(1 + (selectionPressure - 1) * (pool.get(0).getFitness().getValue() - worstValue) / (bestValue - worstValue));
            for(int i = 1; i < pool.size(); i++) {
                    double temp = 1 + (selectionPressure - 1) * (pool.get(i).getFitness().getValue() - worstValue) / (bestValue - worstValue);
                    howFit.add(temp + howFit.get(i - 1));
            }
        }
        
        for (int i = 0; i < numberOfRepeats; i++) {
            double randomValue = randomizer.nextDouble() * howFit.get(howFit.size() - 1);
            int chosen = 0, begin = 0, end = howFit.size() - 1;
            
            while ((begin + 1) < end) {
                chosen = (begin + end) / 2;
                if (howFit.get(chosen) > randomValue) {
                    end = chosen;
                } else {
                    begin = chosen;
                }
            }
            
            if (howFit.get(begin) >= randomValue) {
                chosen = begin;
            } else {
                chosen = end;
            }
            
            selected.add(pool.get(chosen));
        }

        return selected;
    }
}
