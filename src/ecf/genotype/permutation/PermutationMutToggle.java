package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Mutacija permutacijskog niza. Prvo se nasumično
 * odaberu dva elementa iz permutacijskog niza, te
 * se zamijene.
 *
 * @author Rene Huić
 */
public class PermutationMutToggle extends MutationOp{

    public PermutationMutToggle(State state){
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("mut.toggle"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.toggle", String.valueOf(probability));
    }

    @Override
    public void mutate(Genotype ind) {
        
        Random random = random = state.getRandomizer();
        
        Permutation perm = (Permutation) ind;

        int ind1 = random.nextInt(perm.getSize() - 1);
        int ind2 = ind1 + random.nextInt(perm.getSize() - 1 - ind1);

        int tmp = perm.getElement(ind1);
        perm.setElement(ind1, perm.getElement(ind2));
        perm.setElement(ind2, tmp);
    }

}
