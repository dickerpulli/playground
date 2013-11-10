package de.tbosch.tools.googleapps.gui;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.sun.javafx.application.PlatformImpl;

import de.tbosch.tools.googleapps.controller.GoogleAppsApplicationController;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

public class GoogleAppsApplication {

	/**
	 * Startet die Anwendungs-GUI.
	 * @throws IOException
	 */
	public void startApplication() {
		PlatformImpl.startup(new Runnable() {

			@Override
			public void run() {
				Parent root;
				try {
					root = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/GoogleAppsApplication.fxml",
							GoogleAppsApplicationController.class);
				}
				catch (IOException e) {
					throw new IllegalStateException("JavaFX scene cannot be created", e);
				}
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MessageHelper.getMessage("main.title"));
				stage.setScene(scene);
				stage.show();
			}
		});
	}
}
