package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private static final int x = 0;
	private static final int y = 1;

	public NewJunctionEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		JSONObject jo = data.getJSONObject("ls_strategy");
		
		int x = data.getJSONArray("coor").getInt(this.x);
		int y = data.getJSONArray("coor").getInt(this.y);
		
		
		
		
		Builder<LightSwitchingStrategy> b = new Builder<LightSwitchingStrategy>(jo.getString("type"));
		
	}
	
}
