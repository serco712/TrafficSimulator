package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> ws;
	
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if (ws == null)
			throw new IllegalArgumentException("Weather list must be a not null list");
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> p : ws) {
			if(map.getRoad(p.getFirst()) == null)
				throw new IllegalArgumentException("The specified road doesn't exist");
			map.getRoad(p.getFirst()).setWeather(p.getSecond());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Change Weather: [");
		for(Pair<String, Weather> c : ws) {
			str.append("(" + c.getFirst() + "," + c.getSecond() + "), ");
		}
		if(str.length() > 17)
			str.setLength(str.length() - 2);
		str.append("]");
		return str.toString();
	}
}
