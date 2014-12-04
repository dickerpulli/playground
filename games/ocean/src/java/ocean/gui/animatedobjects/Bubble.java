package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;

/**
 * A bubble rising to the ocean surface.
 *
 * @see OceanCanvas
 */
public class Bubble extends AnimationObject {
	protected int x;
	protected double y;
	protected double speed;
	protected int kindOfBubble;
	protected ImageData imageData;

	/**
	 * Create bubble with image, initial coordinates and speed.
	 *
	 * @param ocean
	 *            The ocean to bubble in.
	 * @param image
	 *            The image to draw.
	 * @param x
	 *            The initial x coordinate.
	 * @param y
	 *            The initial y coordinate.
	 * @param s
	 *            The initial speed.
	 */

	public Bubble(OceanCanvas ocean, ImageData imageData, int x, double y, double s, int kindOfBubble) {
		super(ocean);
		this.imageData = imageData;
		if (kindOfBubble < 0 || kindOfBubble >= imageData.getAllImages().length)
			throw new IndexOutOfBoundsException("This type of bubble does not exist!");
		this.kindOfBubble = kindOfBubble;

		this.x = x;

		// the position of the bubble is never deeper than the bottom of the water
		this.y = Math.min(y, ocean.getHeight() - ocean.getGroundHeight());
		this.speed = s;
	}

	/**
	 * Move the bubble. Request deletion when the surface has been reached.
	 *
	 * @return True when the surface has been reached.
	 */
	public void tick() {
		// System.err.println("Tick bubble");
		y -= speed;

	}

	public boolean deleteNow() {
		return y < ocean.getWaterTop();
	}

	/**
	 * Draw the bubble.
	 *
	 * @param g
	 *            Draw into this Graphics object.
	 */
	public void paint(Graphics g) {
		// System.err.println("Paint bubble");
		g.drawImage(imageData.getAllImages()[kindOfBubble], x, (int) Math.round(y), null);
	}

}
