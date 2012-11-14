package ecf.genotype.tree;

import java.util.Random;
import java.util.Vector;


/**
 * Razred koji predstavlja ERC (Ephemeral Random Constant).
 * ERC je konstanta.
 * 
 * @author Marko Pielić
 * @param <T> Tip konstante
 */
public class ERC<T> extends Terminal<T>{
	
	/**Vrijednosti koje konstanta može poprimiti*/
	private Vector<Object> values = new Vector<Object>();
	
	/**Tip podatka konstante*/
	private String className;
	
	/**True ako je zadan interval mogućih vrijednosti konstante, false ako su zadane diskretne vrijednosti*/
	private boolean useInterval;
	
	/**
	 * Konstruktor koji kreira novu konstantu.
	 * Konstanta može biti zadana intervalom ili
	 * nizom diskretnih vrijednosti. Interval može
	 * biti zadan samo za konstante tipa int i double.
	 * 
	 * @param className Tip konstante
	 * @param values Vrijednosti učitane iz datoteke
	 * @param useInterval True ako je zadan interval, false ako su zadane diskretne vrijednosti
	 * 					  koje konstanta može poprimiti
	 */
	public ERC(String className, Vector<String> values, boolean useInterval) {
		this(className, useInterval);
		pretvoriTipPodatka(values);
	}
	
	
	/**
	 * Konstruktor koji se koristi za stvaranje kopije
	 * ERC objekta.
	 * 
	 * @param className Tip konstante
	 * @param values Vrijednosti učitane iz datoteke
	 */
	public ERC(String className, boolean useInterval) {
		super();
		this.className = className;
		this.useInterval = useInterval;
	}
	

	@Override
	public ERC<T> copy() {
		ERC<T> erc = new ERC<T>(className, useInterval);
		erc.values = this.values;
		erc.name = this.name;
		return erc;
	}
	
	@SuppressWarnings("unchecked")
	public Primitive<?> assignToNode(Primitive<?> primitive){
		Random random = new Random();
		ERC<T> erc = copy();
		
		if(useInterval){
			if(className.equalsIgnoreCase("Double")){
				double d1 = (Double)this.values.get(0);
				double d2 = (Double)this.values.get(1);
				erc.value = (T)Double.valueOf(random.nextDouble()*(d2 - d1) + d1);
			}
			else if(className.equalsIgnoreCase("Int")){
				int i1 = (Integer)this.values.get(0);
				int i2 = (Integer)this.values.get(1);
				erc.value = (T)Integer.valueOf(random.nextInt(i2-i1) + i1);
			}
		}
		else{
			int pozicija = random.nextInt(this.values.size());
			erc.value = (T)this.values.get(pozicija);
		}
		return erc;
	}
	
	
	/**
	 * Metoda koja pretvara podatke učitane iz datoteke u obliku stringa
	 * u njihov ispravni oblik.
	 * 
	 * @param values Učitani podaci iz datoteke
	 */
	private void pretvoriTipPodatka(Vector<String> values) {
		if(className.equalsIgnoreCase("Int")){
			for(int i=0;i<values.size();i++){
				this.values.add(Integer.valueOf(values.get(i)));
			}
			name = INT_PREFIX + this.values;
		}
		else if(className.equalsIgnoreCase("Double")){
			for(int i=0;i<values.size();i++){
				this.values.add(Double.valueOf(values.get(i)));
			}
			name = DOUBLE_PREFIX + this.values;
		}
		else if(className.equalsIgnoreCase("Bool")){
			for(int i=0;i<values.size();i++){
				this.values.add(Boolean.valueOf(values.get(i)));
			}
			name = BOOL_PREFIX + this.values;
		}
		else if(className.equalsIgnoreCase("Char")){
			for(int i=0;i<values.size();i++){
				this.values.add(values.get(i).charAt(0));
			}
			name = CHAR_PREFIX + this.values;
		}
		else if(className.equalsIgnoreCase("String")){
			for(int i=0;i<values.size();i++){
				this.values.add(values.get(i));
			}
			name = STRING_PREFIX + this.values;
		}
	}

}
