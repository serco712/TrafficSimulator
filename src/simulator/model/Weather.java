package simulator.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Weather {
	
	SUNNY(2, 2, "sun.png"), 
	CLOUDY(3, 2, "cloud.png"), 
	RAINY(10, 2, "rain.png"), 
	WINDY(15, 10, "wind.png"), 
	STORM(20, 10, "storm.png");
	
	private int factIntCit;
	
	private int factCit;
	
	private String str;
	
	private Image ima;
	
	private Weather (int factIntCit, int factCit, String ima) {
		this.factIntCit = factIntCit;
		this.factCit = factCit;
		this.str = ima;
		this.ima = loadImage(str);
	}
	
	public int getFactIntCit() {
		return factIntCit;
	}

	public int getFactCit() {
		return factCit;
	}
	
	public Image getImage() {
		return ima;
	}
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
}
