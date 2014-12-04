package ocean.gui.animatedobjects;

import java.awt.Graphics;
import java.awt.Polygon;

import ocean.gui.OceanCanvas;

/**
 * The ocean surface with waves.
 *
 * @see OceanCanvas
 */
public class Surface extends AnimationObject {
	protected int zeroline, bottom;
	protected double amplPhase;
	protected double phase;
	protected double amplitude;
	protected double waveLength;
	protected double waveLengthPhase;
	protected int pos;

	protected final int stepSize = 5; // Used when stepping through all x values.
	protected final double maxAmplitude = 5;
	protected final double amplPhaseFactor = .1;
	protected final double phaseFactor = .5;
	protected final double waveLengthPhaseFactor = .01;
	protected final double waveLengthAmplitude = 5;
	protected final double waveLengthZero = 10;

	/**
	 * Create the surface of an Ocean.
	 * 
	 * @param ocean
	 *            The ocean needing a surface.
	 */
	public Surface(OceanCanvas ocean) {
		super(ocean);

		phase = 0;
		amplPhase = 0;
		waveLengthPhase = 0;
		bottom = ocean.getWaterTop();
		zeroline = ocean.getSkyHeight() + ocean.getSurfaceHeight() / 3;
		tick();
	}

	/**
	 * Animate the surface. The surface never requests to be deleted.
	 *
	 * @return Always false.
	 */
	public void tick() {
		// System.err.println("Surface.tick() called");

		pos = (pos + 1) % 360;
		phase = phaseFactor * pos;
		amplPhase = amplPhaseFactor * pos;
		amplitude = maxAmplitude * Math.abs(Math.sin(amplPhase)) + 1;
		waveLengthPhase = pos * waveLengthPhaseFactor;
		waveLength = waveLengthAmplitude * Math.cos(waveLengthPhase) + waveLengthZero;
	}

	public boolean deleteNow() {
		return false;
	}

	/**
	 * Draw the surface.
	 *
	 * @param g
	 *            Draw into this Graphics object.
	 */
	public void paint(Graphics g) {
		int y = 0;
		Polygon p = new Polygon();

		// System.err.println("Surface.paint() called");

		p.addPoint(ocean.getWidthInPixels(), bottom);
		p.addPoint(0, bottom);
		for (int x = 0; x < ocean.getWidthInPixels(); x += stepSize) {
			y = (int) Math.round(amplitude * Math.sin(phase + x / waveLength)) + zeroline;
			p.addPoint(x, y);
		}
		p.addPoint(ocean.getWidthInPixels(), y);

		g.setColor(ocean.getWaterColor());
		g.fillPolygon(p);
	}
}
