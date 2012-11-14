package ecf.genotype.binary;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Mutacija binarnog genotipa. Određuje dvije
 * točke na genotipu i bitove između tih točaka
 * preslaguje slučajnim odabirom, ali tako da
 * ukupan broj jedinica i ukupan broj nula između
 * tih točaka ostanu isti kao u roditeljskom genotipu.
 *
 * @author Rene Huić
 */
public class BinaryMutMix extends MutationOp {

    public BinaryMutMix(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.valueOf(myGenotype.getParameterValue("mut.mix"));

    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.mix", String.valueOf(probability));
    }

    @Override
    public void mutate(Genotype ind) {
        Binary genotip = (Binary) ind;
        
        Random random = random = state.getRandomizer();
                
        int dimenzija = random.nextInt(genotip.getVariablesSize());

        int bitIndexSmaller = random.nextInt(genotip.getNumBits());
        int bitIndexBigger;

        // osiguravamo nejednakost brojaca
        do {
            bitIndexBigger = random.nextInt(genotip.getNumBits());
        } while (bitIndexSmaller == bitIndexBigger);

        //osiguravamo da je Smaller<Bigger
        int tmp = bitIndexSmaller;
        if (bitIndexSmaller > bitIndexBigger) {
            bitIndexSmaller = bitIndexBigger;
            bitIndexBigger = tmp;
        }

        // brojaci jedinica i nula unutar segmenta kromosoma
        int counter0 = 0;
        int counter1 = 0;

        for (int i = bitIndexSmaller; i <= bitIndexBigger; i++) {
            if (genotip.getVariables(dimenzija, i)) {
                counter1++;
            } else {
                counter0++;
            }
        }

        // varijable koje osiguravaju pravednu raspodjelu
        int fairness0 = counter0;
        int fairness1 = counter1;

        // izvrsavamo mutaciju nad segmentom
        for (int i = bitIndexSmaller; i <= bitIndexBigger; i++) {
            int rnd = random.nextInt(fairness0 + fairness1) + 1;
            if (rnd <= fairness1) {
                if (counter1 > 0) {
                    genotip.setVariables(true, dimenzija, i);
                    counter1--;
                } else {
                    genotip.setVariables(false, dimenzija, i);
                    counter0--;
                }
            } else {
                if (counter0 > 0) {
                    genotip.setVariables(false, dimenzija, i);
                    counter0--;
                } else {
                    genotip.setVariables(true, dimenzija, i);
                    counter1--;
                }
            }
        }
        genotip.update();
    }
}
