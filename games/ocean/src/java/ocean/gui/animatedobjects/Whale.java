package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;


/**
 * A neat whate moving under water
 *
 * @see OceanCanvas
 */
public class Whale extends AnimationObject
{
  protected int zeroline;
  protected double waveLength;
  protected int amplitude;
  protected double speed;
  protected double x;
  protected int y;
  protected ImageData imageData;
  protected int frame = 0;

  protected final int frameSleep = 1;
  protected int count;

  static protected int visibleCount = 0;

  /**
   * Create whale with the given images, start coordinate,
   * amplitude, wavelength (of the motion) and speed.
   * The whale enters the screen on the right hand side.
   *
   * @param ocean Ocean the submarine has to move in.
   * @param images Array of images to be drawn.
   * @param y Initial y coordinate.
   * @param amplitude Amplitude of the sinus motion.
   * @param wavlength Wave length of the sinus motion.
   * @param speed Speed (x direction) of the submarine.
   */
  public Whale(OceanCanvas ocean, ImageData imageData, int y, int amplitude, 
	    double waveLenght, double speed)
  {
    super(ocean);

    this.imageData = imageData;
    this.speed = speed;
    this.amplitude = amplitude;
    this.zeroline = this.y = y;
    this.x = ocean.getWidthInPixels();
    this.waveLength = waveLenght;
    frame = 0;
    count = 0;
    visibleCount++;
  }

  /**
   * Move a whale a little bit.
   * Request deletion when it reaches the left hand side of the screen.
   *
   * @return True if it wants to be deleted.
   */   
  public void tick()
  {
	  x -= speed;
	  y = zeroline + (int)(Math.round(amplitude * Math.cos(x / waveLength)));
	    
	    if(++count > frameSleep)
	      {
		count = 0;
		frame = (frame + 1) % imageData.getLength();
	      }
	    
   
  }
  
  public boolean deleteNow() {
	  if(x <= -imageData.getAllImages()[0].getWidth(null))
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
    g.drawImage(imageData.getAllImages()[frame], (int)Math.round(x), y, null);
  }

  /**
   * Return the number of visible submarines.
   *
   * @return Number of visible submarines.
   */
  static int getVisible()
  {
    return visibleCount;
  }
}
