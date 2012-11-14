package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja operaciju max.
 * Operacija max vraća veći od dva ulazna primitiva.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa max, mora biti broj
 */
public class Max<T extends Number> extends Primitive<T> {
	
	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 2.
	 */
	public Max() {
		name = "max";
		complementName = "min";
		nArguments = 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		T operand2 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			return (T)Integer.valueOf(Math.max(operand1.intValue(), operand2.intValue()));
		}
		if("Long".equals(ime)) {
			return (T)Long.valueOf(Math.max(operand1.longValue(), operand2.longValue()));
		}
		if("Float".equals(ime)) {
			return (T)Float.valueOf(Math.max(operand1.floatValue(), operand2.floatValue()));
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(Math.max(operand1.doubleValue(), operand2.doubleValue()));
		}
		return null;
	}
}
