package de.tbosch.tools.googleapps.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.springframework.context.ApplicationContext;

/**
 * Loader to load JavaFX objects that are Spring-aware.
 * @author Thomas Bosch
 */
public class SpringFXMLLoader {

	private final ApplicationContext context;

	/**
	 * Konstructor.
	 * @param context Spring Application-Context.
	 */
	public SpringFXMLLoader(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * Loads a JavaFX-Scene.
	 * @param url The URL to the FXML file.
	 * @param controllerClass The controller class.
	 * @return The loaded JavaFX file (i.e. Scene)
	 * @throws IOException
	 */
	public Parent load(String url, Class<?> controllerClass) throws IOException {
		return load(url, controllerClass, null);
	}

	/**
	 * Loads a JavaFX-Scene.
	 * @param url The URL to the FXML file.
	 * @param controllerClass The controller class.
	 * @param bundle Resource bundle for i18n.
	 * @return The loaded JavaFX file (i.e. Scene)
	 * @throws IOException
	 */
	public Parent load(String url, Class<?> controllerClass, ResourceBundle bundle) throws IOException {
		InputStream fxmlStream = null;
		try {
			InputStream inputStream = controllerClass.getResourceAsStream(url);
			Object instance = context.getBean(controllerClass);
			FXMLLoader loader = new FXMLLoader();
			loader.setController(instance);
			loader.setResources(bundle);
			return (Parent)loader.load(inputStream);
		}
		finally {
			if (fxmlStream != null) {
				fxmlStream.close();
			}
		}
	}

}