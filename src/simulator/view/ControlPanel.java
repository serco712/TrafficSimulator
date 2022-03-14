package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

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
	
	private JButton fileChooser;
	
	private JButton contButton;
	
	private JButton weButton;
	
	private JButton runButton;
	
	private JButton stopButton;
	
	private JSpinner ticks;
	
	private JButton quitButton;
	
	private boolean stop;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
	}
	
	public void initGUI() {
		JToolBar jtb = new JToolBar();
		
		// File chooser
		
		fileChooser = new JButton();
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
		contButton = new JButton();
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
		weButton = new JButton();
		weButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		weButton.setToolTipText("Change Weather of a Road");
		weButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWeather();
			}
			
		});
		
		// Run button
		runButton = new JButton();
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setToolTipText("Run the simulator");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}

		});
		
		jtb.add(runButton);
		
		// Stop Button
		stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("Stops the simulation");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopSim();
			}
			
		});
		
		jtb.add(stopButton);
		
		// Ticks Spinner
		ticks = new JSpinner(new SpinnerNumberModel());
		ticks.setMaximumSize(new Dimension(80, 40));
		ticks.setMinimumSize(new Dimension(80, 40));
		ticks.setPreferredSize(new Dimension(80, 40));
		ticks.setToolTipText("Simulator tick to run: 1-10000");
		
		jtb.add(new JLabel("Ticks:"));
		jtb.add(ticks);
		
		jtb.add(Box.createGlue());
		jtb.addSeparator();
		
		// Quit Button
		quitButton = new JButton();
		quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		quitButton.setToolTipText("Exits the program");
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				JOptionPane.showOptionDialog(this, "Are you sure you want to quit?",
						"Quit", boolean.class, String.class, new ImageIcon("resources/icons/listener.png"), 
						new Option(true, false), initialValue);
				*/
			}
			
		});
		this.add(jtb, BorderLayout.PAGE_START);
	}
	
	private void stopSim() {
		stop = true;
	}
	
	private void start() {
		stop = false;
		toggleButtons();
		runSim((Integer) ticks.getValue());
	}
	
	private void runSim (int tc) {
		if(tc > 0 && !stop) {
			try {
				_ctrl.run(1);
			}
			catch (Exception e) {
				stop = true;
				toggleButtons();
				System.out.println("The run is not possible");
			}
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					runSim(tc - 1);
				}
				
			});
		}
		else {
			stop = true;
			toggleButtons();
		}
	}
	
	private void toggleButtons() {
		fileChooser.setEnabled(stop);
		contButton.setEnabled(stop);
		weButton.setEnabled(stop);
		runButton.setEnabled(stop);
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
