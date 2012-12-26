package hr.fer.zemris.projekt2012.polygon;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;

/**
 * Razred PolygonRandom služi kako bi se baznom razredu Polygon pridružile
 * neke funkcionalnosti koje će nam trebati u ovom projektu.
 * @author Edi Smoljan
 *
 */
public class PolygonRandom extends Polygon {
	
	private static final long serialVersionUID = 1L;
	
	private Point center;
	private SimplePolygon2D me;

	public PolygonRandom(Polygon a) {
		super(a.xpoints, a.ypoints, a.npoints); 
		me = createFromPolygon(a);
		racunajSrediste();
	}
	
	public PolygonRandom(Polygon a, Point center) {
		super(a.xpoints, a.ypoints, a.npoints);
		me = createFromPolygon(a);
		this.center = center;
	}

	public PolygonRandom(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
		me = createFromPoints(xpoints, ypoints);
		racunajSrediste();
	}
	
	private SimplePolygon2D createFromPoints(int[] xpoints, int[] ypoints) {
		
		double[] x = new double[xpoints.length];
		double[] y = new double[xpoints.length];
		for (int i = 0; i < xpoints.length; ++i) {
			x[i] = xpoints[i];
			y[i] = ypoints[i];
		}
		return new SimplePolygon2D(x, y);
		
	}
	
	private SimplePolygon2D createFromPolygon(Polygon p) {
		return createFromPoints(p.xpoints, p.ypoints);
	}
	
	/**
	 * Pomoćna funkcija koja računa referentnu točku rotacije ako se u konstruktoru
	 * ne navede ta točka. 
	 */
	private void racunajSrediste() {
		long sumx = (long) 0;
		long sumy = (long) 0;
		for(int i = 0; i < npoints; i++) {
			sumx += xpoints[i];
			sumy += ypoints[i];
		}
		center = new Point((int)(sumx/npoints), (int)(sumy/npoints));
	}
	
	/**
	 * Overrideana funkcija translate kako bi se osiguralo pomicanje referentne točke za rotaciju.
	 */
	@Override
	public void translate(int deltaX, int deltaY) {
		super.translate(deltaX, deltaY);
		center = new Point(center.x + deltaX, center.y + deltaY);
	}
	
	/**
	 * Metoda služi za rotiranje poligona za navedeni broj radijana. Rotira se pomoću vektora.
	 */
	public void rotate(double radians) {
		for(int i = 0; i < npoints; i++) {
			Point ac = new Point(xpoints[i] - center.x, ypoints[i] - center.y);
			double angle = radians + Math.atan2(ac.y, ac.x);
			double length = ac.distance(0, 0);
			xpoints[i] = (int)(center.x + length * Math.cos(angle));
			ypoints[i] = (int)(center.y + length * Math.sin(angle));
		}
	}
	
	/**
	 * Metoda rotira poligon za navedeni broj stupnjeva.
	 * @param degrees
	 */
	public void rotateDeg(double degrees) {
		rotate(Math.PI * degrees / 180);
	}
	
	/**
	 * Metoda pomiče poligon koji je postavljen proizvoljno u ravnini na poziciju najbliže
	 * moguće ishodištu tako da čitav poligon bude u 1. kvadrantu koordinatnog sustava.
	 * Metoda ne rotira poligon.
	 */
	public void pomakniNaPocetno() {
		Rectangle b = new Polygon(xpoints, ypoints, npoints).getBounds();
		translate(-b.x, -b.y);
	}
	
	/**
	 * Otkriva siječe li se poligon s danim poligonom p
	 * @param p
	 * @return true ako se sijeku, u suprotnom false
	 */
	public boolean intersectsWith(PolygonRandom p) {
		
		return Polygons2D.intersection(me, p.me).edgeNumber() > 0;
		
	}
	
}
