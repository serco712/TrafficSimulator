package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends Builder<Event>{
	
	private static final String TYPE = "new_city_road";
	
	NewCityRoadEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		return new NewCityRoadEvent(data.getInt("time"), data.getString("id"),
				);
	}	
}
