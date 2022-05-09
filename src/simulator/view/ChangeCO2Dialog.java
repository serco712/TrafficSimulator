package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.SpinnerNumberModel;

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