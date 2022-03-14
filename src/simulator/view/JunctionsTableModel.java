package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private List<Junction> junctions;
	private String[] cols = {"Id", "Green", "Queues"};

	@Override
	public int getRowCount() {
		return junctions.size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
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
				str += " " + r.getId() + ":" + junctions.get(x).;
			return str;
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
