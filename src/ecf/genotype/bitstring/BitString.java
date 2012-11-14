package ecf.genotype.bitstring;

import java.util.Random;
import java.util.Vector;
import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja bitstring genotip.
 * U bitstring genotipu podaci se čuvaju kao niz
 * bitova, čija vrijednost može biti true ili false.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class BitString extends Genotype{

	/**Podaci u genotipu*/
	private Vector<Byte> data;
	
	/**Veličina bitstringa*/
	private int nBits;
	
	
	/**
	 * Konstruktor koji kreira novi bitstring genotip.
	 */
	public BitString(State state){
		this(state, 0);
                b1 = true;
	}
	boolean b1;
	
	/**
	 * Konstruktor koji kreira novi bitstring
	 * genotip veličine nBits.
	 * 
	 * @param nBits Veličina bitstring genotipa
	 */
	public BitString(State state, int nBits){
		super(state, "BitString");
		data = new Vector<Byte>(nBits);
		this.nBits = nBits;
		data.setSize(nBits);
	}


	public void initialize(){
		Random random = state.getRandomizer();
		
		nBits = Integer.valueOf(getParameterValue("size"));
		data.setSize(nBits);
		for(int i=0;i<data.size();i++){
			data.set(i, (byte)random.nextInt(2));
		}
	}

	
	public void registerParameters(){
		if(nBits==0) nBits = 10;
		registerParameter("size", String.valueOf(nBits));
	}
	
	
	public BitString copy(){
		BitString kopija = new BitString(state, data.size());
		
		for(int i=0;i<kopija.size();i++){
			kopija.set(i, data.get(i));
		}
		kopija.genotypeId = genotypeId;
		kopija.name = name;
		kopija.state = state;
		return kopija;
	}

	
	public Vector<CrossoverOp> getCrossoverOp(){
		Vector<CrossoverOp> krizanja = new Vector<CrossoverOp>(2);
		krizanja.add(new BitStringCrxOnePoint(state));
		krizanja.add(new BitStringCrxUniform(state));
		return krizanja;
	}

	
	public Vector<MutationOp> getMutationOp(){
		Vector<MutationOp> mutacije = new Vector<MutationOp>(2);
		mutacije.add(new BitStringMutSimple(state));
                mutacije.add(new BitStringMutMix(state));
		return mutacije;
	}


	/**
	 * Metoda koja dohvaća jedan bit bitstring genotipa.
	 * 
	 * @param index Index bita koji se dohvaća
	 * @return Traženi bit genotipa
	 */
	public byte get(int index){
		return data.get(index);
	}
	
	
	/**
	 * Metoda koja postavlja jedan bit bitstring genotipa.
	 * 
	 * @param index Index na kojem se bit postavlja
	 * @param element Nova vrijednost bita
	 */
	public void set(int index, byte element){
		data.set(index, element);
	}
	
	
	/**
	 * Metoda koja vraća broj bitova bitstring genotipa.
	 * 
	 * @return Broj bitova genotipa
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Metoda koja dodaje 1 bit u genotip.
	 * 
	 * @param element Vrijednost novog bita
	 */
	public void add(byte element){
		data.add(element);
	}


	/**
	 * Metoda koja dohvaća sve podatke bitstring genotipa.
	 * 
	 * @return Svi podaci bitstring genotipa
	 */
	public Vector<Byte> getData() {
		return data;
	}

	/**
	 * Metoda koja postavlja sve podatke bitstring genotipa.
	 * 
	 * @param data Novi podaci bitstring genotipa
	 */
	public void setData(Vector<Byte> data) {
		this.data = data;
	}
        
	@Override
	public String toString(){
		String str = "";

		for(byte bit : data){
			str += bit;
		}
		return str;
	}

    @Override
    public void write(Element elem) {
        elem.setAttribute("size", Integer.toString(nBits));
        String str = "";
        for (byte bit : data) {
            str += bit;
        }
        elem.setTextContent(str);
    }

    @Override
    public void read(Element elem) {
        String txt = elem.getTextContent();
        for(int i = 0; i < data.size(); i++){
            if(txt.charAt(i) == '0'){
                data.set(i, (byte)0);
            }
            else {
                data.set(i, (byte)1);
            }
        }
    }
}
