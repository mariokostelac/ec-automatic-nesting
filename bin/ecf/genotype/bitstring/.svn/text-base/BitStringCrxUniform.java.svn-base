package ecf.genotype.bitstring;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;


/**
 * Uniformno križanje bitstring genotipa.
 * Dijete nasljeđuje bit roditelja ako su 
 * jednaki, a inače je bit nasumičan.
 * 
 * @author Marko Pielić
 */
public class BitStringCrxUniform extends CrossoverOp{

	public BitStringCrxUniform(State state) {
		super(state);
	}

	
	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("crx.uniform"));
	}

	
	@Override
	public void registerParameters() {
		myGenotype.registerParameter("crx.uniform", String.valueOf(probability));
	}
	
	
	@Override
	public Genotype mate(Genotype ind1, Genotype ind2) {
		BitString p1 = (BitString)(ind1);
		BitString p2 = (BitString)(ind2);
		BitString ch = new BitString(state, p1.size());
        
        Random random = random = state.getRandomizer();

		for(int i = 0; i < p1.size(); i++){
			if(p1.get(i)==p2.get(i)) ch.set(i, p1.get(i));
			else ch.set(i, (byte)random.nextInt(2));
		}
		return ch;
	}
}
