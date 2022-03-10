package simulator.view;


import java.awt.Frame;
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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2Dialog extends JDialog {
	
	private static final long serialVersionUID = 1L;

	private int _status;
	
	private JComboBox<Vehicle> vehicle;
	
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	
	private JSpinner ticks;
	
	private JComboBox<Integer> contClass;
	
	private DefaultComboBoxModel<Integer> contClassModel;
	
	public ChangeCO2Dialog () {
		initGUI();
	}

	private void initGUI() {
		_status = 0;
		
		setTitle("Change CO2 Class");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(new JLabel("Schedule an event to change the CO2 class of a vehicle"
				+ " after a given number of simulation ticks from now."));
		
		JPanel secPanel = new JPanel();
		vehicleModel = new DefaultComboBoxModel<>();
		vehicle = new JComboBox<>(vehicleModel);
		contClassModel = new DefaultComboBoxModel<>();
		contClass = new JComboBox<>(contClassModel);
		ticks = new JSpinner();
		
		secPanel.add(new JLabel("Vehicle:"));
		secPanel.add(vehicle);
		secPanel.add(new JLabel("CO2 class:"));
		secPanel.add(contClass);
		secPanel.add(new JLabel("Ticks:"));
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
	}
	
	public int open(RoadMap rm) {
		vehicleModel.removeAllElements();
		for(Vehicle v : rm.getVehicles())
			vehicleModel.addElement(v);
		
		contClassModel.removeAllElements();
		for(int i = 0; i <= 10; i++)
			contClassModel.addElement(i);
		
		// TODO put on the f** window
		setVisible(true);
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
