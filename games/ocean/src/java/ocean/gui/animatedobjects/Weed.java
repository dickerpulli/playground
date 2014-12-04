package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.ImageData;
import ocean.gui.OceanCanvas;


/**
 * Weed growing on the ocean bottom.
 *
 * @see OceanCanvas
 */
public class Weed extends AnimationObject
{
  protected int x;
  protected ImageData imageData;
  protected int frame;
  protected int sleep;
  protected int count;

  static protected int visibleCount = 0;

  /**
   * Create weed with the given images, coordinate and sleep value.
   *
   * @param ocean The ocean to exist in.
   * @param images The images to loop through.
   * @param x The x coordinate of the weed.
   * @param sleep Number of ticks to wait before the next image is displayed.
   */  
  public Weed(OceanCanvas ocean, ImageData imageData, int x, int sleep)
  {
    super(ocean);
    this.imageData = imageData;
    frame = 0;
    this.sleep = sleep;
    count = 0;
    this.x = x;
    visibleCount++;
  }
     
  /**
   * Animate weed.
   * Never request to be deleted.
   *
   * @return Always false.
   */
  public void tick()
  {
    if(++count >= sleep)
      {
	count = 0;
	frame = (frame + 1) % imageData.getLength();
      }    
  }
  
  public boolean deleteNow() {
	  return false;  
  }
  
  /**
   * Draw weed.
   *
   * @param g Draw into this Graphics object.
   */
  public void paint(Graphics g)
  {
    g.drawImage(imageData.getAllImages()[frame], x, 
		ocean.getHeightInPixels() - ocean.getFishHeight() 
		- ocean.getGroundHeight() / 2, null);
  }

  /**
   * Return the number of weed instances.
   *
   * @return Number of weed instances.
   */
  static int getVisible()
  {
    return visibleCount;
  }
}
