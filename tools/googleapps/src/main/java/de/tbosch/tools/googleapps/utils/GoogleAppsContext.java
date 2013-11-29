package de.tbosch.tools.googleapps.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Holds the Spring application context and provides access to beans in this context.
 * 
 * @author thomas.bosch
 */
public class GoogleAppsContext {

	/** The Spring context */
	private static ConfigurableApplicationContext context;

	/** Spring FXML Loader */
	private static SpringFXMLLoader loader;

	/**
	 * Default private constructor avoiding instanciation.
	 */
	private GoogleAppsContext() {
		// Nothing
	}

	/**
	 * Gets the Spring application context.
	 * 
	 * @return The context
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * Gets a bean from the context.
	 * 
	 * @param name
	 *            The name of the bean
	 * @return The bean
	 */
	public static Object getBean(String name) {
		return context.getBean(name);
	}

	/**
	 * Gets a bean from the context.
	 * 
	 * @param name
	 *            The type of the bean
	 * @return The bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	/**
	 * Closes the application context.
	 */
	public static void close() {
		context.close();
	}

	/**
	 * Creates the context, if it is null.
	 */
	public static void load() {
		if (context != null) {
			throw new IllegalStateException("context already loaded");
		}
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	/**
	 * Creates the context from given applicationContext. Is overriden, when loaded a second time.
	 * 
	 * @param applicationContext
	 *            The context to use
	 */
	public static void load(ConfigurableApplicationContext applicationContext) {
		Assert.notNull(applicationContext);
		context = applicationContext;
	}

	/**
	 * Creates a Spring FXML Loader that is used to create Spring-Aware JavaFX-Scenes.
	 * 
	 * @return SpringFxmlLoader
	 */
	public static SpringFXMLLoader getSpringFXMLLoader() {
		if (context == null) {
			throw new IllegalArgumentException("context not loaded");
		}
		if (loader == null) {
			loader = new SpringFXMLLoader(context);
		}
		return loader;
	}

}
