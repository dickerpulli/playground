package ocean.fish;

import ocean.gui.ImageData;

public class Herring extends RandomMovingFish implements Eatable {

	public Herring() {
		this(ImageData.HERRING);
	}

	public Herring(ImageData imageData) {
		super(imageData);
	}

	@Override
	public String toString() {
		return "This is a Herring";
	}

}
