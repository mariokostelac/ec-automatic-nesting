package hr.fer.zemris.projekt2012.polygon;


import hr.fer.zemris.projekt2012.polygon.PolygonRandom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred služi isključivo u svrhu testiranja generatora poligona.
 * Stvara se prozor u kojemu se nalazi prostor za prikaz poligona,
 * gumb "Stvori" koji stvara novi poligon i gumb Rotiraj koji poligon rotira za 45°.
 * @author Edi Smoljan
 *
 */
public class RandomPolygonTest extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public RandomPolygonTest() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0, 0);
		setSize(800, 600);
		setTitle("PolygonTest");
		initGUI();
	}
	
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		JButton dodavanje = new JButton();
		dodavanje.setText("Stvori");
		PolygonGenerator.setUpper(100);
		final PolygonPainter crtanje = new PolygonPainter(PolygonGenerator.generate());
		JScrollPane zaCrt = new JScrollPane(crtanje);
		dodavanje.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				crtanje.setZaCrtanje(PolygonGenerator.generate());
			}
		});
		
		JButton rotir = new JButton();
		rotir.setText("Rotiraj");
		rotir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				crtanje.rotiraj45();
			}
		});
		JPanel cont = new JPanel();
		FlowLayout fl = new FlowLayout();
		cont.setLayout(fl);
		cont.add(dodavanje);
		cont.add(rotir);
		getContentPane().add(cont, BorderLayout.PAGE_END);
		getContentPane().add(zaCrt, BorderLayout.CENTER);
	}
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new RandomPolygonTest().setVisible(true);
				
			}
		});
	}
	
	private static class PolygonPainter extends JComponent {
		private static final long serialVersionUID = 1L;
		
		private PolygonRandom zaCrtanje;

		public PolygonPainter(PolygonRandom zaCrtanje) {
			super();
			this.zaCrtanje = zaCrtanje;
		}
		
		
		public void rotiraj45() {
			zaCrtanje.rotateDeg(45);
			zaCrtanje.pomakniNaPocetno();
			getGraphics().clearRect(0, 0, getWidth(), getHeight());
			paintComponent(getGraphics());
		}


		public void setZaCrtanje(PolygonRandom zaCrtanje) {
			this.zaCrtanje = zaCrtanje;
			getGraphics().clearRect(0, 0, getWidth(), getHeight());
			paintComponent(getGraphics());
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			super.setBackground(Color.white);
			g.setColor(Color.white);
			g.clearRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			for(int i = 0; i < zaCrtanje.npoints; i++)
				g.drawLine(zaCrtanje.xpoints[i], zaCrtanje.ypoints[i],
						zaCrtanje.xpoints[(i+1)%zaCrtanje.npoints], zaCrtanje.ypoints[(i+1)%zaCrtanje.npoints]);
		}
	}
	
}
