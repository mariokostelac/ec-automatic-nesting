package ecf.genotype.permutation;
import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import java.util.Random;
import java.util.Vector;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja permutacijski niz genotip.
 * Permutacijski niz se sastoji od niza cijelih, pozitivnih
 * brojeva, bez duplikata.
 * 
 * @author Rene Huić
 */
public class Permutation extends Genotype{

    private Vector<Integer> variables;
    private int size;

    public Permutation(State state){
        this(state, 0);
    }

    public Permutation(State state, int size){
        super(state, "Permutation");
        this.size = size;
        variables = new Vector<Integer>();
        variables.setSize(size);
    }

    @Override
    public void initialize() {
        Random random = state.getRandomizer();
        size = Integer.parseInt(getParameterValue("size"));
        if(size < 0){
            state.getLogger().log(1, "Error: 'size' must be > 0 for Permutation genotype!");
            return;
        }
        variables.setSize(size);
        for(int i = 0; i < size; i++){
            variables.set(i, i);
        }
        int ind1, ind2, temp;
        
        //generiranje nasumične permutacije
	for(int i = 0; i < size; i++) {
		ind1 = random.nextInt(size);
		ind2 = random.nextInt(size);
		temp = variables.get(ind1);
		variables.set(ind1, variables.get(ind2));
		variables.set(ind2, temp);
	}
    }

    @SuppressWarnings("unchecked")
	@Override
    public Genotype copy() {
        Permutation kopija = new Permutation(state, size);
        kopija.genotypeId = genotypeId;
        kopija.name = name;
        kopija.variables = (Vector<Integer>)variables.clone();
        return kopija;
    }

    @Override
    public void registerParameters() {
        registerParameter("size", String.valueOf(size));
    }

    @Override
    public Vector<CrossoverOp> getCrossoverOp() {
        Vector<CrossoverOp> krizanja = new Vector<CrossoverOp>(3);
        krizanja.add(new PermutationCrxPMX(state));
        krizanja.add(new PermutationCrxOX(state));
        krizanja.add(new PermutationCrxPBX(state));
        return krizanja;
    }

    @Override
    public Vector<MutationOp> getMutationOp() {
        Vector<MutationOp> mutacije = new Vector<MutationOp>(3);
        mutacije.add(new PermutationMutIns(state));
        mutacije.add(new PermutationMutInv(state));
        mutacije.add(new PermutationMutToggle(state));
        return mutacije;
    }

    @Override
    public void write(Element element) {
        element.setAttribute("size", Integer.toString(size));
        String str = "";
        for(int i = 0; i < size; i++){
            str += variables.get(i) + "\t";
        }
        element.setTextContent(str);
    }

    @Override
    public void read(Element element) {
        String str = element.getTextContent();
        for(int i = 0; i < size; i++){
            variables.set(i, Integer.parseInt(str.split("\t")[i]));
        }
    }

    /**
     * Metoda koja vraća veličinu permutacijskog niza
     * @return Veličina permutacijskog niza.
     */
    public int getSize(){
        return size;
    }

    /**
     * Metoda koja dohvaća jedan element iz permutacijskog niza.
     * @param index Indeks elementa koji se želi dohvatiti.
     * @return Element sa zadanim indeksom.
     */
    public int getElement(int index){
        return variables.get(index);
    }

    /**
     * Metoda koja postavlja jedan element permutacijskog niza
     * @param index Indeks elementa
     * @param vrijednost Vrijednost na koju postavljamo element
     */
    public void setElement(int index, int vrijednost){
        variables.set(index, vrijednost);
    }
    
    /**
     * Metoda koja vraća permutacijski niz.
     * @return Permutacijski niz.
     */
    public Vector<Integer> getVariables(){
        return variables;
    }

    /**
     * Metoda koja postavlja veličinu permutacijskog niza.
     *
     * @param vrijednost Veličina perm. niza
     */
    public void setSize(int vrijednost){
        size = vrijednost;
        variables.setSize(size);
    }

    @Override
    public String toString(){
        return variables.toString();
    }
}
