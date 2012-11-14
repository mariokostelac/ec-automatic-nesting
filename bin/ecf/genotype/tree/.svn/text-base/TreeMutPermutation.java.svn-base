package ecf.genotype.tree;

import java.util.Vector;
import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;


/**
 * Mutacija tree genotipa. Odabire funkciju koja 
 * sadrži barem dva argumenta i njena slučajno
 * odabrana dva podstabla zamjenjuje.
 * 
 * @author Marko Pielić
 */
public class TreeMutPermutation extends MutationOp{

	public TreeMutPermutation(State state) {
		super(state);
	}

	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("mut.permutation"));
	}

	@Override
	public void mutate(Genotype ind) {
		Tree tree = (Tree)ind;
		int iNode, nArgs;
        
        Random random = random = state.getRandomizer();
		
		Vector<Integer> indeksi = new Vector<Integer>(); //indeksi čvorova čije funkcije imaju bar 2 argumenta
		for(int i=0;i<tree.size();i++){
			if(tree.get(i).getPrimitive().getNArguments()>1) indeksi.add(i);
		}
		
		if(indeksi.isEmpty()) return;	//sve funkcije imaju samo 1 argument, ne mogu ništa mutirati
		
		//odaberi čvor čiji primitiv ima barem 2 argumenta
		int slucajni = random.nextInt(indeksi.size());
		iNode = indeksi.get(slucajni);
		nArgs = tree.get(iNode).getPrimitive().getNArguments();
		
		//izgradi permutacijski vektor
		Vector<Integer> permutation = new Vector<Integer>(nArgs);
		for(int i=0;i<nArgs;i++){
			permutation.add(i);
		}
		
		int ind1, ind2, temp;
		for(int i=0;i<nArgs-1;i++){
			ind1 = permutation.get(i);
			ind2 = random.nextInt(nArgs-i) + i;
			temp = permutation.get(ind1);
			permutation.set(ind1, permutation.get(ind2));
			permutation.set(ind2, temp);
		}

		//promijeni pomake čvorova djeteta
		Vector<Integer> offsets = new Vector<Integer>();
		offsets.add(1);
		for(int i=1;i<nArgs;i++){
			int offset = offsets.get(i-1);
			offsets.add(offset + tree.get(iNode+offset).size());
		}

		//promijeni poredak mutiranih čvorova
		Vector<Node> mutNodes = new Vector<Node>();
		for(int i=0;i<tree.get(iNode).size();i++){
			mutNodes.add(tree.get(iNode+i));
		}
		int iCopiedTo = 1;
		for(int iArg=0;iArg<nArgs;iArg++){
			int nNodes = mutNodes.get(offsets.get(permutation.get(iArg))).size();
			for(int i=0;i<nNodes;i++){
				tree.set(iNode+iCopiedTo, mutNodes.get(offsets.get(permutation.get(iArg)) + i));
				iCopiedTo++;
			}
		}
		tree.update(tree.getPrimitiveSet());
	}

	@Override
	public void registerParameters() {
		myGenotype.registerParameter("mut.permutation", String.valueOf(probability));
	}

}
