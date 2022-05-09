package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Event> events;
	private String cols[] = {"Time", "Desc."};
	
	public EventsTableModel(Controller ctrl) {
		events = new ArrayList<>();
		ctrl.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public int getRowCount() {
		return events.size();
	}
	
	public String getColumnName(int column) {
		if (column >= cols.length || column < 0)
			throw new IllegalArgumentException("The column is not found");
		
		return cols[column];
	}

	@Override
	public Object getValueAt(int x, int y) {
		if (y != 0 && y != 1)
			throw new IllegalArgumentException("The column is not valid");
		
		if (x < 0 || x >= events.size())
			throw new IllegalArgumentException("The row is not found");
		
		Event e = events.get(x);
		if (y == 0)
			return ("" + e.getTime());
		else
			return e.toString();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		
	}
	
}
