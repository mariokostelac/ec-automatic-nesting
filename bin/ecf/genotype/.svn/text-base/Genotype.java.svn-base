package ecf.genotype;

import java.util.Vector;
import org.w3c.dom.Element;
import ecf.Individual;
import ecf.State;


/**
 * Razred koji predstavlja genotip.
 * Jedinka može imati više genotipa, a svaki
 * implementirani genotip mora naslijediti
 * ovaj apstraktni razred.
 * 
 * @author Marko Pielić/Rene Huić
 */
public abstract class Genotype {

    /**Jedinstveni identifikator genotipa*/
    protected int genotypeId;

    /**Ime genotipa*/
    protected String name;

    /**Stanje algoritma*/
    protected State state;

    /**Jedinka koja ima ovaj genotip*/
    protected Individual individual;
    
    /**
        * Konstruktor koji kreira novi genotip.
        */
    public Genotype(State state, String name) {
            this.state = state;
            this.name = name;
    }

    /**
        * Metoda za inicijalizaciju genotipa.
        */
    public abstract void initialize();


    /**
        * Metoda za stvaranje kopije genotipa.
        * Kopirani je genotip po svemu isti originalu.
        * 
        * @return Kopija genotipa
        */
    public abstract Genotype copy();


    /**
        * Helper metoda za registraciju jednog parametra.
        * 
        * @param kljuc Ključ parametra
        * @param vrijednost Vrijednost parametra
        */
    public void registerParameter(String kljuc, String vrijednost){
            state.getRegistry().registerEntry(name + genotypeId + "." + kljuc, vrijednost);
    }


    /**
        * Helper metoda za dohvat vrijednosti jednog
        * registriranog parametra.
        * 
        * @param kljuc Ključ parametra koji se dohvaća
        * @return Traženi parametar
        */
    public String getParameterValue(String kljuc){
            return state.getRegistry().getEntry(name + genotypeId + "." + kljuc);
    }

    /**
        * Metoda za registriranje potrebnih parametara
        * genotipa.
        */
    public abstract void registerParameters();


    /**
        * Metoda koja vraća operatore križanja za 
        * dani genotip.
        * 
        * @return Operator križanja
        */
    public abstract Vector<CrossoverOp> getCrossoverOp();


    /**
        * Metoda koja vraća operatore mutacije za
        * dani genotip.
        * 
        * @return Operator mutacije
        */
    public abstract Vector<MutationOp> getMutationOp();



    /**
        * Metoda za zapisivanje stanja u datoteku.
        * @param element XML čvor u koji zapisuje stanje
        */
    public abstract void write(Element element);

    /**
        * Metoda za učitavanje stanja.
        * @param element XML čvor iz kojeg čita stanje
        */
    public abstract void read(Element element);


    /**
        * Metoda koja vraća ID genotipa.
        * 
        * @return ID genotipa
        */
    public int getGenotypeId() {
            return genotypeId;
    }


    /**
        * Metoda koja postavlja ID genotipa.
        * 
        * @param genotypeId ID genotipa
        */
    public void setGenotypeId(int genotypeId) {
            this.genotypeId = genotypeId;
    }


    /**
        * Metoda koja vraća ime genotipa.
        * 
        * @return Ime genotipa
        */
    public String getName() {
            return name;
    }


    /**
        * Metoda koja postavlja ime genotipa.
        * 
        * @param name Ime genotipa
        */
    public void setName(String name) {
            this.name = name;
    }


    /**
        * Metoda koja vraća jedinku čiji je ovo genotip.
        * 
        * @return Jedinka čiji je ovo genotip
        */
    public Individual getIndividual() {
            return individual;
    }


    /**
        * Metoda koja postavlja jedinku čiji je ovo genotip.
        * 
        * @param individual Jedinka čiji  je ovo genotip
        */
    public void setIndividual(Individual individual) {
            this.individual = individual;
    }

}
