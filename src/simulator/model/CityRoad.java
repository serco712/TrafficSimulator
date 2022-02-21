package simulator.model;

public class CityRoad extends Road {
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
		origin.addOutgoingRoad(this);
		end.addIncomingRoad(this);
	}

	@Override
	void reduceTotalContamination() {
		int x = conditions.getFactCit();
		if(totalCO2 - x < 0) 
			totalCO2 = 0;
		else 
			totalCO2 -= x;
	}

	@Override
	void updateSpeedLimit() {
		limitSpeed = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass()) * limitSpeed) / 11;
	}
}
