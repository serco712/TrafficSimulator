package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy> {

	public MoveAllStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		return new MoveAllStrategy();
	}

}
