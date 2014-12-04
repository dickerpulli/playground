package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;


/**
 * A ship breaking the waves on our ocean.
 *
 * @see OceanCanvas
 */
public class Ship extends AnimationObject
{
	protected double speed;
	protected double x;
	protected final int y;
	protected int frame;
	protected int count;
	protected ImageData imageData;

	protected final int shipFrameSleep = 4;

	private static int numberOfShips = 0; 


	/**
	 * Create a ship with the given animation frames and speed.
	 * The ship enters the screen on the left hand side.
	 *
	 * @param ocean The ocean to sail.
	 * @param images Array of images to loop through.
	 * @param speed Ship speed.
	 */
	public Ship(OceanCanvas ocean, ImageData imageData, double speed)
	{
		super(ocean);
		this.imageData = imageData;
		this.speed = speed;
		frame = 0;
		x = -imageData.getAllImages()[0].getWidth(null);
		y = ocean.getWaterTop() - 3 - imageData.getAllImages()[frame].getHeight(null);

		numberOfShips++;
	}

	/**
	 * Move and animate the ship.
	 * Request deletion when the right hand side of the screen has been reached.
	 *
	 * @return True when the right hand side of the screen has been reached.
	 */
	public void tick()
	{
		x += speed;
		if(++count >= shipFrameSleep)
			frame = ( frame + 1) % imageData.getLength();

	}

	public boolean deleteNow() {

		if (x >= ocean.getWidthInPixels()) {
			numberOfShips--;
			return true;
		}
		return false;
	}

	/**
	 * Draw the ship.
	 *
	 * @param g Draw into this Graphics object.
	 */
	public void paint(Graphics g)
	{
		g.drawImage(imageData.getAllImages()[frame], (int)Math.round(x), y, null);
	}

	public static int getNumberOfShips() {
		// TODO Auto-generated method stub
		return numberOfShips;
	}

}
