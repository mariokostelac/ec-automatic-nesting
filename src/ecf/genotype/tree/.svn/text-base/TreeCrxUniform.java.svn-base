package ecf.genotype.tree;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import java.util.Random;


/**
 * Uniformno križanje tree genotipa.
 * Čvorovi djeteta se određuju slučajnim odabirom pripadnih
 * čvorova roditelja ako se oni nalaze u zajedničkom području
 * (područje u kojem oba roditelja imaju istu strukturu). Ako
 * se čvor nalazi na dnu zajedničkog područja, tada se cijelo
 * podstablo kopira iz odabranog roditelja.
 * 
 * Ovakvim načinom križanja postiže se veća izmjena genetskog
 * materijala pri vrhu stabla.
 * 
 * @author Marko Pielić
 */
public class TreeCrxUniform extends CrossoverOp {

	public TreeCrxUniform(State state) {
		super(state);
	}

	@Override
	public void initialize() {
		probability = Double.valueOf(myGenotype.getParameterValue("crx.uniform"));
	}

	@Override
	public Genotype mate(Genotype ind1, Genotype ind2) {
		Tree male = (Tree)ind1;
		Tree female = (Tree)ind2;
		Tree child = new Tree(state);
		
		int mRange = male.size();
		int fRange = female.size();
		
		//kopiraj podatke jednog roditelja
		child.setMaxDepth(male.getMaxDepth());	
		child.setMinDepth(male.getMinDepth());
		child.setStartDepth(male.getStartDepth());
		
        
        Random random = random = state.getRandomizer();
		
		int iMale = 0, iFemale = 0, iChild = 0;
		while(iMale<mRange && iFemale<fRange){
			
			boolean pickMale = random.nextBoolean();				//odaberi čvor muškog ili ženskog roditelja
			if(male.get(iMale).getPrimitive().getNArguments() == 		//ako oba roditelja imaju
				female.get(iFemale).getPrimitive().getNArguments()){	//isti broj argumenata
				if(pickMale){											//izaberi jednog
					Node node = new Node(male.get(iMale).getPrimitive());
					node.setDepth(male.get(iMale).getDepth());
					child.add(node);
				}
				else{
					Node node = new Node(female.get(iFemale).getPrimitive());
					node.setDepth(female.get(iFemale).getDepth());
					child.add(node);
				}
				iMale++;
				iFemale++;
				iChild++;
			}
			else{													//inače kopiraj podstablo u dijete
				if(pickMale){
					for(int i=0;i<male.get(iMale).size();i++){
						iChild++;
						Node node = new Node(male.get(iMale+i).getPrimitive());
						node.setDepth(male.get(iMale+i).getDepth());
						child.add(node);
					}
				}
				else{
					for(int i=0;i<female.get(iFemale).size();i++){
						iChild++;
						Node node = new Node(female.get(iFemale+i).getPrimitive());
						node.setDepth(female.get(iFemale+i).getDepth());
						child.add(node);
					}
				}
				
				iMale += male.get(iMale).size();	//preskoči čvorove koji nisu u zajedničkom području
				iFemale += female.get(iFemale).size();
			}
		}
		child.update(male.getPrimitiveSet());
		return child;
	}

	@Override
	public void registerParameters() {
		myGenotype.registerParameter("crx.uniform", String.valueOf(probability));
	}
}
