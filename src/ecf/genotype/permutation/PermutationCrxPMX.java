package ecf.genotype.permutation;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * PMX križanje gradi dijete tako da odabire podniz od jednog roditelja i
 * očuva poziciju i red što je više moguće elemenata drugog roditelja.
 * Podniz roditelja se selektira nasumičnim odabirom dva mjesta rezanja,
 * koja služe kao granica kod zamjene.
 * 
 * @author Rene Huić
 */
public class PermutationCrxPMX extends CrossoverOp {

    public PermutationCrxPMX(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.PMX"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.PMX", String.valueOf(probability));
    }
    
    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        
        Random random = random = state.getRandomizer();
        
        Permutation genotip1 = (Permutation) gen1;
        Permutation genotip2 = (Permutation) gen2;
        Permutation dijete = new Permutation(state);
        dijete.setSize(genotip1.getSize());
        
        int start = random.nextInt(genotip1.getSize() - 1);
        int end = start + random.nextInt(genotip1.getSize() - 1 - start);
        
        Map<Integer, Integer> subSet = new HashMap<Integer, Integer>();
        for (int i = start; i <= end; i++) {
            dijete.setElement(i, genotip1.getElement(i));
            subSet.put(genotip1.getElement(i), genotip2.getElement(i));
        }
        
        for(int i = 0; i < genotip1.getSize(); i++){
            if(i >= start && i <= end)
                continue;
            int value = genotip2.getElement(i);
            for(int j = 0; j < subSet.size(); j++){
                if(!subSet.containsKey(value)){
                    break;
                }
                else {
                    value = subSet.get(value);
                }
            }
            dijete.setElement(i, value);
        }
        return dijete;
    }
}
