package hr.fer.zemris.projekt2012;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.projekt2012.models.Generation;
import hr.fer.zemris.projekt2012.models.GenerationListModel;
import hr.fer.zemris.projekt2012.parsers.PolyFileParser;
import hr.fer.zemris.projekt2012.polygon.*;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AutomaticNestingGUI extends JFrame implements IGenerationListener {

	private static int currentGenerationNumber = 1;

	private int maxHeight;

	private GenerationListModel listModel;

	private JList<Generation> generationListView;
	private List<Generation> generationList = new LinkedList<>();

	private static final long serialVersionUID = 1L;
	private VisualizePolygons polygonDrawer;
	private List<Polygon> polygons = null;

	/**
	 * area with, should be changed through setWidth method
	 */
	private int width = 400;

	/**
	 * Polygons file
	 */
	private Path polygonsPath = null;

	/**
	 * Settings file path
	 */
	private Path settingsPath = Paths.get("settings.json");

	/**
	 * JSON Serializer
	 */
	private JsonSerializer<AutomaticNestingGUI> settingsSeriazlier = new JsonSerializer<AutomaticNestingGUI>() {
		@Override
		public JsonElement serialize(AutomaticNestingGUI src,
				java.lang.reflect.Type typeOfSrc,
				JsonSerializationContext context) {

			JsonObject jObj = new JsonObject();
			jObj.addProperty("width", src.width);
			if (polygonsPath != null)
				jObj.addProperty("polygonsPath", polygonsPath.toString());
			return jObj;
		}
	};

	final JButton algorithmStart = new JButton("Start");
	final JTextField widthEntry = new JTextField(0);

	private Thread calculatingThread = new Thread();

	public AutomaticNestingGUI() {

		this.addWindowListener(settingsListener);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

		setLocation(0, 0);
		setSize(800, 600);
		setTitle("Automatic nesting GUI v0.1");

		/*
		 * try {
		 * UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName
		 * ()); } catch (Exception e) { e.printStackTrace(); } /*
		 */
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

		setWidth(width);

		final JButton parseButton = new JButton();
		parseButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open text file");

				if (fc.showOpenDialog(AutomaticNestingGUI.this) != JFileChooser.APPROVE_OPTION)
					return;

				Path filePath = fc.getSelectedFile().toPath();
				drawPolygonsFromPath(filePath);
			}
		});

		parseButton.setText("Parse polygons from text file");

		polygonDrawer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		polygonDrawer.setBackground(new Color(255, 255, 255));

		final JLabel widthLabel = new JLabel("Set width (algorithm parameter)");
		widthEntry.setHorizontalAlignment(JTextField.RIGHT);

		JButton widthSet = new JButton();
		widthSet.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					AutomaticNestingGUI.this.setWidth(Integer
							.parseInt(widthEntry.getText()));
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
					JOptionPane.showMessageDialog(null,
							"Prvo učitaj datoteku poligona.");
					return;
				}

				calculatingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						algorithmStart.setEnabled(false);
						Algorithm test = new BottomLeftAlgorithmHookeJeevesWithRotation(
								polygons, width);
						test.run();

						drawPolygons(test.getBestSolution());
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

		this.listModel = new GenerationListModel(generationList);
		generationListView = new JList<Generation>(listModel);

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

	public void setWidth(int w) {
		width = w;
		widthEntry.setText(Integer.toString(width));
	}

	public void setPolygonsPath(Path p) {
		polygonsPath = p;
	}

	public void parsePolygons() {
		polygons = PolyFileParser.getPolygonsFromFile(polygonsPath.toFile());
	}

	public void drawPolygonsFromPath(Path p) {
		setPolygonsPath(p);
		parsePolygons();
		polygonDrawer.setPolygons(polygons);
	}

	WindowListener settingsListener = new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {

			try (Reader fileReader = Files.newBufferedReader(settingsPath,
					StandardCharsets.UTF_8)) {
				Gson gson = new Gson();
				JsonObject settings = gson.fromJson(fileReader,
						JsonElement.class).getAsJsonObject();
				if (settings.has("width")) {
					setWidth(settings.get("width").getAsInt());
				}
				if (settings.has("polygonsPath")) {
					drawPolygonsFromPath(Paths.get(settings.get("polygonsPath")
							.getAsString()));
				}
			} catch (IOException ioex) {
				System.out.println(ioex);
			}

		}

		@Override
		public void windowClosing(WindowEvent e) {
			GsonBuilder gb = new GsonBuilder();
			gb.registerTypeAdapter(AutomaticNestingGUI.class,
					settingsSeriazlier);
			Gson gson = gb.setPrettyPrinting().create();
			try (BufferedWriter jsonWriter = Files.newBufferedWriter(
					settingsPath, StandardCharsets.UTF_8)) {
				jsonWriter.write(gson.toJson(AutomaticNestingGUI.this));
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	};

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AutomaticNestingGUI().setVisible(true);
			}
		});
	}

	@Override
	public void generationEvaluated(List<Polygon> generationBest) {
		this.generationList.add((new Generation(currentGenerationNumber,
				generationBest)));

		this.listModel.notifyOfAddition();

		currentGenerationNumber++;
	}

	public void setMaxHeight(int height) {
		this.maxHeight = height;
	}

}
