package hr.fer.zemris.projekt2012.polygon;

import hr.fer.zemris.projekt2012.AutomaticNestingGUI;
import hr.fer.zemris.projekt2012.models.Generation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class VisualizePolygons extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<Polygon> polygons = new LinkedList<>();

	private List<Generation> generations = new LinkedList<>();

	private float xScale = 1;
	private float yScale = 1;

	public void addGenerationBest(Generation generation) {
		this.generations.add(generation);
	}

	public List<Generation> getGenerations() {
		return this.generations;
	}

	public String[] getGenerationNames() {
		String[] generationNameArray = new String[this.generations.size()];

		for (int i = 0; i < this.generations.size(); i++) {
			generationNameArray[i] = "Generacija " + (i + 1);
		}

		return generationNameArray;
	}

	public void setScaleX(float xScale) {
		this.xScale = xScale;
	}

	public void setScaleY(float yScale) {
		this.yScale = yScale;
	}

	public void setPolygons(List<Polygon> list) {
		this.generations.add(new Generation(1, list));
		System.out.println(generations.size());

		this.polygons = new LinkedList<>();
		for (Polygon poly : list) {
			polygons.add(poly);
		}
		this.paintComponent(getGraphics());

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		Color outline = new Color(200, 10, 10);
		Color inline = new Color(50, 50, 50);

		int i = 0;
		for (Polygon p : polygons) {
			AffineTransform scaleMatrix = new AffineTransform();
			scaleMatrix.scale(this.xScale, this.yScale);

			Shape p2 = scaleMatrix.createTransformedShape(p);
			g2d.setColor(inline);
			g2d.draw(p2);
			g2d.setColor(outline);
			g2d.draw(p2.getBounds());

			String text = new Integer(i).toString();
			g.setColor(Color.BLUE);
			Rectangle r = p.getBounds();
			g.drawString(text, r.x + r.width / 2, r.y + r.height / 2);
			++i;
		}

	}

}
