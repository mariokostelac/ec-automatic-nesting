package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja funkciju cos.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa cos, mora biti broj
 */
public class Cos<T extends Number> extends Primitive<T>{

	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 1. 
	 */
	public Cos(){
		name = "cos";
		complementName = "sin";
		nArguments = 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand = getNextArgument(tree);
		String ime = operand.getClass().getSimpleName();

		if("Float".equals(ime)) {
			return (T)(new Float(Math.cos((Float)operand.floatValue())));
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(Math.cos((Double)operand));
		}
		return null;
	}

}
