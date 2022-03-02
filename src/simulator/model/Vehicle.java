package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	
	private List<Junction> itinerary;
	
	private int maxSpeed; 
	
	private int actSpeed;
	
	private VehicleStatus status;
	
	private Road road;
	
	private int location;
	
	private int contClass;
	
	private int totalCO2;
	
	private int totalDist;
	
	private int actJunct;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if (maxSpeed <= 0)
			throw new IllegalArgumentException("’maxspeed’ must be a positive number.");
		else if (contClass < 0 || contClass > 10)
			throw new IllegalArgumentException("’contclass’ must be a number between 0 and 10 both included.");
		else if (itinerary.size() < 2)
			throw new IllegalArgumentException("’itinerary size’ must be at least 2.");
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		status = VehicleStatus.PENDING;
		actJunct = 0;
	}

	@Override
	void advance(int time) {
		int aux = location;
		if(status == VehicleStatus.TRAVELING) {
			if(location + actSpeed > road.getLength()) {
				totalDist += (road.getLength() - location);
				location = road.getLength();
			}
				
			else {
				location += actSpeed;
				totalDist += actSpeed;
			}
				
			int c = (location - aux) * contClass;
			
			totalCO2 += c;
			road.addContamination(c);
			if(location >= road.getLength()) {
				road.getDest().enter(this);
				status = VehicleStatus.WAITING;
				setSpeed(0);
			}
		}
		else {
			actSpeed = 0;
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		jo.put("speed", actSpeed);
		jo.put("distance", totalDist);
		jo.put("co2", totalCO2);
		jo.put("class", contClass);
		jo.put("status", status.toString());
		
		if(!status.equals(VehicleStatus.ARRIVED) && road != null) {
			jo.put("road", road.getId());
			jo.put("location", location);
		}
		
		return jo;
	}
	
	void setSpeed(int s) {
		if (s < 0)
			throw new IllegalArgumentException("Speed s must be a positive number");
		
		if (status == VehicleStatus.TRAVELING) {
			if(s > maxSpeed) 
				actSpeed = maxSpeed;
			else 
				actSpeed = s;
		}
		else 
			actSpeed = 0;
	}
	
	void setContaminationClass(int c) {
		if (c < 0 || c > 10)
			throw new IllegalArgumentException("’contclass’ must be a number between 0 and 10 both included.");
		else
			contClass = c;
	}
	
	void moveToNextRoad() {
		if (status != VehicleStatus.PENDING && status != VehicleStatus.WAITING)
			throw new IllegalArgumentException("The car is neither pending nor waiting");
		
		if(road != null)
			road.exit(this);
		if (status.equals(VehicleStatus.PENDING) || status.equals(VehicleStatus.WAITING)) {
			if (actJunct == itinerary.size() - 1 || actJunct == itinerary.size()) {
				status = VehicleStatus.ARRIVED;
			}
			else {
				road = itinerary.get(actJunct).roadTo(itinerary.get(actJunct + 1));
				actJunct++;
				location = 0;
				road.enter(this);
				
				status = VehicleStatus.TRAVELING;
			}
		}
	}

	public int getLocation() {
		return location;
	}
	
	public int getSpeed() {
		return actSpeed;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getContClass() {
		return contClass;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}
	
	public int getTotalCO2() {
		return totalCO2;
	}
	
	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public static class OrderVehicle implements Comparator<Vehicle> {

		@Override
		public int compare(Vehicle v1, Vehicle v2) {
			if (v1.location < v2.location) 
				return 1;
			else if (v1.location > v2.location) 
				return -1;
			else 
				return 0;
		}
		
	}
}
