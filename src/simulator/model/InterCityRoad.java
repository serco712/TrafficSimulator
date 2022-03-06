package simulator.model;

public class InterCityRoad extends Road {
	
	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = conditions.getFactIntCit();
		totalCO2 = ((100 - x)* totalCO2) / 100;
	}

	@Override
	void updateSpeedLimit() {
		if (totalCO2 > alarm)
			limitSpeed = maxSpeed /2;
		else
			limitSpeed = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(conditions.equals(Weather.STORM))
			return (limitSpeed * 8) / 10;
		else
			return limitSpeed;
	}
}
