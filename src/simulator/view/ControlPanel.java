package simulator.view;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	private ChangeWeatherDialog cw;
	private ChangeCO2Dialog co;
	
	private boolean stop;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		JToolBar jtb = new JToolBar();
		this.setLayout(new BorderLayout());
		this.add(jtb, BorderLayout.PAGE_START);
		
		// File chooser
		fileChooser = newJFCButton();
		jtb.add(fileChooser);
		jtb.addSeparator();
		
		//Contamination class change
		contButton = newContButton();
		jtb.add(contButton);
		
		// Weather change
		weButton = newWeatherButton();
		jtb.add(weButton);
		jtb.addSeparator();
		
		// Run button
		runButton = newRunButton();
		jtb.add(runButton);
		
		// Stop Button
		stopButton = newStopButton();
		jtb.add(stopButton);
		
		// Ticks Spinner
		ticks = newTicksSpinner();
		jtb.add(new JLabel("Ticks:"));
		jtb.add(ticks);
		
		jtb.add(Box.createGlue());
		jtb.addSeparator();
		
		// Quit Button
		quitButton = newQuitButton(jtb);
		jtb.add(quitButton);
	}
	
	private JButton newJFCButton() {
		JButton fileChooser = new JButton();
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("resources/examples/"));
		
		fileChooser.setIcon(new ImageIcon("resources/icons/open.png"));
		fileChooser.setToolTipText("Load a new events file");
		fileChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
			
		});
		
		return fileChooser;
	}
	
	private JButton newContButton() {
		JButton contButton = new JButton();
		contButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		contButton.setToolTipText("Change CO2 Class of a Vehicle");
		contButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCO2class();
			}
			
		});
		
		return contButton;
	}
	
	private JButton newWeatherButton() {
		JButton weButton = new JButton();
		weButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		weButton.setToolTipText("Change Weather of a Road");
		weButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWeather();
			}
			
		});
		
		return weButton;
	}
	
	private JButton newRunButton() {
		JButton runButton = new JButton();
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setToolTipText("Run the simulator");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}

		});
		
		return runButton;
	}
	
	private JButton newStopButton() {
		JButton stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("Stops the simulation");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopSim();
			}
			
		});
		
		return stopButton;
	}
	
	private JSpinner newTicksSpinner() {
		JSpinner ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		
		ticks.setMaximumSize(new Dimension(80, 40));
		ticks.setMinimumSize(new Dimension(80, 40));
		ticks.setPreferredSize(new Dimension(80, 40));
		ticks.setToolTipText("Simulator tick to run: 1-10000");
		
		return ticks;
	}
	
	private JButton newQuitButton(JToolBar jtb) {
		JButton quitButton = new JButton();
		quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		quitButton.setToolTipText("Exits the program");
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(ControlPanel.this), "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, new ImageIcon("resources/icons/error.jpg"));
				if (res == JOptionPane.OK_OPTION)
					System.exit(0);
			}
			
		});
		
		return quitButton;
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
				JOptionPane.showMessageDialog(
						SwingUtilities.getWindowAncestor(ControlPanel.this), 
						"Ha surgido un error durante la simulacion", 
						"Error",  
						JOptionPane.ERROR_MESSAGE);
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
		if (cw == null)
			cw = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		
		int status = cw.open(map);
		if (status == 1) {
			try {
				List<Pair<String, Weather>> l = new ArrayList<>();
				Pair<String, Weather> p = new Pair<String, Weather>(cw.getRoad().getId(), cw.getWeather());
				l.add(p);
				_ctrl.addEvent(new SetWeatherEvent(cw.getTicks() + _time, l));
			}
			catch (IllegalArgumentException e) {
			}
		}
	}

	private void changeCO2class() {
		if (co == null)
			co = new ChangeCO2Dialog((Frame) SwingUtilities.getWindowAncestor(this));
		
		int status = co.open(map);
		if(status == 1) {
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
			try {
				if(jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
					throw new IOException();
					
				File f = jfc.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(f.getAbsolutePath()));
			}
			catch (IOException io) {
				new JOptionPane("Error");
			}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.map = map;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.map = map;
		_time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.map = map;
		_time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.map = map;
		_time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.map = map;
		_time = time;
	}

	@Override
	public void onError(String err) {
		
	}

}
