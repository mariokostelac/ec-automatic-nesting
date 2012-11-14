package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja operaciju oduzimanja.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa oduzimanje, mora biti broj
 */
public class Sub<T extends Number> extends Primitive<T> {

	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 2. 
	 */
	public Sub() {
		name = "-";
		complementName = "+";
		nArguments = 2;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		T operand2 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			return (T)Integer.valueOf(operand1.intValue() - operand2.intValue());
		}
		if("Long".equals(ime)) {
			return (T)Long.valueOf(operand1.longValue() - operand2.longValue());
		}
		if("Float".equals(ime)) {
			return (T)Float.valueOf(operand1.floatValue() - operand2.floatValue());
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(operand1.doubleValue() - operand2.doubleValue());
		}
		return null;
	}

}
