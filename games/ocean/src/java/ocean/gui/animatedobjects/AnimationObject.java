package ocean.gui.animatedobjects;

import java.awt.Graphics;

import ocean.gui.OceanCanvas;


/**
 * A class representing objects that are to be animated
 * and moved around.
 *
 * @see AnimationObjectList
 */
public abstract class AnimationObject
{
  protected OceanCanvas ocean;
  
  /**
   * Accept ticks. Use them to move around etc.
   */
  public abstract void tick();
  
  /**
   * Has the object expired?
   * @return True if the objects requests to be deleted.
   */
  public abstract boolean deleteNow();

  
  /**
   * Paint yourself.
   *
   * @param g Draw into this Graphics object.
   */
  public abstract void paint(Graphics g);

  /**
   * Create AnimationObject with reference to its Ocean.
   *
   * @param ocean Ocean this object is for.
   * @see OceanCanvas
   */
  public AnimationObject(OceanCanvas ocean)
  {
    this.ocean = ocean;
  }
}
