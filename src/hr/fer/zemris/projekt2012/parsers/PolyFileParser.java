package hr.fer.zemris.projekt2012.parsers;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolyFileParser {
		
	public static List<Polygon> getPolygonsFromFile( File file ) {
		
		List<Polygon> polygons = new ArrayList<>();
		
		try ( BufferedReader fReader = new BufferedReader(new FileReader(file)) ) {
			String currLine;
			while ( (currLine = fReader.readLine()) != null ) {
				String polys[] = currLine.split("\\s*#\\s*");
				for (String poly : polys) {
					Polygon currPolygon = new Polygon();
					String points[] = poly.split("\\s*;\\s*");
					for (String point : points) {
						point = point.substring(1, point.length()-1);
						String coords[] = point.split("\\s*,\\s*");
						currPolygon.addPoint(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
					}
					polygons.add(currPolygon);
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Datoteka "+file.toPath().toAbsolutePath()+" nije pronadjena.");
		} catch (IOException e) {
			System.out.println("Doslo je do greska u citanju datoteke "+file.toPath().toAbsolutePath());
		}
		
		return polygons;
	}
	
}
