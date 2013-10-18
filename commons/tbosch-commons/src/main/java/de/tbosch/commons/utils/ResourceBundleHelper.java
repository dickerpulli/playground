package de.tbosch.commons.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper für ResourceBundle und MessageFormat
 */
public class ResourceBundleHelper {

	/**
	 * Geladenes ResourceBundle
	 */
	private final ResourceBundle resourceBundle;

	/**
	 * Konstruktor. Lädt ResourceBundle. Es wird das Default-Locale der JVM verwendt
	 * 
	 * @param name
	 *            Name des ResourceBundles
	 */
	public ResourceBundleHelper(final String name) {
		resourceBundle = ResourceBundle.getBundle(name);
	}

	/**
	 * Konstruktor. Lädt ResourceBundle.
	 * 
	 * @param name
	 *            Name des ResourceBundles
	 * @param locale
	 *            Zu verwendendes Locale
	 */
	public ResourceBundleHelper(final String name, final Locale locale) {
		resourceBundle = ResourceBundle.getBundle(name, locale);
	}

	/**
	 * Liefert Text aus ResourceBundle zusammen Parameterauflösung wie bei {@link MessageFormat}.
	 * 
	 * @param key
	 * @param arguments
	 * 
	 * @return Text
	 */
	public String getText(String key, Object... arguments) {
		final String pattern = resourceBundle.getString(key);
		final String text = MessageFormat.format(pattern, arguments);
		return text;
	}

	/**
	 * @return
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

}
