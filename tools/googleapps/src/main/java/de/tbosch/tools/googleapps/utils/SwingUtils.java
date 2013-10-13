package de.tbosch.tools.googleapps.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Utilities for Swing Components.
 * 
 * @author thomas.bosch
 */
public class SwingUtils {

	private SwingUtils() {
		// utilities
	}

	/**
	 * Centers the given window on the screen.
	 * 
	 * @param window
	 *            The window
	 */
	public static void centerOnScreen(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (screenSize.getWidth() - window.getWidth()) / 2;
		int y = (int) (screenSize.getHeight() - window.getHeight()) / 2;
		window.setLocation(x, y);
	}

}
