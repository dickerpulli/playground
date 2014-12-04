package ocean.fish;

import java.awt.Graphics;

import ocean.core.Coordinate;
import ocean.core.Direction;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;

/**
 * An abstract fish class with provision for obtaining mouse clicks.
 *
 * @see OceanCanvas
 * @author Alexander Schwartz, Daniel Dressler
 */
public abstract class Fish {
	/** The image to draw. */
	protected ImageData imageData;

	/*** constructors ***/

	/**
	 * Constructs a fish with a specific image.
	 */
	public Fish(ImageData imageData) {
		this.imageData = imageData;
	}

	/*** methods ***/

	/**
	 * The strategy method: Returns the direction of this fish in the next step, based on the given matrix. It should
	 * not change the matrix!
	 *
	 * @param from
	 *            The current coordinate of this fish
	 * @param matrix
	 *            The FishMatrix to be examined.
	 * 
	 * @return a Direction (enum-Type)
	 */
	public abstract Direction getDirection(Coordinate from, FishMatrix matrix);

	/**
	 * Returns any useful information about this fish.
	 */
	public String toString() {
		return "Just an abstract fish.";
	}

	/**
	 * Does something special for this fish.
	 * 
	 * @param matrix
	 *            The FishMatrix, which may also be changed in this method.
	 * @return A string describing what happened (to be displayed to the user)
	 */
	public String doSomething(FishMatrix matrix) {
		return "Nothing happened.";
	}

	/**
	 * Draw an image of the fish at the given position (upper left corner).
	 *
	 * @param g
	 *            current Graphics object
	 * @param x
	 *            x-coordinate in pixels
	 * @param y
	 *            y-coordinate in pixels
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(imageData.getImage(), x, y, null);
	}

} // class Fish
