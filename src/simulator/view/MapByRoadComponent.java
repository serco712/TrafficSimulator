package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _BLACK_COLOR = Color.BLACK;
	
	private RoadMap _map;

	private Image _car;
	
	private Image[] img = {loadImage("cont_0.png"), loadImage("cont_1.png"), loadImage("cont_2.png"),
			loadImage("cont_3.png"), loadImage("cont_4.png"), loadImage("cont_5.png")};
	
	public MapByRoadComponent(Controller ctrl) {
		ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		_car = loadImage("car.png");
		setPreferredSize(new Dimension(300, 200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} 
		else 
			drawMap(g);
	}
	
	private void drawMap(Graphics g) {
		int y = 50;
		int x1 = 50;
		int x2 = getWidth() - 100;
		for(Road r : _map.getRoads()) {
			g.setColor(_BLACK_COLOR);
			g.drawString(r.getId(), x1 - 40, y + 4);
			g.drawLine(x1, y, x2, y);
			
			// Junction left
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), x1 - 5, y - 10);
			
			// Junction right
			if(r.getDest().getGreenOne().equals(r))
				g.setColor(_GREEN_LIGHT_COLOR);
			else
				g.setColor(_RED_LIGHT_COLOR);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), x2 - 5, y - 10);
			
			drawVehicles(g, r.getVehicles(), x1, x2, y);
			drawImage(g, r, x2, y);
			y += 40;
		}
	}
	
	private void drawVehicles(Graphics g, List<Vehicle> v, int x1, int x2, int y) {
		for (Vehicle ve : v) {
			if (ve.getStatus() != VehicleStatus.ARRIVED) {
				
				float x = (ve.getLocation()*(x2 - x1)) / ve.getRoad().getLength();

				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) ve.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				// draw an image of a car (with circle as background) and it identifier
				g.drawImage(_car, (int) (x1 + x), y - 6, 12, 12, this);
				g.drawString(ve.getId(), (int) (x1 + x), y - 6);
			}
		}
	}
	
	private void drawImage(Graphics g, Road r, int x, int y) {
		g.drawImage(r.getWeather().getImage(), x + 20, y - 18, 32, 32, this);
		int C = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getContLimit()),1.0) /
			0.19);
		Image i = img[C];
		g.drawImage(i, x + 60, y - 18, 32, 32, this);
	}
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
		
	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
