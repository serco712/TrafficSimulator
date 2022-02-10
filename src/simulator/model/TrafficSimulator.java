package simulator.model;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	private RoadMap map;
	
	private SortedArrayList<Event> events;
	
	private int _time;
	
	TrafficSimulator() {
		map = new RoadMap();
		events = new SortedArrayList<>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		//TODO sorted?
	}
	
	public void advance() {
		_time++;
		
		SortedArrayList<Event> aux = new SortedArrayList<>();
		for (Event e : events) {
			if (e.getTime() == _time)
				e.execute(map);
			else
				aux.add(e);
		}
		events = aux;
		
		for (Junction j : map.getJunctions())
			j.advance(_time);
		
		for (Road r : map.getRoads())
			r.advance(_time);
	}
	
	public void reset() {
		map.reset();
		events.clear();
		_time = 0;
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("time", _time);
		jo.put("state", map.report());
		
		return jo;
	}
	
	
}
