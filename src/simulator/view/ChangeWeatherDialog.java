package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	
	private JComboBox<Road> road;
	
	private DefaultComboBoxModel<Road> roadModel;
	
	private JSpinner ticks;
	
	private JComboBox<Weather> weather;
	
	private DefaultComboBoxModel<Weather> weatherModel;
	
	public ChangeWeatherDialog() {
		initGUI();
	}

	private void initGUI() {
		_status = 0;
		
		setTitle("Change Road Weather");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(new JLabel("Schedule an event to change the weather of a road after"
				+ " a given number of simulation ticks from now."));
		
		JPanel secPanel = new JPanel();
		roadModel = new DefaultComboBoxModel<>();
		road = new JComboBox<>(roadModel);
		weatherModel = new DefaultComboBoxModel<>();
		weather = new JComboBox<>(weatherModel);
		ticks = new JSpinner();
		
		secPanel.add(new JLabel("Road:"));
		secPanel.add(road);
		secPanel.add(new JLabel("Weather:"));
		secPanel.add(weather);
		secPanel.add(new JLabel("Ticks:"));
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
	}
	
	public int open(RoadMap rm) {
		roadModel.removeAllElements();
		for(Road v : rm.getRoads())
			roadModel.addElement(v);
		
		weatherModel.removeAllElements();
		for (Weather w : Weather.values())
			weatherModel.addElement(w);
		
		// TODO put on the f** window
		setVisible(true);
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
