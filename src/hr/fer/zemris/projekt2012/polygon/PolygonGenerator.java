package hr.fer.zemris.projekt2012.polygon;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	private static Integer maxBrStr = 10;
	
	/**
	 * Metoda postavlja maksimalni broj stranica u nasumično generiranom poligonu.
	 * Uobičajena vrijednost je 10.
	 */
	public static void setMaxBrStr(Integer maxBrStr) {
		PolygonGenerator.maxBrStr = maxBrStr;
	}

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
	public static PolygonRandom generate(int sideNumber) {
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
		double duljinaC = duljinaStr / (2 * Math.sin(centralAngle/2));
		int duljinaCentar = (int)(duljinaC);
		Point center = new Point((int)(duljinaC * Math.cos((Math.PI - centralAngle)/2)), 
				(int)(duljinaC * Math.sin((Math.PI - centralAngle)/2)));
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
		PolygonRandom d = new PolygonRandom(c, center);
		Rectangle w = d.getBounds();
		d.translate(-w.x, -w.y);
		return d;
	}
	
	/**
	 * Generira num nasumičnih poligona sa brojem stranica od 3 do maxBrStr.
	 * @param num Broj željenih nasumičnih poligona.
	 * @return Lista nasumičnih poligona.
	 */
	public static List<PolygonRandom> generateMany(int num) {
		List<PolygonRandom> lista = new ArrayList<>(num);
		Random b = new Random();
		for(int i = 0; i < num; i++) {
			lista.add(generate(b.nextInt(maxBrStr - 3 + 1) + 3));
		}
		return lista;
	}
	
	/**
	 * Generira jedan poligon s nasumičnim brojem stranica.
	 * @return Nasumični poligon.
	 */
	public static PolygonRandom generate() {
		Random b = new Random();
		return generate(b.nextInt(maxBrStr - 3 + 1) + 3);
	}
	
	/*
	 * Metoda koja prepisuje random poligone u .txt datoteku
	 * @param number Broj generiranih poligona
	 * @param location lokacija datoteke
	 */
	public static void toFile(String location, int number){
		
		PolygonGenerator.setUpper(100);
		
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(location));
			List<PolygonRandom> list = generateMany(number);
			for(Polygon poly: list){
				for(int i=0; i < poly.npoints; i++){
					out.write("(" + poly.xpoints[i] + ", " + poly.ypoints[i] + ")");
					if(i == poly.npoints-1)
						out.write("#\n");
					else
						out.write(";");
				}
			}
			out.close();
		} catch(IOException e){
			System.out.println("Greska u stvaranju datoteke!");
		}
	}
	
	
	public static void main(String args[]) {
		
		toFile("polysets/izlaz.txt", 20);
		
	}
}
