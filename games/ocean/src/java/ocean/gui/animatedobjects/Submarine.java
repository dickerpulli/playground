package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;


/**
 * A neat little submarine moving along a sinus curve.
 *
 * @see OceanCanvas
 */
public class Submarine extends AnimationObject
{
  protected int zeroline;
  protected double waveLength;
  protected int amplitude;
  protected double speed;
  protected double x;
  protected int y;
  protected ImageData imageData;

  static protected int visibleCount = 0;

  /**
   * Create submarine with the given image, start coordinate,
   * amplitude, wavelength (of the motion) and speed.
   * The submarine enters the screen on the right hand side.
   *
   * @param ocean Ocean the submarine has to move in.
   * @param image Image to be drawn.
   * @param y Initial y coordinate.
   * @param amplitude Amplitude of the sinus motion.
   * @param wavlength Wave length of the sinus motion.
   * @param speed Speed (x direction) of the submarine.
   */
  public Submarine(OceanCanvas ocean, ImageData imageData, int y, int amplitude, 
	    double waveLenght, double speed)
  {
    super(ocean);

    this.imageData = imageData;
    this.speed = speed;
    this.amplitude = amplitude;
    this.zeroline = this.y = y;
    this.x = ocean.getWidthInPixels();
    this.waveLength = waveLenght;
    visibleCount++;
  }

  /**
   * Move a submarine a little bit.
   * Request deletion when it reaches the left hand side of the screen.
   *
   * @return True if it wants to be deleted.
   */   
  public void tick()
  {
	x  -= speed;
    y = zeroline + (int)(Math.round(amplitude * Math.cos(x / waveLength)));    
  }

  public boolean deleteNow() {
	  if (x  <= -imageData.getImage().getWidth(null))
	  {
		  visibleCount--;
		  return true;
	  }
	  return false;

  }
  
  /**
   * Draw the submarine.
   *
   * @param g Draw into this Graphics object.
   */
  public void paint(Graphics g)
  {
    g.drawImage(imageData.getImage(), (int)Math.round(x), y, null);
  }

  /**
   * Return the number of visible submarines.
   *
   * @return Number of visible submarines.
   */
  public static int getVisible()
  {
    return visibleCount;
  }
}
