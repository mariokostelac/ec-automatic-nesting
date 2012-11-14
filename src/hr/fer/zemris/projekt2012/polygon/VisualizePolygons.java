package hr.fer.zemris.projekt2012.polygon;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class VisualizePolygons extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<Polygon> polygons = new LinkedList<>();

	private float xScale = 1;
	private float yScale = 1;
	
	public void setScaleX(float xScale) {
		this.xScale = xScale;
	}
	
	public void setScaleY(float yScale) {
		this.yScale = yScale;
	}

	public void setPolygons(List<Polygon> list) {
		this.polygons = new LinkedList<>();
		for (Polygon poly : list) {
			polygons.add(poly);
		}
		this.paintComponent(getGraphics());
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g; 
		
		for (Polygon p : polygons) {
			AffineTransform scaleMatrix = new AffineTransform();
			scaleMatrix.scale(this.xScale, this.yScale);
			
			Shape p2 = scaleMatrix.createTransformedShape(p);

			g2d.draw(p2);
		}

	}

}
