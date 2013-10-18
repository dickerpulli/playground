package de.tbosch.tools.jsudoku;

import javax.swing.SwingUtilities;

import de.tbosch.tools.jsudoku.gui.JSudokuView;

/**
 * The main class of the application.
 */
public class JSudokuStarter {

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		/* Create and display the form */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JSudokuView().setVisible(true);
			}
		});
	}
}
