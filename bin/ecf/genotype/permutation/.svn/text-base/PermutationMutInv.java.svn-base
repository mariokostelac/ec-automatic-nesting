package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Mutacija permutacijskog niza. Prvo se nasumično odredi
 * podniz jedinke, te se tada mijenjaju mjesta elemenata.
 * Prvo prvi i zadnji element podniza, tada sljedeći itd.
 * 
 * @author Rene Huić
 */
public class PermutationMutInv extends MutationOp {

    public PermutationMutInv(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("mut.inv"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.inv", String.valueOf(probability));
    }

    @Override
    public void mutate(Genotype ind) {
        
        Random random = random = state.getRandomizer();
        
        Permutation perm = (Permutation) ind;

        int ind1 = random.nextInt(perm.getSize() - 1);
        int ind2 = ind1 + random.nextInt(perm.getSize() - 1 - ind1);
        int tmp;
        int distance = ind2 - ind1 + 1;

        for (int i = 0; i < distance / 2; i++) {
            tmp = perm.getElement(ind1 + i);
            perm.setElement(ind1 + i, perm.getElement(ind2 - i));
            perm.setElement(ind2 - i, tmp);
        }
    }
}
