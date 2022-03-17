package simulator.model;

public enum Weather {
	
	SUNNY(2, 2, "resources/icons/sun.png"), 
	CLOUDY(3, 2, "resources/icons/cloud.png"), 
	RAINY(10, 2, "resources/icons/rain.png"), 
	WINDY(15, 10, "resources/icons/wind.png"), 
	STORM(20, 10, "resources/icons/storm.png");
	
	private int factIntCit;
	
	private int factCit;
	
	private String file;
	
	private Weather (int factIntCit, int factCit, String file) {
		this.factIntCit = factIntCit;
		this.factCit = factCit;
		this.file = file;
	}
	
	public int getFactIntCit() {
		return factIntCit;
	}

	public int getFactCit() {
		return factCit;
	}
	
	public String getFile() {
		return file;
	}
}
