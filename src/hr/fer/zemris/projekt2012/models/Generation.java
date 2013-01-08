package hr.fer.zemris.projekt2012.models;

import java.awt.Polygon;
import java.util.LinkedList;
import java.util.List;

public class Generation {

	private List<Polygon> list;
	private GenerationListModel listener;

	private int generationNumber;

	public Generation(int generationNumber, List<Polygon> list) {
		this.list = list;
		this.generationNumber = generationNumber;
	}

	public void addPolygon(Polygon p) {
		this.list.add(p);
	}

	public List<Polygon> getPolygonList() {
		return this.list;
	}

	@Override
	public String toString() {
		return "GENERATION " + generationNumber;
	}
}
