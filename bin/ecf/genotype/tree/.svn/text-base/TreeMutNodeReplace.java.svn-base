package ecf.genotype.tree;

import ecf.State;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;


/**
 * Mutacija tree genotipa. Zamjenjuje jedan primitiv
 * slučajno odabranog čvora stabla sa drugim primitivom
 * stabla koji ima isti broj argumenata.
 * 
 * @author Marko Pielić
 */
public class TreeMutNodeReplace extends MutationOp{

	public TreeMutNodeReplace(State state) {
		super(state);
	}

	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("mut.nodereplace"));
	}

	@Override
	public void mutate(Genotype ind) {
		Tree tree = (Tree)ind;
        
        Random random = random = state.getRandomizer();
		
		//odaberi proizvoljni čvor stabla
		int origNodeIndex = random.nextInt(tree.size());
		Primitive<?> origPrimitive = tree.get(origNodeIndex).getPrimitive();
		String origNodeName = origPrimitive.getName();
		int origNodeNArguments = origPrimitive.getNArguments(); 
		
		//probaj odabrati primitiv od drugog čvora sa istim brojem argumenata
		Primitive<?> primitive;
		int tries = 0;
		do{
			primitive = tree.getPrimitiveSet().getRandomPrimitive();
			tries++;
		}
		while(origNodeName.equals(primitive.getName()) || 
				origNodeNArguments!=primitive.getNArguments() && tries <4);
		
		if(origNodeName.equals(primitive.getName()) || origNodeNArguments!=primitive.getNArguments()){
			state.getLogger().log(5, "TreeMutNodeReplace not successful");
			return;
		}
		
		tree.get(origNodeIndex).setPrimitive(primitive);
		state.getLogger().log(5, "TreeMutNodeReplace successful (oldNode = " + origNodeName + ", " + 
				"newNode = " + 	primitive.getName() + ")");
	}

	@Override
	public void registerParameters() {
		myGenotype.registerParameter("mut.nodereplace", String.valueOf(probability));
	}
}
