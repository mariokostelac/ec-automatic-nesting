package hr.fer.zemris.projekt2012;

import hr.fer.zemris.projekt2012.polygon.PolygonRandom;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import ecf.fitness.IEvaluate;

/**
 * Apstraktan razred algoritma osigurava omogućavanje komunikacije između GUI-ja i algoritma.
 * Konačan razred mora implementirati okidanje listenera
 * Omogućuje kvačenje i brisanje listenera za pojedine evente
 * @author mario
 *
 */
abstract public class Algorithm implements IEvaluate {

	protected List<IGenerationListener> generationListeners = new ArrayList<>();
	protected List<IBestSolutionListener> bestSolutionListerners = new ArrayList<>();
	
	public void attachGenerationListener(IGenerationListener listener) {
		generationListeners.add(listener);
	}
	
	public void removeGenerationListener(IGenerationListener listener) {
		generationListeners.remove(listener);
	}
	
	public void attachBestSolutionListener(IBestSolutionListener listener) {
		bestSolutionListerners.add(listener);
	}
	
	public void removeBestSolutionListener(IGenerationListener listener) {
		bestSolutionListerners.remove(listener);
	}
	
	abstract public void run();
	
	abstract public List<Polygon> getBestSolution();
	
}
