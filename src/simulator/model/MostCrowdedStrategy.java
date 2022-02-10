package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy (int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.size() == 0)
			return -1;
		else if(currGreen == -1) {
			int max = 0, ind = 0, indmax = 0;
			for(List<Vehicle> lv : qs) {
				if (lv.size() > max) {
					max = lv.size();
					indmax = ind;
				}
				ind++;
			}
			return indmax;
		}
		else if(currTime-lastSwitchingTime <timeSlot)
			return currGreen;
		else {
			int i = (currGreen + 1) % roads.size();
			int max = 0, indmax = 0;
			do {
				if(qs.get(i).size() > max) {
					max = qs.get(i).size();
					indmax = i;
				}
				i = (i + 1) % roads.size();
			}
			while (i != currGreen);
			
			return indmax;
		}
	}
}
