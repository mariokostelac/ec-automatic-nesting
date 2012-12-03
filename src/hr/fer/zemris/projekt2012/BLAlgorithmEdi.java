package hr.fer.zemris.projekt2012;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Algoritam za smještavanje pravokutnika u prostor. Trebalo bi ga prilagoditi
 * za ecf i debuggirati (već vidim nekoliko problematičnih situacija gdje alg. pada).
 * Pretpostavka: okružujući (hehe) pravokutnici poligona imaju BL(ili TL) točku u 0,0.
 *
 */
public class BLAlgorithmEdi {
	private List<Polygon> poligoni;
	private Integer ukupnaDuljina = 0;
	private Vector<Integer> indeksi; /* misli se na redoslijed dolaženja poligona, odnosno genotip*/
	private List<Rectangle> boundingRect;
	private Integer visinaSpremnika = 0;
	
	private static enum smjer{LIJEVO, DOLJE}
	
	public BLAlgorithmEdi(List<Polygon> lista, Integer ukupnaSirina,
			Vector<Integer> indexi, Integer visina) {
		super();
		this.poligoni = lista;
		this.ukupnaDuljina = ukupnaSirina;
		this.indeksi = indexi;
		this.boundingRect = new ArrayList<>(poligoni.size());
		this.visinaSpremnika = visina;
		for (Polygon poli : poligoni) {
			boundingRect.add(poli.getBounds());
		}
	}
	
	
	public List<Polygon> getPoligoni() {
		return poligoni;
	}


	public void setPoligoni(List<Polygon> lista) {
		this.poligoni = lista;
	}


	public Integer getUkupnaDuljina() {
		return ukupnaDuljina;
	}


	public void setIndeksi(Vector<Integer> indeksi) {
		this.indeksi = indeksi;
	}

	/**
	 * Implementacija BL algoritma koja smjesta poligone(odnosno njihove pravokutnike)
	 * s lijeva na desno, pretpostavlja da je spremnik beskonacno (jeli) dug, ali ogranicene
	 * visine.
	 * @param visinaSpremnika Visina spremnika u kojeg se stavljaju poligoni.
	 */
	public void racunajDuljinu() {
		SortedSet<ModPoint> BLKandidati = new TreeSet<>();
		BLKandidati.add(new ModPoint(0, 0));
		Prozor.racunajDimenzije(boundingRect, visinaSpremnika);
		List<Prozor> prozori = new ModArrayList<>();
		for(int i = 0, n = boundingRect.size(); i < n; i++) {
			Rectangle pom = boundingRect.get(i);
			for(ModPoint kand : BLKandidati) {
				if(postavi(pom, kand, prozori) == true) {
					poligoni.get(i).translate(kand.x, kand.y);
					BLKandidati.remove(kand);
					ModPoint BLn1 = new ModPoint(pom.x, pom.y + pom.height);
					ModPoint BLn2 = new ModPoint(pom.x + pom.width, pom.y);
					pomakni(BLn1, smjer.LIJEVO, prozori);
					pomakni(BLn2, smjer.DOLJE, prozori);
					BLKandidati.add(BLn1);
					BLKandidati.add(BLn2);
					dodajUProzore((ModArrayList<Prozor>)prozori, i);
					break;
				}
			}
		}
	}

	private Boolean postavi(Rectangle rectangle, ModPoint kand, List<Prozor> prozori) {
		rectangle.setLocation(kand);
		if(rectangle.y + rectangle.height > visinaSpremnika) {
			return false;
		}
		Set<Rectangle> moguceKolizije = moguceKolizijeZa(rectangle, prozori);
		for(Rectangle r : moguceKolizije) {
			if(rectangle.intersects(r))
				return false;
		}
		if(ukupnaDuljina < rectangle.x + rectangle.width) {
			ukupnaDuljina = rectangle.x + rectangle.width;
		}
		return true;
	}

	private Set<Rectangle> moguceKolizijeZa(Rectangle rect, List<Prozor> prozori) {
		Set<Rectangle> moguce = new HashSet<>();
		int brStupcal = rect.x / Prozor.width;
		int brStupcar = (rect.x + rect.width - 1) / Prozor.width;
		int brRetkaT = rect.y / Prozor.height;
		int brRetkaB = (rect.y + rect.height - 1) / Prozor.height;
		for (int i = brRetkaT; i <= brRetkaB; i++) {
			for (int j = brStupcal; j <= brStupcar; j++) {
				if(prozori.size() > i * Prozor.brojPrUStup + j &&
						prozori.get(i * Prozor.brojPrUStup + j) != null) {
					for (Integer k : prozori.get(i * Prozor.brojPrUStup + j).indeksiPol) {
						moguce.add(boundingRect.get(k));
					}
				}
			}
		}	
		return moguce;
	}
	
	private void pomakni(ModPoint tocka, smjer kuda, List<Prozor> prozori) {
		int brStupcaPr = tocka.x / Prozor.width;
		int brRetkaPr = tocka.y / Prozor.height;
		Boolean nasao = false;
		if(kuda == smjer.LIJEVO) {
			for(int i = brStupcaPr; i >= 0 && !nasao; i--) {
				if(prozori.get(brRetkaPr * Prozor.brojPrUStup + i) == null || 
						prozori.get(brRetkaPr * Prozor.brojPrUStup + i).indeksiPol == null) {
					continue;
				}
				List<Integer> reverse = new LinkedList<>(prozori.get(brRetkaPr * Prozor.brojPrUStup + i).indeksiPol);
				Collections.reverse(reverse); /* razmotriti ovo*/
				for(Integer r : reverse) {
					Rectangle rect = boundingRect.get(r);
					if(rect.x + rect.width - 1 < tocka.x && rect.y <= tocka.y
							&& rect.y + rect.height > tocka.y) {
						tocka.x = rect.x + rect.width;
						nasao = true;
						break;
					}
				}
			}
		} else if (kuda == smjer.DOLJE) {
			for(int i = brRetkaPr; i >= 0 && !nasao; i--) {
				if(prozori.get(i * Prozor.brojPrUStup + brStupcaPr) == null || 
						prozori.get(i * Prozor.brojPrUStup + brStupcaPr).indeksiPol == null) {
					continue;
				}
				List<Integer> reverse = new LinkedList<>(prozori.get(i * Prozor.brojPrUStup + brStupcaPr).indeksiPol);
				Collections.reverse(reverse); /* razmotriti ovo, mislim da ću morati sve*/
				for(Integer r : reverse) {
					Rectangle rect = boundingRect.get(r);
					if(rect.y + rect.height - 1 < tocka.y && rect.x <= tocka.x
							&& rect.x + rect.width > tocka.x) {
						tocka.y = rect.y + rect.height;
						nasao = true;
						break;
					}
				}
			}
		}
	}

	
	private void dodajUProzore(ModArrayList<Prozor> prozori, Integer indexPoligona) {
		Rectangle pom = boundingRect.get(indexPoligona);
		int brStupcal = pom.x / Prozor.width;
		int brStupcar = (pom.x + pom.width - 1) / Prozor.width;
		int brRetkaT = pom.y / Prozor.height;
		int brRetkaB = (pom.y + pom.height - 1) / Prozor.height;
		for (int i = brRetkaT; i <= brRetkaB; i++) {
			for (int j = brStupcal; j <= brStupcar; j++) {
				if(prozori.size() <= i * Prozor.brojPrUStup + j || 
						prozori.get(i * Prozor.brojPrUStup + j) == null) {
					prozori.set(i * Prozor.brojPrUStup + j, new Prozor());
				}
				prozori.get(i * Prozor.brojPrUStup + j).dodajPoligon(indexPoligona);
			}
		}	
	}
	
	
	/**
	 * Razred prozor opisuje dio prostora u kojem se nalaze pravokutnici.
	 * Razred služi smanjivanju broja pravokutnika koje je potrebno pretražiti za neke
	 * operacije u algoritmu.
	 *
	 */
	private static class Prozor {
		public static Integer width = 0;
		public static Integer height = 0;
		public static Integer brojPrUStup = 0; 
		private Set<Integer> indeksiPol = new LinkedHashSet<>(); /* za sada*/
		
		public static void racunajDimenzije(List<Rectangle> prav, Integer visina) {
			Double avgVisina = 0.;
			Double avgDuljina = 0.;
			Integer brojPravok = prav.size();
			for (Rectangle pravok : prav) {
				avgVisina += ((double)pravok.height) / brojPravok;
				avgDuljina += ((double)pravok.width) / brojPravok;
			}
			avgDuljina *= 1.3;
			avgVisina *= 1.3;
			
			int brojProzoraUStupcu = (int)(visina / avgVisina);
			brojPrUStup = (int)Math.ceil(visina / avgVisina);
			
			width = visina / brojProzoraUStupcu;
		}
		
		public void dodajPoligon(Integer indeksPol) {
			indeksiPol.add(indeksPol);
		}
		
	}
	
	/**
	 * Razred koji omogućava jednu željenu funkcionalnost ArrayListe-
	 * dodavanje elemenata na proizvoljne pozicije bez da su pozicije
	 * unaprijed zauzete (definirane). Sve nedefinirane pozicije prije
	 * željene puni null vrijednostima.
	 *
	 * @param <T> 
	 */
	private static class ModArrayList<T> extends ArrayList<T> {
		
		public T set(int index, T element) {
			if (this.size() < index) {
				for(int i = index - this.size(); i >= 0; i--)
					this.add(null);
			}
			return super.set(index, element);
		}
	}
	
	private static class ModPoint extends Point implements Comparable<ModPoint>{
		
		
		public ModPoint() {
			super();
		}

		public ModPoint(int x, int y) {
			super(x, y);
		}

		public ModPoint(Point p) {
			super(p);
		}

		@Override
		public int compareTo(ModPoint arg0) {
			if(x + y < arg0.x + arg0.y)
				return -1;
			else if(x + y > arg0.x + arg0.y)
				return 1;
			else if(x == arg0.x && y == arg0.y)
				return 0;
			return 1;
		}
	}
}
