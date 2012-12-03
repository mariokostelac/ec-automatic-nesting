package hr.fer.zemris.projekt2012.polygon;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred služi isključivo u svrhu testiranja generatora poligona.
 * Stvara se prozor u kojemu se nalazi prostor za prikaz poligona i
 * gumb "Stvori" koji stvara novi poligon.
 * @author Davor
 *
 */
public class RandomPolygonTest extends JFrame {

	public RandomPolygonTest() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0, 0);
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
		getContentPane().add(dodavanje, BorderLayout.PAGE_END);
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
		private Polygon zaCrtanje;

		public PolygonPainter(Polygon zaCrtanje) {
			super();
			this.zaCrtanje = zaCrtanje;
		}
		
		
		public void setZaCrtanje(Polygon zaCrtanje) {
			getGraphics().clearRect(0, 0, getWidth(), getHeight());
			paintComponent(getGraphics());
			this.zaCrtanje = zaCrtanje;
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			super.setBackground(Color.white);
			g.setColor(Color.white);
			g.clearRect(0, 0, getWidth(), getHeight());
			Rectangle a = zaCrtanje.getBounds();
			setPreferredSize(new Dimension(a.width, a.height));
			zaCrtanje.translate(-a.x, -a.y);
			g.setColor(Color.BLACK);
			for(int i = 0; i < zaCrtanje.npoints; i++)
				g.drawLine(zaCrtanje.xpoints[i], zaCrtanje.ypoints[i],
						zaCrtanje.xpoints[(i+1)%zaCrtanje.npoints], zaCrtanje.ypoints[(i+1)%zaCrtanje.npoints]);
		}
	}
	
}
