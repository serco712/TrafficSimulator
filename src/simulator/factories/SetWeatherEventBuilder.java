package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {
	
	private static final String TYPE = "set_weather";
	
	SetWeatherEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Weather>> ws = new ArrayList<>();

		for(int i = 0; i < data.getJSONArray("info").length(); i++) {
			JSONObject jo = (JSONObject) data.getJSONArray("info").get(i);
			
			Pair<String, Weather> p = new Pair<>(jo.getString("road"), 
					Weather.valueOf(jo.getString("weather")));
			
			ws.add(p);
		}
		
		return new SetWeatherEvent(data.getInt("time"), ws);
	}

}
