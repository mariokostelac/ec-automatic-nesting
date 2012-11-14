package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja funkciju sin.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa sin, mora biti broj
 */
public class Sin<T extends Number> extends Primitive<T> {
	
	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 1. 
	 */
	public Sin(){
		name = "sin";
		complementName = "cos";
		nArguments = 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand = getNextArgument(tree);
		String ime = operand.getClass().getSimpleName();

		if("Float".equals(ime)) {
			return (T)(new Float(Math.sin((Float)operand.floatValue())));
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(Math.sin((Double)operand));
		}
		return null;
	}

}
