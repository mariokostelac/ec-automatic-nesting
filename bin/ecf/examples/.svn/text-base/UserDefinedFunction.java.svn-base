package ecf.examples;

import ecf.genotype.tree.Primitive;
import ecf.genotype.tree.Tree;


/**
 * Razred koji predstavlja korisnički definiranu
 * funkciju. On se ne nalazi u skupu primitiva 
 * već ga korisnik mora sam dodati. U ovom primjeru
 * korisnički definirana funkcija se zove udf i
 * ekvivalentna je množenju.
 * 
 * @author Marko Pielić
 * @param <T> Tip podatka nad kojim se izvršava korisnički definirana funkcija
 */
public class UserDefinedFunction<T extends Number> extends Primitive<T> {
	
	/**
	 * Konstruktor. Postavlja broj potrebnih argumenata na 2.
	 */
	public UserDefinedFunction() {
		name = "udf";
		complementName = "-";
		nArguments = 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T execute(Tree tree) {
		T operand1 = getNextArgument(tree);
		T operand2 = getNextArgument(tree);
		String ime = operand1.getClass().getSimpleName();
		
		if("Integer".equals(ime)) {
			return (T)Integer.valueOf(operand1.intValue() * operand2.intValue());
		}
		if("Long".equals(ime)) {
			return (T)Long.valueOf(operand1.longValue() * operand2.longValue());
		}
		if("Float".equals(ime)) {
			return (T)Float.valueOf(operand1.floatValue() * operand2.floatValue());
		}
		else if("Double".equals(ime)) {
			return (T)Double.valueOf(operand1.doubleValue() * operand2.doubleValue());
		}
		return null;
	}
}
