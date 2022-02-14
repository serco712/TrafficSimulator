package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {
	
	private static final String TYPE = "set_cont_class";
	
	SetContClassEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Integer>> cc = new ArrayList<>();

		for(int i = 0; i < data.getJSONArray("info").length(); i++) {
			JSONObject jo = (JSONObject) data.getJSONArray("info").get(i);
			
			Pair<String, Integer> p = new Pair<>(jo.getString("vehicle"), 
					jo.getInt("class"));
			
			cc.add(p);
		}
		
		return new SetContClassEvent(data.getInt("time"), cc);
	}

}
