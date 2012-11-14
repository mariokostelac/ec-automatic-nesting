package ecf.genotype.binary;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;
import java.util.Vector;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja binarni genotip.
 * 
 * @author Rene Huić
 */
public class Binary extends Genotype {

    protected double minValue;
    protected double maxValue;
    protected int nDecimal;
    protected int nDimension;
    protected int nBits;
    protected long potention;
    protected boolean bRounding;
    protected Vector<Boolean> vBool;
    private Vector<Long> decValue;
    public Vector<Double> realValue;
    private Vector<Vector<Boolean>> variables;

    public Binary(State state) {
        this(state, 0, 0);
    }

    public Binary(State state, int nBits, int nDimension) {
        super(state, "Binary");
        this.nBits = nBits;
        this.nDimension = nDimension;
        bRounding = false;
        decValue = new Vector<Long>();
        decValue.setSize(nDimension);
        realValue = new Vector<Double>();
        realValue.setSize(nDimension);
        vBool = new Vector<Boolean>();
        vBool.setSize(nBits);
        variables = new Vector<Vector<Boolean>>();
        variables.setSize(nDimension);
        for(int i = 0; i < variables.size(); i++)
            variables.set(i, vBool);
    }

    @Override
    public void initialize() {
        minValue = Double.parseDouble(getParameterValue("lbound"));
        maxValue = Double.parseDouble(getParameterValue("ubound"));
        if (minValue >= maxValue) {
            state.getLogger().log(1, "Error: 'lbound' must be smaller than 'ubound' for Binary genotype!");
            return;
        }
        nDecimal = Integer.parseInt(getParameterValue("precision"));
        if(nDecimal > 16){
            state.getLogger().log(1, "Error: 'precision' too large (> 16) for Binary genotype!");
            return;
        }
        nDimension = Integer.parseInt(getParameterValue("dimension"));
        if(nDimension < 1){
            state.getLogger().log(1, "Error: 'dimension' must be > 0 for Binary genotype!");
            return;
        }
        bRounding = getParameterValue("rounding").equals("1");
        double numIndividual  = ((maxValue - minValue) * Math.pow(10., (int) nDecimal));
        nBits = (int)logbase(numIndividual, 2) + 1;
        variables.setSize(nDimension);
        decValue.setSize(nDimension);
        realValue.setSize(nDimension);
        vBool.setSize(nBits);
        potention = (long)Math.pow(2, nBits) - 1;
        Random random = state.getRandomizer();
        for(int i = 0; i < nDimension; i++){
            realValue.set(i, minValue + (maxValue - minValue) * random.nextDouble());
            decValue.set(i, Double.doubleToLongBits((realValue.get(i) - minValue) / (maxValue - minValue) * potention));
            if(bRounding){
                realValue.set(i, round(realValue.get(i), nDecimal));
            }
            long dec = decValue.get(i);
            for(int bit = nBits - 1; bit >= 0; dec = dec / 2, bit--){
                vBool.set(bit, (dec % 2 == 1));
            }
            variables.set(i, vBool);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public Genotype copy() {
        Binary kopija = new Binary(state, nBits, nDimension);
        kopija.bRounding = bRounding;
        kopija.maxValue = maxValue;
        kopija.minValue = minValue;
        kopija.nDecimal = nDecimal;
        kopija.potention = potention;
        kopija.genotypeId = genotypeId;
        kopija.name = name;
        kopija.state = state;
        kopija.individual = individual;

        kopija.decValue = new Vector<Long>(decValue);
        kopija.realValue = new Vector<Double>(realValue);
        kopija.vBool = new Vector<Boolean>(vBool);
        
        Vector<Vector<Boolean>> varKopija = new Vector<Vector<Boolean>>();
        for(int i = 0; i < variables.size(); i++){
            varKopija.add(new Vector<Boolean>(variables.get(i)));
        }
        kopija.variables = varKopija;
        return kopija;
    }

    @Override
    public void registerParameters() {
        registerParameter("lbound", String.valueOf(minValue));
        registerParameter("ubound", String.valueOf(maxValue));
        registerParameter("precision", String.valueOf(nDecimal));
        registerParameter("dimension", String.valueOf(nDimension));
        registerParameter("rounding", String.valueOf((bRounding ? 1 : 0)));
    }

    @Override
    public Vector<CrossoverOp> getCrossoverOp() {
        Vector<CrossoverOp> krizanja = new Vector<CrossoverOp>(2);
        krizanja.add(new BinaryCrxOnePoint(state));
        krizanja.add(new BinaryCrxUniform(state));
        return krizanja;
    }

    @Override
    public Vector<MutationOp> getMutationOp() {
        Vector<MutationOp> mutacije = new Vector<MutationOp>(2);
        mutacije.add(new BinaryMutSimple(state));
        mutacije.add(new BinaryMutMix(state));
        return mutacije;
    }

    @Override
    public void write(Element element) {
        element.setAttribute("size", Integer.toString(nDimension));
        String str = "";
        for(int i = 0; i < nDimension; i++){
            str += realValue.get(i) + "\t";
        }
        element.setTextContent(str);
    }

    @Override
    public void read(Element element) {
        nDimension = Integer.parseInt(element.getAttribute("size"));
        String str = element.getTextContent();

        for(int i = 0; i < nDimension; i++){
            realValue.set(i, Double.parseDouble(str.split("\t")[i]));

            decValue.set(i, Double.doubleToLongBits((realValue.get(i) - minValue) /
                    (maxValue - minValue)*potention));

            long dec = decValue.get(i);
            for(int bit = nBits - 1; bit >= 0; dec = dec / 2, bit--){
                vBool.set(bit, (dec % 2 == 1));
            }
            variables.set(i, vBool);
        }
    }


    /**
     * Metoda koja vraća broj bitova korišten za binarni
     * prikaz.
     * @return Broj bitova
     */
    public int getNumBits() {
        return nBits;
    }


    /**
     * Metoda koja računa logaritam po zadanoj bazi.
     * @param a Vrijednost za koju računamo logaritam
     * @param base Baza logaritma
     * @return Vrijednost logaritma
     */
    private double logbase(double a, double base) {
        return Math.log(a) / Math.log(base);
    }

    /**
     * Metoda koja zaokružuje zadanu vrijednost na zadan
     * broj decimala.
     * @param val Vrijednost koju zaokružujemo
     * @param decimals Broj decimala na koji zaokružujemo
     * @return Zadana vrijednost zaokružena na broj decimala
     */
    private double round(double val, int decimals) {
        double r = val * Math.pow(10, decimals);
        r = Math.floor(r + 0.5);
        return r / Math.pow(10, decimals);
    }


    /**
     * Metoda koja izračunava decimalne i realne vrijednosti
     * trenutnog binarnog prikaza.
     */
    public void update(){
        for(int i = 0; i < nDimension; i++){
            long dec = 0;
            long weight = 1;
            for(int bit = nBits - 1; bit >= 0; bit--){
                dec += (variables.get(i).get(bit) ? 1 : 0) * weight;
                weight *= 2;
            }

            decValue.set(i, dec);
            double value = minValue + (maxValue - minValue) / potention * dec;
            realValue.set(i, value);
            if(bRounding){
                realValue.set(i, round(realValue.get(i), nDecimal));
            }
        }
    }

    /**
     * Metoda koja dohvaća binarni niz jedne varijable.
     *
     * @param index Indeks varijable
     * @return Binarni prikaz varijable
     */
    public Vector<Boolean> getVariables(int index) {
        return variables.get(index);
    }

    /**
     * Metoda koja dohvaća jedan bit iz binarnog
     * prikaza zadane varijable
     * 
     * @param index1 Indeks varijble
     * @param index2 Indeks bita
     * @return Vrijednost bita
     */
    public Boolean getVariables(int index1, int index2) {
        return variables.get(index1).get(index2);
    }

    /**
     * Metoda koja postavlja bit u binarnom prikazu
     * zadane varijable
     * 
     * @param vrijednost Vrijednost bita
     * @param index1 Indeks varijable
     * @param index2 Indeks bita
     */
    public void setVariables(Boolean vrijednost, int index1, int index2) {
        variables.get(index1).set(index2, vrijednost);
    }

    /**
     * Metoda koja dohvaća broj varijabli unutar
     * binarnog niza
     * 
     * @return Broj varijabli
     */
    public int getVariablesSize(){
        return variables.size();
    }

    /**
     * Metoda koja dohvaća realnu vrijednost
     * zadane varijable
     * 
     * @param index Indeks varijable
     * @return Realna vrijednost varijable
     */
    public Double getRealValue(int index) {
        return realValue.get(index);
    }

    /**
     * Metoda koja dohvaća vektor svih realnih
     * vrijednosti za binarni niz
     *
     * @return Vektor realnih vrijednosti
     */
    public Vector<Double> getRealValue() {
        return realValue;
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
     * Metoda koja dohvaća boolean vrijednost
     * da li se koristi zaokruživanje kod
     * izračuna realnih vrijednosti binarnog niza
     * 
     * @return
     */
    public boolean getRounding(){
        return bRounding;
    }

    /**
     * Metoda koja dohvaća vektor cjelobrojnih
     * vrijednosti binarnog niza
     *
     * @return Vektor cjelobrojnih vrijednosti
     */
    public Vector<Long> getDecValue(){
        return decValue;
    }

    @Override
    public String toString() {
        String str = "";

//        for (boolean bit : variables.get(0)) {
//            str += (bit) ? "1" : "0";
//        }

        for(int i = 0; i < realValue.size(); i++){
            if(i == realValue.size() - 1){
                str += realValue.get(i);
            }
            else {
                str += realValue.get(i) + ",";
            }
        }

        return str;
    }

}
