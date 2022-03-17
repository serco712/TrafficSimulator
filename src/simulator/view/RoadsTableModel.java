package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Road> roads;
	private String[] cols = {"Id", "Length", "Weather", 
			"Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};
	
	public RoadsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		roads = new ArrayList<>();
	}

	@Override
	public int getRowCount() {
		return roads.size();
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
		switch(y) {
		case 0:
			return roads.get(x).getId();
		case 1:
			return roads.get(x).getLength();
		case 2:
			return roads.get(x).getWeather();
		case 3:
			return roads.get(x).getMaxSpeed();
		case 4:
			return roads.get(x).getSpeedLimit();
		case 5:
			return roads.get(x).getTotalCO2();
		case 6:
			return roads.get(x).getContLimit();
		default:
			return "";
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
