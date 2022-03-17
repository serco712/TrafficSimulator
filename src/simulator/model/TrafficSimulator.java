package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap map;
	private SortedArrayList<Event> events;
	private int _time;
	// TODO doubt
	private List<TrafficSimObserver> tso;
	
	public TrafficSimulator() {
		map = new RoadMap();
		events = new SortedArrayList<>();
		tso = new ArrayList<>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		for(TrafficSimObserver ts : tso)
			ts.onEventAdded(map, events, e, _time);
	}
	
	public void advance() {
		_time++;
		
		for(TrafficSimObserver ts : tso)
			ts.onAdvanceStart(map, events, _time);
		
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
			for(TrafficSimObserver ts : tso)
				ts.onError(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		for(TrafficSimObserver ts : tso)
			ts.onAdvanceEnd(map, events, _time);
	}
	
	public void reset() {
		map.reset();
		events.clear();
		_time = 0;
		for(TrafficSimObserver ts : tso)
			ts.onReset(map, events, _time);
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("time", _time);
		jo.put("state", map.report());
		
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		tso.add(o);
		for(TrafficSimObserver ts : tso)
			ts.onRegister(map, events, _time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		List<TrafficSimObserver> aux = new ArrayList<>();
		for(TrafficSimObserver ts : tso) 
			if(!ts.equals(o))
				aux.add(ts);
		tso = aux;
	}
}
