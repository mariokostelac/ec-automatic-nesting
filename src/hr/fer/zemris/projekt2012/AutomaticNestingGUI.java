package hr.fer.zemris.projekt2012;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.projekt2012.polygon.VisualizePolygons;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class AutomaticNestingGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VisualizePolygons polygonDrawer;
	
	public AutomaticNestingGUI() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

		setLocation(0, 0);
		setSize(800, 600);
		setTitle("Automatic nesting GUI v0.1");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initGUI();
	}

	public void drawPolygons(List<Polygon> polygons) {
		this.polygonDrawer.setPolygons(polygons);
	}
	
	public void initGUI() {
		polygonDrawer = new VisualizePolygons();

		JPanel leftComponents = new JPanel();
		leftComponents.setLayout(new BoxLayout(leftComponents,
				BoxLayout.PAGE_AXIS));

		leftComponents.setPreferredSize(new Dimension(200, 600));

		JButton parseButton = new JButton();
		parseButton.setSize(new Dimension(200, 100));
		parseButton.setMaximumSize(new Dimension(200, 100));
		parseButton.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open text file");

				if (fc.showOpenDialog(AutomaticNestingGUI.this) != JFileChooser.APPROVE_OPTION)
					return;

				File fileName = fc.getSelectedFile();
				Path filePath = fileName.toPath();

				polygonDrawer.parsePolysFromFile(filePath);
			}
		});

		parseButton.setText("Parse polygons from text file");

		polygonDrawer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		leftComponents.add(parseButton);

		this.getContentPane().add(leftComponents);
		this.getContentPane().add(polygonDrawer);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new AutomaticNestingGUI().setVisible(true);
			}
		});
	}

}
