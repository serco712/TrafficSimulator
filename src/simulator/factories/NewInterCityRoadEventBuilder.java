package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {
	
	private static final String TYPE = "new_inter_city_road";
	
	public NewInterCityRoadEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheRoad() {
		return new NewInterCityRoadEvent(time, _id, src, dest, length,
				co2, maxSpeed, weather);
	}	
}
