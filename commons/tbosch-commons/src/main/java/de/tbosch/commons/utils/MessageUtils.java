package de.tbosch.commons.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

/**
 * Hilfsklasse für das Auslesen von Nachrichten aus einem MessageBundle
 * 
 */
public class MessageUtils {

	/**
	 * Ein leerer privater Konstruktor, da es sich um eine Utility Klasse handelt.
	 */
	private MessageUtils() {
		// Gewollt leer
	}

	/**
	 * Liest eine Nachricht mit dem übergebene Key aus dem ResourceBundle und gibt ihn mit dem eingebetten
	 * Applikationsnamen zurück.
	 * 
	 * @param messageKey Der Key in der Properties-Datei
	 * @param applikationsName Der Name der Applikation
	 * @param bundleName Das ResourceBundle
	 * @param messageParameter Das Array von Parametern für die Nachricht
	 * @return Die Nachricht
	 */
	public static String leseNachrichtAusResourceBundle(String messageKey, String applikationsName, String bundleName,
			Object... messageParameter) {
		String message = messageKey;

		// Locale locale = (Locale) Context.getContext().get(Context.LOCALE);
		Locale locale = null;

		// Wenn keine Local angegeben wurde, dann default Locale nutzen
		// if (locale == null) {
		locale = Locale.getDefault();
		// }

		// Wenn die Exception weiss, aus welchem Projekt sie kommt, suchen
		// wir den Key im entsprechenden ResourceBundle, sonst im aktuellen
		// Projekt
		// if (applikationsName == null) {
		// applikationsName = ((String)
		// Context.getContext().get(Context.SERVLET_CONTEXT_NAME));
		// }

		boolean translationFound = false;

		// Erst die Nachricht aus den Defaults versuchen zu lesen
		try {
			ResourceBundle messageResourceBundle = PropertyResourceBundle.getBundle(bundleName, locale);
			if (messageResourceBundle.containsKey(messageKey)) {
				message = messageResourceBundle.getString(messageKey);
				translationFound = true;
			}

			// Die Nachricht überschreiben, wenn es eine Applikationsspezifische
			// Nachricht gibt
			if (applikationsName != null && applikationsName.length() > 0) {
				final String bundleNameExtended = bundleName + "-" + applikationsName;
				ResourceBundle applicationResourceBundle = ResourceBundle.getBundle(bundleNameExtended, locale);
				if (applicationResourceBundle.containsKey(messageKey)) {
					translationFound = true;
					message = applicationResourceBundle.getString(messageKey);
				}
			}

			// Wenn Message Parameter vorhanden sind, dann werden diese in der
			// Message ersetzt
			if (messageParameter != null) {
				if (translationFound) {
					message = MessageFormat.format(message, messageParameter);
				}
				else {
					message = erzeugeNachrichtOhneUebersetzung(messageKey, messageParameter);
				}
			}
		}
		catch (MissingResourceException e) {
			message = erzeugeNachrichtOhneUebersetzung(messageKey, messageParameter);
		}
		return message;
	}

	/**
	 * Liest eine Nachricht mit dem übergebene Key aus dem ResourceBundle und gibt ihn zurück.
	 * 
	 * @param messageKey Der Key in der Properties-Datei
	 * @param bundleName Das ResourceBundle
	 * @param messageParameter Das Array von Parametern für die Nachricht
	 * @return Die Nachricht
	 */
	public static String leseNachrichtAusResourceBundle(String messageKey, String bundleName,
			Object... messageParameter) {
		return leseNachrichtAusResourceBundle(messageKey, null, bundleName, messageParameter);
	}

	/**
	 * Liest eine Nachricht mit dem übergebene Key und gibt ihn zurück.
	 * 
	 * @param messageKey Der Key in der Properties-Datei
	 * @param messageParameter Das Array von Parametern für die Nachricht
	 * @return Die Nachricht
	 */
	public static String erzeugeNachrichtOhneUebersetzung(String messageKey, Object... messageParameter) {
		StringBuilder message = new StringBuilder(messageKey);
		if (messageParameter != null) {
			message.append(", parameter=[");
			message.append(StringUtils.join(messageParameter, ';'));
			message.append("]");
		}
		return message.toString();
	}
}
