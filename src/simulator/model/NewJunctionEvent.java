package simulator.model;

public class NewJunctionEvent extends Event {
	
	private String _id;
	
	private LightSwitchingStrategy lsStrategy;
	
	private DequeuingStrategy dqStrategy;
	
	private int xCoor;
	
	private int yCoor;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy
			lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		_id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}


	@Override
	void execute(RoadMap map) {
		map.addJunction(new Junction(_id, lsStrategy, dqStrategy, xCoor, yCoor));
	}
}
