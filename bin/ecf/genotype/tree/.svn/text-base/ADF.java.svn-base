package ecf.genotype.tree;

import java.util.HashMap;
import java.util.Map;


/**
 * Razred koji predstavlja automatski definiranu funkciju.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se izvršava automatski definirana funkcija
 */
public class ADF<T> extends Primitive<T>{

	/**Mapa koja povezuje ime ADF-a i broj njegovih argumenata*/
    private static Map<String, Integer> adfImeArgumenti = new HashMap<String, Integer>();
    
    /**Mapa koja povezuje ime ADF-a i ID genotipa nad kojim se on izvršava*/
    private static Map<String, Integer> adfImeGenotip = new HashMap<String, Integer>();
	
	
	/**
	 * Konstruktor koji kreira ADF.
	 * 
	 * @param name Ime ADF funkcije
	 */
	public ADF(String name) {
		this.name = name;
		complementName = "";
		nArguments = adfImeArgumenti.get(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		Tree autoDefinedFunction = (Tree)tree.getIndividual().getGenotype(adfImeGenotip.get(name));
	
		PrimitiveSet primitiveSet = autoDefinedFunction.getPrimitiveSet();
		for(int i=0;i<primitiveSet.getTerminalSetSize();i++){
			Primitive<?> primitive = primitiveSet.getTerminalById(i);	
			if(!(primitive instanceof ERC<?>)){
				T argument = getNextArgument(tree);
				//System.out.print(argument + " ");
				autoDefinedFunction.setTerminalValue(primitive.getName(), argument);
			}
		}
		
		T value = (T)autoDefinedFunction.execute();
		//System.out.println("\nRješenje: " + value);
		return value;
	}
	
	
	/**
	 * Metoda koja postavlja broj argumenata automatski 
	 * definirane funkcije.
	 * 
	 * @param adfName Ime automatski definirane funkcije
	 * @param numberOfArguments Broj argumenata automatski definirane funkcije
	 */
	public static void setAdfNumberOfArguments(String adfName, int numberOfArguments){
    	adfImeArgumenti.put(adfName, numberOfArguments);
    }
	
	
	/**
	 * Metoda koja postavlja ID genotipa nad kojim se
	 * automatski definirana funkcija izvršava.
	 * 
	 * @param adfName Ime automatski definirane funkcije
	 * @param adfGenotypeID ID genotipa
	 */
	public static void setAdfGenotypeID(String adfName, Integer adfGenotypeID){
		adfImeGenotip.put(adfName, adfGenotypeID);
	}
	
	
	/**
	 * Metoda koja provjerava postoji li automatski
	 * definirana funkcija zadanog imena.
	 * 
	 * @param adfName Ime automatski definirane funkcije
	 * @return True ako postoji automatski definirana funkcija, inače false
	 */
	public static boolean contains(String adfName){
		return adfImeArgumenti.containsKey(adfName);
	}
}
