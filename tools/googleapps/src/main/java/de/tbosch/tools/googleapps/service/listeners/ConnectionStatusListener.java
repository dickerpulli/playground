package de.tbosch.tools.googleapps.service.listeners;

/**
 * Listener to signal changes to the connection status.
 */
public interface ConnectionStatusListener {

	/**
	 * Signals an change in local status.
	 */
	void changed(boolean connected);

}