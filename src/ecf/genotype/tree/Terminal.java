package ecf.genotype.tree;


/**
 * Razred koji predstavlja terminal.
 * Terminal je primitiv koji je ujedno i varijabla.
 * 
 * @author Marko Pielić
 * @param <T> Tip varijable
 */
public class Terminal<T> extends Primitive<T> {
	
	/**Vrijednost terminala*/
	protected T value;
	
	/**
	 * Konstruktor terminala. Broj argumenata
	 * se postavlja na nulu.
	 */
	public Terminal(){
		nArguments = 0;
	}
	
	@Override
	public Primitive<T> copy() {
		Terminal<T> novi = new Terminal<T>();
		
		novi.value = value;
		novi.name = name;
		novi.nArguments = nArguments;
		return novi;
	}

	@Override
	public T execute(Tree tree) {
		return value;
	}

	@Override
	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.value = (T)value;
	}
	
	@Override
	public String toString() {
		if(name.startsWith(INT_PREFIX) || name.startsWith(DOUBLE_PREFIX) ||
		name.startsWith(BOOL_PREFIX) || name.startsWith(CHAR_PREFIX) || name.startsWith(STRING_PREFIX)){
			return value.toString();
		}
		return name;
	}
}
