package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{
	
	private static final String TYPE = "new_city_road";
	
	NewCityRoadEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheRoad () {
		return new NewCityRoadEvent(time, _id, src, dest, length,
				co2, maxSpeed, weather);
	}	
}
