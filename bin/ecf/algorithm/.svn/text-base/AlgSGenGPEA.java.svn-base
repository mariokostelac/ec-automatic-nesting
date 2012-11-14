/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecf.algorithm;

import ecf.Communicator;
import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.selection.SelBestOp;
import ecf.selection.SelFitnessProportionalOp;
import ecf.selection.SelRandomOp;
import ecf.selection.SelectionOperator;
import java.util.Vector;
import mpi.MPI;

/**
 *
 * @author Marijan
 */
public class AlgSGenGPEA extends ParallelAlgorithm {

    private SelFitnessProportionalOp selFitPropOp;
    
    private SelectionOperator selRandomOp;
    
    private SelectionOperator selBestOp;
    
    private double crxRate;
    
    private double selPressure;
    
    private int jobSize;
    
    public static final int MASTER      = 0;
    
    private Vector<Individual> myJob    = new Vector<Individual>();
    
    public AlgSGenGPEA(State state) { 
        super(state, "AlgSGenGPEA"); 
        selFitPropOp = new SelFitnessProportionalOp(state);
        selRandomOp = new SelRandomOp(state);
        selBestOp = new SelBestOp();
        
    }
    
    @Override
    public void initialize() {
        selFitPropOp.initialize();
        selRandomOp.initialize();
        selBestOp.initialize();
        crxRate = Double.parseDouble(getParameterValue("crxprob"));
        selPressure = Double.parseDouble(getParameterValue("selpressure"));
        selFitPropOp.setSelPressure(selPressure);
        jobSize = Integer.parseInt(getParameterValue("jobsize"));
        
        if (state.getCommunicator().getCommRank() != MASTER) {
            
        }
    }

    @Override
    public void registerParameters() {
        registerParameter("crxprob", "0.5");
        registerParameter("selpressure", "2.0");
        registerParameter("jobsize", "10");
    }

    @Override
    public void advanceGeneration(Deme deme) {
        Communicator communicator = state.getCommunicator();
        
        if (communicator.getCommRank() == MASTER) {
            
            Individual best = selBestOp.select(deme);
            
            Vector<Individual> wheel = selFitPropOp.selectMany(deme, deme.getSize());
            
            for (int i = wheel.size() - 1; i >= 0; i--) {
                wheel.set(i, wheel.get(i).copy());
            }
            
            for (int i = 0; i < deme.size(); i++) {
                deme.replace(i, wheel.get(i));
            }
            
            state.getLogger().log(5, "Selected individuals: ");
            
            for (int i = 0; i < deme.size(); i++) {
                state.getLogger().log(5, Double.toString(deme.get(i).getFitness().getValue()));
            }
            
            int noCrx = (int) (deme.getSize() * crxRate / 2);
            
            for (int i = 0; i < noCrx; i++) {
                Individual parent1 = selRandomOp.select(deme);
                Individual parent2 = selRandomOp.select(deme);
                state.getLogger().log(5, "Parents: {0}, {1}", new Object[] {
                    Double.toString(parent1.getFitness().getValue()),
                    Double.toString(parent2.getFitness().getValue())
                });
                
                Individual child1 = parent1.copy();
                Individual child2 = parent2.copy();
                
                mate(parent1, parent2, child1);
                mate(parent1, parent2, child2);
                                
                replaceWith(parent1, child1);
                replaceWith(parent2, child2);
            }
            
            mutate(deme);
            
            Vector<Individual> pool = new Vector<Individual>();
            
            for (int i = 0; i < deme.size(); i++) {
                if (!deme.get(i).getFitness().isValid()) {
                    pool.add(deme.get(i));
                }
            }
            
            int current = 0, remaining = pool.size();
            
            while (current < pool.size()) {
                if (communicator.messageWaiting()) {
//                    System.out.println("Imam idlea");
                    remaining -= communicator.receiveDemeFitness(deme, MPI.ANY_SOURCE);
                    int idleWorker = communicator.getLastSource();
//                    System.out.println("Idle: " + idleWorker);
                    Vector<Individual> job = new Vector<Individual>();
                    for (int i = 0; i < jobSize && current < pool.size(); i++, current++) {
                        job.add(pool.get(current));
                    }
//                    System.out.println("Saljem " + job.size() + " elemenata");
                    communicator.sendIndividuals(job, idleWorker);
                } else {
                    evaluate(pool.get(current++));
                    remaining--;
                }
            }
            
            int remainingWorkers = communicator.getCommSize() - 1;
            
            Vector<Individual> job = new Vector<Individual>();
            
            while (remaining  > 0 || remainingWorkers > 0) {
                remaining -= communicator.receiveDemeFitness(deme, MPI.ANY_SOURCE);
                int idleWorker = communicator.getLastSource();
                communicator.sendIndividuals(job, idleWorker);
                remainingWorkers--;
            }
            
            Individual random = selRandomOp.select(deme);
            if (best.getFitness().compareTo(random.getFitness()) > 0) {
                replaceWith(random, best);
            }
            
            for (int i = 0; i < deme.getSize(); i++) {
                state.getLogger().log(5, "deme[{0}]: {1}", new Object[] {
                    Integer.toString(i),
                    Double.toString(deme.get(i).getFitness().getValue())
                });
            }
            
        } else {
            communicator.sendFitness(new Vector<Individual>(), MASTER);
//            System.out.println("Poslo fitness, cekam na jedinke");
            int myJobSize = communicator.receiveReplaceIndividuals(myJob, MASTER);
//            System.out.println("Primio " + myJobSize + " jedinki");
            while(myJobSize > 0) {
                for (Individual individual : myJob) { evaluate(individual); }
                communicator.sendFitness(myJob, MASTER, myJobSize);
                myJobSize = communicator.receiveReplaceIndividuals(myJob, MASTER);
            }
        }
    }
    
}
