package hr.fer.zemris.projekt2012;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;

import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import ecf.genotype.permutation.Permutation;
import ecf.genotype.permutation.PermutationCrxPMX;
import ecf.genotype.permutation.PermutationMutInv;

public class BottomLeftAlgorithmHookeJeeves extends Algorithm {

	private Polygon2D[] polygons;
	private int width = 0;
	
	private State state = null;
	private Genotype genotype = null;
	
	public BottomLeftAlgorithmHookeJeeves(List<Polygon> polygons, int width) {
		// kopiraj poligone (kako bi u ostatku programa ostali u izvornom obliku)
		this.polygons = new Polygon2D[polygons.size()];
		int i = 0;
		for (Polygon p : polygons) {
			double[] xcoords = new double[p.npoints];
			double[] ycoords = new double[p.npoints];
			for (int j = 0; j < p.npoints; ++j) {
				xcoords[j] = p.xpoints[j];
				ycoords[j] = p.ypoints[j];
			}
			this.polygons[i++] = new SimplePolygon2D(xcoords, ycoords);
		}
			
		this.width = width;
	}
	
	/**
	 * Pokreće računanje
	 */
	public void run() {
		String[] args = new String[]{ "configs/config1.xml" };
		state = new State(this);
        
		setGenotype();
		setMutation();
		setCrossover();
		
		state.initialize(args);
		state.run();
    }
	
	public List<Polygon> getBestSolution() {
		
		List<Polygon> translatedPolygons = new ArrayList<>();
		
		Deme currPopulation = state.getPopulation().getLocalDeme();
		Individual bestSolution = null;
		for (Individual currSolution : currPopulation) {
			if (bestSolution == null || currSolution.getFitness().getValue() < bestSolution.getFitness().getValue())
				bestSolution = currSolution;
		}
		
		Set<Event> openEvents = getOpenEvents((Permutation) bestSolution.get(0));
		int i = 0;
		for (Event currEvent : openEvents) {
			i++;
			if (i == 1) continue;
			Polygon currPolygon = new Polygon();
			for (Point2D p : currEvent.poly.vertices())
				currPolygon.addPoint((int)p.x(), (int)p.y());
			translatedPolygons.add(currPolygon);
		}
		
		return translatedPolygons;
		
	}
	
	private Genotype getGenotype() {
		if (genotype == null)
			genotype = new Permutation(state, polygons.length);
		return genotype;	
	}
	
	private void setGenotype() {
		state.addGenotype(getGenotype());
	}
	
	private void setMutation() {
		MutationOp mutacija = new PermutationMutInv(state);
		mutacija.setMyGenotype(genotype.copy());
		mutacija.registerParameters();
		state.getMutation().addOperator(mutacija, genotype.getGenotypeId());
	}
	
	private void setCrossover() {
		CrossoverOp krizanje = new PermutationCrxPMX(state);
        krizanje.setMyGenotype(genotype.copy());
        krizanje.registerParameters();
        state.getCrossover().addOperator(krizanje, genotype.getGenotypeId());
	}
	
	/**
	 * Traži bottom left točku na koju može smjestiti novi poligon tako da najviše pola
	 * okružujućeg pravokutnika viri van. Nakon toga se primjenjuje Hooke-Jeeves algoritam
	 * za približavanje pravokutnika. U slučaju da nakon HJ-a pravokutnik i dalje viri van,
	 * traženje se nastavlja.
	 * @param startEvents
	 * @param polygonIndex
	 */
	private void placePolygon(Set<Event> startEvents, int polygonIndex) {
		
		Polygon2D newPoly = polygons[polygonIndex];
		System.out.println(polygonIndex);
		int polyWidth = (int) newPoly.boundingBox().getWidth();
		int polyHeight = (int) newPoly.boundingBox().getHeight();
		Set<Event> allEvents = new TreeSet<>(startEvents);
		// popuni evente s završnim eventima
		for (Event currEvent : startEvents) {
			if (currEvent.type == Event.eventType.CLOSE) continue;
			/* dodaje novi event na razinu (trenutna razina + visina trenutnog + visina kojeg dodajemo) jer nailaskom na taj event
			 * znamo da je prošli završio i da imamo dovoljno visine za taj kojeg želimo dodati
			 */
			allEvents.add(new Event(currEvent.x, currEvent.y+(int)currEvent.poly.boundingBox().getHeight()+polyHeight, Event.eventType.CLOSE, currEvent.poly));
		}
		
		// polje koje će za svaki pixel pamtiti počinje/završava li neki pravokutnik na tom pixelu
		int phantomWidth = this.width;
		int bins[] = new int[phantomWidth+1];

		boolean added = false;
		for (Event currEvent : allEvents) {
			Box2D boundary = currEvent.poly.boundingBox();
			if (currEvent.type == Event.eventType.OPEN) {
				// otvori pravokutnik
				bins[currEvent.x] += 1;
				bins[currEvent.x+(int)boundary.getWidth()] -= 1;
			} else {
				// zatvori pravokutnik
				bins[currEvent.x] -= 1;
				bins[currEvent.x+(int)boundary.getWidth()] += 1;
				// provjeri postoji li dovoljno mjesta za smjestiti novi pravokutnik
				int zerosInRow = 0;
				int currSum = 0;
				for (int i = 0; i < phantomWidth; ++i) {
					currSum += bins[i];
					if (currSum == 0)
						zerosInRow++;
					else
						zerosInRow = 0;
					// TODO (playWidth)!
					if (zerosInRow == polyWidth) {
						// nasli smo najbolje bottom-left mjesto, smjesti ga i izadji van
						int x = i-zerosInRow+1;
						int y = currEvent.y-polyHeight;
						Event startEvent = getStartEventHJ(startEvents, polygonIndex, x, y);
						if (startEvent != null) {
							startEvents.add(startEvent);
							added = true;
							break;
						}
					}
				}
				if (added) break;
			}
			
		}
		
	}

	@Override
	public void evaluate(Fitness fitness) {
		
        Permutation solution = (Permutation) fitness.getIndividual().get(0);

        int topY = 0;
        for (Event currEvent : getOpenEvents(solution))
        	topY = Math.max(topY, currEvent.y+(int)currEvent.poly.boundingBox().getHeight());
                
		fitness.setValue(topY);
		
	}
	
	private Set<Event> getOpenEvents(Permutation solution) {
		
		Set<Event> staticEvents = new TreeSet<Event>();
        // osiguraj da stvar funkcionira od početka
        // dodaje se poligon koji se raširi od početka do kraja i zatvoren je točno na nuli (tako da se pozove stavljanje novog poligona)
        staticEvents.add(new Event(0, 0, Event.eventType.OPEN, new SimplePolygon2D(new double[]{0, 400}, new double[]{0, 0})));
        for (int polyIndex : solution.getVariables())
        	placePolygon(staticEvents, polyIndex);
		
        return staticEvents;
        
	}

	@Override
	public Fitness createFitness() {
		return new FitnessMin();
	}

	@Override
	public void registerParameters() {}

	@Override
	public void initialize() {}
	
	/**
	 * Pokušava pomaknuti dani poligon s točke x, y što više prema dolje i lijevo,
	 * a da ne se poligon ne siječe s ostalim poligonima.
	 * Za traženje takve točke se koristi Hooke-Jevesov algoritam
	 * @param polygonIndex polygon index
	 * @param x x koordinata početne točke traženja
	 * @param y y koordinate početne točke traženja
	 * @return Event Open na ciljnoj lokaciji s translatiranim poligonom
	 */
	private Event getStartEventHJ(Set<Event> startEvents, int polygonIndex, int x, int y) {
		
		/*PolygonRandom p = polygons[polygonIndex];
		int xP[] = new int[]{x, y};
		int xB[] = new int[]{x, y};
		int xN[] = new int[]{x, y};
		double D[] = new double[] { -p.getBounds().width/2, -p.getBounds().height/2 };
		do {
			xN = explore(startEvents, polygonIndex, xP, D);
			if (povrsina(startEvents, polygonIndex, xN) < povrsina(startEvents, polygonIndex, xB)) {
				xP[0] = 2*xN[0] - xB[0];
				xP[1] = 2*xN[1] - xB[1];
				xB[0] = xN[0];
				xB[1] = xN[1];
			} else {
				D[0] /= 2;
				D[1] /= 2;
				xP[0] = xB[0];
				xP[1] = xB[1];
			}
		} while (Math.abs(D[0]) > 1 || Math.abs(D[1]) > 1);
		
		System.out.println(x +", " + y + "|"+ xB[0] + ", " + xB[1]);
		if (povrsina(startEvents, polygonIndex, xB) == Double.MAX_VALUE)
			return null;
		
		/**/
		int xB[] = new int[]{x, y};
		int xN[] = new int[]{x, y};
		int d = 100;
		do {
			xN[0] -= d;
			double a = povrsina(startEvents, polygonIndex, xN);
			double b = povrsina(startEvents, polygonIndex, xB); 
			System.out.println(a+"|"+b+";d:"+d+";x:"+xN[0]);
			if (a <= b) {
				xB[0] = xN[0];
			} else {
				xN[0] = xB[0];
				d /= 2;
			}
		} while (d != 0);
		
		return new Event((int) xB[0], (int) xB[1], Event.eventType.OPEN,
			getTranslated(polygons[polygonIndex], xB[0], xB[1])
		);
	}
	
	private int[] explore(Set<Event> events, int polygonIndex, int[] xP, double[] D) {
		
		int[] x = new int[]{xP[0], xP[1]};
		double P, N;
		for (int i = 0; i < x.length; ++i) {
			P = povrsina(events, polygonIndex, x);
			x[i] += D[i];
			N = povrsina(events, polygonIndex, x);
			if (N > P) {
				// ako ne valja, vraćamo
				x[i] = x[i] - (int)(2*D[i]);
				N = povrsina(events, polygonIndex, x);
				// ako opet ne valja, vraćamo na početnu točku
				if (N > P)
					x[i] += D[i];
			}
		}
		
		return x;
		
	}
	
	/**
	 * Vraća omjer ukupne površine (širina * visina) i površine "okružujućih pravokutnika" svih poligona
	 * (manje je bolje). U slučaju da neki poligon ide van okvira ili u slučaju sudara,
	 * vraća Double.MAX_VALUE
	 * @param events
	 * @param polyIndex
	 * @param x
	 * @return
	 */
	private double povrsina(Set<Event> events, int polyIndex, int[] x) {

		double height = 0;
		double sum = 0;
		for (Event currEvent : events) {
			Box2D bounding = currEvent.poly.boundingBox();
			height = Math.max(height, currEvent.y+bounding.getHeight());
			sum += bounding.getHeight()*bounding.getWidth();
		}
		Box2D bounding = polygons[polyIndex].boundingBox();
		height = Math.max(height, x[1] + bounding.getHeight());
		sum += bounding.getHeight()*bounding.getWidth();
		
		// kažnjavamo sve van granica
		if (x[0] < 0 || (x[0] + bounding.getWidth()) > width)
			return Double.MAX_VALUE;
		if (x[1] < 0)
			return Double.MAX_VALUE;	
		
		Polygon2D newPolygon = getTranslated(polygons[polyIndex], x[0], x[1]);
		for (Event currEvent : events) {
			Box2D r = currEvent.poly.boundingBox();
			if (Polygons2D.intersection(currEvent.poly, newPolygon).edgeNumber() > 0)
				return Double.MAX_VALUE;
			//Box2D intersection = r.intersection(newPolygon.boundingBox());
			//sum += Math.abs(intersection.getHeight()*intersection.getWidth());
		}
	
		return (width*height)/sum;

	}
	
	/**
	 * @param r1 pravokutnik 1
	 * @param r2 pravokutnik 2
	 * @return površina presjeka dvaju pravokutnika
	 */
	private static int rectangleIntersection(Rectangle r1, Rectangle r2) {
	
		int minX = (int) Math.min(r1.getMinX(), r2.getMinX());
		int maxX = (int) Math.max(r1.getMaxX(), r2.getMaxX());
		int minY = (int) Math.min(r1.getMinY(), r2.getMinY());
		int maxY = (int) Math.max(r1.getMaxY(), r2.getMaxY());
		int bigX = maxX - minX;
		int bigY = maxY - minY;
				
		int smallX = r1.width+r2.width - bigX;
		int smallY = r1.height+r2.height - bigY;
		
		return smallX * smallY;
					
	}
	
	private Polygon2D getTranslated(Polygon2D p, int tx, int ty) {
		
		Point2D[] points = p.vertices().toArray(new Point2D[]{});
		double[] x = new double[points.length];
		double[] y = new double[points.length];
		for (int i = 0; i < points.length; ++i) {
			x[i] = points[i].x() + tx;
			y[i] = points[i].y() + ty;
		}
		
		return new SimplePolygon2D(x, y);
		
	}
		
	private static class Event implements Comparable<Event> {
		
		public static enum eventType {OPEN, CLOSE};
		public int x;
		public int y;
		public eventType type;
		public Polygon2D poly;
				
		public Event(int x, int y, eventType type, Polygon2D poly) {
			super();
			this.x = x;
			this.y = y;
			this.type = type;
			this.poly = poly;
		}

		@Override
		public int compareTo(Event o) {
			
			if (type == Event.eventType.OPEN &&  o.type == Event.eventType.CLOSE) return -1;
			if (type == Event.eventType.CLOSE &&  o.type == Event.eventType.OPEN) return 1;
			if (y < o.y) return -1;
			if (y > o.y) return 1;
			if (x < o.x) return -1;
			if (x > o.x) return 1;
			
			return 1;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}
		
		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
		
	}
	
}
