package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;
import java.util.Vector;

/**
 * OX križanje gradi dijete tako da odabire podniz od jednog
 * roditelja i očuva poziciju i red što je više moguće elemenata
 * drugog roditelja.
 * 
 * @author Rene Huić
 */
public class PermutationCrxOX extends CrossoverOp {

    public PermutationCrxOX(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.OX"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.OX", String.valueOf(probability));
    }

    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        
        Random random = random = state.getRandomizer();
        
        Permutation genotip1 = (Permutation) gen1;
        Permutation genotip2 = (Permutation) gen2;
        Permutation dijete = new Permutation(state);
        dijete.setSize(genotip1.getSize());

        int ind1 = random.nextInt(genotip1.getSize() - 1);
        int ind2 = ind1 + random.nextInt(genotip1.getSize() - 1 - ind1);

        Vector<Integer> temp = new Vector<Integer>(genotip2.getVariables());

        for (int i = ind1; i <= ind2; i++) {
            dijete.setElement(i, genotip1.getElement(i));
            temp.removeElementAt(temp.indexOf(genotip1.getElement(i)));
        }

        int index = ind2;
        int index2 = ind1;
        while(true){
            index = (index + 1) % genotip1.getSize();
            index2 = (index2 + 1) % temp.size();
            if(index == ind1)
                break;
            dijete.setElement(index, temp.get(index2));
        }
        return dijete;
    }
}
