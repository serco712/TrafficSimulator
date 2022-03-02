package simulator.model;

import java.util.ArrayList;
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
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
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
				v.moveToNextRoad();
			}
			
			for(Iterator<Vehicle> iterator = queue.get(currGreen).iterator(); iterator.hasNext();) {
			    Vehicle ve = iterator.next();
			    if(aux.contains(ve)) {
			        iterator.remove();
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
		
		
		for(Road r : incomingRoad) {
			for (Map.Entry<Road,List<Vehicle>> m : road_queue.entrySet()) {
				if(m.getKey().getId().equalsIgnoreCase(r.getId())) {
					jo1 = new JSONObject();
					jo1.put("road", m.getKey().getId());
					for(Vehicle v : m.getValue()) {
						jaV.put(v.getId());
					}
					jo1.put("vehicles", jaV);
		     		ja.put(jo1);
				}
			}
		}
		
		for(Vehicle v : queue.get(currGreen)) {
			jo1 = new JSONObject();
		}
		
		
		jo.put("queues", ja);
			
		return jo;
	}
	
	void addIncomingRoad(Road r) {
		if (!r.getDest().getId().equals(this._id))
			throw new IllegalArgumentException("the road must entry into this junction.");
		
		incomingRoad.add(r);
		List<Vehicle> aux = new ArrayList<>();
		for(Vehicle v : r.getVehicles()) {
			aux.add(v);
		}
		queue.add(aux);
		
		road_queue.put(r, aux);
	}
	
	void addOutgoingRoad(Road r) {
		if(outgoingRoad.containsKey(r.getDest()) && r.getSrc().equals(this))
			throw new IllegalArgumentException("the road must go out of this junction.");
		
		outgoingRoad.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		for (Map.Entry<Road,List<Vehicle>> m : road_queue.entrySet())
            if(m.getKey().equals(v.getRoad()))
            	m.getValue().add(v);
	}
	
	Road roadTo (Junction j) {
		for (Map.Entry<Junction,Road> m : outgoingRoad.entrySet())
            if(m.getKey().equals(j))
            	return m.getValue();
		return null;
	}
}
