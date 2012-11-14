package ecf;

import ecf.algorithm.Algorithm;
import ecf.fitness.Fitness;
import ecf.fitness.IEvaluate;
import ecf.genotype.*;
import ecf.operators.*;
import ecf.utils.Logger;
import ecf.utils.Random;
import ecf.utils.Registry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Razred koji označava trenutno stanje algoritma.
 * Postoji samo jedan objekt ovog razreda, a služi
 * za dohvaćanje parametera, genotipa, populacije,
 * algoritma, logiranja,...
 * 
 * @author Marko Pielić/Rene Huić
 *
 */
public class State {

    /**Populacija u trenutnom stanju*/
    private Population population;
    
    /**Algoritam koji se koristi*/
    private Algorithm algorithm;
    
    /**Fitness koji se koristi*/
    private Fitness fitness;
    
    /**Korišteni operatori križanja*/
    private Crossover crossover;
    
    /**Korišteni operatori mutacije*/
    private Mutation mutation;
    
    /**Sučelje koje sadrži implementaciju fitnessa*/
    public final IEvaluate ievaluate;
    
    /**Korišteni genotipi*/
    private Vector<Genotype> genotype;
    
    /**Korišteni registry*/
    private Registry registry;
    
    /**Korišteni logger*/
    private Logger logger;
    
    /**Broj trenutne generacije*/ 
    private int generationNo;
    
    /**Zastavica koja označava je li kraj evolucijskoj postupka*/
    private boolean bTerminate;
    
    /**Podaci o vremenu izvođenja algoritma*/
    private long startTime, currentTime, elapsedTime;
    
    /**Sve operatori zaustavljanja algoritma*/
    private Vector<Operator> allTerminationOps;
    
    /**Korišteni operatori zaustavljanja algoritma */
    private Vector<Operator> activeTerminationOps;
    
    /**Korišteni operator migracije */
    private Migration migration;
    
    /**Direktorij koji sadrži dinamičke razrede ECF-a*/
    private String[] direktorijSDinamickimRazredima = {"./build/classes", "./bin", "./ECFJ.jar", ""};
    
    private boolean batchMode = false;
    
    private Communicator communicator;
    
    private Random randomizer;
    
    @SuppressWarnings("unused")
	private int milestoneInterval;
    @SuppressWarnings("unused")
    private String milestoneFilename;
    @SuppressWarnings("unused")
	private boolean bSaveMilestone;

    /**
     * Konstruktor koji kreira stanje algoritma.
     * Kreira sve objekte potrebne za rad algoritma.
     * 
     * @param ievaluate Sučelje sa metodom za evaulaciju fitnessa
     */
    public State(IEvaluate ievaluate) {
        //stvaranje populacije, križanja i mutacije
        population = new Population(this, ievaluate);
        crossover = new Crossover(this);
        mutation = new Mutation(this);
        fitness = ievaluate.createFitness();

        //kreiranje containera genotipova
        genotype = new Vector<Genotype>();

        //stvoriti sve operatore terminiranja
        activeTerminationOps = new Vector<Operator>();
        allTerminationOps = new Vector<Operator>();
        allTerminationOps.add(new TermMaxGenOp(this));
        allTerminationOps.add(new TermMaxTimeOp(this));
        allTerminationOps.add(new TermStagnationOp(this));

        //stvaranje pomoćnih razreda
        registry = new Registry();
        logger = new Logger(this);
        migration = new Migration(this);
        
        this.ievaluate = ievaluate;
    }
    
    public String[] enableMPI(String[] args) {
        communicator = new Communicator();
        communicator.initialize(this, args);
        return communicator.getRealArgs();
    }

    /**
     * Metoda koja inicijalizira stanje algoritma.
     * 
     * @param args Argumenti iz komandne linije<br>
     * 				1. Ime konfiguracijske datoteke<br>
     * 				2. Direktorij sa dinamičkim razredima ECF-a
     */
    public void initialize(String[] args) {
        
        String configFile = "Parametri.xml";
        if (args.length >= 1) {
            configFile = args[0];
        }
        
        if (args.length > 1) {
            direktorijSDinamickimRazredima[3] = args[1];
        }
        registerParameters();

        parseConfig(configFile);
        readParameters(configFile);
        
        if (registry.isRegistered("randomizer.seed")) {
            long seed = Long.parseLong(registry.getEntry("randomizer.seed"));
            if (seed == 0) { randomizer = new Random(); }
            else { randomizer = new Random(seed); }
        } else { randomizer = new Random(); }

        //inicijalizacija svih genotipova
        for (int i = 0; i < genotype.size(); i++) {
//    		Genotype kopija = genotype.get(i).copy();
//    		kopija.initialize();
            Genotype gen = genotype.get(i);
            gen.initialize();
        }

        logger.initialize();

        //inicijalizacija algoritma
        algorithm.setCrossover(crossover);
        algorithm.setMutation(mutation);
        algorithm.initialize();
        
        //inicijalizacija populacije, križanja, mutacije i evaluacijskog sučelja
        population.initialize();
        crossover.initialize();
        mutation.initialize();
        ievaluate.initialize();

        //inicijalizacija operatora terminiranja
        for (Operator op : allTerminationOps) {
            if(op.initialize()){
                activeTerminationOps.add(op);
            }
        }

        //inicijalizacija migracije
        migration.initialize();
    }
    
    /**
     * Returns random number generator.
     * 
     * @return Random number generator 
     */
    public Random getRandomizer() { return randomizer; }

    /**
     * Metoda koja pokreće genetski algoritam.
     */
    public void run() {
        if (algorithm.isParallel()) { 
            if (communicator == null) {
                throw new IllegalStateException("MPI must be enabled for parallel algorithms");
            }
            runParallel(); 
        }
        else { runSequential(); }
    }
    
    private void runParallel() {
        communicator.createDemeCommunicator(population.getNoDemes());
        
        if (communicator.getCommGlobalRank() == 0) {
            
            startTime = System.currentTimeMillis();
            logger.log(3, "Start time: " + ispisiVrijeme(startTime));

            bTerminate = false;
            generationNo = 0;

            logger.log(2, "Evaluating initial population...");
            algorithm.initializePopulation();

            logger.log(2, "Generation: " + generationNo);
            zabiljeziProtekloVrijeme();
            
            population.updateDemeStats(); // operate nad operatorima terminiranja
            for (Operator op : this.activeTerminationOps) {
                op.operate();
            }
        
            logger.log(4, "Current best: " + population.getHof().getBest().get(0));
            
            logger.saveTo(true);
        }
//        System.out.println(population);

        while (!bTerminate) {
//            logger.saveTo(true);
            algorithm.advanceGeneration();
            generationNo++;

            if (communicator.getCommGlobalRank() == 0) {
                logger.log(2, "Generation: " + generationNo);
                zabiljeziProtekloVrijeme();

                population.updateDemeStats();
            }
            
            if (communicator.getCommGlobalRank() == 0) {
                for (Operator op : this.activeTerminationOps) {
                    op.operate();
                }
                //TODO: Treba radit sa deme masterom
                for (int i = 1; i < population.getNoDemes(); i++) {
                    communicator.sendTerminateMessage(i, bTerminate);
                }
            } else if (communicator.getCommRank() == 0) {
                bTerminate = communicator.receiveTerminateMessage(0);
            }
            
            algorithm.broadCastTermination();
            
            if (communicator.getCommGlobalRank() == 0) {
                logger.log(4, "Current best: " + population.getHof().getBest().get(0));
            }
            
            logger.saveTo(true);

            if (communicator.getCommRank() == 0) {
                migration.operate();
            }
        }
        
        if (communicator.getCommGlobalRank() == 0) {
            logger.log(1, population.toString());
            logger.log(1, "Best of run: " + population.getHof().getBest().get(0));
            logger.log(1, "End time: " + ispisiVrijeme(System.currentTimeMillis()));
            zabiljeziProtekloVrijeme();
            logger.saveTo(true);
        }
    }
    
    private void runSequential() {
        startTime = System.currentTimeMillis();
        logger.log(3, "Start time: " + ispisiVrijeme(startTime));

        bTerminate = false;
        generationNo = 0;

        logger.log(2, "Evaluating initial population...");
        algorithm.initializePopulation();
        
        logger.log(2, "Generation: " + generationNo);
        zabiljeziProtekloVrijeme();
        
        population.updateDemeStats(); // operate nad operatorima terminiranja
        for (Operator op : this.activeTerminationOps) {
            op.operate();
        }
        logger.log(4, "Current best: " + population.getHof().getBest().get(0));

        while (!bTerminate) {
//            logger.saveTo(true);
            algorithm.advanceGeneration();
            generationNo++;

            logger.log(2, "Generation: " + generationNo);
            zabiljeziProtekloVrijeme();
//            logger.saveTo(true);

            population.updateDemeStats();
            for (Operator op : this.activeTerminationOps) {
                op.operate();
            }
            
            logger.log(4, "Current best: " + population.getHof().getBest().get(0));

            migration.operate();
            
            logger.saveTo(true);
        }
        logger.log(1, population.toString());
        logger.log(1, "Best of run: " + population.getHof().getBest().get(0));
        logger.log(1, "End time: " + ispisiVrijeme(System.currentTimeMillis()));
        zabiljeziProtekloVrijeme();
        logger.saveTo(true);
    }

    //TODO: Fali run batch
    
    /**
     * Metoda koja parsira konfiguracijsku xml datoteku.
     * 
     * @param imeDatoteke Ime konfiguracijske datoteke
     */
    private void parseConfig(String imeDatoteke) {
        try {
            boolean algoritamParsiran = false;
            boolean genotipParsiran = false;
            boolean registryParsiran = false;

            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new FileInputStream(imeDatoteke)));
            Document doc = parser.getDocument();
            Element root = doc.getDocumentElement();
            NodeList djeca = root.getChildNodes();

            for (int i = 0; i < djeca.getLength(); i++) {
                Node cvor = djeca.item(i);
                if (cvor.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element element = (Element) cvor;
                if (element.getLocalName().equalsIgnoreCase("Algorithm") && !algoritamParsiran) {
                    parseAlgorithm(element);
                    algoritamParsiran = true;
                } else if (element.getLocalName().equalsIgnoreCase("Genotype") && !genotipParsiran) {
                    parseGenotype(element);
                    genotipParsiran = true;
                } else if (element.getLocalName().equalsIgnoreCase("Registry") && !registryParsiran) {
                    registry.readEntries(element);
                    registryParsiran = true;
                } else {
                    logger.log(2, "Warning: unknown node or already parsed: " + element.getLocalName());
                }
            }
        } catch (FileNotFoundException e) {
            greska("Ne mogu pronaći datoteku s parametrima (" + imeDatoteke + ")");
        } catch (Exception e) {
            greska("Greška: " + e.getMessage());
        }
    }

    /**
     * Metoda koja čita iz konfiguracijske datoteke
     * koji će se algoritam koristiti.
     * 
     * @param element Čvor koji sadrži algoritam
     */
    private void parseAlgorithm(Element element) {
        try {
            boolean algoritamUcitan = false;
            NodeList djeca = element.getChildNodes();
            for (int i = 0; i < djeca.getLength(); i++) {
                Node cvor = djeca.item(i);
                if (cvor.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (algoritamUcitan) {
                    logger.log(1, "Warning: multiple Algorithm nodes found! (using the first one)");
                    return;
                }

                Element algoritam = (Element) cvor;

                String imeRazreda = null;
                int dirPoz = 0;
                Object objekt = null;		//potraži u svim direkorijima potrebne razrede
                while (dirPoz < direktorijSDinamickimRazredima.length && objekt == null) {
                    String trenutni = direktorijSDinamickimRazredima[dirPoz];
                    String imeDatoteke = nadjiDatoteku(trenutni + "/ecf/algorithm/",
                            algoritam.getLocalName() + ".class");
                    if (imeDatoteke != null) { 
                        imeRazreda = "ecf.algorithm." + imeDatoteke;
                        URLClassLoader loader = kreirajClassLoader(trenutni);
                        objekt = ucitajRazred(loader, imeRazreda);
                        if (objekt != null) {
                            break ;
                        }
                    }
                    dirPoz++;
                }

                if (objekt == null) {
                    greska("Ne mogu učitati razred " + algoritam.getLocalName());
                }
                if (!(objekt instanceof Algorithm)) {
                    greska("Greška: Učitan razred koji nije Algorithm");
                }
                logger.log(1, "Učitan razred " + imeRazreda);
                algoritamUcitan = true;

                algorithm = (Algorithm) objekt;
                NodeList parametri = algoritam.getChildNodes();
                for (int j = 0; j < parametri.getLength(); j++) {
                    Node node = parametri.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Element parametar = (Element) node;
                    algorithm.registerParameter(parametar.getAttribute("key"), parametar.getTextContent());
                }

            }
        } catch (MalformedURLException e) {
            logger.log(1, "Error: malformed url");
        }
    }

    /**
     * Metoda koja kreira URLClassLoader objekt.
     * 
     * @param trenutni Trenutni direktorij
     * @return Kreirani objekt
     * @throws MalformedURLException Iznimka zbog pogrešno zadanog direktorija
     */
    private URLClassLoader kreirajClassLoader(String trenutni)
            throws MalformedURLException {
        File direktorij = new File(trenutni);
        URL[] url = new URL[1];
        url[0] = direktorij.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(url, Thread.currentThread().getContextClassLoader());
        return loader;
    }

    /**
     * Metoda koja parsira genotipe iz konfiguracijske datoteke.
     * 
     * @param element Čvor koji sadrži genotipove
     */
    private void parseGenotype(Element element) {
        try {
            NodeList djeca = element.getChildNodes();
            for (int i = 0; i < djeca.getLength(); i++) {
                Node cvor = djeca.item(i);
                if (cvor.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element genotip = (Element) cvor;

                String imeRazreda = null;
                String trenutni = null;
                URLClassLoader loader = null;
                int dirPoz = 0;
                Object objekt = null;		//potraži u svim direkorijima potrebne razrede
                while (dirPoz < direktorijSDinamickimRazredima.length && objekt == null) {
                    trenutni = direktorijSDinamickimRazredima[dirPoz];
                    String imeDatoteke = nadjiDatoteku(trenutni + "/ecf/genotype/"
                            + genotip.getLocalName().toLowerCase(), genotip.getLocalName() + ".class");
                    if (imeDatoteke != null) {
                        imeRazreda = "ecf.genotype." + genotip.getLocalName().toLowerCase() + "." + imeDatoteke;

                        loader = kreirajClassLoader(trenutni);
                        objekt = ucitajRazred(loader, imeRazreda);
                    }
                    dirPoz++;
                }


                if (objekt == null) {
                    greska("Ne mogu učitati razred " + genotip.getLocalName());
                }
                if (!(objekt instanceof Genotype)) {
                    greska("Greška: Učitan razred koji nije Genotype");
                }
                logger.log(1, "Učitan razred " + imeRazreda);

                Genotype gen = (Genotype) objekt;
                addGenotype(gen);

                NodeList operatori = genotip.getChildNodes();
                for (int j = 0; j < operatori.getLength(); j++) {
                    Node node = operatori.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Element operator = (Element) node;
                    parseCrxMut(operator, gen, "/ecf/genotype/" + genotip.getLocalName());
                }

            }
        } catch (MalformedURLException e) {
            greska("Error: malformed url " + direktorijSDinamickimRazredima);
        }
    }
    
    /**
     * Adds genotype to current state.
     * @param gen Genortype that will be used
     */
    public void addGenotype(Genotype gen) {
        gen.setGenotypeId(genotype.size());
        gen.registerParameters();
        genotype.add(gen);
    }

    /**
     * Metoda koja parsira parametre genotipa te operatore
     * križanja i mutacije iz datoteke.
     *
     * @param element XML čvor koji se parsira
     * @param genotip Genotip koji se parsira
     * @param direktorij Direktorij u kojem se traže datoteke
     * @throws MalformedURLException
     */
    private void parseCrxMut(Element element, Genotype genotip, String direktorij) throws MalformedURLException {
        if (!element.hasAttribute("key")) {
            return;
        }

        String atribut = element.getAttribute("key");
        String vrijednost = element.getTextContent();
        String imeJavaRazreda = "ecf.genotype." + genotip.getName().toLowerCase() + ".";

        if (atribut.startsWith("crx.")) {				//ako je riječ o križanju
            String imeDatoteke = genotip.getName() + atribut.replace(".", "") + ".class";
            String datoteka = null;

            int dirPoz = 0;
            Object objekt = null;		//potraži u svim direkorijima potrebne razrede
            while (dirPoz < direktorijSDinamickimRazredima.length && objekt == null) {
                String trenutni = direktorijSDinamickimRazredima[dirPoz];
                datoteka = nadjiDatoteku(trenutni + direktorij, imeDatoteke);

                URLClassLoader loader = kreirajClassLoader(trenutni);
                objekt = ucitajRazred(loader, imeJavaRazreda + datoteka);
                dirPoz++;
            }

            if (objekt == null) {
                greska("Ne mogu učitati razred " + imeJavaRazreda + imeDatoteke);
            }
            if (!(objekt instanceof CrossoverOp)) {
                greska("Greška: Učitan razred koji nije CrossoverOp");
            }
            logger.log(1, "Učitan razred " + imeJavaRazreda + datoteka);

            CrossoverOp krizanje = (CrossoverOp) objekt;
            krizanje.setMyGenotype(genotip.copy());
            krizanje.registerParameters();
            crossover.addOperator(krizanje, genotip.getGenotypeId());
        } 
        else if (atribut.startsWith("mut.")) {		//ako je riječ o mutaciji
            String imeDatoteke = genotip.getName() + atribut.replace(".", "") + ".class";
            String datoteka = null;

            int dirPoz = 0;
            Object objekt = null;		//potraži u svim direkorijima potrebne razrede
            while (dirPoz < direktorijSDinamickimRazredima.length && objekt == null) {
                String trenutni = direktorijSDinamickimRazredima[dirPoz];
                datoteka = nadjiDatoteku(trenutni + direktorij, imeDatoteke);
                URLClassLoader loader = kreirajClassLoader(trenutni);
                objekt = ucitajRazred(loader, imeJavaRazreda + datoteka);
                dirPoz++;
            }

            if (objekt == null) {
                greska("Ne mogu učitati razred " + imeJavaRazreda + imeDatoteke);
            }
            if (!(objekt instanceof MutationOp)) {
                greska("Greška: Učitan razred koji nije MutationOp");
            }
            logger.log(1, "Učitan razred " + imeJavaRazreda + datoteka);

            MutationOp mutacija = (MutationOp) objekt;
            mutacija.setMyGenotype(genotip.copy());
            mutacija.registerParameters();
            mutation.addOperator(mutacija, genotip.getGenotypeId());
        }

        registry.modifyEntry(genotip.getName() + genotip.getGenotypeId() + "." + atribut, vrijednost);
    }

    /**
     * Metoda koja dinamički učitava razred.
     *
     * @param loader Classloader koji se koristi za učitavanje
     * @param imeJavaRazreda Ime razreda koji se učitava
     * @return Učitani razred
     */
    private Object ucitajRazred(URLClassLoader loader, String imeJavaRazreda) {
        try {
//            Thread.currentThread().setContextClassLoader(loader);
//            Class<?> razred = Thread.currentThread().getContextClassLoader().loadClass(imeJavaRazreda);
            Class<?> razred = loader.loadClass(imeJavaRazreda);
            Constructor<?> konstruktor = razred.getConstructor(State.class);
            return konstruktor.newInstance(this);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Metoda koja nalazi .class datoteku u nekom direktoriju
     * tretirajući velika i mala slova jednako. Pretražuje i
     * unutrašnjost jar arhiva.
     *
     * @param imeDirektorija Ime direktorija u kojem se traži datoteka
     * @param imeDatoteke Ime datoteke koja se traži
     * @return Ime tražene datoteke bez ekstenzije ili null ako datoteka nije nađena
     */
    private String nadjiDatoteku(String imeDirektorija, String imeDatoteke) {
        try {
            if (imeDirektorija.contains(".jar")) {					//ako je riječ o jar datoteci
                String trenutniDir = imeDirektorija.substring(0, imeDirektorija.indexOf(".jar") + 4);
                JarFile jar = new JarFile(trenutniDir);
                Enumeration<JarEntry> enumeration = jar.entries();

                while (enumeration.hasMoreElements()) {
                    String temp = enumeration.nextElement().getName();
                    int index = temp.lastIndexOf("/");
                    String ime = temp.substring(index + 1);
                    if (ime.equalsIgnoreCase(imeDatoteke)) {
                        return ime.substring(0, ime.length() - 6);	//micanje .class ekstenzije
                    }
                }
            }
        } catch (IOException e) {
            return null;
        }

        File direktorij = new File(imeDirektorija);
        if (!direktorij.isDirectory()) {
            return null;
        }

        File[] datoteke = direktorij.listFiles();
        for (File datoteka : datoteke) {
            if (datoteka.isFile() && datoteka.getName().equalsIgnoreCase(imeDatoteke)) {
                String ime = datoteka.getName();
                return ime.substring(0, ime.length() - 6);	//micanje .class ekstenzije
            }
        }
        return null;
    }

    /**
     * Metoda koja u log bilježi koliko dugo algoritam radi.
     * Vrijeme se bilježi u obliku pogodnom za čovjeka.
     */
    private void zabiljeziProtekloVrijeme() {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - startTime;

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        logger.log(1, "Elapsed time: " + dateFormat.format(new Date(elapsedTime)));
    }

    /**
     * Metoda koja ispisuje vrijeme u obliku
     * pogodnom za čovjeka.
     * 
     * @param time Oznaka proteklog vremena
     * @return
     */
    private String ispisiVrijeme(long time) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S").format(new Date(time));
    }

    /**
     * Metoda koja vraća trenutnu iteraciju algoritma.
     * 
     * @return Broj generacije u kojoj se algoritam nalazi
     */
    public int getGenerationNo() {
        return generationNo;
    }

    /**
     * Metoda koja registrira parametre potrebne za rad 
     * algoritma. Ne registriraju se parametri razreda 
     * koji se dinamički učitavaju, a to su Genotype,
     * CrossoverOp, MutationOp i Algorithm.
     */
    private void registerParameters() {
        //registriraj svoje parametre
        registry.registerEntry("milestone.interval", "0");
        registry.registerEntry("milestone.filename", "");
        registry.registerEntry("randomizer.seed", "0");

        //registriraj parametre populacije, križanja, mutacije, evaluacijskog sučelja, operatora i loggera
        population.registerParameters();
        crossover.registerParameters();
        mutation.registerParameters();
        ievaluate.registerParameters();
        logger.registerParameters();
        migration.registerParameters();
        
        //registirati parametre operatora
        for (Operator op : allTerminationOps) {
            op.registerParameters();
        }
    }

    /**
     * Metoda koja učitava parametre iz konfiguracijske
     * datoteke pozivom odgovarajuće metode u Registryju.
     * Postavlja nove vrijednosti svojih parametara.
     *  
     * @param imeDatoteke Ime konfiguracijske datoteke
     */
    private void readParameters(String imeDatoteke) {
        milestoneInterval = Integer.parseInt(registry.getEntry("milestone.interval"));
        milestoneFilename = registry.getEntry("milestone.filename");
        bSaveMilestone = registry.isModified("milestone.filename");
    }

    /**
     * Metoda koja prekida program u slučaju greške.
     * Dotadašnji log ispisuje u datoteku error.txt.
     */
    private void greska(String poruka) {
        logger.log(1, poruka);
        logger.saveTo("error.txt", false);
        System.exit(1);
    }

    /**
     * Metoda koja vraća populaciju trenutnog stanja.
     * 
     * @return Tražena populacija
     */
    public Population getPopulation() {
        return population;
    }

    /**
     * Metoda koja vraća algoritam trenutnog stanja.
     * 
     * @return Traženi algoritam
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Method returns communicator object.
     * 
     * @return Communicator
     */
    public Communicator getCommunicator() {
        return communicator;
    }

    /**
     * Metoda koja vraća fitness objekt.
     * 
     * @return Traženi fitness
     */
    public Fitness getFitness() {
        return fitness;
    }

    /**
     * Metoda koja vraća sve genotipe trenutnog stanja.
     * 
     * @return Vektor genotipova
     */
    public Vector<Genotype> getGenotypes() {
        return genotype;
    }

    /**
     * Metoda koja vraća Registry objekt.
     * 
     * @return Registry objekt
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * Metoda koja dohvaća logger objekt.
     *
     * @return Objekt koji se koristi za logiranje
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Metoda koja postavlja uvjet prekida algoritma
     */
    public void setTerminateCond() {
        bTerminate = true;
    }
    
    
    /**
     * Metoda koja provjerava je li uvjet zaustavljanja
     * algoritma postavljen.
     * 
     * @return True ako je uvjet postavljen, inače false
     */
    public boolean getTeminateCond() {
    	return bTerminate;
    }

    /**
     * Metoda koja vraća koliko dugo algoritam radi.
     * 
     * @return vrijednost trajanja algoritma u ms
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Returns Crossover object.
     * 
     * @return Crossover object 
     */
    public Crossover getCrossover() {
        return crossover;
    }
    
    /**
     * Returns Mutation object.
     * 
     * @return Mutation object 
     */
    public Mutation getMutation() {
        return mutation;
    }
    
    /**
     * Metoda koja vraća korištene operatore zaustavljanja
     * algoritma.
     * 
     * @return aktivni operatori zaustavljanja
     */
    public Vector<Operator> getTerminatingOperators(){
        return activeTerminationOps;
    }
    
    /**
     * Returs flag that tells if we are running in batch mode
     * 
     * @return True if in batch mode, false if not
     */
    public boolean getBatchMode() {
        return batchMode;
    }
}
