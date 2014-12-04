package ocean.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import ocean.core.Coordinate;
import ocean.core.FishMatrix;
import ocean.fish.Fish;
import ocean.gui.animatedobjects.AnimationObject;
import ocean.gui.animatedobjects.Bubble;
import ocean.gui.animatedobjects.Ship;
import ocean.gui.animatedobjects.Submarine;
import ocean.gui.animatedobjects.Surface;
import ocean.gui.animatedobjects.Weed;
import ocean.gui.animatedobjects.Whale;


/**
 * An ocean with plenty of fish and some nice goodies.
 *
 */
public class OceanCanvas extends Canvas
{
	/**
	 * not important ...
	 */
	private static final long serialVersionUID = 1L;
	
	FishWindow fishWindow;
	
	// Dimensions of each Fish tile.
	protected final int fishWidth = 32;
	protected final int fishHeight = 32;

	// Dimensions of various areas.
	protected final int surfaceHeight = 15;
	protected final int skyHeight = 50;
	protected final int bottomHeight = 25;
	protected final int groundHeight = 10;

	// Colour for the very basic parts of an ocean.
	protected final Color waterColor = new Color(.3F, .3F, 1F);
	protected final Color gridColor = new Color(.2F, .2F, .2F);
	protected final Color skyColor = Color.cyan;
	protected final Color groundColor = Color.yellow;

	// Bubbles rise to the surface...
	
	protected final double bubbleProbability = .03;
	
	// Ships cruise the ocean...
	protected final double shipProbability = .02;	

	// A submarine roams under water...
	protected final double submarineProbability = .00025;

	// There is a whale ... 
	protected final double whaleProbability = 0.001;

	// Sleep .2 seconds between each animation frame.
	protected final int animationSleep = 200;

	// Dimensions in numbers of Fish
	protected final int widthInFish, heightInFish;
	protected FishMatrix matrix;   // Matrix of our ocean.

	// Dimensions in pixels.
	protected final int widthInPixels, heightInPixels;

	// Height of the rectangular water are.
	protected final int waterHeight;

	// y coordinate of the water surface line.
	protected final int waterTop;

	// Foreground means in front of our fish.
	LinkedList<AnimationObject> foregroundObjects;
	LinkedList<AnimationObject> backgroundObjects;

	// Draw a grid?
	protected boolean grid;

	// Employ double buffering.
	protected Image bufferImage;
	protected Graphics bufferGraphics;

	// An external thread giving impulses (ticks).
	protected Ticker ticker;

	// True iff the ticker has been started.
	protected boolean tickerStarted = false;
	
	// the random number generator
	Random random = new Random();

	/**
	 * Construct an empty ocean-drawing area with the given dimension (measured in fish).
	 *
	 * @param fishWindow The Window commanding the Ocean.
	 * @param h Height in number of fish.
	 * @param w Width in number of fish.
	 */
	public OceanCanvas(FishWindow fishWindow, int h, int w)
	{
		super();
		
		this.fishWindow = fishWindow;
		
		widthInFish = w;
		heightInFish = h;

		grid = false;

		widthInPixels = widthInFish * fishWidth;
		waterHeight = heightInFish * fishHeight;
		waterTop = skyHeight + surfaceHeight;
		heightInPixels = waterHeight + waterTop + bottomHeight;
		setSize(widthInPixels, heightInPixels);

		backgroundObjects = new LinkedList<AnimationObject>();
		foregroundObjects = new LinkedList<AnimationObject>();

		/* Surface waves should alwas be last in the background list and 
       therefore it is always drawn above all other background objects. */
		backgroundObjects.addLast(new Surface(this));
		createWeed();

		/* Force creating of the bufferImage the first time
       paint() is called. */
		bufferImage = null;

		// We want to be informed of mouse clicks.
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				callFish(e);
				
				repaint();
			}
		});

		ticker = new Ticker(this);
	}

	/**
	 * Set new contents. The matrix automatically gets copied.
	 *
	 * @param newMatrix Matrix containing the fish or <tt>null</tt> for water squares.
	 */
	public synchronized void setMatrix(FishMatrix newMatrix)
	{
		matrix = newMatrix;
	}

	/**
	 * Set if you want a grid to be painted.
	 *
	 * @param b Do you want a grid?
	 */
	public synchronized void setGrid(boolean b)
	{
		grid = b;
	}

	/**
	 * Return the size measured in fish.
	 *
	 * @return Dimension in Fish.
	 */
	public Dimension getSizeInFish()
	{
		return new Dimension(widthInFish, heightInFish);
	}

	/**
	 * Start animations (use when applet becomes visible).
	 */
	public synchronized void start()
	{
		if(tickerStarted)
			ticker.myResume();
		else
		{
			ticker.start();
			tickerStarted = true;
		}
	}

	/**
	 * Stop animations (use when applet is scrolled off the screen)
	 */
	public synchronized void stop()
	{
		ticker.mySuspend();
	}

	/**
	 * Destroy all depending objects/tasks.
	 */
	public synchronized void destroy()
	{
		// Tell the ticker to commit suicide.
		ticker.die();
	}

	//-----------------------------------------------------------------

	/**
	 * Do an animation step.
	 */
	synchronized void tick()
	{
		createBubbleAtRandom();
		createShipAtRandom();
		createSubmarineAtRandom();
		createWhaleAtRandom();

		// Tick all AnimationObjects.
		for (Iterator<AnimationObject> iter = foregroundObjects.iterator(); iter.hasNext();) {
		      AnimationObject ao = iter.next();
		      ao.tick();
		      if (ao.deleteNow()) {
		    	iter.remove();  
		      }			
		}
		
		// Tick all AnimationObjects.
		for (Iterator<AnimationObject> iter = backgroundObjects.iterator(); iter.hasNext();) {
		      AnimationObject ao = iter.next();
		      ao.tick();
		      if (ao.deleteNow()) {
		    	iter.remove();  
		      }			
		}		

		// We could get a tick before anything is visible, so:
		if(bufferImage != null)
			redraw();
	}

	//-----------------------------------------------------------------

	/**
	 * Paint the ocean.
	 * Since we use double buffering we only have to redisplay the buffer.
	 * Create a buffer when called for the first time.
	 *
	 * @param g Graphics object to paint into.
	 */
	public void paint(Graphics g)
	{
		// Do we already have got a buffer?
		if(bufferImage == null)
		{
			// Create buffer.
			bufferImage = createImage(widthInPixels, heightInPixels);
			bufferGraphics = bufferImage.getGraphics();
			redraw();  // Draw contents.
		}

		g.drawImage(bufferImage, 0, 0, this);
	}

	/**
	 * Repaint everything. Does not clear the canvas before painting
	 * as it gets completely overwritten.
	 *
	 * @param g Graphics object to paint into.
	 */
	public void update(Graphics g)
	{
		paint(g);
	}

	/**
	 * Do the actual drawing into the buffer.
	 */
	protected void redraw()
	{
		
		// paint the background image and background objects
		paintBackground(bufferGraphics);
		if(grid)
			paintGrid(bufferGraphics);
		
		for (AnimationObject ao : backgroundObjects) {			
			ao.paint(bufferGraphics);
		}
		
		// paint the fish
		paintFish(bufferGraphics);
		
		// paint the foreground
		
		for (AnimationObject ao : foregroundObjects) {
			ao.paint(bufferGraphics);
		}

		repaint();
	}

	//--------------------------------------------------------------------

	/**
	 * Create a bubble -- perhaps.
	 */
	void createBubbleAtRandom()
	{
		// Really create?
		if(random.nextDouble() > bubbleProbability)
			return;

		//System.err.print("Creating bubble: ");

		// Choose a list randomly
		LinkedList<AnimationObject> list = random.nextDouble() < .5 ? backgroundObjects : foregroundObjects;

		// Choose bubble
		int kindOfBubble = random.nextInt(ImageData.BUBBLE.getAllImages().length);

		// Choose position
		int x = random.nextInt(widthInPixels + 20) - 10;
		int y = 9999; // auto choose position, that is, start at the deepest possible position 

		// Choose speed
		double speed = random.nextDouble() * 5 + 1;
		//double speed = random.nextDouble();

		list.addLast(new Bubble(this, ImageData.BUBBLE, x, y, speed, kindOfBubble));
	}

	//--------------------------------------------------------------------

	/**
	 * Create a nice set of plants for the ocean bottom.
	 */
	protected void createWeed()
	{
		int x;
		int type;
		int sleep;
				
		ImageData[] weeds = new ImageData[] {ImageData.WEED1, ImageData.WEED2, ImageData.WEED3 };
				
		for (int i = 0; i < fishWidth / 2; i++) {
			x = random.nextInt(this.getWidth() + 20) - 10;
			type = random.nextInt(3);
			sleep = random.nextInt(5) + 1;
			Weed weed = new Weed(this, weeds[type], x, sleep);			
			backgroundObjects.addFirst(weed);			
		}
	}

	//--------------------------------------------------------------------

	/**
	 * Create a ship if there isn't one visible at the moment and if
	 * Math.random() indicates it.
	 */
	protected void createShipAtRandom()
	{		
		if(Ship.getNumberOfShips() > 0 || random.nextDouble() > shipProbability)
			return;

		backgroundObjects.addFirst(new Ship(this, ImageData.SHIP, random.nextDouble() * 3 + 1));		
	}

	//--------------------------------------------------------------------

	/**
	 * Create a whale if Math.random() indicates it.
	 */
	protected void createWhaleAtRandom()
	{
		if(random.nextDouble() > whaleProbability)
			return;
		
		int amplitude = random.nextInt(waterHeight / 5);
		// lowest point should be a bit above the ground
		int zero = this.getHeight() - random.nextInt(waterHeight / 3) - 2*groundHeight - amplitude;
		zero -= ImageData.WHALE.getAllImages()[0].getHeight(null);		
		double wavelength = random.nextDouble() * 10 + 60;
		backgroundObjects.add(new Whale(this, ImageData.WHALE, zero, amplitude,
				wavelength, random.nextDouble() * 2 + .3));
	}

	//--------------------------------------------------------------------

	/**
	 * Create a submarine if there isn't once visible at the moment and if
	 * Math.random() indicates it.
	 */
	protected void createSubmarineAtRandom()
	{
		if(Submarine.getVisible() > 0 || random.nextDouble() > submarineProbability)
			return;

		int zero = random.nextInt(waterHeight - ImageData.SUBMARINE.getImage().getHeight(null) - 10) + 20; 				
		int amplitude = random.nextInt(Math.min(zero - 2, waterHeight - zero 
						- ImageData.SUBMARINE.getImage().getHeight(null) - 2));
		double wavelength = random.nextDouble() * 10 + 40;
		backgroundObjects.add(new Submarine(this, ImageData.SUBMARINE, zero + waterTop, 
				amplitude, wavelength,
				random.nextDouble() * 2 + .5));
	}

	//--------------------------------------------------------------------

	/**
	 * Paint the ocean background (including bottom).
	 *
	 * @param g Graphics object to paint into.
	 */   
	protected void paintBackground(Graphics g)
	{
		g.setColor(skyColor);
		g.fillRect(0, 0, widthInPixels, waterTop);

		g.setColor(waterColor);
		g.fillRect(0, waterTop, widthInPixels, 
				waterHeight + bottomHeight - groundHeight);

		g.setColor(groundColor);
		g.fillRect(0, heightInPixels - groundHeight, widthInPixels, groundHeight);
	}

	/**
	 * Paint the grid.
	 *
	 * @param g Graphics object to paint into.
	 */
	protected void paintGrid(Graphics g)
	{
		g.setColor(gridColor);

		int i, j, x, y;

		for(i = 0; i <= heightInFish; i++)
		{
			y = i * fishHeight + waterTop;
			g.drawLine(0, y, widthInPixels - 2, y);
		}

		for(j = 1; j < widthInFish; j++)
		{
			x = j * fishWidth;
			g.drawLine(x, waterTop, x, heightInPixels - bottomHeight);
		}
	}

	/**
	 * Paint all the <tt>Fish</tt> from the matrix.
	 *
	 * @param g Graphics object to paint into.
	 */
	protected void paintFish(Graphics g)
	{
		if (matrix == null)
			return;

		int screenX, screenY;
		int x, y;

		screenY = waterTop + 1;
		for(y = 0; y < heightInFish; y++) {
			screenX = 0;
			for(x = 0; x < widthInFish; x++) {
				Coordinate where = new Coordinate(x,y);
				
				// is there a fish?
				Fish fish = matrix.getFish(where);
				if (fish != null) 	{
					// Call the Fish's own paint() method.
					fish.draw(g, screenX, screenY);
				}
				screenX += fishWidth;
			}
			screenY += fishHeight;
		}
	}

	//--------------------------------------------------------------------

	/**
	 * Calculate column index at pixel x-coordinate
	 *
	 * @param x X-coordinate.
	 * @return Matrix column corresponding to x.
	 * @exception IndexOutOfBoundsException Thrown if coordinate is invalid.
	 */
	protected int columnAtX(int x) throws IndexOutOfBoundsException
	{
		if(x < 0 || x > widthInPixels)
			throw new IndexOutOfBoundsException("x coordinate invalid.");
		return x / fishWidth;
	}

	/**
	 * Calculate row index at pixel y-coordinate
	 *
	 * @param y Y-coordinate.
	 * @return Matrix row corresponding to y.
	 * @exception IndexOutOfBoundsException Thrown if coordinate is invalid.
	 */
	protected int rowAtY(int y) throws IndexOutOfBoundsException
	{
		if(y < waterTop || y > waterTop + waterHeight)
			throw new IndexOutOfBoundsException("y coordinate invalid.");
		return (y - waterTop) / fishHeight;
	}

	/**
	 * Call the doSomething of the Fish at the given mouse position
	 *
	 * @param ml The generated MouseListener.
	 */
	protected void callFish(MouseEvent m)
	{
		int x = m.getX(), y = m.getY();

		//System.err.println("Mouse clicked x = " + x + ", y = " + y);

		// Check if the the click actually hit the matrix region.
		if (y < waterTop || y > waterTop + waterHeight)
			return;

		int row = rowAtY(y);
		int column = columnAtX(x);			
		
		if (matrix != null) {
		  String s = matrix.doSomething(new Coordinate(column, row));
		  if (s != null)
		    fishWindow.setStatus(s);
		}
	}

	/** 
	 *  
	 * @return the water level in pixels
	 */
	public int getWaterTop() {
		return waterTop;
	}

	/**
	 * 
	 * @return the surface height in pixels
	 */
	public int getSurfaceHeight() {
		return surfaceHeight;
	}

	/**
	 * 
	 * @return the sky height in pixels
	 */
	public int getSkyHeight() {
		return skyHeight;
	}

	/**
	 * 
	 * @return width of the ocean in pixels
	 */
	public int getWidthInPixels() {
		return widthInPixels;
	}

	/**
	 * 
	 * @return the height of the ocean in pixels
	 */
	public int getHeightInPixels() {
		return heightInPixels;
	}

	/**
	 * 
	 * @return the color of the water
	 */
	public Color getWaterColor() {
		return waterColor;
	}

	/**
	 * 
	 * @return the width of one fish
	 */
	public int getFishWidth() {
		return fishWidth;
	}

	/**
	 * 
	 * @return the height of one fish
	 */
	public int getFishHeight() {
		return fishHeight;
	}

	/**
	 * 
	 * @return the height of the ground
	 */
	public int getGroundHeight() {
		return groundHeight;
	}  
}
