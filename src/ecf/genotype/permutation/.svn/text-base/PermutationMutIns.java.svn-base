package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Mutacija permutacijskog niza. Prvo se nasumično odredi
 * podniz jedinke, tada se elementi podniza posmaknu u desno,
 * pri čemu se najdesniji element postavi na prvo mjesto
 * podniza.
 *
 * @author Rene Huić
 */
public class PermutationMutIns extends MutationOp{

    public PermutationMutIns(State state){
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("mut.ins"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.ins", String.valueOf(probability));
    }

    @Override
    public void mutate(Genotype ind) {
        
        Random random = random = state.getRandomizer();
        
        Permutation perm = (Permutation) ind;
        int ind1 = random.nextInt(perm.getSize() - 1);
        int ind2 = ind1 + random.nextInt(perm.getSize() - 1 - ind1);
        int tmp = perm.getElement(ind2);

        for(int i = ind2; i > ind1; i--){
            perm.setElement(i, perm.getElement(i - 1));
        }
        if(ind1 != ind2){
            perm.setElement(ind1 + 1, tmp);
        }
    }
}
