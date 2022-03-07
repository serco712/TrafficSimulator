package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	protected Junction origin;
	protected Junction end;
	protected int length;
	protected int maxSpeed;
	protected int limitSpeed;
	protected int alarm;
	protected Weather conditions;
	protected int totalCO2;
	protected List<Vehicle> vehicles;
	
	
	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id);
		if(maxSpeed <= 0)
			throw new IllegalArgumentException("’maxspeed’ must be a positive number.");
		else if(contLimit < 0)
			throw new IllegalArgumentException("’contLimit’ must be a positive number.");
		else if(length <= 0)
			throw new IllegalArgumentException("’length’ must be a positive number.");
		else if(srcJunc == null || destJunc == null)
			throw new IllegalArgumentException("the Junctions must be not null.");
		else if (weather == null)
			throw new IllegalArgumentException("’Weather’ must be not null.");
		origin = srcJunc;
		end = destJunc;
		alarm = contLimit;
		this.maxSpeed = maxSpeed;
		limitSpeed = maxSpeed;
		conditions = weather;
		this.length = length;
		vehicles = new ArrayList<>();
		
		origin.addOutgoingRoad(this);
		end.addIncomingRoad(this);
	}


	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		
		
		vehicles.sort(new Vehicle.OrderVehicle());
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for(Vehicle v : vehicles) {
			ja.put(v.getId());
		}
		
		jo.put("id", _id);
		jo.put("speedlimit", limitSpeed);
		jo.put("weather", conditions.toString());
		jo.put("co2", totalCO2);
		jo.put("vehicles", ja);
		
		return jo;
	}
	
	void enter(Vehicle v) {
		if (v.getLocation() != 0 && v.getSpeed() != 0) 
			throw new IllegalArgumentException("Location and speed must be 0");
		
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		List<Vehicle> aux = new ArrayList<>();
		for(Vehicle ve : vehicles) {
			if(!ve.equals(v)) 
				aux.add(ve);
		}
		
		vehicles = aux;
	}
	
	void setWeather(Weather w) {
		if(w == null)
			throw new IllegalArgumentException("weather must be not null.");
		conditions = w;
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("contamination� must be a positive number.");
		totalCO2 += c;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	public int getLength() {
		return length;
	}
	
	public Junction getDest() {
		return end;
	}
	
	public Junction getSrc() {
		return origin;
	}
	
	public Weather getWeather() {
		return conditions;
	}
	
	public int getContLimit(){
		return alarm;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getTotalCO2() {
		return totalCO2;
	}
	
	public int getSpeedLimit() {
		return limitSpeed;
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
}
