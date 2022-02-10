package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private static final String TYPE = "new_junction";
	
	private static final int X = 0;
	
	private static final int Y = 1;
	
	private Factory<LightSwitchingStrategy> lssFactory;
	
	private Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory,
			Factory<DequeuingStrategy> dqsFactory) {
		super(TYPE);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		JSONObject jo1 = data.getJSONObject("ls_strategy");
		JSONObject jo2 = data.getJSONObject("dq_strategy");
		
		int x = data.getJSONArray("coor").getInt(X);
		int y = data.getJSONArray("coor").getInt(Y);
		
		return new NewJunctionEvent(data.getInt("time"), data.getString("id"),
				lssFactory.createInstance(jo1), dqsFactory.createInstance(jo2),
				x, y); 
	}
	
}
