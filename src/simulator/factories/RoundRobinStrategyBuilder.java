package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	private static final String TYPE = "round_robin_lss";
	
	public RoundRobinStrategyBuilder() {
		super(TYPE);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int ts;
		
		if (data.get("timeslot") == null)
			ts = 1;
		else
			ts = data.getInt("timeslot");
		
		return new RoundRobinStrategy(ts);
	}

}
