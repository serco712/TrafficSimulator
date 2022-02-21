package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	protected String id;
	
	protected String srcJunc;
	
	protected String destJunc;
	
	protected int maxSpeed;
	
	protected int contLimit;
	
	protected int length;
	
	protected Weather weather;
	
	protected Junction src;
	
	protected Junction dest;
	
	NewRoadEvent(int time, String id, String srcJunc, String destJunc, int maxSpeed,
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
		src = map.getJunction(srcJunc);
		dest = map.getJunction(destJunc);
		
		map.addRoad(createRoadObject());
	}
	
	abstract Road createRoadObject();
}
