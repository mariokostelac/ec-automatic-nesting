package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Razred koji implementira mutaciju floating point
 * genotipa. Mutira se jedan element koji se određuje
 * slučajnim odabirom. Nova vrijednost elementa se
 * postavlja potpuno nasumično unutar zadanih
 * intervala.
 *
 * @author Rene Huić
 */
public class FloatingPointMutSimple extends MutationOp{

    public FloatingPointMutSimple(State state) {
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
    public void mutate(Genotype ind) {
        
        Random random = random = state.getRandomizer();
        
        FloatingPoint genotip = (FloatingPoint) ind;
        int pozicija = random.nextInt(genotip.getnDimension());
        double broj = genotip.minValue + ((double) random.nextInt(
                (int) (genotip.maxValue - genotip.minValue)) + random.nextDouble());
        if (broj > genotip.getMaxValue()) {
            broj = genotip.getMaxValue();
        }
        genotip.setNumber(broj, pozicija);
    }

}
