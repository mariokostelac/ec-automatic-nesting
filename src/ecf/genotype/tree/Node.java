package ecf.genotype.tree;


/**
 * Razred koji predstavlja čvor stabla.
 * Svaki čvor sadrži jedan primitiv.
 * 
 * @author Marko Pielić
 */
public class Node {

	/**Veličina podstabla čvora*/
	private int size;
	
	/**Dubina na kojoj se nalazi čvor u stablu*/
	private int depth;
	
	/**Primitiv čvora*/
	private Primitive<?> primitive;
	
	
	/**
	 * Pretpostavljeni konstruktor.
	 * Postavlja veličinu čvora na 1.
	 */
	public Node() {
		size = 1;
	}
	
	
	/**
	 * Konstruktor koji kreira čvor tako
	 * da on sadrži primitiv.
	 * 
	 * @param primitive Primitiv koji će čvor sadržavati
	 */
	public Node(Primitive<?> primitive){
//		setPrimitive(primitive);
		this.primitive = primitive;
		
		size = 1;
	}
	
	/**
	 * Konstruktor koji kreira čvor iz postojećeg 
	 * čvora (copy konstruktor).
	 * 
	 * @param node Postojeći čvor
	 */
	public Node(Node node){
//		setPrimitive(node.primitive);
		this.primitive = node.primitive;
		
		size = node.size;
		depth = node.depth;
	}

	
	/**
	 * Metoda koja vraća veličinu podstabla čvora.
	 * 
	 * @return Veličina podstabla čvora
	 */
	public int size() {
		return size;
	}

	
	/**
	 * Metoda koja postavlja veličinu čvora.
	 * 
	 * @param size Veličina čvora
	 */
	public void setSize(int size) {
		this.size = size;
	}

	
	/**
	 * Metoda koja vraća dubinu čvora.
	 * 
	 * @return Dubina čvora
	 */
	public int getDepth() {
		return depth;
	}

	
	/**
	 * Metoda koja postavlja dubinu čvora.
	 * 
	 * @param depth Nova dubina čvora
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	
	/**
	 * Metoda koja vraća primitiv čvora.
	 * 
	 * @return Primitiv čvora
	 */
	public Primitive<?> getPrimitive() {
		return primitive;
	}

	
	/**
	 * Metoda koja postavlja primitiv čvora. Ako je primitiv 
	 * Ephemereal Random Constant on se kopira.
	 * 
	 * @param primitive Novi primitiv čvora
	 */
	public void setPrimitive(Primitive<?> primitive) {
//		if(primitive.isEphemereal()) this.primitive = primitive.copy();
//		else this.primitive = primitive;
		this.primitive = primitive.assignToNode(primitive);
	}
	
	@Override
	public String toString() {
		return primitive.toString();
	}
}
