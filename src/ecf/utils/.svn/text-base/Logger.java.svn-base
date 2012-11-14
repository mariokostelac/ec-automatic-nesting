package ecf.utils;

import ecf.Communicator;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import ecf.State;
import java.text.MessageFormat;


/**
 * Razred koji služi za izradu log datoteke.
 * Log datoteka daje popis postupaka koji su se
 * izvodili pod utjecajem ECF-a.
 * Dozvoljava se 5 razina logova (1-5), s time
 * da veća razina označava više detalja u logu.
 * 
 * @author Marko Pielić
 */
public class Logger {

	private boolean bFileDefined;
	private State state;
	private Vector<Log> logs;
	private int currentLevel;
	private int logFrequency;
	private String  logFileName;
	private BufferedWriter logFile;
	
	
	/**
	 * Konstruktor.
	 * Pretpostavljena razina logiranja je 3 (medium logging level).
	 * 
	 * @param state Objekt koji sadrži stanje algoritma
	 */
	public Logger(State state) {
		logs = new Vector<Log>();
		currentLevel = 3;
		bFileDefined = false;
		this.state = state;
	}
	
	
	/**
	 * Metoda za registraciju parametara loggera.
	 */
	public void registerParameters(){
		state.getRegistry().registerEntry("log.level", "3");
		state.getRegistry().registerEntry("log.filename", "");
		state.getRegistry().registerEntry("log.frequency", "1");
	}
	
	
	/**
	 * Metoda za inicijalizaciju razreda.
	 */
	public void initialize(){
		currentLevel = Integer.valueOf(state.getRegistry().getEntry("log.level"));
		logFrequency = Integer.valueOf(state.getRegistry().getEntry("log.frequency"));
		if (state.getCommunicator() != null && state.getCommunicator().getCommGlobalRank() != 0) { 
                    bFileDefined = true;
                    return ; 
                } 
		try{
			if(state.getRegistry().isModified("log.filename")){
				logFileName = state.getRegistry().getEntry("log.filename");
				logFile = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(logFileName), "UTF-8"));
				bFileDefined = true;
			}
		}
		catch(IOException e){
			System.err.println("Greška: ne mogu otvoriti datoteku " + logFileName);
			System.exit(10);
		}
	}
	
	
	/**
	 * Metoda koja dodaje novu poruku u log.
	 * 
	 * @param logLevel Razina loga
	 * @param message Poruka koja se dodaje u log
	 */
	public void log(int logLevel, String message){
		if(logLevel>currentLevel) return;
		
		if(state.getGenerationNo()==0 || state.getTeminateCond() || 	//piši ako algoritam nije počeo, ako je
				state.getGenerationNo()%logFrequency==0){				//završio ili zbog logFrequency

			if(logLevel>5) logLevel = 5;
			if(logLevel<1) logLevel = 1;
                        Log log;
                        if (state.getCommunicator() != null) {
                            log = new Log(logLevel, state.getCommunicator().getCommGlobalRank() + ": " + message);
                        } else {
                            log = new Log(logLevel, message);
                        }
			logs.add(log);
		}
	}
        
        /**
         * Adds new log using java.text.MessageFormat format.
         * 
         * @param logLevel Log level
         * @param message Message pattern
         * @param params Objects used to replace placeholders in pattern
         */
        public void log(int logLevel, String message, Object[] params) {
            log(logLevel, MessageFormat.format(message, params));
        }
	
	
	/**
	 * Metoda koja ispisuje logove u log datoteku.
	 * Ako datoteka već postoji, logovi se dodaju na kraj.
	 * Format zapisa je "logLevel message".
	 * 
	 * @param filename Ime log datoteke
	 * @param append Ako je true, dodaje se na kraj datoteke, inače se sadržaj datoteke briše
	 */
	public void saveTo(String filename, boolean append){
		try{
                    if (state.getCommunicator() == null || state.getCommunicator().getCommGlobalRank() == 0) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename, append), "UTF-8"));
			ispisiLogove(writer, false);
                    } else { ispisiLogove(null, false); }
		}
		catch(IOException e){
			System.err.println("Greška: ne mogu kreirati datoteku " + filename);
		}	
	}

	
	/**
	 * Metoda koja ispisuje logove u predefiniranu log datoteku.
	 * Format zapisa je "logLevel message".
	 * 
	 * @param check Ako je true logira se sve bez obzira na frekvenciju logiranja
	 */
	public void saveTo(boolean check){
		if(!bFileDefined) return;
		if(!check && state.getGenerationNo()>1 && state.getGenerationNo()%logFrequency!=0) {
			logs.clear();
			return;
		}
		ispisiLogove(logFile, false);
	}
	
	
	/**
	 * Metoda koja ispisuje logove u log datoteku.
	 * Ako datoteka već postoji, logovi se dodaju na kraj.
	 * Koristi se XML zapis.
	 * 
	 * @param filename Ime log datoteke
	 */
	public void saveToX(String filename){
		try{
                    if (state.getCommunicator().getCommGlobalRank() == 0) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename, true), "UTF-8"));
			ispisiLogove(writer, true);
                    } else { ispisiLogove(null, false); }
		}
		catch(IOException e){
			System.err.println("Greška: ne mogu kreirati datoteku " + filename);
		}	
	}
	
	
	/**
	 * Metoda koja ispisuje logove u predefiniranu log datoteku.
	 * Koristi se XML zapis.
	 */
	public void saveToX(){
		if(!bFileDefined) return;
		ispisiLogove(logFile, true);
	}
	
	
	
	/**
	 * Metoda koja ispisuje sve pohranjene logove u zadanu datoteku.
	 * Ispisane logove briše iz memorije, a datoteku na kraju zatvara.
	 * 
	 * @param datoteka Datoteka u koju se piše
	 * @param xml True ako se koristi xml zapis, inače false
	 */
	private void ispisiLogove(BufferedWriter datoteka, boolean xml){
		try{
                        StringBuilder logsString = new StringBuilder(1024);
			if(xml){
				logsString.append("<log>\n");
				for(Log log:logs){
					logsString.append("<log><message logLevel=\"")
                                            .append(log.getLogLevel())
                                            .append("\">")
                                            .append(log.getMessage())
                                            .append("</message></log>\n");
				}
				logsString.append("</log>");
			}
			else{
				for(Log log:logs){
					logsString.append(log.getLogLevel())
                                            .append(" ")
                                            .append(log.getMessage())
                                            .append("\n");
				}
			}
                        Communicator communicator = state.getCommunicator();
                        if (communicator != null && communicator.getCommGlobalSize() != 1) {
                            if (communicator.getCommGlobalRank() == 0) {
                                String temp = logsString.toString();
                                datoteka.write(temp);
                                System.out.println(temp);
                                temp = communicator.receiveLogsGlobal();
                                datoteka.write(temp);
                                System.out.println(temp);
                                datoteka.flush();
                            } else {
                                communicator.sendLogsGlobal(logsString.toString());
                            }
                        } else { 
                            String temp = logsString.toString();
                            datoteka.write(temp); 
                            System.out.println(temp);
                            datoteka.flush();
                        }
			flushLog(); 
		}
		catch(IOException e){
			System.err.println("Greška prilikom pisanja u datoteku!");
		}
	}
	
	
	/**
	 * Metoda koja briše sve log podatke iz memorije.
	 */
	public void flushLog(){
		logs.clear();
	}
	
	
	/**
	 * Metoda koja sprema log u datoteku.
	 */
	public void operate(){
		saveTo(false);
	}
	
	
	/**
	 * Metoda koja vraća frekvenciju logiranja.
	 * 
	 * @return Broj koji označava frekvenciju logiranja
	 */
	public int getLogFrequency() {
		return logFrequency;
	}

	
	/**
	 * Metoda koja postavlja frekvenciju logiranja.
	 * 
	 * @param logFrequency Nova frekvencija logiranja.
	 */
	public void setLogFrequency(int logFrequency) {
		this.logFrequency = logFrequency;
	}
	
	
	/**
	 * Razred koji predstavlja jedan log.
	 * Svaki log ima svoju razinu i poruku.
	 * 
	 * @author Marko Pielić
	 */
	private static class Log {
		private int logLevel;
		private String message;
		
		/**
		 * Konstruktor koji kreira novi log.
		 * 
		 * @param logLevel Razina loga
		 * @param message Log poruka
		 */
		public Log(int logLevel, String message) {
			this.logLevel = logLevel;
			this.message = message;
		}
		
		/**
		 * Metoda koja vraća razinu loga.
		 * 
		 * @return Razina loga
		 */
		public int getLogLevel() {
			return logLevel;
		}
		
		/**
		 * Metoda koja postavlja razinu loga.
		 * 
		 * @param logLevel Nova razina loga
		 */
		@SuppressWarnings("unused")
		public void setLogLevel(int logLevel) {
			this.logLevel = logLevel;
		}
		
		/**
		 * Metoda koja dohvaća poruku loga.
		 * 
		 * @return Poruka loga
		 */
		public String getMessage() {
			return message;
		}
		
		/**
		 * Metoda koja postavlja poruku loga.
		 * 
		 * @param message Nova poruka loga
		 */
		@SuppressWarnings("unused")
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
