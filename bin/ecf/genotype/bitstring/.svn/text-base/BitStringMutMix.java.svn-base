package ecf.genotype.bitstring;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;


/**
 * Mutacija bitstring genotipa. Određuje dvije
 * točke na genotipu i bitove između tih točaka
 * preslaguje slučajnim odabirom, ali tako da
 * ukupan broj jedinica i ukupan broj nula između
 * tih točaka ostanu isti kao u roditeljskom genotipu.
 * 
 * @author Marko Pielić
 *
 */
public class BitStringMutMix extends MutationOp{

	public BitStringMutMix(State state) {
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
		BitString bitstring = (BitString)ind;
        
        Random random = random = state.getRandomizer();
		
		int bitIndexSmaller = random.nextInt(bitstring.size());
		int bitIndexBigger;
		
	    // osiguravamo nejednakost brojaca
		do {
			bitIndexBigger = random.nextInt(bitstring.size());
		} while (bitIndexSmaller == bitIndexBigger);

		//osiguravamo da je Smaller<Bigger
		int tmp = bitIndexSmaller;
		if(bitIndexSmaller > bitIndexBigger){
			bitIndexSmaller = bitIndexBigger;
			bitIndexBigger = tmp;
		}

		// brojaci jedinica i nula unutar segmenta kromosoma
		int counter0 = 0;
		int counter1 = 0;

		for (int i = bitIndexSmaller; i <= bitIndexBigger; i++){
			if(bitstring.get(i) == 1) counter1++;
			else counter0++;
		}

		// varijable koje osiguravaju pravednu raspodjelu
		int fairness0 = counter0;
		int fairness1 = counter1;

		// izvrsavamo mutaciju nad segmentom
		for (int i = bitIndexSmaller; i <= bitIndexBigger; i++){
			int rnd = random.nextInt(fairness0 + fairness1) + 1;
			if(rnd <= fairness1){
				if(counter1 > 0){
					bitstring.set(i, (byte)1);
					counter1--;
				}
				else{
					bitstring.set(i, (byte)0);
					counter0--;
				}
			}
			else{
				if(counter0 > 0){
					bitstring.set(i, (byte)0);
					counter0--;
				}
				else{
					bitstring.set(i, (byte)1);
					counter1--;
				}
			}
		}
	}
}
