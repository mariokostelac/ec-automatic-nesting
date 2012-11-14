
package ecf;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mpi.Intracomm;
import mpi.MPI;
import mpi.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Marijan Šuflaj
 */
public class Communicator {
    
    private boolean isInitialized   = false;
    
    private double idleTimeLocal         = 0;
    
    private double sendTime         = 0;
    
    private double receiveTime      = 0;
    
    private double compTime         = 0;
    
    private double packTime         = 0;
    
    private double unpackTime       = 0;
    
    private double endTime          = 0;
    
    private double lastTime;
    
    private double beginTime;
    
    private int logLevel            = 4;
    
    private int sendCount           = 0;
    
    private int receiveCount        = 0;
    
    private int mpiGlobalRank;
    
    private int mpiGlobalSize;
    
    private String processorName;
    
    private State state;
    
    private Intracomm demeCommunicator;
    
    private Intracomm frameworkCommunicator;
    
    private int mpiSize;
    
    private int mpiRank;
    
    private int[] demeMasters;
    
    private Status status;
    
    private String[] realArgs;
    
    //Tagovi
    public static final int T_DEFAULT = 0;
        
    public static final int T_CONTROL = 99;
    
    public static final int T_CONTINUE = 1;
    
    public static final int T_TERMINATE = 2;
    
    public static final int T_VALUES = 3;
    
    public static final int T_LOGS = 4;
    
    public static final int T_FINAL = 5;
    
    public static final int T_DATA = 6;
        
    public enum Timing {
        COMP, IDLE, SEND, RECEIVE, PACK, UNPACK
    }
    
    private enum SendReceiveType {
        INDIVIDUAL {
            @Override
            public String getLoggerName() {
                return "individuals";
            }
        }, FITNESS {
            @Override
            public String getLoggerName() {
                return "fitness objects";
            }
        };
        
        public abstract String getLoggerName();
    }
    
    public Communicator() { }
    
    public String[] getRealArgs() { return realArgs; }
    
    public boolean initialize(State state, String[] args) {
        if (isInitialized) {
            beginTime = lastTime = MPI.Wtime();
            state.getLogger().log(2, "Process {0} of {1} on {2}", new Object[] { mpiGlobalRank, mpiGlobalSize, processorName });
            return true;
        }
        
        this.state = state;
        
        realArgs = MPI.Init(args);

        mpiGlobalSize = MPI.COMM_WORLD.Size();
        mpiGlobalRank = MPI.COMM_WORLD.Rank();
        
        demeCommunicator = (Intracomm) MPI.COMM_WORLD.clone();
        mpiSize = demeCommunicator.Size();
        mpiRank = demeCommunicator.Rank();
        
        frameworkCommunicator = (Intracomm) MPI.COMM_WORLD.clone();

        try {
            processorName = InetAddress.getLocalHost().getHostName();
        } catch(UnknownHostException e) {
            processorName = "Unknown-" + mpiGlobalRank;
        }

        beginTime = lastTime = MPI.Wtime();
        
        state.getLogger().log(2, "Process {0} of {1} on {2}", new Object[] { mpiGlobalRank, mpiGlobalSize, processorName });
        
        isInitialized = true;

        return true;
    }
    
    public int createDemeCommunicator(int numberOfDemes) {
        //TODO: Cini se da bas ne radi dobro MPJ sa splitanjem komunikatora
        int myColor = mpiGlobalRank % numberOfDemes;
        
        demeMasters = new int[numberOfDemes];
        
        for (int i = 0; i < numberOfDemes; i++) {
            demeMasters[i] = i;
        }
        
        demeCommunicator = MPI.COMM_WORLD.Split(myColor, mpiGlobalRank);
        mpiSize = demeCommunicator.Size();
        mpiRank = demeCommunicator.Rank();
        
        state.getLogger().log(
            2, "Global process {0} joined deme communicator with index "
            + "{1} (local rank: {2} of {3})", new Object[] {
                mpiGlobalRank, myColor, mpiRank, mpiSize
        });
        
        return myColor;
    }
    
    public int getCommGlobalSize() {
        return mpiGlobalSize;
    }
    
    
    public int getCommGlobalRank() {
        return mpiGlobalRank;
    }
    
    public int getCommRank() { 
        return mpiRank;
    }
    
    public int getCommSize() {
        return mpiSize;
    }
    
    public boolean finish() {
        if (!isInitialized) { return true; }
        
        endTime = MPI.Wtime();
        
        StringBuilder builder = new StringBuilder(200);
        builder.append("Process ")
            .append(mpiGlobalRank)
            .append(": total MPI time: ")
            .append(endTime - beginTime)
            .append(", COMP: ")
            .append(compTime)
            .append(", IDLE: ")
            .append(idleTimeLocal)
            .append(", SEND: ")
            .append(sendTime)
            .append(", RECV: ")
            .append(receiveTime)
            .append(", PACK: ")
            .append(packTime)
            .append(", UNPACK: ")
            .append(unpackTime)
            .append("\n");
        
        if (mpiGlobalRank == 0) {
            
            String[] in = new String[1];
            Status statusLocal;
            
            for (int i = 0; i < mpiGlobalSize; i++) {
                statusLocal = frameworkCommunicator.Probe(i, T_FINAL);
                frameworkCommunicator.Recv(in, 0, statusLocal.Get_count(MPI.OBJECT), MPI.OBJECT, i, T_FINAL);
                builder.append(in[0]);
            }
            
            state.getLogger().log(2, builder.toString());
            
        } else {
            frameworkCommunicator.Send(new String[] {
                builder.toString()
            }, 0, 1, MPI.OBJECT, 0, T_FINAL);
        }
        
        if (!state.getBatchMode()) {
            MPI.Finalize();
        }
         
        return true;
    }
    
    public double time(Timing time) {
        
        double currentTime = MPI.Wtime();
        double elpased = currentTime - lastTime;
        lastTime = currentTime;
        
        switch (time) {
            case COMP :
                compTime += elpased;
                break ; 
            case IDLE :
                idleTimeLocal += elpased;
                break ; 
            case SEND :
                sendTime += elpased;
                break ; 
            case RECEIVE :
                receiveTime += elpased;
                break ; 
            case PACK :
                packTime += elpased;
                break ; 
            case UNPACK :
                unpackTime += elpased;
                break ; 
        }
        return 1000 * elpased;
    }
    
    public int getDemeMaster(int demeID) {
        return demeMasters[demeID];
    }
    
    public int getLastSource() {
        if (status == null) { return -1; }
        return status.source;
    }
    
    public void synchronize() {
        MPI.COMM_WORLD.Barrier();
    }
    
    public boolean messageWaiting() {
        return messageWaiting(MPI.ANY_SOURCE, MPI.ANY_TAG);
    }
    
    public boolean messageWaiting(int processID, int tag) {
        Status temp = demeCommunicator.Iprobe(processID, tag);
        if (temp != null) {
            status = temp;
        }
        return temp != null;
    }
    
    public boolean sendControlMessage(int processID, int control) {
        demeCommunicator.Send(new int[] { control }, 0, 1, MPI.INT, processID, T_CONTROL);
        return true;
    }
    
    public int receiveControlMessage(int processID) {
        int[] control = new int[1];
        demeCommunicator.Recv(control, 0, 1, MPI.INT, processID, T_CONTROL);
        return control[0];
    }

    public boolean sendTerminateMessage(int processID, boolean termination) {
        state.getLogger().log(logLevel, "Sending terminate message to process {0}", new Object[] { processID });
	int tag = termination ? T_TERMINATE : T_CONTINUE;
	frameworkCommunicator.Send(new boolean[] { termination }, 0, 1, MPI.BOOLEAN, processID, tag);
        return true;
    }
    
    public boolean receiveTerminateMessage(int processID) {
	boolean[] termination = new boolean[1];
//        System.out.println("Primio terminate message");
	status = frameworkCommunicator.Recv(termination, 0, 1, MPI.BOOLEAN, processID, MPI.ANY_TAG);
	return termination[0];
    }
    
    public boolean checkTerminationMessage(int master) {
        Status tempStatus;
        
        if((tempStatus = frameworkCommunicator.Iprobe(master, MPI.ANY_TAG)) != null) {
            if(tempStatus.tag == T_TERMINATE) { return true; }
            else { return receiveTerminateMessage(master); }
        }
        
        return false;
    }
    
    public boolean sendIndividuals(Vector<Individual> pool, int processID) {
        return sendIndividuals(pool, processID, 0);
    }
    
    public boolean sendIndividuals(Vector<Individual> pool, int processID, int numberOfIndividuals) {
        return sendIndividualsHelperInternal(demeCommunicator, "", pool, processID, numberOfIndividuals, SendReceiveType.INDIVIDUAL);
    }
    
    public boolean sendIndividualsGlobal(Vector<Individual> pool, int processID) {
        return sendIndividualsGlobal(pool, processID, 0);
    }
    
    public boolean sendIndividualsGlobal(Vector<Individual> pool, int processID, int numberOfIndividuals) {
        return sendIndividualsHelperInternal(frameworkCommunicator, " (global)", pool, processID, numberOfIndividuals, SendReceiveType.INDIVIDUAL);
    }

    private boolean sendIndividualsHelperInternal(Intracomm communicator, String extra, Vector<Individual> pool, int processID, int numberOfIndividuals, SendReceiveType sendType) {
        
        time(Timing.COMP);
        
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        // root elements
        Document document = documentBuilder.newDocument();
        Element packElement = document.createElement("Pack");
        document.appendChild(packElement);
        
        if (numberOfIndividuals < 1 || numberOfIndividuals > pool.size()) {
            numberOfIndividuals = pool.size();
        }
        
        packElement.setAttribute("size", Integer.toString(numberOfIndividuals));


        for (int i = 0; i < numberOfIndividuals; i++) {
            Element element = document.createElement("Individual");
            switch (sendType) {
                case INDIVIDUAL :
                    pool.get(i).write(element);
                    break;
                case FITNESS :
                    pool.get(i).getFitness().write(element);
                    break ;
            }
            element.setAttribute("i", Integer.toString(pool.get(i).getIndex()));
            //TODO: Spominje se nekakav cid no ne znam trenutno sto je to
            packElement.appendChild(element);
        }
        
        Transformer transformer;
        
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            state.getLogger().log(2, ex.getMessage());
            return false;
        }
        
        StringWriter stringWriter = new StringWriter(200);
        
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(stringWriter);
        
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            state.getLogger().log(2, ex.getMessage());
            return false;
        }
        
        String message = stringWriter.toString();
        double createTime = time(Timing.PACK);
        
        communicator.Send(new String[] { message }, 0, 1, MPI.OBJECT, processID, T_DEFAULT);
        
        double sendTimeLocal = time(Timing.SEND);
        
        switch (sendType) {
            case INDIVIDUAL :
                state.getLogger().log(
                    logLevel, 
                    "sent {0} individuals{1}, {2} bytes (P: {3} | S: {4})", 
                    new Object[] {
                        numberOfIndividuals, extra, message.length(), createTime, sendTimeLocal
                    }
                );
                break;
            case FITNESS :
                state.getLogger().log(
                    logLevel, 
                    "sent {0} fitness objects, {1} bytes (P: {2} | S: {3})", 
                    new Object[] {
                        numberOfIndividuals, message.length(), createTime, sendTimeLocal
                    }
                );
                break ;
        }
        
	return true;
    }
    
    
    public int receiveDemeIndividuals(Vector<Individual> deme, int processID) {
        return receiveIndividualsHelperInternal(demeCommunicator, deme, processID, false, SendReceiveType.INDIVIDUAL);
    }
    
    public Vector<Individual> receiveIndividuals(int processID) {
        Vector<Individual> pool = new Vector<Individual>();
        receiveIndividualsHelperInternal(demeCommunicator, pool, processID, true, SendReceiveType.INDIVIDUAL);
        return pool;
    }
    
    public Vector<Individual> receiveIndividualsGlobal() {
        return receiveIndividualsGlobal(MPI.ANY_SOURCE);
    }
    
    public Vector<Individual> receiveIndividualsGlobal(int processID) {
        Vector<Individual> pool = new Vector<Individual>();
        receiveIndividualsHelperInternal(frameworkCommunicator, pool, processID, true, SendReceiveType.INDIVIDUAL);
        return pool;
    }
    
    public int receiveReplaceIndividuals(Vector<Individual> pool, int processID) {
        return receiveIndividualsHelperInternal(demeCommunicator, pool, processID, true, SendReceiveType.INDIVIDUAL);
    }
    
    private int receiveIndividualsHelperInternal(Intracomm communicator, Vector<Individual> deme, int processID, boolean initialize, SendReceiveType receiveType) {
        time(Timing.COMP);
        
        Status statusLocal = communicator.Probe(processID, MPI.ANY_TAG);
        
        double idle = time(Timing.IDLE);
        
        String[] messageIn = new String[1];
        
        status = communicator.Recv(messageIn, 0, 1, MPI.OBJECT, processID, T_DEFAULT);
        
        double receiveTimeLocal = time(Timing.RECEIVE);
        
        DocumentBuilder documentBuilder;
        
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            state.getLogger().log(2, ex.getMessage());
            return 0;
        }
        
        Document document;
        
        try {
            document = documentBuilder.parse(new InputSource(new StringReader(messageIn[0])));
        } catch (SAXException ex) {
            state.getLogger().log(2, ex.getMessage());
            return 0;
        } catch (IOException ex) {
            state.getLogger().log(2, ex.getMessage());
            return 0;
        }
        
        Element documentElement = document.getDocumentElement();
        int numberOfIndividuals = Integer.parseInt(documentElement.getAttributes().getNamedItem("size").getNodeValue());
        
        NodeList list = documentElement.getChildNodes();
        
        int size = deme.size();
        
        for (int i = 0; i < numberOfIndividuals; i++) {
            Element singleElement = (Element) list.item(i);
            int index = Integer.parseInt(singleElement.getAttributes().getNamedItem("i").getNodeValue());
            
            switch (receiveType) {
                case INDIVIDUAL :
                    Individual individual;

                    if (i >= size) {
                        individual = new Individual(state.ievaluate, state);
                        individual.initialize();
                        individual.setFitness(state.getFitness().copy());
                        deme.add(individual);
                    } else { individual = deme.get(i); }

                    if (initialize) { individual.setIndex(index); }

                    individual.read(singleElement);
                    break ;
                case FITNESS :
                    deme.get(i).getFitness().read(singleElement);
                    break ;
            }
            
        }
        
	double readTimeLocal = time(Timing.UNPACK);
                
        state.getLogger().log(
            logLevel, 
            "received {0} {1}, {2} bytes (I: {3} P: {4} | U: {5})", 
            new Object[] {
                numberOfIndividuals, receiveType.getLoggerName(), messageIn[0].length(), idleTimeLocal, receiveTimeLocal, unpackTime
            }
        );
        
        return numberOfIndividuals;
    }
    
    public boolean sendFitness(Vector<Individual> pool, int processID) {
        return sendFitness(pool, processID, 0);
    }
    
    public boolean sendFitness(Vector<Individual> pool, int processID, int numberOfIndividuals) {
        return sendIndividualsHelperInternal(demeCommunicator, null, pool, processID, numberOfIndividuals, SendReceiveType.FITNESS);
    }
    
    public int receiveDemeFitness(Vector<Individual> pool, int processID) {
        return receiveIndividualsHelperInternal(demeCommunicator, pool, processID, true, SendReceiveType.FITNESS);
    }
    
    public boolean sendValuesGlobal(double[] values, int processID) {
        time(Timing.COMP);
        frameworkCommunicator.Send(values, 0, values.length, MPI.DOUBLE, processID, T_VALUES);
        time(Timing.SEND);
        state.getLogger().log(logLevel, "sent {0} doubles", new Object[] { values.length });
        return true;
    }
    
    public double[] receiveValuesGlobal(int processID) {
        time(Timing.COMP);
        Status statusLocal = frameworkCommunicator.Probe(processID, T_VALUES);
        
        time(Timing.IDLE);
        
        double[] elements = new double[statusLocal.Get_elements(MPI.DOUBLE)];
        
        frameworkCommunicator.Recv(elements, 0, elements.length, MPI.DOUBLE, processID, T_VALUES);
        
        time(Timing.RECEIVE);
        
        state.getLogger().log(logLevel, "received {0} doubles", new Object[] { elements.length });
        
        return elements;
    }
    
    public boolean sendLogsGlobal(String log) {
        return sendLogsGlobal(log, 0, false);
    }
    
    public boolean sendLogsGlobal(String log, int processID, boolean blocking) {
        
        time(Timing.COMP);
        
        if (blocking) {
            frameworkCommunicator.Send(new String[] { log }, 0, 1, MPI.OBJECT, processID, T_LOGS);
        } else {
            frameworkCommunicator.Isend(new String[] { log }, 0, 1, MPI.OBJECT, processID, T_LOGS);
        }
        
        time(Timing.SEND);
        
        state.getLogger().log(logLevel, "sent {0} log chars", new Object[] { log.length() });
        
        return true;
    }
    
    public String receiveLogsGlobal() {
        StringBuilder builder = new StringBuilder(200);
        time(Timing.COMP);
        
        Status localStatus;
        int logCount = 0;
        
        while((status = frameworkCommunicator.Iprobe(MPI.ANY_SOURCE, T_LOGS)) != null) {
            String[] in = new String[1];
            frameworkCommunicator.Recv(in, 0, 1, MPI.OBJECT, status.source, T_LOGS);
            logCount++;
            builder.append(in[0]);
        }
        state.getLogger().log(logLevel, "received {0} logs", new Object[] { logCount });
        
        return builder.toString();
    }
    
    public boolean sendDataGlobal(Object data, int processID) {
        
        time(Timing.COMP);
        
        frameworkCommunicator.Send(new Object[] { data }, 0, 1, MPI.OBJECT, processID, T_DATA);
        
        time(Timing.SEND);
        
        state.getLogger().log(logLevel, "sent 1 object");
        
        return true;
    }
    
    public Object receiveDataGlobal() { return receiveDataGlobal(MPI.ANY_SOURCE); }
    
    public Object receiveDataGlobal(int processID) {
        time(Timing.COMP);
        frameworkCommunicator.Probe(processID, T_DATA);
        time(Timing.IDLE);
        Object[] in = new Object[1];
        frameworkCommunicator.Recv(in, 0, 1, MPI.OBJECT, processID, T_DATA);
        time(Timing.RECEIVE);
        
        state.getLogger().log(logLevel, "received 1 object");
        
        return in[0];
    }
    
}
