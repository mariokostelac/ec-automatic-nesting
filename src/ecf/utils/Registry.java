package ecf.utils;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Razred koji barata konfiguracijskom XML
 * datotekom. Sadrži sve parametre potrebne
 * za rad algoritma.
 * 
 * @author Marko Pielić/Rene Huić
 */
public class Registry {
	
	/**Mapa koja sadrži registrirane parametre*/
	private Map<String, Param> mapa = new HashMap<String, Param>();
	

	/**
	 * Metoda koja učitava podatke iz Registry čvora
	 * konfiguracijske datoteke.
	 * 
	 * @param registry XML čvor sa podacima za registry
	 */
	public void readEntries(Element registry) {
		NodeList djeca = registry.getChildNodes();
		
		for(int i=0;i<djeca.getLength();i++){
			Node cvor = djeca.item(i);
			if(cvor.getNodeType()!=Node.ELEMENT_NODE) continue;
			
			Element element = (Element)cvor;
			NodeList vrste = element.getChildNodes();
			String ime = element.getNodeName();
			
			if(element.hasAttribute("key")){	//ako element nema podtipove
				String kljuc = element.getAttribute("key");
				if(isRegistered(kljuc)){		//spremi samo registrirane parametre
//                                    System.out.println("Dodajem " + kljuc + " " + element.getTextContent());
					modifyEntry(kljuc, element.getTextContent());
				}
			}	
			else{								//ako element ima podtipove
				for(int j=0;j<vrste.getLength();j++){
					Node vrsta = vrste.item(j);
					if(vrsta.getNodeType()!=Node.ELEMENT_NODE) continue;

					Element dijete = (Element)vrsta;
					String kljuc = ime + "." + dijete.getAttribute("key");
					if(isRegistered(kljuc)){		//spremi samo registrirane parametre
						modifyEntry(kljuc, dijete.getTextContent());
					}
				}
			}
		}
	}
	
	
	/**
	 * Metoda koja registrira novi ključ.
	 * Ako ključ već postoji javlja grešku.
	 * 
	 * @param kljuc Ključ koji se registrira
	 * @param podatak Vrijednost ključa
	 */
	public void registerEntry(String kljuc, String podatak){
		if(isRegistered(kljuc)) throw new IllegalArgumentException("Ključ " + kljuc + " već postoji!");
		mapa.put(kljuc, new Param(podatak));
	}
	
	
	/**
	 * Mapa koja postavlja novu vrijednost zadanog ključa.
	 * Ako ključ ne postoji javlja grešku.
	 * 
	 * @param kljuc Ključ čija se nova vrijednost postavlja
	 * @param novaVrijednost Nova vrijednost ključa
	 */
	public void modifyEntry(String kljuc, String novaVrijednost){
		if(!isRegistered(kljuc)) throw new IllegalArgumentException("Ključ " + kljuc + " ne postoji!");
		Param parametar = mapa.get(kljuc);
		parametar.setValue(novaVrijednost);
	}
	
	
	/**
	 * Metoda koja dohvaća vrijednost traženog ključa.
	 * 
	 * @param kljuc Ključ tražene vrijednosti
	 * @return Vrijednost koja se dohvaća
	 */
	public String getEntry(String kljuc){
		return mapa.get(kljuc).getValue();
	}
	
	
	/**
	 * Metoda koja provjerava je li ključ registriran.
	 * 
	 * @param kljuc Ključ parametra koji se provjerava
	 * @return True ako je ključ registriran, inače false
	 */
	public boolean isRegistered(String kljuc){
		return mapa.containsKey(kljuc);
	}
	
	
	/**
	 * Metoda koja provjerava je li parametar modificiran.
	 * 
	 * @param kljuc Ključ parametra koji se provjerava
	 * @return True ako je parametar mijenjan, inače false
	 */
	public boolean isModified(String kljuc){
		
		return mapa.get(kljuc).isModified();
	}
	
	
	@Override
	public String toString() {
		return mapa.toString();
	}
	
	
	/**
	 * Razred koji predstavlja jedan parametar.
	 * Svaki parametar ima svoju vrijednost i
	 * zastavicu koja govori je li mijenjan.
	 * 
	 * @author Marko Pielić
	 */
	public static class Param{
		private String value;
		private boolean modified;
		
		
		/**
		 * Konstruktor koji kreira novi parametar.
		 * Novi parametar nije modificiran.
		 * 
		 * @param value Vrijednost novog parametra
		 */
		public Param(String value){
			this.value = value;
			this.modified = false;
		}
		
		/**
		 * Metoda koja vraća vrijednost parametra.
		 * 
		 * @return Vrijednost parametra
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Metoda koja postavlja vrijednost parametra.
		 * Postavljanje vrijednosti ujedno znači i da
		 * je parametar promijenjen (modificiran).
		 * 
		 * @param value Nova vrijednost parametra
		 */
		public void setValue(String value) {
			this.value = value;
			this.modified = true;
		}
		
		/**
		 * Metoda koja provjerava je li parametar modificiran.
		 * 
		 * @return True ako je parametar mijenjan, inače false
		 */
		public boolean isModified() {
			return modified;
		}
		
		
		@Override
		public String toString() {
			return "{value:" + value + ", modified:" + modified + "}";
		}
	}
}
