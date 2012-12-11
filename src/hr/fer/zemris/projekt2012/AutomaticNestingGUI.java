package hr.fer.zemris.projekt2012;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import hr.fer.zemris.projekt2012.parsers.PolyFileParser;
import hr.fer.zemris.projekt2012.polygon.VisualizePolygons;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AutomaticNestingGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private VisualizePolygons polygonDrawer;
	private List<Polygon> polygons = null;

	private int width = 400;
	//private int height;
	
	final JButton algorithmStart = new JButton("Start");
	
	private Thread calculatingThread = new Thread();

	public AutomaticNestingGUI() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

		setLocation(0, 0);
		setSize(800, 600);
		setTitle("Automatic nesting GUI v0.1");
		
		/*try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { e.printStackTrace(); } /**/
		initGUI();
	}

	public void drawPolygons(List<Polygon> polygons) {
		this.polygonDrawer.setPolygons(polygons);
	}

	public void initGUI() {
		polygonDrawer = new VisualizePolygons();

		final JPanel leftComponents = new JPanel();
		leftComponents.setLayout(new BoxLayout(leftComponents,
				BoxLayout.PAGE_AXIS));

		final JButton parseButton = new JButton();
		parseButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open text file");

				if (fc.showOpenDialog(AutomaticNestingGUI.this) != JFileChooser.APPROVE_OPTION)
					return;

				File fileName = fc.getSelectedFile();

				polygons = PolyFileParser.getPolygonsFromFile(fileName);
				polygonDrawer.setPolygons(polygons);
			}
		});

		parseButton.setText("Parse polygons from text file");

		polygonDrawer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		polygonDrawer.setBackground(new Color(255, 255, 255));

		final JLabel widthLabel = new JLabel("Set width (algorithm parameter)");
		final JTextField widthEntry = new JTextField(Integer.toString(width));
		widthEntry.setHorizontalAlignment(JTextField.RIGHT);

		JButton widthSet = new JButton();
		widthSet.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					AutomaticNestingGUI.this.width = Integer
							.parseInt(widthEntry.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(AutomaticNestingGUI.this,
							"Width must be an integer!");
				}
			}
		});
		widthSet.setText("SET");

		algorithmStart.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (polygons == null) {
					JOptionPane.showMessageDialog(null, "Prvo učitaj datoteku poligona.");					
					return;
				}
				
				calculatingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						algorithmStart.setEnabled(false);
						AutomaticNestingCalc test = new AutomaticNestingCalc(polygons, width);
						test.run();
						
						drawPolygons( test.getBestSolution() );
						algorithmStart.setEnabled(true);
					}
				});
				calculatingThread.start();
								
			}
			
		});
		algorithmStart.setText("START");

		leftComponents.add(parseButton);
		leftComponents.add(Box.createRigidArea(new Dimension(0, 50)));
		leftComponents.add(widthLabel);
		leftComponents.add(widthEntry);
		leftComponents.add(widthSet);
		leftComponents.add(Box.createRigidArea(new Dimension(0, 50)));
		leftComponents.add(algorithmStart);

		Font usedFont = new Font(widthLabel.getFont().getName(), widthLabel
				.getFont().getStyle(), 15);
		widthLabel.setFont(usedFont);
		parseButton.setFont(usedFont);
		widthEntry.setFont(usedFont);
		widthSet.setFont(usedFont);
		algorithmStart.setFont(usedFont);

		parseButton.setMaximumSize(new Dimension(300, 90));
		widthEntry.setMaximumSize(new Dimension(300, 30));
		leftComponents.setMaximumSize(new Dimension(300, 600));
		widthSet.setMaximumSize(new Dimension(150, 30));
		algorithmStart.setMaximumSize(new Dimension(200, 80));

		algorithmStart.setBackground(Color.GREEN);

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
