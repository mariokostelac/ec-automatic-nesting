package ecf.fitness;

import org.w3c.dom.Element;


/**
 * Razred koji implementira fitness.
 * Fitness je bolji ako je njegova
 * vrijednost (value) manja.
 * 
 * @author Marko Pielić
 */
public class FitnessMin extends Fitness{

	@Override
	public Fitness copy() {
		return new FitnessMin();
	}

	@Override
	public int compareTo(Fitness fitness) {
		if(getValue()<fitness.getValue()) return 1;
		if(getValue()>fitness.getValue()) return -1;
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
