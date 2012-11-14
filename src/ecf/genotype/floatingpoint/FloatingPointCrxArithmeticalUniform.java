package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;

/**
 * Križanje koje gradi dijete kao linearnu kombinaciju
 * njegovih roditelja. S obzirom da je a konstantan, 
 * križanje je uniformno.
 *
 * @author Rene Huić
 */
public class FloatingPointCrxArithmeticalUniform extends CrossoverOp{

    //konstanta kod linearne kombinacije iz [0,1]
    double a;

    public FloatingPointCrxArithmeticalUniform(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.parseDouble(myGenotype.getParameterValue("crx.arithmeticaluniform"));
        a = Double.parseDouble(myGenotype.getParameterValue("arithmeticaluniform.a"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("crx.arithmeticaluniform", String.valueOf(probability));
        myGenotype.registerParameter("arithmeticaluniform.a", String.valueOf(a));
    }

    @Override
    public Genotype mate(Genotype gen1, Genotype gen2) {
        FloatingPoint genotip1 = (FloatingPoint) gen1;
        FloatingPoint genotip2 = (FloatingPoint) gen2;
        FloatingPoint dijete = new FloatingPoint(state, genotip1.getnDimension());
        dijete.initialize();

        for(int i = 0; i < genotip1.getnDimension(); i++){
            double value = a * genotip1.getNumber(i) + (1 - a) * genotip2.getNumber(i);
            if(value < genotip1.getMinValue())
                value = genotip1.getMinValue();
            else if(value > genotip1.getMaxValue())
                value = genotip1.getMaxValue();
            dijete.setNumber(value, i);
        }
        return dijete;
    }

}
