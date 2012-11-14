package ecf.genotype.bitstring;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Razred koji implementira mutaciju bitstring
 * genotipa. Mutira se jedan bit koji se određuje
 * slučajnim odabirom.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class BitStringMutSimple extends MutationOp {

    public BitStringMutSimple(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.valueOf(myGenotype.getParameterValue("mut.simple"));

    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.simple", String.valueOf(probability));
    }

    @Override
    public void mutate(Genotype gene) {
        BitString genotip = (BitString) gene;
        
        Random random = random = state.getRandomizer();
        
        int pozicija = random.nextInt(genotip.size());
        genotip.set(pozicija, (byte) ((genotip.get(pozicija) + 1) % 2));
    }
}
