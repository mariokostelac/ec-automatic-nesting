package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;

/**
 * Uniformno križanje floating point genotipa.
 * Dijete nasljeđuje element roditelja ako su
 * jednaki, a inače je element nasumičan.
 *
 * @author Rene Huić
 */
public class FloatingPointCrxUniform extends CrossoverOp{

    public FloatingPointCrxUniform(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.uniform"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.uniform", String.valueOf(probability));
    }

    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        FloatingPoint genotip1 = (FloatingPoint) gen1;
        FloatingPoint genotip2 = (FloatingPoint) gen2;
        FloatingPoint dijete = new FloatingPoint(state, genotip1.getnDimension());
        dijete.initialize();
        
        Random random = random = state.getRandomizer();

        for (int i = 0; i < genotip1.getnDimension(); i++) {
            if (genotip1.getNumber(i) == genotip2.getNumber(i)) {
                dijete.setNumber(genotip1.getNumber(i), i);
            } else {
                double broj = genotip1.minValue + ((double) random.nextInt(
                        (int) (genotip1.maxValue - genotip1.minValue)) + random.nextDouble());
                if (broj > genotip1.getMaxValue()) {
                    broj = genotip1.getMaxValue();
                }
                dijete.setNumber(broj, i);
            }
        }
        return dijete;
    }

}
