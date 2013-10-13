package de.tbosch.tools.googleapps.utils;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Helper for accessing messages from resource bundle.
 * 
 * @author thomas.bosch
 */
public class MessageHelper {

	/**
	 * Get the default message from resource bundle.
	 * 
	 * @param messageKey
	 *            The key
	 * @return The text
	 */
	public static String getMessage(String messageKey) {
		ResourceBundleMessageSource resourceBundleMessageSource = (ResourceBundleMessageSource) GoogleAppsContext
				.getBean("resourceBundleMessageSource");
		return resourceBundleMessageSource.getMessage(messageKey, null, Locale.getDefault());
	}

	/**
	 * Get a message with some parameters filled in from resource bundle.
	 * 
	 * @param messageKey
	 *            The key
	 * @param args
	 *            The arguments
	 * @return The text
	 */
	public static String getMessage(String messageKey, Object... args) {
		ResourceBundleMessageSource resourceBundleMessageSource = (ResourceBundleMessageSource) GoogleAppsContext
				.getBean("resourceBundleMessageSource");
		return resourceBundleMessageSource.getMessage(messageKey, args, Locale.getDefault());
	}

}
