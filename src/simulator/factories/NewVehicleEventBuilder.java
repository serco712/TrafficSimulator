package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder <Event> {
	
	private static final String TYPE = "new_vehicle";
	public NewVehicleEventBuilder() {
		super(TYPE);
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		List<String> it = new ArrayList<>();
		
		for(int i = 0; i < data.getJSONArray("itinerary").length(); i++) {
			it.add(data.getJSONArray("itinerary").get(i).toString());
		}
		
		return new NewVehicleEvent(data.getInt("time"), data.getString("id"),
				data.getInt("maxspeed"), data.getInt("class"), it);
	}

}
