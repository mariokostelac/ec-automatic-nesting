package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;
import java.util.Vector;

/**
 * PBX križanje gradi dijete tako da se prvo nasumične vrijednosti
 * iz prvog roditelja kopiraju na jednaka mjesta u djetetu, a nakon
 * toga se na ostala mjesta kopiraju vrijednosti od drugog roditelja.
 *
 * @author Rene Huić
 */
public class PermutationCrxPBX extends CrossoverOp{

    public PermutationCrxPBX(State state){
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.PBX"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.PBX", String.valueOf(probability));
    }

    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        Permutation genotip1 = (Permutation) gen1;
        Permutation genotip2 = (Permutation) gen2;
        Permutation dijete = new Permutation(state);
        dijete.setSize(genotip1.getSize());

        Vector<Boolean> taged = new Vector<Boolean>();
        taged.setSize(genotip1.getSize());
        int count = 0;
        
        Random random = random = state.getRandomizer();

        for (int i = 0; i < dijete.getSize(); i++) {
            if (random.nextBoolean()) {
                dijete.setElement(i, genotip1.getElement(i));
                taged.set(genotip1.getElement(i), true);
                count++;
            } else {
                taged.set(genotip1.getElement(i), false);
                dijete.setElement(i, -1);
            }
        }

        int indCh = 0;
        count = dijete.getSize() - count;
        for (int i = 0; i < dijete.getSize() && count > 0; i++) {
            //trazimo prazno mjesto u djetetu
            while (dijete.getElement(indCh) != -1) {
                indCh++;
            }
            //ako element iz drugog roditelja nije vec u djetetu onda ga kopiramo
            if (!taged.get(genotip2.getElement(i))) {
                dijete.setElement(indCh, genotip2.getElement(i));
                count--;
            }
        }
        return dijete;
    }
}
