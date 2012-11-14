package ecf.genotype.binary;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;

/**
 * Uniformno križanje binarnog genotipa.
 * Dijete nasljeđuje bit roditelja ako su
 * jednaki, a inače je bit nasumičan.
 *
 * @author Rene Huić
 */
public class BinaryCrxUniform extends CrossoverOp{

    public BinaryCrxUniform(State state) {
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
        Binary genotip1 = (Binary) gen1;
        Binary genotip2 = (Binary) gen2;
        Binary dijete = new Binary(state, genotip1.getNumBits(), genotip1.getnDimension());
        dijete.initialize();
        
        Random random = random = state.getRandomizer();
                
        for(int i = 0; i < genotip1.getVariablesSize(); i++){
            for(int j = 0; j < genotip1.getNumBits(); j++){
                if(genotip1.getVariables(i, j) == genotip2.getVariables(i, j)){
                    dijete.setVariables(genotip1.getVariables(i, j), i, j);
                }
                else {
                    dijete.setVariables(random.nextBoolean(), i, j);
                }
            }
        }
        dijete.update();
        
        return dijete;
    }
}
