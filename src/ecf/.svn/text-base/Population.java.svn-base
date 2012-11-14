package ecf;

import java.util.Vector;
import ecf.fitness.IEvaluate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Razred koji predstavlja populaciju.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Population extends Vector<Deme> {

    private static final long serialVersionUID = 1L;
    
    /**Sučelje sa metodom evaluate*/
    private IEvaluate ievaluate;
    
    /**Broj jedinki populacije*/
    private int nDemes;
    
    /**Index demea*/
    private int myDemeIndex;
    
    /**Najbolje jedinke*/
    private HallOfFame hof;
    
    /**Broj jedinki populacije*/
    private int nIndividuals;
    
    /**Stanje algoritma*/
    private State state;

    /**
     * Konstruktor koji kreira novu populaciju.
     * 
     * @param ievaluate Sučelje sa metodom za evaulaciju fitnessa
     */
    public Population(State state, IEvaluate ievaluate) {
        this.state = state;
        this.ievaluate = ievaluate;
        hof = new HallOfFame();
    }

    /**
     * Metoda koja inicijalizira populaciju.
     */
    public void initialize() {
        nIndividuals = Integer.valueOf(state.getRegistry().getEntry("population.size"));
        nDemes = Integer.valueOf(state.getRegistry().getEntry("population.demes"));

        hof.initialize(state);
        
        if (nDemes == 1) {
            Deme deme = new Deme(ievaluate, nIndividuals);
            deme.initialize(state);
            add(deme);
            myDemeIndex = 0;
            return ;
        } else if (state.getCommunicator() == null) {
            for (int i = 0; i < nDemes; i++) {
                Deme deme = new Deme(ievaluate, nIndividuals);//ili poslati nIndividuals/nDemes?
                deme.initialize(state);
                add(deme);
            }
            return ;
        }
        
        myDemeIndex = state.getCommunicator().createDemeCommunicator(nDemes);
        Deme deme = new Deme(ievaluate, nIndividuals);
        deme.initialize(state);
        add(deme);
    }

    /**
     * Metoda za registriranje potrebnih parametara
     * populacije.
     */
    public void registerParameters() {
        state.getRegistry().registerEntry("population.size", "100"); //defaultne vrijednosti
        state.getRegistry().registerEntry("population.demes", "1");
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < size(); i++) {
            s.append("Deme " + i + ":\n" + get(i) + "\n");
        }
        return s.toString();
    }

    /**
     * Vraća broj demeova populacije.
     * 
     * @return Broj demeova populacije
     */
    public int getNoDemes() {
        return nDemes;
    }

    /**
     * Vraća index demea
     *
     * @return Traženi indeks
     */
    public int getMyDemeIndex() {
        return myDemeIndex;
    }

    /**
     * Vraca lokalni deme.
     * @return Prvi deme.
     */
    public Deme getLocalDeme() {
        return this.get(0);
    }

    /**
     * Metoda za zapisivanje stanja u datoteku.
     * @param elem xml cvor u koji zapisuje stanje
     */
    public void write(Element elem) {
        
        Communicator communicator = state.getCommunicator();
        
        if (communicator.getCommRank() != 0) { return ; }
        
        try {
            elem.setAttribute("size", Integer.toString(nDemes));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element elem2 = doc.createElement("HallOfFame");
            hof.write(elem2);
            elem.appendChild(elem2);
            
            Element demeNode = doc.createElement("Deme");
            get(0).write(demeNode);
            elem.appendChild(demeNode);

            if (communicator.getCommGlobalRank() == 0) {
                
                for (int i = 1; i < nDemes; i++) {
                    elem.appendChild((Element) communicator.receiveDataGlobal());
                }
                
            } else {
//                Transformer transformer;
//        
//                try {
//                    transformer = TransformerFactory.newInstance().newTransformer();
//                } catch (TransformerConfigurationException ex) {
//                    state.getLogger().log(2, ex.getMessage());
//                    return ;
//                }
//
//                StringWriter stringWriter = new StringWriter(200);
//
//                DOMSource source = new DOMSource(demeNode);
//                StreamResult result = new StreamResult(stringWriter);
//
//                try {
//                    transformer.transform(source, result);
//                } catch (TransformerException ex) {
//                    state.getLogger().log(2, ex.getMessage());
//                }

                communicator.sendDataGlobal(demeNode, 0);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za ucitavanje stanja.
     * @param elem xml cvor iz kojeg cita stanje
     */
    public void read(Element elem) {
//        for (int i = 0; i < elem.getChildNodes().getLength(); i++) {
//            if (i < this.size()) {
//                this.get(i).read((Element) elem.getChildNodes().item(i));
//            }
//        }
        Communicator communicator = state.getCommunicator();
        
        if (communicator.getCommRank() != 0) { return ; }
        
        hof.read((Element) elem.getChildNodes().item(0));
        
        getLocalDeme().read((Element) elem.getChildNodes().item(getMyDemeIndex() + 1));
        
    }

    /**
     * Metoda za dohvat HallOfFame instance
     * @return HoF instanca
     */
    public HallOfFame getHof() {
        return hof;
    }

    //HoF dohvaca najbolje jedinke
    public void updateDemeStats() {
        //TODO: Testirat statistiku demova i migraciju iz deama u deam
        if (nDemes > 1) {
            
            if (state.getCommunicator().getCommGlobalRank() == 0) {
                Vector<Individual> pool = get(0).getHof().getBest();
                for (int i = 1; i < nDemes; i++) {
                    for (Individual individual : state.getCommunicator().receiveIndividualsGlobal(i)) {
                        pool.add(individual);
                    }
                }
                hof.operate(pool);
            } else {
                state.getCommunicator().sendIndividualsGlobal(get(0).getHof().getBest(), 0);
            }
            
        } else {
            
            Vector<Individual> pool;

            for (int i = 0; i < size(); i++) {
                pool = new Vector<Individual>();
                get(i).getHof().operate(this.get(i));

                Vector<Individual> bestOfDeme = get(i).getHof().getBest();
                for(int j = 0; j < bestOfDeme.size(); j++){
                    pool.add(bestOfDeme.get(j));
                }

                hof.operate(pool);
            }
        }

    }
}
