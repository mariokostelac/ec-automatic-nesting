package hr.fer.zemris.projekt2012.polygon;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Razred služi za generiranje nasumičnih poligona. Stvaranje se pokreće pozivanjem
 * statične metode. Također postoje metode za postavljenje gornjih i donjih granica
 * generiranja brojeva (veličina stranica pravokutnika). Početni raspon je između 20 i 200.
 *
 */
public class PolygonGenerator {
	private static Integer lower = 20;
	private static Integer upper = 200;
	
	/**
	 * Postavlja donju granicu za raspon slučajnih brojeva.
	 */
	public static void setLower(Integer lower) {
		PolygonGenerator.lower = lower;
	}

	/**
	 * Postavlja gornju granicu za raspon slučajnih brojeva.
	 */
	public static void setUpper(Integer upper) {
		PolygonGenerator.upper = upper;
	}
	
	/**
	 * Generira jedan nasumični poligon sa zadanim brojem stranica.
	 * @param sideNumber Broj stranica poligona
	 * @return Nasumični poligon.
	 */
	public static Polygon generate(int sideNumber) {
		Polygon a = new Polygon();
		Random b = new Random();
		int duljinaStr = b.nextInt(upper - lower) + lower;
//		System.out.println(duljinaStr);
		a.addPoint(0, 0);
		a.addPoint(duljinaStr, 0);
		/* stvaranje pravilnog poligona */
		double centralAngle = 2 * Math.PI / sideNumber;
		double angle = 0;
		for(int i = 2; i < sideNumber; i++) {
			angle += centralAngle;
			Point x = new Point(a.xpoints[i-1] + (int)(duljinaStr * Math.cos(angle)),
					a.ypoints[i-1] + (int)(duljinaStr * Math.sin(angle)));
//			System.out.println("(" + x.x + ", " + x.y + ")");
			a.addPoint(x.x, x.y);
		}
		Polygon c = new Polygon();
		int duljinaCentar = (int)(duljinaStr / (2 * Math.sin(centralAngle/2)));
//		System.out.println(duljinaCentar);
		angle = (Math.PI - centralAngle) / 2;
//		System.out.println(angle);
		for(int i = 0; i < sideNumber; i++) {
			/* vektor od tocke prema centru*/
			Point x = new Point((int)(duljinaCentar * Math.cos(angle)), (int)(duljinaCentar * Math.sin(angle)));
			double pomak = (double)(b.nextInt(2 * duljinaCentar)) / duljinaCentar - 1;
			x.x = (int)(x.x * pomak);
			x.y = (int)(x.y * pomak);
			/* translacija točke po vektoru okomitom na vektor od točke prema centru*/
			Point transVector = new Point(x.y, -x.x); 
			double transDist = transVector.distance(0, 0);
			double duljinaTrans = Math.abs((1 - pomak) * duljinaCentar * Math.tan(centralAngle/2));
			int ratio = (int) (((duljinaTrans * 2 > 1) ? (b.nextInt((int)(duljinaTrans * 2))) : 0) - duljinaTrans);
			
			transVector.x = (int)(transVector.x / transDist * ratio);
			transVector.y = (int)(transVector.y / transDist * ratio);
//			System.out.println("(" + transVector.x + ", " + transVector.y + ")");
			c.addPoint(a.xpoints[i] + x.x + transVector.x, a.ypoints[i] + x.y + transVector.y);
			angle += centralAngle;
		}
		return c;
	}
	
	/**
	 * Generira num nasumičnih poligona sa brojem stranica od 3 do 20.
	 * @param num Broj željenih nasumičnih poligona.
	 * @return Lista nasumičnih poligona.
	 */
	public static List<Polygon> generateMany(int num) {
		List<Polygon> lista = new ArrayList<>(num);
		Random b = new Random();
		for(int i = 0; i < num; i++) {
			lista.add(generate(b.nextInt(20 - 3 + 1) + 3));
		}
		return lista;
	}
	
	/**
	 * Generira jedan poligon s nasumičnim brojem stranica.
	 * @return Nasumični poligon.
	 */
	public static Polygon generate() {
		Random b = new Random();
		return generate(b.nextInt(20 - 3 + 1) + 3);
	}
	
	public static void main(String args[]) {
		Polygon b = generate(4);
		for(int i = 0; i < b.npoints; i++) {
			System.out.println("(" + b.xpoints[i] + ", " + b.ypoints[i] + ")");
		}
	}
}
