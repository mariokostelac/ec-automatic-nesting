package ecf.genotype.bitstring;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;

/**
 * Razred koji implementira križanje bitstring
 * genotipa. Koristi se križanje s jednom točkom
 * prekida.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class BitStringCrxOnePoint extends CrossoverOp {


	/**
	 * Konstruktor. Poziva konstruktor CrossoverOp.
	 * @param state
	 */
	public BitStringCrxOnePoint(State state) {
		super(state);
	}


	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("crx.onepoint"));
	}

	
	@Override
	public void registerParameters() {
		myGenotype.registerParameter("crx.onepoint", String.valueOf(probability));
	}
	
	
	@Override
	public Genotype mate(Genotype gen1, Genotype gen2) {
		BitString genotip1 = (BitString) gen1;
		BitString genotip2 = (BitString) gen2;
		int i;
        
            Random random = random = state.getRandomizer();

		int pozicija = random.nextInt(genotip1.size());
		BitString novi = new BitString(state, genotip1.size());
		switch(random.nextInt(2)){
		case 0:
			for (i = 0; i < pozicija; i++) {		//uzimaju se lijevi bitovi od prvog
				novi.set(i, genotip1.get(i));		//i desni od drugog genotipa
			}
			for (i = pozicija; i < genotip1.size(); i++) {
				novi.set(i, genotip2.get(i));
			}
			break;
		case 1:
			for (i = 0; i < pozicija; i++) {		//uzimaju se lijevi bitovi od drugog
				novi.set(i, genotip2.get(i));		//i desni od prvog genotipa
			}
			for (i = pozicija; i < genotip1.size(); i++) {
				novi.set(i, genotip1.get(i));
			}
			break;

		}
		return novi;
	}

}
