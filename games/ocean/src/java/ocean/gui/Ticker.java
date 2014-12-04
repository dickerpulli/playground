package ocean.gui;

/**
 * A separate thread giving ticks.
 * 
 * @see OceanCanvas
 */
class Ticker extends Thread {
	/** The ocean we send ticks to. */
	OceanCanvas ocean;

	/** Die automatically if this ever turns to false. */
	boolean alive;

	/** for allowing suspending the ticker */
	boolean suspended;

	/**
	 * Create ticker to send ticks to an Ocean.
	 *
	 * @param ocean
	 *            The Ocean to send ticks to by calling its tick() method.
	 */
	Ticker(OceanCanvas ocean) {
		super();
		this.ocean = ocean;
		alive = true;
		suspended = false;
	}

	/**
	 * The body of the tread. Run as long as <tt>alive</tt> is true.
	 */
	public void run() {
		while (alive) {
			if (!this.suspended)
				ocean.tick();
			try {
				sleep(ocean.animationSleep);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Let the thread commit suicide.
	 */
	public synchronized void die() {
		alive = false;
	}

	public void mySuspend() {
		this.suspended = true;
	}

	public void myResume() {
		this.suspended = false;
	}
}
