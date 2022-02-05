package simulator.model;

import java.util.List;
import java.util.Map;

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
	}

	@Override
	void advance(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
