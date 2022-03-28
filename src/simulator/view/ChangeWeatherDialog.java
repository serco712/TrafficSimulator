package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
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
import javax.swing.SpinnerNumberModel;

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
