package hr.fer.zemris.projekt2012.polygon;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Razred služi za generiranje nasumičnih pravokutnika. Stvaranje se pokreće pozivanjem
 * statične metode. Također postoje metode za postavljenje gornjih i donjih granica
 * generiranja brojeva (veličina stranica pravokutnika). Početni raspon je između 25 i 200.
 *
 */
public class RectangleGenerator {
	private static Integer lower = 25;
	private static Integer upper = 200;

	/**
	 * Postavlja donju granicu za raspon slučajnih brojeva.
	 */
	public static void setLower(Integer lower) {
		RectangleGenerator.lower = lower;
	}

	/**
	 * Postavlja gornju granicu za raspon slučajnih brojeva.
	 */
	public static void setUpper(Integer upper) {
		RectangleGenerator.upper = upper;
	}
	
	/**
	 * Stvara jedan nasumičan pravokutnik.
	 * @return nasumičan pravokutnik.
	 */
	public static Rectangle generate() {
		Random a = new Random();
		return new Rectangle(a.nextInt(upper - lower) + lower, a.nextInt(upper - lower) + lower);
	}
	
	/**
	 * Stvara listu num nasumičnih pravokutnika. 
	 * @param num Broj željenih nasumičnih pravokutnika.
	 * @return Lista nasumičnih pravokutnika.
	 */
	public static List<Rectangle> generateMany(int num) {
		List<Rectangle> lista = new ArrayList<>(num);
		for(int i = 0; i < num; i++) {
			lista.add(generate());
		}
		return lista;
	}

}
