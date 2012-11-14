package ecf.genotype.tree;

import java.util.Vector;
import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;


/**
 * Križanje tree genotipa.
 * Slučajnim se odabirom odabire točka križanja na svakom
 * od roditelja. Dijete nastaje tako da se kopija podstabla 
 * čiji je korijen točka križanja jednog roditelja zamjenjuje 
 * sa kopijom podstabla čiji je korijen točka križanja drugog 
 * roditelja. 
 * 
 * Kopije se koriste da se izbjegne dovođenje roditelja u 
 * nekonzistentno stanje te se na taj način omogućuje da 
 * jedan roditelj da više djece.
 * 
 * @author Marko Pielić
 */
public class TreeCrxSimple extends CrossoverOp{
	
	/**Vjerojatnost odabira funkcija pri križanju*/
	private double functionProb;

	public TreeCrxSimple(State state) {
		super(state);
	}

	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("crx.simple"));
		functionProb = Double.valueOf(myGenotype.getParameterValue("functionprob"));
	}

	@Override
	public Genotype mate(Genotype ind1, Genotype ind2) {
		Tree male = (Tree)ind1;
		Tree female = (Tree)ind2;
		Tree child = new Tree(state);

		int mIndex, fIndex;
		int mRange, fRange;
		int mNodeDepth, fNodeDepth, fNodeDepthSize;

		mRange = male.size();
		fRange = female.size();
		
		//pronađi listove i funkcije prvog roditelja
		Vector<Integer> maleLeafIndexes = new Vector<Integer>();
		Vector<Integer> maleNonLeafIndexes = new Vector<Integer>();
		for(int i=0;i<mRange;i++){
			if(male.get(i).size()==1) maleLeafIndexes.add(i);
			else maleNonLeafIndexes.add(i);
		}
		
		//pronađi listove i funkcije drugog roditelja
		Vector<Integer> femaleLeafIndexes = new Vector<Integer>();
		Vector<Integer> femaleNonLeafIndexes = new Vector<Integer>();
		for(int i=0;i<fRange;i++){
			if(female.get(i).size()==1) femaleLeafIndexes.add(i);
			else femaleNonLeafIndexes.add(i);
		}
        
        Random random = random = state.getRandomizer();

		int nTries = 0;
		while(true){
			//odaberi točku križanja prvog roditelja
			if(random.nextDouble()>functionProb){
				mIndex = maleLeafIndexes.get(random.nextInt(maleLeafIndexes.size()));
			}
			else{
				mIndex = maleNonLeafIndexes.get(random.nextInt(maleNonLeafIndexes.size()));
			}
			
			//odaberi točku križanja drugog roditelja
			if(random.nextDouble()>functionProb){
				fIndex = femaleLeafIndexes.get(random.nextInt(femaleLeafIndexes.size()));
			}
			else{
				fIndex = femaleNonLeafIndexes.get(random.nextInt(femaleNonLeafIndexes.size()));
			}
			
			mNodeDepth = male.get(mIndex).getDepth();
			fNodeDepth = female.get(fIndex).getDepth();
			
			//pronađi najveću dubinu
			int depth, maxDepth = fNodeDepth;
			for(int i=0;i<female.get(fIndex).size();i++){
				depth = female.get(fIndex+i).getDepth();
				maxDepth = depth>maxDepth ? depth : maxDepth;
			}
			
			fNodeDepthSize = maxDepth - fNodeDepth;
			int novaDubina = mNodeDepth+fNodeDepthSize;
			nTries++;
			
			if(novaDubina<=male.getMaxDepth() && novaDubina>male.getMinDepth()) break;
			if(nTries>4){
				state.getLogger().log(5, "TreeCrxSimple not successful");
				if(random.nextBoolean()) return male.copy();
				return female.copy();
			}
		}
		
		//kopiraj podatke jednog roditelja
		child.setMaxDepth(male.getMaxDepth());
		child.setMinDepth(male.getMinDepth());
		child.setStartDepth(male.getStartDepth());

		//kopiraj genetski materijal iz prvog roditelja
		for(int i=0;i<mIndex;i++){
			Node node = new Node(male.get(i).getPrimitive());
			node.setDepth(male.get(i).getDepth());
			child.add(node);
		}
		
		//kopiraj genetski materijal iz drugog roditelja
		for(int i=0;i<female.get(fIndex).size();i++){
			Node node = new Node(female.get(fIndex+i).getPrimitive());
			child.add(node);
		}
		
		//kopiraj ostatak iz prvog roditelja
		for(int i=mIndex+male.get(mIndex).size();i<mRange;i++){
			Node node = new Node(male.get(i).getPrimitive());
			child.add(node);
		}
		
		child.update(new PrimitiveSet(male.getPrimitiveSet()));
		return child;
	}

	@Override
	public void registerParameters() {
		myGenotype.registerParameter("crx.simple", String.valueOf(probability));
		myGenotype.registerParameter("functionprob", String.valueOf(probability));
	}
}
