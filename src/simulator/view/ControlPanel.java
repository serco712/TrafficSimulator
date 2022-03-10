package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	
	private int _time;
	
	private RoadMap map;
	
	private JFileChooser jfc;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
	}
	
	public void initGUI() {
		JToolBar jtb = new JToolBar();
		
		// File chooser
		JButton fileChooser = new JButton();
		jfc = new JFileChooser();
		
		fileChooser.setIcon(new ImageIcon("resources/icons/open.png"));
		fileChooser.setToolTipText("Load a new events file");
		fileChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
			
		});
		
		jtb.add(fileChooser);
		
		//Contamination class change
		JButton contButton = new JButton();
		contButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		contButton.setToolTipText("Change CO2 Class of a Vehicle");
		contButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCO2class();
			}
			
		});
		
		jtb.add(contButton);
		
		// Weather change
		JButton weButton = new JButton();
		weButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		weButton.setToolTipText("Change Weather of a Road");
		weButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWeather();
			}
			
		});
		this.add(jtb, BorderLayout.PAGE_START);
	}
	
	private void changeWeather() {
		ChangeWeatherDialog cw = new ChangeWeatherDialog();
		if(cw.open(map) == 1) {
			try {
				List<Pair<String, Weather>> l = new ArrayList<>();
				Pair<String, Weather> p = new Pair<String, Weather>(cw.getRoad().getId(), cw.getWeather());
				l.add(p);
				_ctrl.addEvent(new SetWeatherEvent(cw.getTicks() + _time, l));
			}
			catch (IllegalArgumentException e) {
				// TODO
			}
		}
	}

	private void changeCO2class() {
		ChangeCO2Dialog co = new ChangeCO2Dialog();
		if(co.open(map) == 1) {
			try {
				List<Pair<String, Integer>> l = new ArrayList<>();
				Pair<String, Integer> p = new Pair<String, Integer>(co.getVehicle().getId(), co.getContClass());
				l.add(p);
				_ctrl.addEvent(new SetContClassEvent(co.getTicks() + _time, l));
			}
			catch(IllegalArgumentException e) {
				// TODO 
			}
		}
	}

	private void loadFile() {
		//TODO we dont understand the errors
			try {
				if(jfc.showOpenDialog(this) != jfc.APPROVE_OPTION)
					throw new IOException();
					
				File f = jfc.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(f.getAbsolutePath()));
			}
			catch (IOException io) {
				JOptionPane jop = new JOptionPane("Error");
			}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
