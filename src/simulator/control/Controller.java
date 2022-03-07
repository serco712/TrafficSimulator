package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	
	public Controller (TrafficSimulator sim, Factory<Event> eventsFactory) {
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents (InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		if(!jo.has("events"))
			throw new IllegalArgumentException("The JSONObject doesn't match the model");
		
		JSONArray ja = jo.getJSONArray("events");
		
		for(int i = 0; i < ja.length(); i++) {
			sim.addEvent(eventsFactory.createInstance(ja.getJSONObject(i)));
		}
	}
	
	public void run (int n, OutputStream out) {
		PrintStream p = new PrintStream(out);

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		
		for(int i = 0; i < n; i++) {
			sim.advance();
			ja.put(sim.report());
		}
		
		jo.put("states", ja);
		p.print(jo.toString(2));
	}
	
	public void reset () {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
	
	public int getEventsNumber () {
		return 
	}
}
