package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import ecf.operators.Operator;
import ecf.operators.TermMaxGenOp;
import java.util.Random;

/**
 * Mutacija floating point prikaza, koja mijenja jedan
 * element vektora na taj način, da pri nižim generacijama
 * se uniformno pretražuje prostor rješenja, dok se pri 
 * višim generacijama taj prostor smanjuje na lokalni.
 *
 * @author Rene Huić
 */
public class FloatingPointMutNonUniform extends MutationOp{

    //sistemski parametar koji određuje stupanj ovisnosti
    //o broju generacije u kojoj se algoritam nalazi
    double b;

    public FloatingPointMutNonUniform(State state) {
        super(state);
    }

    @Override
    public void initialize() {
        probability = Double.valueOf(myGenotype.getParameterValue("mut.nonuniform"));
        b = Double.valueOf(myGenotype.getParameterValue("nonuniform.b"));
    }

    @Override
    public void registerParameters() {
        myGenotype.registerParameter("mut.nonuniform", String.valueOf(probability));
        myGenotype.registerParameter("nonuniform.b", String.valueOf(b));
    }

    @Override
    public void mutate(Genotype ind) {
        int maxgen = 0;
        
        Random random = random = state.getRandomizer();
        
        FloatingPoint genotip = (FloatingPoint) ind;
        int pozicija = random.nextInt(genotip.getnDimension());

        for(Operator operator : state.getTerminatingOperators()){
            if(operator instanceof TermMaxGenOp){
                maxgen = ((TermMaxGenOp)operator).getMaxGeneration();
            }
        }

        if(maxgen == 0) return;

        if(random.nextBoolean()){
            double newGen = delta(genotip.getNumber(pozicija) - genotip.getMinValue(),
                    maxgen, state.getGenerationNo());
            genotip.setNumber(genotip.getNumber(pozicija) - newGen, pozicija);
        }
        else {
            double newGen = delta(genotip.getMaxValue() - genotip.getNumber(pozicija),
                    maxgen, state.getGenerationNo());
            genotip.setNumber(genotip.getNumber(pozicija) + newGen, pozicija);
        }
    }

    /**
     * Metoda koja izračunava delta vrijednost potrebnu za
     * neuniformnu mutaciju. Parametar Y može predstavljati
     * između gornje granice i trenutne vrijednosti elementa iz FP
     * genotipa ili razliku trenutne vrijednosti i donje
     * granice. Koja će biti vrijednost se odabire nasumično.
     * 
     * @param y Parametar Y
     * @param maxgen Maksimalni broj generacije
     * @param t Trenutna generacija
     * @return Delta vrijednost
     */
    private double  delta(double y, int maxgen, int t){
        double rez = 0;
        
        Random random = random = state.getRandomizer();
        
        double r = random.nextDouble();

        rez = y*(1 - Math.pow(r, Math.pow(1 - t/maxgen, b)));

        return rez;
    }

}
