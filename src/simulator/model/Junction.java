package simulator.model;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> incomingRoad;
	private Map<Junction, Road> outgoingRoad;
	private List<List<Vehicle>> queue;
	private Map<Road, List<Vehicle>> road_queue;
	private int currGreen;
	private int lastSwitchingTime;
	private LightSwitchingStrategy switchStrategy;
	private DequeuingStrategy dequeStrategy;
	private int x;
	private int y;
	
	
	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) {
		super(id);
		if(lsStrategy == null || dqStrategy == null)
			throw new IllegalArgumentException("strategies must be not null.");
		else if (xCoor < 0 || yCoor < 0)
			throw new IllegalArgumentException("coords must be positive numbers.");
		switchStrategy = lsStrategy;
		dequeStrategy = dqStrategy;
		x = xCoor;
		y = yCoor;
		incomingRoad = new ArrayList<>();
		outgoingRoad = new HashMap<Junction, Road>();
		road_queue = new HashMap<Road, List<Vehicle>>();
		queue = new ArrayList<>();
		currGreen = -1;
	}

	@Override
	void advance(int time) {
		if((currGreen != -1) && (!queue.isEmpty()) && !queue.get(currGreen).isEmpty()) {
			List<Vehicle> aux = dequeStrategy.dequeue(queue.get(currGreen));
			
			for(Vehicle v : aux) {
				road_queue.get(v.getRoad()).remove(v);
				v.moveToNextRoad();
			}
			
			for(Vehicle ve : road_queue.get(incomingRoad.get(currGreen))) {
				if(aux.contains(ve)) {
					road_queue.get(incomingRoad.get(currGreen)).remove(ve);
			    }
			}
		}
		
		
		int change = switchStrategy.chooseNextGreen(incomingRoad, queue, currGreen, lastSwitchingTime, 
				time);
		
		if(change != currGreen) {
			currGreen = change;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONObject jo1 = new JSONObject();
		JSONArray jaV = new JSONArray();
		JSONArray ja = new JSONArray();
		
		jo.put("id", _id);
		if(currGreen == -1 || incomingRoad.isEmpty()) {
			jo.put("green", "none");
		}
		else {
			jo.put("green", incomingRoad.get(currGreen).getId());
		}
		
		for (Road r : incomingRoad) {
			if (road_queue.containsKey(r)) {
				jo1 = new JSONObject();
				jaV = new JSONArray();
				jo1.put("road", r.getId());
				for (Vehicle v : road_queue.get(r))
					jaV.put(v.getId());
				jo1.put("vehicles", jaV);
				ja.put(jo1);
			}
		}
		
		jo.put("queues", ja);
			
		return jo;
	}
	
	void addIncomingRoad(Road r) {
		if (!r.getDest().getId().equals(this._id))
			throw new IllegalArgumentException("the road must entry into this junction.");
		
		incomingRoad.add(r);
		List<Vehicle> aux = new ArrayList<>();
		
		queue.add(aux);
		
		road_queue.put(r, aux);
	}
	
	void addOutgoingRoad(Road r) {
		if(outgoingRoad.containsKey(r.getDest()) && r.getSrc().equals(this))
			throw new IllegalArgumentException("the road must go out of this junction.");
		
		outgoingRoad.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		road_queue.get(v.getRoad()).add(v);
	}
	
	Road roadTo (Junction j) {
		if (outgoingRoad.containsKey(j))
			return outgoingRoad.get(j);
		else
			return null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getGreenLightIndex() {
		return currGreen;
	}

	public List<Road> getInRoads() {
		// TODO check if unmodifiableList
		return Collections.unmodifiableList(incomingRoad);
	}
	
	public String getQueue(Road r) {
		String str = "[";
		
		str += "]";
		return str;
	}
}
