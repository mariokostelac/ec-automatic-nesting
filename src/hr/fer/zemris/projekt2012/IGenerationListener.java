package hr.fer.zemris.projekt2012;

import java.awt.Polygon;
import java.util.List;

public interface IGenerationListener {
	public void generationEvaluated(List<Polygon> generationBest);
}
