package hr.fer.zemris.projekt2012.polygon;

import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class VisualizePolygons extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<Polygon> polygons = new LinkedList<>();

	public void parsePolysFromFile(Path path) {
		this.polygons = new LinkedList<>();
		
		try {
			byte[] data = Files.readAllBytes(path);
			String stringData = new String(data, Charset.forName("UTF-8"));

			String[] polygonDefinitions = stringData.split("#");

			for (int i = 0; i < polygonDefinitions.length; ++i) {
				String[] currentPolyPoints = polygonDefinitions[i].split(";");

				Polygon currentPoly = new Polygon();
				for (int j = 0; j < currentPolyPoints.length; ++j) {

					String[] coords = currentPolyPoints[j]
							.replaceAll("\\(", "").replaceAll("\\)", "")
							.split(",");

					currentPoly.addPoint(Integer.parseInt(coords[0]),
							Integer.parseInt(coords[1]));
				}

				polygons.add(currentPoly);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.paintComponent(getGraphics());
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

		for (Polygon p : polygons) {

			g.drawPolygon(p);
		}

	}

}
