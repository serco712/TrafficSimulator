package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {

	private List<Pair<String,Integer>> cs;
	
	
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if (cs == null)
			throw new IllegalArgumentException("Contamination list must be a not null list");
		
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> c : cs) {
			if(map.getVehicle(c.getFirst()) == null)
				throw new IllegalArgumentException("The specified vehicle doesn't exist");
			map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond());;
		}
	}
	

}
