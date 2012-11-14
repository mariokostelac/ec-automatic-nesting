package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja operaciju min.
 * Operacija min vraća manji od dva ulazna primitiva.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa min, mora biti broj
 */
public class Min<T extends Number> extends Primitive<T> {
	
	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 2.
	 */
	public Min() {
		name = "min";
		complementName = "max";
		nArguments = 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		T operand2 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			return (T)Integer.valueOf(Math.min(operand1.intValue(), operand2.intValue()));
		}
		if("Long".equals(ime)) {
			return (T)Long.valueOf(Math.min(operand1.longValue(), operand2.longValue()));
		}
		if("Float".equals(ime)) {
			return (T)Float.valueOf(Math.min(operand1.floatValue(), operand2.floatValue()));
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(Math.min(operand1.doubleValue(), operand2.doubleValue()));
		}
		return null;
	}
}
