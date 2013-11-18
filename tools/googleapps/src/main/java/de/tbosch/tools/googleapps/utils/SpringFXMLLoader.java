package de.tbosch.tools.googleapps.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
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
	 * @param bundle Resource bundle for i18n.
	 * @return The loaded JavaFX file (i.e. Scene)
	 * @throws IOException
	 */
	public Parent load(String url, Class<?> controllerClass) throws IOException {
		InputStream fxmlStream = null;
		try {
			InputStream inputStream = controllerClass.getResourceAsStream(url);
			List<String> lines = IOUtils.readLines(inputStream);
			byte[] bytes = new byte[0];
			for (String line : lines) {
				bytes = ArrayUtils.addAll(bytes, line.replaceAll("fx:controller=\".*\"", "").getBytes());
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			Object instance = context.getBean(controllerClass);
			FXMLLoader loader = new FXMLLoader();
			loader.setController(instance);
			loader.setResources(ResourceBundle.getBundle("messages"));
			return (Parent)loader.load(bais);
		}
		finally {
			if (fxmlStream != null) {
				fxmlStream.close();
			}
		}
	}
}