package ecf.genotype.binary;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;

/**
 * Razred koji implementira križanje binarnog
 * genotipa. Koristi se križanje s jednom točkom
 * prekida.
 *
 * @author Rene Huić
 */
public class BinaryCrxOnePoint extends CrossoverOp {

   public BinaryCrxOnePoint(State state) {
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
        Binary genotip1 = (Binary) gen1;
        Binary genotip2 = (Binary) gen2;
        Binary dijete = new Binary(state, genotip1.getNumBits(), genotip1.getnDimension());
        dijete.initialize();

        Random random = state.getRandomizer();
        
        int pozicija = random.nextInt(genotip1.getNumBits());
        for (int i = 0; i < genotip1.getVariablesSize(); i++) {
            switch (random.nextInt(2)) {
                case 0:
                    for (int j = 0; j < pozicija; j++) {		
                        dijete.setVariables(genotip1.getVariables(i, j), i, j);
                    }
                    for (int j = pozicija; j < genotip1.getNumBits(); j++) {
                        dijete.setVariables(genotip2.getVariables(i, j), i, j);
                    }
                    break;
                case 1:
                    for (int j = 0; j < pozicija; j++) {
                        dijete.setVariables(genotip2.getVariables(i, j), i, j);
                    }
                    for (int j = pozicija; j < genotip1.getNumBits(); j++) {
                        dijete.setVariables(genotip1.getVariables(i, j), i, j);
                    }
                    break;

            }
        }
        dijete.update();
        return dijete;
    }
}
