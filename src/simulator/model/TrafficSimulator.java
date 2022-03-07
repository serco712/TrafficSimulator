package simulator.model;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap map;
	private SortedArrayList<Event> events;
	private int _time;
	// TODO doubt
	private TrafficSimObserver tso;
	
	public TrafficSimulator() {
		map = new RoadMap();
		events = new SortedArrayList<>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		tso.onEventAdded(map, events, e, _time);
	}
	
	public void advance() {
		_time++;
		
		tso.onAdvanceStart(map, events, _time);
		
		SortedArrayList<Event> aux = new SortedArrayList<>();
		for (Event e : events) {
			if (e.getTime() == _time)
				e.execute(map);
			else
				aux.add(e);
		}
		events = aux;
		
		// TODO doubt
		try {
			for (Junction j : map.getJunctions())
				j.advance(_time);
			
			for (Road r : map.getRoads())
				r.advance(_time);
		}
		catch(Exception e) {
			tso.onError(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		tso.onAdvanceEnd(map, events, _time);
	}
	
	public void reset() {
		map.reset();
		events.clear();
		_time = 0;
		tso.onReset(map, events, _time);
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("time", _time);
		jo.put("state", map.report());
		
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		tso = o;
		tso.onRegister(map, events, _time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		// TODO doubt
		tso = null;
	}
}
