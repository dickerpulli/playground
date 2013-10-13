package de.tbosch.tools.googleapps;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tbosch.tools.googleapps.utils.MessageHelper;

/**
 * Handler for all exception occuring in AWT threads.
 * 
 * @author thomas.bosch
 */
public class AWTExceptionHandler {

	private static final Log LOG = LogFactory.getLog(AWTExceptionHandler.class);

	/**
	 * Handles exceptions thrown somewhere.
	 * 
	 * @param t
	 *            The exception
	 */
	public void handle(Throwable t) {
		LOG.info("exception thrown - handle exception in AWTExceptioHandler");
		String text = t.getClass().getCanonicalName() + " at " + t.getStackTrace()[0].getClassName() + ":"
				+ t.getStackTrace()[0].getLineNumber() + "\n" + t.getLocalizedMessage();
		JOptionPane.showMessageDialog(null, text, MessageHelper.getMessage("title.error"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Registers this Handler.
	 */
	public static void registerExceptionHandler() {
		LOG.info("registering AWTExceptioHandler");
		System.setProperty("sun.awt.exception.handler", AWTExceptionHandler.class.getName());
	}
}
