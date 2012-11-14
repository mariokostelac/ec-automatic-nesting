package ecf.genotype.tree;


/**
 * Primitiv koji predstavlja operaciju dijeljenja.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se računa dijeljenje, mora biti broj
 */
public class Div<T extends Number> extends Primitive<T> {
	
	/**Konstanta da se izbjegne dijeljenje s malim brojevima*/
	public static final double MIN = 0.000000001;
	
	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 2.
	 */
	public Div() {
		name = "/";
		complementName = "*";
		nArguments = 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		T operand2 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			if(operand2.intValue()==0) return (T)(new Integer(1));
			return (T)Integer.valueOf(operand1.intValue() / operand2.intValue());
		}
		if("Long".equals(ime)) {
			if(operand2.longValue()==0) return (T)(new Long(1));
			return (T)Long.valueOf(operand1.longValue() / operand2.longValue());
		}
		if("Float".equals(ime)) {
			if(Math.abs(operand2.floatValue())<MIN) return (T)(new Float(1.));
			return (T)Float.valueOf(operand1.floatValue() / operand2.floatValue());
		}
		else if("Double".equals(ime)) {
			if(Math.abs(operand2.doubleValue())<MIN) return (T)(new Double(1.));
			return (T)Double.valueOf(operand1.doubleValue() / operand2.doubleValue());
		}
		return null;
	}
}
