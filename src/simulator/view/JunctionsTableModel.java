package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Junction> junctions;
	private String[] cols = {"Id", "Green", "Queues"};

	public JunctionsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		junctions = new ArrayList<>();
	}

	@Override
	public int getRowCount() {
		return junctions.size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}
	
	@Override
	public String getColumnName(int column) {
		if (column >= cols.length || column < 0)
			throw new IllegalArgumentException("The column is not found");
		
		return cols[column];
	}

	@Override
	public Object getValueAt(int x, int y) {
		switch(y) {
		case 0:
			return junctions.get(x).getId();
		case 1:
			int currGreen = junctions.get(x).getGreenLightIndex();
			if (currGreen == -1)
				return "NONE";
			else
				return junctions.get(x).getInRoads().get(currGreen).getId();
		case 2:
			String str = "";
			for (Road r : junctions.get(x).getInRoads())
				str += " " + r.getId() + ":" + junctions.get(x).getQueue(r);
			return str;
		}
		return null;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
