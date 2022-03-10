package simulator.model;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	public Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		if (_time < o._time)
			return -1;
		else if (_time > o._time)
			return 1;
		else
			return 0;
	}

	abstract void execute(RoadMap map);
}
