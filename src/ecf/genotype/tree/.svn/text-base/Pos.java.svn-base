package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja operaciju pos.
 * pos(x) = x>0 ? x : 0
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa pos, mora biti broj
 */
public class Pos<T extends Number> extends Primitive<T> {

	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 1. 
	 */
	public Pos() {
		name = "pos";
		complementName = "neg";
		nArguments = 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			return (T)Integer.valueOf(operand1.intValue()>0 ? operand1.intValue() : 0);
		}
		if("Long".equals(ime)) {
			return (T)Long.valueOf(Math.abs(operand1.intValue()>0 ? operand1.longValue() : 0));
		}
		if("Float".equals(ime)) {
			return (T)Float.valueOf(Math.abs(operand1.intValue()>0 ? operand1.floatValue() : 0));
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(Math.abs(operand1.intValue()>0 ? operand1.doubleValue() : 0));
		}
		return null;
	}

}
