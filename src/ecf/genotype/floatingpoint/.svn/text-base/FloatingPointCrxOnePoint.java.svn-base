package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;

/**
 * Razred koji implementira križanje floating point
 * genotipa. Koristi se križanje s jednom točkom
 * prekida.
 *
 * @author Rene Huić
 */
public class FloatingPointCrxOnePoint extends CrossoverOp{

    public FloatingPointCrxOnePoint(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.onepoint"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.onepoint", String.valueOf(probability));
    }

    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        FloatingPoint genotip1 = (FloatingPoint) gen1;
        FloatingPoint genotip2 = (FloatingPoint) gen2;
        FloatingPoint dijete = new FloatingPoint(state, genotip1.getnDimension());
        dijete.initialize();
        
        Random random = random = state.getRandomizer();
        
        int pozicija = random.nextInt(genotip1.getnDimension());

        switch (random.nextInt(2)) {
            case 0:
                for (int j = 0; j < pozicija; j++) {
                    dijete.setNumber(genotip1.getNumber(j), j);
                }
                for (int j = pozicija; j < genotip1.getnDimension(); j++) {
                    dijete.setNumber(genotip2.getNumber(j), j);
                }
                break;
            case 1:
                for (int j = 0; j < pozicija; j++) {
                    dijete.setNumber(genotip2.getNumber(j), j);
                }
                for (int j = pozicija; j < genotip1.getnDimension(); j++) {
                    dijete.setNumber(genotip1.getNumber(j), j);
                }
                break;

        }
        return dijete;
    }

}
