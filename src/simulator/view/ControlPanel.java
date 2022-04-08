package simulator.view;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
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
		initGUI();
	}
	
	public void initGUI() {
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
	
	public JButton newJFCButton() {
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
	
	public JButton newContButton() {
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
	
	public JButton newWeatherButton() {
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
	
	public JButton newRunButton() {
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
	
	public JButton newStopButton() {
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
	
	public JSpinner newTicksSpinner() {
		JSpinner ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		
		ticks.setMaximumSize(new Dimension(80, 40));
		ticks.setMinimumSize(new Dimension(80, 40));
		ticks.setPreferredSize(new Dimension(80, 40));
		ticks.setToolTipText("Simulator tick to run: 1-10000");
		
		return ticks;
	}
	
	public JButton newQuitButton(JToolBar jtb) {
		JButton quitButton = new JButton();
		quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		quitButton.setToolTipText("Exits the program");
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(jtb, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, new ImageIcon("resources/icons/error.jpg"));
				
				if (res == 0)
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
		ChangeWeatherDialog cw = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
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
		ChangeCO2Dialog co = new ChangeCO2Dialog((Frame) SwingUtilities.getWindowAncestor(this));
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
		//TODO we dont understand the errors
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
	
	public class ChangeCO2Dialog extends JDialog {
		
		private static final long serialVersionUID = 1L;

		private int _status;
		private JComboBox<Vehicle> vehicle;
		private DefaultComboBoxModel<Vehicle> vehicleModel;
		private JSpinner ticks;
		private JComboBox<Integer> contClass;
		private DefaultComboBoxModel<Integer> contClassModel;
		
		public ChangeCO2Dialog (Frame frame) {
			super(frame, true);
			initGUI();
		}

		private void initGUI() {
			_status = 0;
			
			setTitle("Change CO2 Class");
			
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			
			JLabel jl = new JLabel("<html><p>Schedule an event to change the CO2 class of a vehicle after a given number "
					+ "of <br> simulation ticks from now.</p></html>");
			JPanel lbp = new JPanel(new FlowLayout());
			
			lbp.add(jl, FlowLayout.LEFT);
			mainPanel.add(lbp);
			JPanel secPanel = new JPanel();
			vehicleModel = new DefaultComboBoxModel<>();
			vehicle = new JComboBox<>(vehicleModel);
			vehicle.setPreferredSize(new Dimension(85, 20));
			contClassModel = new DefaultComboBoxModel<>();
			contClass = new JComboBox<>(contClassModel);
			contClass.setPreferredSize(new Dimension(50, 20));
			ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
			
			JLabel jv = new JLabel("Vehicle: ");
			secPanel.add(jv);
			jv.setLabelFor(vehicle);
			secPanel.add(vehicle);
			
			JLabel jc = new JLabel("CO2 class: ");
			secPanel.add(jc);
			jc.setLabelFor(contClass);
			secPanel.add(contClass);
			JLabel jt = new JLabel("Ticks: ");
			secPanel.add(jt);
			jt.setLabelFor(ticks);
			secPanel.add(ticks);
			
			mainPanel.add(secPanel);
			
			JPanel confiPanel = new JPanel();
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					_status = 0;
					ChangeCO2Dialog.this.setVisible(false);
				}
				
			});
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(vehicleModel.getSelectedItem() != null && contClassModel.getSelectedItem() != null) {
						_status = 1;
						ChangeCO2Dialog.this.setVisible(false);
					}
				}
				
			});
			confiPanel.add(cancelButton);
			confiPanel.add(okButton);
			
			mainPanel.add(confiPanel);
			this.add(mainPanel);
		}
		
		public int open(RoadMap rm) {
			vehicleModel.removeAllElements();
			for(Vehicle v : rm.getVehicles())
				vehicleModel.addElement(v);
			
			contClassModel.removeAllElements();
			for(int i = 0; i <= 10; i++)
				contClassModel.addElement(i);
			
			this.setSize(500, 175);
			this.setLocation((getParent().getWidth() - this.getWidth()) / 2, (getParent().getHeight()
					- this.getHeight()) / 2);
			this.setVisible(true);
			return _status;
		}
		
		public int getTicks() {
			return (Integer) ticks.getValue();
		}
		
		public Vehicle getVehicle() {
			return (Vehicle) vehicleModel.getSelectedItem();
		}
		
		public int getContClass() {
			return (Integer) contClassModel.getSelectedItem();
		}
	}
	
	public class ChangeWeatherDialog extends JDialog {

		private static final long serialVersionUID = 1L;

		private int _status;
		private JComboBox<Road> road;
		private DefaultComboBoxModel<Road> roadModel;
		private JSpinner ticks;
		private JComboBox<Weather> weather;
		private DefaultComboBoxModel<Weather> weatherModel;
		
		public ChangeWeatherDialog(Frame parent) {
			super(parent, true);
			initGUI();
		}

		private void initGUI() {
			_status = 0;
			
			setTitle("Change Road Weather");
			
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			
			JPanel jp = new JPanel(new FlowLayout());
			
			JLabel txt = new JLabel("<html><p>Schedule an event to change the weather of a road after a given number of"
									+ "<br> simulation ticks from now.</p></html>");
			jp.add(txt, FlowLayout.LEFT);
			mainPanel.add(jp);
			JPanel secPanel = new JPanel();
			roadModel = new DefaultComboBoxModel<>();
			road = new JComboBox<>(roadModel);
			road.setPreferredSize(new Dimension(70, 20));
			weatherModel = new DefaultComboBoxModel<>();
			weather = new JComboBox<>(weatherModel);
			weather.setPreferredSize(new Dimension(80, 20));
			ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
			
			secPanel.add(new JLabel("Road: "));
			secPanel.add(road);
			secPanel.add(new JLabel("Weather: "));
			secPanel.add(weather);
			secPanel.add(new JLabel("Ticks: "));
			secPanel.add(ticks);
			
			mainPanel.add(secPanel);
			
			JPanel confiPanel = new JPanel();
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					_status = 0;
					ChangeWeatherDialog.this.setVisible(false);
				}
				
			});
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(roadModel.getSelectedItem() != null && weatherModel.getSelectedItem() != null) {
						_status = 1;
						ChangeWeatherDialog.this.setVisible(false);
					}
				}
				
			});
			confiPanel.add(cancelButton);
			confiPanel.add(okButton);
			
			mainPanel.add(confiPanel);
			pack();
			setResizable(false);
			setVisible(false);
			this.add(mainPanel);
		}
		
		public int open(RoadMap rm) {
			roadModel.removeAllElements();
			for(Road v : rm.getRoads())
				roadModel.addElement(v);
			
			weatherModel.removeAllElements();
			for (Weather w : Weather.values())
				weatherModel.addElement(w);
			
			this.setSize(500, 150);
			this.setLocation((getParent().getWidth() - this.getWidth()) / 2, (getParent().getHeight()
					- this.getHeight()) / 2);
			this.setVisible(true);
			return _status;
		}

		public int getTicks() {
			return (Integer) ticks.getValue();
		}
		
		public Road getRoad() {
			return (Road) roadModel.getSelectedItem();
		}
		
		public Weather getWeather() {
			return (Weather) weatherModel.getSelectedItem();
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.map = map;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.map = map;
		_time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this.map = map;
		_time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

}
