package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	protected String id;
	
	protected Junction srcJunc;
	
	protected Junction destJunc;
	
	protected int maxSpeed;
	
	protected int contLimit;
	
	protected int length;
	
	protected Weather weather;
	
	NewRoadEvent(int time, String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.maxSpeed = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		map.addRoad(createRoadObject());
	}
	
	abstract Road createRoadObject();
}
