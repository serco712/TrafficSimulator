package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Vehicle> vehicles;
	private String[] cols = {"Id", "Location", "Itinerary", "CO2 Class", 
			"Max. Speed", "Speed", "Total CO2", "Distance"};
	
	public VehiclesTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		vehicles = new ArrayList<>();
	}
	
	@Override
	public int getRowCount() {
		return vehicles.size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}
	
	public String getColumnName(int column) {
		if (column >= cols.length || column < 0)
			throw new IllegalArgumentException("The column is not found");
		
		return cols[column];
	}
	
	@Override
	public Object getValueAt(int x, int y) {
		if (y < 0 || y >= cols.length)
			throw new IllegalArgumentException("The column is not valid");
		
		if (x < 0 || x >= vehicles.size())
			throw new IllegalArgumentException("The row is not found");
		
		Vehicle v = vehicles.get(x);
		
		switch (y) {
		case 0:
			return v.getId();
		case 1:
			switch (v.getStatus()) {
			case PENDING:
				return "Pending";
			case TRAVELING:
				return v.getRoad().getId() + ":" + v.getLocation();
			case WAITING:
				return "Waiting:" + v.getRoad().getDest().getId();
			case ARRIVED:
				return "Arrived";
			default:
				return "";
			}
		case 2:
			
			StringBuilder str = new StringBuilder("[");
			for(Junction j : v.getItinerary()) {
				str.append(j.getId() + ", ");
			}
			if(str.length() >= 2)
				str.setLength(str.length() - 2);
			str.append("]");
			return str.toString();
		case 3:
			return v.getContClass();
		case 4:
			return v.getMaxSpeed();
		case 5:
			return v.getSpeed();
		case 6:
			return v.getTotalCO2();
		case 7:
			return v.getDistance();
		default:
			return "";
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		
	}
}
