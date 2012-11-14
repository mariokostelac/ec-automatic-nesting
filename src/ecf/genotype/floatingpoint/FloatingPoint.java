package ecf.genotype.floatingpoint;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;
import java.util.Vector;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja genotip s pomičnom točkom.
 * Genotip predstavlja jedan ili više brojeva tipa double,
 * ovisno o dimenziji genotipa.
 * 
 * @author Rene Huić
 */
public class FloatingPoint extends Genotype{

    private int nDimension;
    private Vector<Double> data;
    protected double minValue;
    protected double maxValue;

    public FloatingPoint(State state) {
        this(state, 0);
    }

    public FloatingPoint(State state, int dimension) {
        super(state, "FloatingPoint");
        
        nDimension = dimension;
        data = new Vector<Double>();
        data.setSize(dimension);
    }
    
    @Override
    public void initialize() {
        Random random = state.getRandomizer();
        minValue = Double.parseDouble(getParameterValue("lbound"));
        maxValue = Double.parseDouble(getParameterValue("ubound"));
        if (minValue >= maxValue) {
            state.getLogger().log(1, "Error: 'lbound' must be smaller than 'ubound' for FloatingPoint genotype!");
            return;
        }
        nDimension = Integer.parseInt(getParameterValue("dimension"));
        if(nDimension < 1){
            state.getLogger().log(1, "Error: 'dimension' must be > 0 for FloatingPoint genotype!");
            return;
        }
        data.setSize(nDimension);
        for(int i = 0; i < nDimension; i++){
            double broj = minValue + ((double)random.nextInt((int)(maxValue - minValue)) + random.nextDouble());
            if(broj > maxValue) broj = maxValue;
            data.set(i, broj);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public Genotype copy() {
        FloatingPoint kopija = new FloatingPoint(this.state, nDimension);
        kopija.maxValue = maxValue;
        kopija.minValue = minValue;
        kopija.data = (Vector<Double>)data.clone();
        return kopija;
    }

    @Override
    public void registerParameters() {
        registerParameter("lbound", String.valueOf(minValue));
        registerParameter("ubound", String.valueOf(maxValue));
        registerParameter("dimension", String.valueOf(nDimension));
    }

    @Override
    public Vector<CrossoverOp> getCrossoverOp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<MutationOp> getMutationOp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(Element element) {
        element.setAttribute("size", Integer.toString(nDimension));
        String str = "";
        for(int i = 0; i < nDimension; i++){
            str += data.get(i) + "\t";
        }
        element.setTextContent(str);
    }

    @Override
    public void read(Element element) {
        nDimension = Integer.parseInt(element.getAttribute("size"));
        String str = element.getTextContent();

        for(int i = 0; i < nDimension; i++){
            data.set(i, Double.parseDouble(str.split("\t")[i]));
        }
    }

    /**
     * Metoda koja dohvaća dimenzionalnost
     * binarnog niza
     *
     * @return Dimenzionalnost binarnog niza
     */
    public int getnDimension() {
        return nDimension;
    }

    /**
     * Dohvaća realni broj sa zadanim indeksom
     *
     * @param indeks Indeks realnog broja
     * @return Realni broj
     */
    public double getNumber(int indeks){
        return data.get(indeks);
    }

    /**
     * Metoda koja postavlja realni broj sa indeksom
     * na određenu vrijednost
     * 
     * @param vrijednost Nova vrijednost realnog broja
     * @param indeks Indeks broja kojemu se mijenja vrijednost
     */
    public void setNumber(double vrijednost, int indeks){
        data.set(indeks, vrijednost);
    }

    /**
     * Metoda koja dohvaća gornju granicu intervala
     *
     * @return Gornja granica intervala
     */
    public double getMaxValue() {
        return maxValue;
    }

     /**
     * Metoda koja dohvaća donju granicu intervala
     *
     * @return Donja granica intervala
     */
    public double getMinValue() {
        return minValue;
    }

    @Override
    public String toString(){
        return data.toString();
    }

}
