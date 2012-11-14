package ecf.genotype.binary;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;

/**
 * Razred koji implementira mutaciju binarnog
 * genotipa. Mutira se jedan bit koji se određuje
 * slučajnim odabirom.
 *
 * @author Rene Huić
 */
public class BinaryMutSimple extends MutationOp {

    private double bitProb;
    private boolean bUseBitProb;

    public BinaryMutSimple(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.valueOf(myGenotype.getParameterValue("mut.simple"));
        bitProb = Double.valueOf(myGenotype.getParameterValue("simple.bitprob"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.simple", String.valueOf(probability));
        myGenotype.registerParameter("simple.bitprob", String.valueOf(bitProb));

        //parametarDefined?? O.o
    }

    @Override
    public void mutate(Genotype ind) {
        Binary genotip = (Binary)ind;

        Random random = random = state.getRandomizer();
        
        if(bUseBitProb){
            for(int i = 0; i < genotip.getVariablesSize(); i++){
                for(int j = 0; j < genotip.getNumBits(); j++){
                    if(random.nextDouble() < bitProb){
                        genotip.setVariables(genotip.getVariables(i, j), i, j);
                    }
                }
            }
        }
        else {
            int bit = random.nextInt(genotip.getNumBits());
            int dimenzija = random.nextInt(genotip.getVariablesSize());
            genotip.setVariables(genotip.getVariables(dimenzija, bit), dimenzija, bit);
        }
        genotip.update();
    }
}
