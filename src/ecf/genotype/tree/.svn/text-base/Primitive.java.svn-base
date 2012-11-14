package ecf.genotype.tree;


/**
 * Razred koji predstavlja primitiv. 
 * Primitiv može biti funkcija ili varijabla (terminal).
 * Pojedine implementacije primitiva moraju naslijediti
 * ovaj razred.
 * 
 * @author Marko Pielić
 */
public abstract class Primitive<T> {
	
	/**Prefiks koji označava da je riječ o varijabli tipa Integer*/
	public static final String INT_PREFIX = "I_";
	
	/**Prefiks koji označava da je riječ o varijabli tipa Double*/
	public static final String DOUBLE_PREFIX = "D_";
	
	/**Prefiks koji označava da je riječ o varijabli tipa Boolean*/
	public static final String BOOL_PREFIX = "B_";
	
	/**Prefiks koji označava da je riječ o varijabli tipa Character*/
	public static final String CHAR_PREFIX = "C_";
	
	/**Prefiks koji označava da je riječ o varijabli tipa String*/
	public static final String STRING_PREFIX = "S_";
	
	
	/**Ime primitiva*/
	protected String name;
	
	/**Ime komplementarnog primitiva*/
	protected String complementName;
		
	/**Broj argumenata koje primitiv koristi*/
	protected int nArguments;
	
	
	/**
	 * Konstruktor primitiva. 
	 * Kreirani primitiv nema drugo ime.
	 */
	public Primitive() {
		complementName = "";
	}
	
	
	/**
	 * Metoda kojom se izvodi primitiv.
	 * Funkcijski primitivi skupljaju argumente
	 * i vraćaju rezultat izvođenja funkcije, a 
	 * terminali vraćaju svoju trenutnu vrijednost.
	 * 
	 * @param tree Stablo u kojem se primitiv nalazi
	 * @return result Rezultat izvođenja primitiva
	 */
	public abstract T execute(Tree tree); 
	
	
	/**
	 * Metoda koja postavlja vrijednost primitiva.
	 * 
	 * @param value Nova vrijednost primitiva
	 */
	public void setValue(Object value){ }
	
	
	/**
	 * Metoda koja vraća vrijednost primitiva.
	 * 
	 * @return Tražena vrijednost primitiva
	 */
	public Object getValue(){
		return null;
	}
	
	
	/**
	 * Metoda koja stvara kopiju primitiva.
	 * 
	 * @return Kopija primitiva
	 */
	public Primitive<T> copy(){
		return null;
	}
	
	
	/**
	 * Metoda koja dohvaća sljedeći argument primitiva.
	 * 
	 * @param tree Stablo u kojem se primitiv nalazi
	 * @return Sljedeći argument primitiva
	 */
	@SuppressWarnings("unchecked")
	public T getNextArgument(Tree tree){
		return (T)tree.getNextArgument();
	}
	
	
	/**
	 * Metoda koja vraća ime primitiva.
	 * 
	 * @return Ime primitiva
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Metoda koja postavlja ime primitiva.
	 * 
	 * @param name Novo ime primitiva
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Metoda koja vraća broj argumenata primitiva.
	 * 
	 * @return Broj argumenata primitiva
	 */
	public int getNArguments() {
		return nArguments;
	}


	/**
	 * Metoda koja vraća ime komplementarnog primitiva.
	 * 
	 * @return Ime komplementarnog primitiva
	 */
	public String getComplementName() {
		return complementName;
	}


	/**
	 * Metoda koja postavlja ime komplementarnog primitiva.
	 * 
	 * @param complementName Ime komplementarnog primitiva
	 */
	public void setComplementName(String complementName) {
		this.complementName = complementName;
	}


	/**
	 * Metoda koja postavlja broj argumenata primitiva.
	 * 
	 * @param arguments Broj argumenata primitiva
	 */
	public void setNArguments(int arguments) {
		nArguments = arguments;
	}
	
	
	/**
	 * Metoda koja vraća primitiv da bi se on pridružio
	 * nekom čvoru. Pretpostavljeno ponašanje je da se
	 * vrati dobiveni primitiv, no ako je primitiv ERC 
	 * Ako je primitiv ERC (Ephemeral Random Constant) 
	 * tada on mora vratiti novu vrijednost.
	 *   
	 * @param primitive Početni primitiv
	 * @return Primitiv koji se pridružuje čvoru
	 */
	public Primitive<?> assignToNode(Primitive<?> primitive){
		return primitive;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
