package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadMap {
	
	private List<Junction> junctions;
	
	private List<Road> roads;
	
	private List<Vehicle> vehicles;
	
	private Map<String, Junction> junction_map;
	
	private Map<String, Road> road_map;
	
	private Map<String, Vehicle> vehicle_map;
	
	protected RoadMap () {
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
		junction_map = new HashMap<String, Junction>();
		road_map = new HashMap<String, Road>();
		vehicle_map = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(junctions.contains(j))
			throw new IllegalArgumentException("The junction exists");
		junctions.add(j);
		junction_map.put(j.getId(), j);
	}
}
