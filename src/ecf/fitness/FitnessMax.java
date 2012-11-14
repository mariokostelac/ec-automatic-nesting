package ecf.fitness;

import org.w3c.dom.Element;

/**
 * Razred koji implementira Fitness.
 * Fitness je tim bolji čim je njegova
 * vrijednost (value) veća.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class FitnessMax extends Fitness{

	@Override
	public FitnessMax copy() {
		FitnessMax novi = new FitnessMax();
		return novi;
	}

	@Override
	public int compareTo(Fitness fitness) {
		if(getValue()>fitness.getValue()) return 1;
		if(getValue()<fitness.getValue()) return -1;
		return 0;
	}

    @Override
    public void write(Element elem) {
        elem.setAttribute("value", Double.toString(getValue()));
    }

    @Override
    public void read(Element element) {
        setValue(Double.parseDouble(element.getAttribute("value")));
    }


}
