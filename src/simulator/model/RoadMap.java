package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String, Junction> junction_map;
	private Map<String, Road> road_map;
	private Map<String, Vehicle> vehicle_map;
	
	protected RoadMap () {
		reset();
	}
	
	void addJunction(Junction j) {
		for(Junction ju : junctions) {
			if(ju.getId().equalsIgnoreCase(j.getId()))
				throw new IllegalArgumentException("The id exists");
		}
		
		junctions.add(j);
		junction_map.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		if (!junctions.contains(r.getSrc()) || !junctions.contains(r.getDest()))
			throw new IllegalArgumentException("The origin and the destiny do not exist");
		
		for(Road ro : roads) {
			if(ro.getId().equalsIgnoreCase(r.getId()))
				throw new IllegalArgumentException("The id exists");
		}
		
		roads.add(r);
		road_map.put(r.getId(), r);
	}
	
	
	void addVehicle(Vehicle v) {
		boolean check = false;
		
		for(Vehicle ve : vehicles) {
			if(ve.getId().equalsIgnoreCase(v.getId()))
				throw new IllegalArgumentException("The id exists");
		}
		
		for(int i = 0; i < v.getItinerary().size() - 1; i++) {
			if(v.getItinerary().get(i).roadTo(v.getItinerary().get(i + 1)) == null)
				check = true;
		}
		
		if(check)
			throw new IllegalArgumentException("The itinerary is not correct");
		
		vehicles.add(v);
		vehicle_map.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		for(Junction j : junctions)
			if(j.getId().equals(id))
				return j;
		return null;
	}
	
	public Road getRoad(String id) {
		for(Road r : roads)
			if(r.getId().equals(id))
				return r;
		return null;
	}
	
	public Vehicle getVehicle(String s) {
		for(Vehicle v : vehicles)
			if(v.getId().equals(s))
				return v;
		return null;
	}
	
	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(junctions);
	}
	
	public List<Road> getRoads() {
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
	
	void reset() {
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
		junction_map = new HashMap<String, Junction>();
		road_map = new HashMap<String, Road>();
		vehicle_map = new HashMap<String, Vehicle>();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for(Junction j : junctions) {
			ja.put(j.report());
		}
		jo.put("junctions", ja);
		
		ja = new JSONArray();
		for(Road r : roads) {
			ja.put(r.report());
		}
		jo.put("roads", ja);
		
		ja = new JSONArray();
		for(Vehicle v : vehicles) {
			ja.put(v.report());
		}
		jo.put("vehicles", ja);
		
		return jo;
	}
}
