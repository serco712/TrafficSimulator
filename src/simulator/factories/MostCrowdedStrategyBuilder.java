package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int ts;
		
		if (data.get("timeslot") == null)
			ts = 1;
		else
			ts = data.getInt("timeslot");
		
		return new MostCrowdedStrategy(ts);
	}

}
