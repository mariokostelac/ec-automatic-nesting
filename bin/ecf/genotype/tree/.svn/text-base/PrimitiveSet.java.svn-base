package ecf.genotype.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;


/**
 * Razred koji predstavlja skup primitiva koji se
 * mogu koristiti u tree genotipu.
 * 
 * @author Marko Pielić
 */
public class PrimitiveSet {

	/**Skup varijabli*/
	private Vector<Primitive<?>> terminalSet = new Vector<Primitive<?>>();
	
	/**Skup funkcija*/
	private Vector<Primitive<?>> functionSet = new Vector<Primitive<?>>();
	
	/**Skup primitiva (varijable + funkcije)*/
	private Vector<Primitive<?>> primitiveSet = new Vector<Primitive<?>>();
	
	/**Mapa za brzo dohvaćanje primitiva*/
	private Map<String, Primitive<?>> mPrimitives = new HashMap<String, Primitive<?>>();
	
	/**Generator slučajnih brojeva*/
	private Random random = new Random();
	
	/**Funkcije koje je definirao korisnik*/
	private static Vector<Primitive<?>> userDefinedFunctions = new Vector<Primitive<?>>();
	
	/**Mapa za brzo dohvaćanje funkcija koje je definirao korisnik*/
	private static Map<String, Primitive<?>> userDefinedFunctionsMap = 
			new HashMap<String, Primitive<?>>();
	
	
	/**
	 * Konstruktor koji u skup primitiva dodaje sve
	 * postojeće primitive.
	 */
	public PrimitiveSet() {
		addPrimitiveToMap(new Add<Number>());
		addPrimitiveToMap(new Sub<Number>());
		addPrimitiveToMap(new Mul<Number>());
		addPrimitiveToMap(new Div<Number>());
		addPrimitiveToMap(new Pos<Number>());
		addPrimitiveToMap(new Neg<Number>());
		addPrimitiveToMap(new Min<Number>());
		addPrimitiveToMap(new Max<Number>());
		addPrimitiveToMap(new Sin<Number>());
		addPrimitiveToMap(new Cos<Number>());
	}
	
	/**
	 * Copy konstruktor.
	 * 
	 * @param primitiveSet Skup primitiva koji se kopira
	 */
	public PrimitiveSet(PrimitiveSet primitiveSet){
//		this();
		this.primitiveSet = new Vector<Primitive<?>>(primitiveSet.primitiveSet);
		this.functionSet = new Vector<Primitive<?>>(primitiveSet.functionSet);
		this.terminalSet = new Vector<Primitive<?>>(primitiveSet.terminalSet);
		this.mPrimitives = new HashMap<String, Primitive<?>>(primitiveSet.mPrimitives);
	}
	
	/**
	 * Metoda za dodavanje funkcijskog primitiva u skup primitiva.
	 * 
	 * @param functionPrimitive Funkcijski primitiv koji se dodaje
	 */
	public void addFunction(Primitive<?> functionPrimitive){
		functionSet.add(functionPrimitive);
		primitiveSet.add(functionPrimitive);
		mPrimitives.put(functionPrimitive.getName(), functionPrimitive);
	}
	
	
	/**
	 * Metoda za dodavanje terminala (varijable) u skup primitiva.
	 * 
	 * @param terminalPrimitive Terminal koji se dodaje
	 */
	public void addTerminal(Primitive<?> terminalPrimitive){
		terminalSet.add(terminalPrimitive);
		primitiveSet.add(terminalPrimitive);
		mPrimitives.put(terminalPrimitive.getName(), terminalPrimitive);
	}
	
	
	/**
	 * Metoda koja vraća slučajno odabrani terminal.
	 * 
	 * @return Slučajno odabrani terminal
	 */
	public Primitive<?> getRandomTerminal(){
		return terminalSet.get(random.nextInt(terminalSet.size()));
	}
	
	
	/**
	 * Metoda koja vraća slučajno odabranu funkciju.
	 * 
	 * @return Slučajno odabrana funkcija
	 */
	public Primitive<?> getRandomFunction(){
		return functionSet.get(random.nextInt(functionSet.size()));
	}
	
	
	/**
	 * Metoda koja vraća slučajno odabrani primitiv.
	 * 
	 * @return Slučajno odabrani primitiv
	 */
	public Primitive<?> getRandomPrimitive(){
		return primitiveSet.get(random.nextInt(primitiveSet.size()));
	}
	
	
	/**
	 * Metoda koja dohvaća terminal.
	 * 
	 * @param name Ime terminala
	 * @return Traženi terminal
	 */
	public Primitive<?> getTerminalByName(String name){
		return mPrimitives.get(name);
	}
	
	
	/**
	 * Metoda koja dohvaća funkciju. Ukoliko je 
	 * potrebno, kreira automatski definiranu funkciju.
	 * 
	 * @param name Ime funkcije
	 * @return Tražena funkcija
	 */
	public Primitive<?> getFunctionByName(String name){
		if(!mPrimitives.containsKey(name) && ADF.contains(name)){	//kreiranje ADF objekta
			ADF<Object> adf = new ADF<Object>(name);
			addPrimitiveToMap(adf);
		}
		
		Primitive<?> p = mPrimitives.get(name);		//ako postoji funkcija među ECF funkcijama
		if(p!=null) return p;						//vrati ju
		p = userDefinedFunctionsMap.get(name); 		//inače traži među korisničkim funkcijama
		if(p!=null) return p;						//ako postoji vrati ju
		throw new IllegalArgumentException("Error: function " + name + " does not exist in primitive set!");
	}
	
	
	/**
	 * Metoda koja dohvaća primitiv.
	 * 
	 * @param name Ime primitiva
	 * @return Traženi primitiv
	 */
	public Primitive<?> getPrimitiveByName(String name){
		return mPrimitives.get(name);
	}
	
	
	/**
	 * Metoda koja vraća terminal.
	 * 
	 * @param index Indeks terminala
	 * @return Traženi terminal
	 */
	public Primitive<?> getTerminalById(int index){
		return terminalSet.get(index);
	}
	
	/**
	 * Metoda koja vraća funkciju.
	 * 
	 * @param index Indeks funkcije
	 * @return Tražena funkcija
	 */
	public Primitive<?> getFunctionById(int index){
		return functionSet.get(index);
	}
	
	/**
	 * Metoda koja vraća primitiv.
	 * 
	 * @param index Indeks primitiva
	 * @return Traženi primitiv
	 */
	public Primitive<?> getPrimitiveById(int index){
		return primitiveSet.get(index);
	}
	
	
	/**
	 * Metoda koja dohvaća broj korištenih terminala.
	 * 
	 * @return Broj korištenih terminala
	 */
	public int getTerminalSetSize(){
		return terminalSet.size();
	}
	
	
	/**
	 * Metoda koja dohvaća broj korištenih funkcija.
	 * 
	 * @return Broj korištenih funkcija
	 */
	public int getFunctionSetSize(){
		return functionSet.size();
	}
	
	
	/**
	 * Metoda koja dohvaća broj korištenih primitiva.
	 * 
	 * @return Broj korištenih primitiva
	 */
	public int getPrimitiveSetSize(){
		return primitiveSet.size();
	}
	
	
	/**
	 * Metoda koja dodaje primitiv u mapu primitiva.
	 * 
	 * @param primitive Primitiv koji se dodaje
	 */
	public void addPrimitiveToMap(Primitive<?> primitive){
		mPrimitives.put(primitive.getName(), primitive);
	}
	
	
	/**
	 * Metoda koja dodaje korisnički definiranu funkciju
	 * u skup funkcija.
	 * 
	 * @param function Funkcija koju je definirao korisnik
	 */
	public static void addUserDefinedFunction(Primitive<?> function){
		userDefinedFunctions.add(function);
		userDefinedFunctionsMap.put(function.getName(), function);
	}
}
