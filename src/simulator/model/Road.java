package simulator.model;

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
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id);
		if(maxSpeed < 0)
			throw new IllegalArgumentException("’maxspeed’ must be a positive number.");
		else if(contLimit < 0)
			throw new IllegalArgumentException("’contLimit’ must be a positive number.");
		else if(length < 0)
			throw new IllegalArgumentException("’length’ must be a positive number.");
		else if(srcJunc != null || destJunc != null)
			throw new IllegalArgumentException("the Junctions must be not null.");
		else if (weather != null)
			throw new IllegalArgumentException("’Weather’ must be not null.");
		origin = srcJunc;
		end = destJunc;
		limitSpeed = contLimit;
		this.maxSpeed = maxSpeed;
		conditions = weather;
		this.length = length;
	}


	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
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
		vehicles.add(v);
		// TODO order
	}
	
	void exit(Vehicle v) {
		List<Vehicle> aux;
		for(Vehicle ve : vehicles) {
			if(!ve.equals(v)) 
				aux.add(ve);
		}
		
		vehicles = aux;
	}
	
	void setWeather(Weather w) {
		if(w == null)
			throw new IllegalArgumentException("’weather’ must be not null.");
		conditions = w;
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("’contamination’ must be a positive number.");
		totalCO2 += c;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	int getLength() {
		return length;
	}
	
	Junction getDest() {
		return origin;
	}
	
	Junction getSrc() {
		return end;
	}
	
	Weather getWeather() {
		return conditions;
	}
	
	int getContLimit(){
		return alarm;
	}
	
	int getMaxSpeed() {
		return maxSpeed;
	}
	
	int getTotalCO2() {
		return totalCO2;
	}
	
	int getSpeedLimit() {
		return limitSpeed;
	}
	
	List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
}