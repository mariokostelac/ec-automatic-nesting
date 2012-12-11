package hr.fer.zemris.projekt2012;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ecf.Deme;
import ecf.Individual;
import ecf.State;
import ecf.fitness.Fitness;
import ecf.fitness.FitnessMin;
import ecf.fitness.IEvaluate;
import ecf.genotype.CrossoverOp;
import ecf.genotype.Genotype;
import ecf.genotype.MutationOp;
import ecf.genotype.permutation.Permutation;
import ecf.genotype.permutation.PermutationCrxPMX;
import ecf.genotype.permutation.PermutationMutInv;

public class AutomaticNestingCalc implements IEvaluate {

	private List<Polygon> polygons;
	private int width = 0;
	
	private State state = null;
	private Genotype genotype = null;
	
	public AutomaticNestingCalc(List<Polygon> polygons, int width) {
		this.polygons = polygons;
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
			Polygon translatedPoly = new Polygon(currEvent.poly.xpoints, currEvent.poly.ypoints, currEvent.poly.npoints);
			translatedPoly.translate(currEvent.x, currEvent.y);
			translatedPolygons.add(translatedPoly);
			System.out.println(currEvent.x + " " + currEvent.y);
		}
		
		return translatedPolygons;
		
	}
	
	private Genotype getGenotype() {
		if (genotype == null)
			genotype = new Permutation(state, polygons.size());
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
	
	private void placePolygon(Set<Event> startEvents, int polygonIndex) {
		
		Polygon newPoly = polygons.get(polygonIndex);
		int polyWidth = newPoly.getBounds().width;
		int polyHeight = newPoly.getBounds().height;
		Set<Event> allEvents = new TreeSet<>(startEvents);
		// popuni evente s završnim eventima
		for (Event currEvent : startEvents) {
			if (currEvent.type == Event.eventType.CLOSE) continue;
			/* dodaje novi event na razinu (trenutna razina + visina trenutnog + visina kojeg dodajemo) jer nailaskom na taj event
			 * znamo da je prošli završio i da imamo dovoljno visine za taj kojeg želimo dodati
			 */
			allEvents.add(new Event(currEvent.x, currEvent.y+currEvent.poly.getBounds().height+polyHeight, Event.eventType.CLOSE, currEvent.poly));
		}
		
		// polje koje će za svaki pixel pamtiti počinje/završava li neki pravokutnik na tom pixelu
		int bins[] = new int[this.width+1];

		boolean added = false;
		for (Event currEvent : allEvents) {
			if (currEvent.type == Event.eventType.OPEN) {
				// otvori pravokutnik
				bins[currEvent.x] += 1;
				bins[currEvent.x+currEvent.poly.getBounds().width] -= 1; 
			} else {
				// zatvori pravokutnik
				bins[currEvent.x] -= 1;
				bins[currEvent.x+currEvent.poly.getBounds().width] += 1;
				// provjeri postoji li dovoljno mjesta za smjestiti novi pravokutnik
				int zerosInRow = 0;
				int currSum = 0;
				for (int i = 0; i < bins.length; ++i) {
					currSum += bins[i];
					if (currSum == 0)
						zerosInRow++;
					else
						zerosInRow = 0;
					if (zerosInRow == polyWidth) {
						// nasli smo najbolje bottom-left mjesto, smjesti ga i izadji van
						int x = i-zerosInRow+1;
						int y = currEvent.y-polyHeight;
						Event startEvent = new Event(
								x,
								y,
								Event.eventType.OPEN,
								newPoly
						);
						startEvents.add(startEvent);
						added = true;
						break;
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
        	topY = Math.max(topY, currEvent.y+currEvent.poly.getBounds().height);
                
		fitness.setValue(topY);
		
	}
	
	private Set<Event> getOpenEvents(Permutation solution) {
		
		Set<Event> staticEvents = new TreeSet<Event>();
        // osiguraj da stvar funkcionira od početka
        // dodaje se poligon koji se raširi od početka do kraja i zatvoren je točno na nuli (tako da se pozove stavljanje novog poligona)
        staticEvents.add(new Event(0, 0, Event.eventType.OPEN, new Polygon(new int[]{0, 400}, new int[]{0, 0}, 2)));
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
		
	private static class Event implements Comparable<Event> {
		
		public static enum eventType {OPEN, CLOSE};
		public int x;
		public int y;
		public eventType type;
		public Polygon poly;
				
		public Event(int x, int y, eventType type, Polygon poly) {
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
