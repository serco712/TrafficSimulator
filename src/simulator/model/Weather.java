package simulator.model;

public enum Weather {
	
	SUNNY(2, 2), CLOUDY(3, 2), RAINY(10, 2), WINDY(15, 10), STORM(20, 10);
	
	int factIntCit;
	
	int factCit;
	
	private Weather (int factIntCit, int factCit) {
		this.factIntCit = factIntCit;
		this.factCit = factCit;
	}
	
	public int getFactIntCit() {
		return factIntCit;
	}

	public int getFactCit() {
		return factCit;
	}
}
