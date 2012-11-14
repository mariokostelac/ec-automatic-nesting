package ecf.genotype.tree;

import java.util.Random;
import java.util.Vector;

import org.w3c.dom.Element;

import ecf.State;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;

/**
 * Razred koji predstavlja stableni genotip.
 * Podaci se čuvaju u obliku stabla. Stablo se
 * sastoji od niza čvorova, od kojih svaki može
 * imati svoje vlastite podčvorove.
 * 
 * @author Marko Pielić
 */
public class Tree extends Genotype {
	
	private int iNode;
	
	/**Početna dubina stabla*/
	private int startDepth;
	
	/**Najveća dubina stabla*/
	private int maxDepth;
	
	/**Najmanja dubina stabla*/
	private int minDepth;
	
	/**Skup primitiva*/
	private PrimitiveSet primitiveSet = new PrimitiveSet();
	
	/**Čvorovi stabla*/
	private Vector<Node> nodes = new Vector<Node>();
	
	/**Funkcije koje je definirao korisnik*/
	private Vector<Primitive<?>> userFunctions = new Vector<Primitive<?>>();

	
	/**
	 * Konstruktor stabla.
	 * 
	 * @param state Stanje algoritma
	 */
	public Tree(State state) {
		super(state, "Tree");
		startDepth = 0;
	}
	
	
	/**
	 * Metoda koja dodaje funkciju koju je definirao korisnik.
	 * @param func Korisnička funkcija
	 */
	public void addFunction(Primitive<?> func) {
		userFunctions.add(func);
	}
	
	
	/**
	 * Metoda koja izvršava trenutno stablo.
	 * 
	 * @return Rezultat izvršavanja
	 */
	public Object execute(){
		iNode = 0;
		
		Node node = nodes.get(iNode);
		Primitive<?> primitive = node.getPrimitive();
		Object obj = primitive.execute(this);
		return obj;
	}
	
	
	/**
	 * Metoda koja dodaje novi čvor u stablo.
	 * Novi se čvor dodaje na kraj stabla.
	 * 
	 * @param node Novi čvor
	 */
	public void add(Node node){
		nodes.add(node);
	}
	
	
	/**
	 * Metoda za izgradnju stabla potpunom metodom.
	 * 
	 * @param primitiveSet Skup primitiva
	 * @param myDepth Trenutna dubina
	 * @return Veličina trenutnog čvora
	 */
	private int fullBuild(PrimitiveSet primitiveSet, int myDepth){
		Node node = new Node();
		node.setDepth(myDepth);

		if(node.getDepth()<maxDepth){
			node.setPrimitive(primitiveSet.getRandomFunction());
			add(node);
		} 
		else{
			node.setPrimitive(primitiveSet.getRandomTerminal());
			add(node);
		}

		for(int i=0;i<node.getPrimitive().getNArguments();i++) {
			int size = node.size();
			node.setSize(size + fullBuild(primitiveSet, myDepth + 1));
		}
		return node.size();
	}
	
	
	/**
	 * Rekurzivna metoda za izgradnju stabla rastućom metodom.
	 * 
	 * @param primitiveSet Skup primitiva
	 * @param myDepth Trenutna dubina
	 * @return Veličina trenutnog čvora
	 */
	private int growBuild(PrimitiveSet primitiveSet, int myDepth){
		Node node = new Node();
		node.setDepth(myDepth);

		if(node.getDepth()<minDepth) {
			node.setPrimitive(primitiveSet.getRandomFunction());
			add(node);
		}
		else if(node.getDepth()<maxDepth) {
			node.setPrimitive(primitiveSet.getRandomPrimitive());
			add(node);
		}
		else {
			node.setPrimitive(primitiveSet.getRandomTerminal());
			add(node);
		}
		
		for(int i=0;i<node.getPrimitive().getNArguments();i++){
			int size = node.size();
			node.setSize(size + growBuild(primitiveSet, myDepth + 1));
		}

		return node.size();
	}
	
	
	/**
	 * Metoda koja izgrađuje stablo metodom rastuće izgradnje.
	 * 
	 * @param primitiveSet Skup primitiva
	 */
	public void growBuild(PrimitiveSet primitiveSet){
		growBuild(primitiveSet, startDepth);
	}


	/**
	 * Metoda koja izgrađuje stablo metodom potpune izgradnje.
	 * 
	 * @param primitiveSet Skup primitiva
	 */
	public void fullBuild(PrimitiveSet primitiveSet){
		fullBuild(primitiveSet, startDepth);
	}

	
	/**
	 * Metoda koja postavlja dubinu čvorova stabla
	 * i veličinu njihovih podstabla.
	 */
	public void update(PrimitiveSet primitiveSet){
		nodes.get(0).setSize(setSize(0));
		int nArgs = nodes.get(0).getPrimitive().getNArguments();

		iNode = 0;
		for(int i=0;i<nArgs;i++) {
			iNode++;
			setDepth(startDepth + 1);
		}
		nodes.get(0).setDepth(startDepth);
		this.primitiveSet = primitiveSet;
	}
	
	
	/**
	 * Metoda koja postavlja veličinu podstabla svakog čvora stabla.
	 * 
	 * @param iNode Indeks trenutnog čvora
	 * @return Trenutna veličina podstabla
	 */
	private int setSize(int iNode){
		int myNode = iNode;
		int mySize = 1;
		int nArgs = nodes.get(myNode).getPrimitive().getNArguments();
		
		for(int i=0;i<nArgs;i++) {
			int childSize = setSize(iNode + 1);
			mySize += childSize;
			iNode += childSize;
		}
		nodes.get(myNode).setSize(mySize);
		return mySize;
	}
	
	/**
	 * Metoda koja postavlja dubinu svih čvorova stabla.
	 * 
	 * @param myDepth Trenutna dubina
	 */
	private void setDepth(int myDepth){
		int index = iNode;
		int nArgs = nodes.get(iNode).getPrimitive().getNArguments();
		
		for(int i=0;i<nArgs;i++){
			iNode++;
			setDepth(myDepth + 1);
		}
		nodes.get(index).setDepth(myDepth);
	}
	
	
	/**
	 * Metoda koja postavlja vrijednost terminala.
	 * Postavljaju se vrijednosti svih terminala
	 * zadanog imena u stablu.
	 * 
	 * @param name Ime terminala
	 * @param value Vrijednost na koju se terminal postavlja
	 */
	public void setTerminalValue(String name, Object value){
		try{
			for(Node node:nodes){
				if(node.getPrimitive().getName().equals(name)){
					node.getPrimitive().setValue(value);
				}
			}
		}
		catch(Exception e){
			state.getLogger().log(1, "Tree genotype: invalid terminal name referenced in setTerminalValue()");
			prekiniIzvodjenje(4);
		}
	}
	
	
	/**
	 * Metoda koja dohvaća sljedeći operand potreban
	 * za izvođenje neke operacije.
	 * 
	 * @return Traženi operand
	 */
	public Object getNextArgument() {
		iNode++;
		
		Node node = nodes.get(iNode);
		Primitive<?> primitive = node.getPrimitive();
		Object obj = primitive.execute(this);
		return obj;
	}

	
	@Override
	public Genotype copy() {
		Tree tree = new Tree(state);
		
		for(int i=0;i<nodes.size();i++){
			tree.add(new Node(nodes.get(i)));
		}
		
		tree.genotypeId = genotypeId;
		tree.primitiveSet = new PrimitiveSet(primitiveSet);
		return tree;
	}

	@Override
	public Vector<CrossoverOp> getCrossoverOp() {
		throw new IllegalArgumentException("getCrossoverOp() not implemented yet!");
	}

	@Override
	public Vector<MutationOp> getMutationOp() {
		throw new IllegalArgumentException("getMutationOp() not implemented yet!");
	}

	@Override
	public void initialize() {
		Random random = state.getRandomizer();
		
		if(primitiveSet.getPrimitiveSetSize()==0){	//ako skup primitiva nije inicijaliziran
			inicijalizirajPrimitiveSet();
		}
		
		maxDepth = Integer.valueOf(getParameterValue("maxdepth"));
		minDepth = Integer.valueOf(getParameterValue("mindepth"));
		inicijalizirajADF();
		
		if(maxDepth<minDepth || maxDepth<1){
			state.getLogger().log(1, "Tree genotype: invalid values for max and min tree depth!");
			prekiniIzvodjenje(3);
		}

		nodes.clear();
		if(random.nextBoolean()) fullBuild(primitiveSet);
		else growBuild(primitiveSet);
	}


	/**
	 * Metoda koja provjerava hoće li se ovo stablo koristiti
	 * kao automatski definirana funkcija (ADF). Ako da, onda
	 * postavlja broj argumenata ADF-a i njegov genotype id.
	 */
	private void inicijalizirajADF() {
		String name = getParameterValue("adf");
		if(!name.trim().equals("")){
//			int brojArgumenata = getParameterValue("terminalset").split("\\s+").length;
			
			int brojArgumenata = 0;
			int size = primitiveSet.getTerminalSetSize();
			for(int i=0;i<size;i++){
				if(!(primitiveSet.getTerminalById(i) instanceof ERC<?>)) brojArgumenata++; 
			}
			
			ADF.setAdfNumberOfArguments(name, brojArgumenata);
			ADF.setAdfGenotypeID(name, genotypeId);
		}
	}
	

	/**
	 * Metoda koja inicijalizira skup primitiva.
	 * Poziva se samo jednom.
	 */
	private void inicijalizirajPrimitiveSet() {
		//dodaj primitive koje je definirao korisnik
		for(int i=0;i<userFunctions.size();i++){	
			primitiveSet.addPrimitiveToMap(userFunctions.get(i));
		}
		
		//dodaj funkcije u skup primitiva
		String functionSet = getParameterValue("functionset");
		String[] funkcije = functionSet.split("\\s+");
		for(int i=0;i<funkcije.length;i++){
			try {
				primitiveSet.addFunction(primitiveSet.getFunctionByName(funkcije[i]));
			}
			catch(Exception e) {
				state.getLogger().log(1, e.getMessage());
				prekiniIzvodjenje(5);
			}
		}
		if(primitiveSet.getFunctionSetSize()==0){
			state.getLogger().log(1, "Tree genotype: empty function set!");
			prekiniIzvodjenje(1);
		}
		
		//dodaj terminale u skup primitiva
		String tipPodatka = "Double";
		String terminalSet = getParameterValue("terminalset");
		String[] terminali = terminalSet.split("\\s+");
		for(int i=0;i<terminali.length;i++){
			
			//parsiranje ERC
			if(terminali[i].contains("[") || terminali[i].contains("{")){
				Vector<String> vektor = new Vector<String>();
				boolean useInterval = false;
				
				if(terminali[i].contains("[")) useInterval = true;
				String s = terminali[i].replace("[", "").replace("{", "");
//				if(s.length()>0) vektor.add(s);
				if(s.contains("]") || s.contains("}")){
					terminali[i] = terminali[i].replace("[", "").replace("{", "");
					i--;
				}
				else if(s.length()>0) vektor.add(s);
				
				i++;
				while(!terminali[i].contains("]") && !terminali[i].contains("}")){
					vektor.add(terminali[i].replace("]", "").replace("}", ""));
					i++;
				}
				
				s = terminali[i].replace("]", "").replace("}", "");
				if(s.length()>0) vektor.add(s);
				
				ERC<?> erc = null;
				if(tipPodatka.equalsIgnoreCase("Int")){
					erc = new ERC<Integer>(tipPodatka, vektor, useInterval);
				}
				if(tipPodatka.equalsIgnoreCase("Double")){
					erc = new ERC<Double>(tipPodatka, vektor, useInterval);
				}
				if(tipPodatka.equalsIgnoreCase("Bool")){
					erc = new ERC<Boolean>(tipPodatka, vektor, useInterval);
				}
				if(tipPodatka.equalsIgnoreCase("Char")){
					erc = new ERC<Character>(tipPodatka, vektor, useInterval);
				}
				if(tipPodatka.equalsIgnoreCase("String")){
					erc = new ERC<String>(tipPodatka, vektor, useInterval);
				}
				
				primitiveSet.addTerminal(erc);
				continue;
			}
			
			
			
			//ako je definiran tip terminala
			if(terminali[i].equalsIgnoreCase("Int") || terminali[i].equalsIgnoreCase("Double") ||
					terminali[i].equalsIgnoreCase("Bool") || terminali[i].equalsIgnoreCase("Char") ||
					terminali[i].equalsIgnoreCase("String")){
				tipPodatka = terminali[i];
				continue;
			}
			
			Primitive<?> terminal = null;
			if(tipPodatka.equalsIgnoreCase("Int")){
				terminal = new Terminal<Integer>();
				
				try{
					int broj = Integer.parseInt(terminali[i]);
					terminal.setValue(broj);
					terminal.setName(Terminal.INT_PREFIX + terminali[i]);
				}
				catch(Exception e){
					terminal.setName(terminali[i]);
				}
			}
			else if(tipPodatka.equalsIgnoreCase("Double")){
				terminal = new Terminal<Double>();
				
				try{
					double broj = Double.parseDouble(terminali[i]);
					terminal.setValue(broj);
					terminal.setName(Terminal.DOUBLE_PREFIX + terminali[i]);
				}
				catch(Exception e){
					terminal.setName(terminali[i]);
				}
			}
			else if(tipPodatka.equalsIgnoreCase("Bool")){
				terminal = new Terminal<Boolean>();
				
				try{
					boolean bool = Boolean.parseBoolean(terminali[i]);
					terminal.setValue(bool);
					terminal.setName(Terminal.BOOL_PREFIX + terminali[i]);
				}
				catch(Exception e){
					terminal.setName(terminali[i]);
				}
			}
			else if(tipPodatka.equalsIgnoreCase("Char")){
				terminal = new Terminal<Character>();
				
				if(terminali[i].length()==1){
					char c = terminali[i].charAt(0);
					terminal.setValue(c);
					terminal.setName(Terminal.CHAR_PREFIX + terminali[i]);
				}
				else{
					terminal.setName(terminali[i]);
				}
			}
			else if(tipPodatka.equalsIgnoreCase("String")){
				terminal = new Terminal<String>();
				
				try{
					terminal.setValue(terminali[i]);
					terminal.setName(Terminal.STRING_PREFIX + terminali[i]);
				}
				catch(Exception e){
					terminal.setName(terminali[i]);
				}
			}
			primitiveSet.addTerminal(terminal);
		}

		
		if(primitiveSet.getTerminalSetSize()==0){
			state.getLogger().log(1, "Tree genotype: empty terminal set!");
			prekiniIzvodjenje(2);
		}
	}


	@Override
	public void read(Element element) {
		throw new IllegalArgumentException("Not supported yet!");
	}

	@Override
	public void registerParameters() {
		registerParameter("maxdepth", String.valueOf(maxDepth));
		registerParameter("mindepth", String.valueOf(minDepth));
		registerParameter("functionset", "");
		registerParameter("terminalset", "");
		registerParameter("adf", "");
	}

	@Override
	public void write(Element element) {
		throw new IllegalArgumentException("Not supported yet!");
	}


	/**
	 * Metoda koja vraća početnu dubinu stabla.
	 * 
	 * @return Početna dubina stabla
	 */
	public int getStartDepth() {
		return startDepth;
	}

	/**
	 * Metoda koja postavlja početnu dubinu stabla.
	 * 
	 * @param startDepth Nova početna dubina stabla
	 */
	public void setStartDepth(int startDepth) {
		this.startDepth = startDepth;
	}

	/**
	 * Metoda koja vraća maksimalnu dubinu stabla.
	 * 
	 * @return Maksimalna dubina stabla
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Metoda koja postavlja maksimalnu dubinu stabla.
	 * 
	 * @param maxDepth Nova maksimalna dubina stabla
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Metoda koja vraća minimalnu dubinu stabla.
	 * 
	 * @return Minimalna dubina stabla
	 */
	public int getMinDepth() {
		return minDepth;
	}

	/**
	 * Metoda koja postavlja minimalnu dubinu stabla.
	 * 
	 * @param minDepth Nova minimalna dubina stabla
	 */
	public void setMinDepth(int minDepth) {
		this.minDepth = minDepth;
	}
	
	/**
	 * Metoda koja vraća najveću dubinu neke funkcije u stablu.
	 * 
	 * @return Najveća dubina funkcije u stablu
	 */
	@SuppressWarnings("unchecked")
	public int getMaxFunctionDepth(){
		int max = 0;
		for(int i=0;i<nodes.size();i++){
			Node node = nodes.get(i);
			if(!(node.getPrimitive() instanceof Terminal) && node.getDepth()>max){
				max = node.getDepth();
			}
		}
		return max;
	}
	
	/**
	 * Metoda koja vraća broj čvorova stabla.
	 * 
	 * @return Broj čvorova stabla
	 */
	public int size(){
		return nodes.size();
	}
	
	/**
	 * Metoda koja dohvaća čvor stabla.
	 * 
	 * @param index Indeks čvora koji se dohvaća
	 * @return Traženi čvor
	 */
	public Node get(int index){
		return nodes.get(index);
	}
	
	/**
	 * Metoda koja postavlja čvor stabla.
	 * 
	 * @param index Indeks čvora koji se postavlja
	 * @param node Novi čvor
	 */
	public void set(int index, Node node){
		nodes.set(index, node);
	}
	
	/**
	 * Metoda koja dohvaća skup primitiva stabla.
	 * 
	 * @return Skup primitiva stabla
	 */
	public PrimitiveSet getPrimitiveSet() {
		return primitiveSet;
	}
	
	/**
	 * Metoda koja prekida izvođenje ECF-a u slučaju greške.
	 * 
	 * @param kod Kod prekida
	 */
	private void prekiniIzvodjenje(int kod) {
		state.getLogger().saveTo("error.txt", false);
		System.exit(kod);
	}
	
	@Override
	public String toString() {
		return ispisiCvor(0);
	}
	
	/**
	 * Rekurzivna metoda koja ispisuje stablo u obliku stringa.
	 * 
	 * @param pozicija Pozicija trenutno odabranog čvora
	 * @return Ispisi trenutnog čvora
	 */
	@SuppressWarnings("unchecked")
	private String ispisiCvor(int pozicija){
		Node node = nodes.get(pozicija);
		if(node.getPrimitive() instanceof Terminal){
			return node.toString();
		}
		else{
			if(node.getPrimitive().getName().equals("+") || node.getPrimitive().getName().equals("-") ||
					node.getPrimitive().getName().equals("*") || node.getPrimitive().getName().equals("/")){
				return "(" + ispisiCvor(pozicija+1) + node.toString() + 
					ispisiCvor(pozicija+nodes.get(pozicija+1).size()+1) + ")";
			}
			
			String s = node.toString() + "(";
			
			int preskoci = 1;
			for(int i=0;i<node.getPrimitive().getNArguments();i++){
				s += ispisiCvor(pozicija + preskoci) + ",";
//				preskoci += nodes.get(pozicija+i+1).size();
				preskoci += nodes.get(pozicija + preskoci).size();
			}
			s = s.substring(0, s.length()-1) + ")";
			return s;
		}
	}
}
