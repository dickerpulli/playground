package de.tbosch.tools.googleapps.gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import de.tbosch.tools.googleapps.controller.GoogleAppsApplicationController;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

public class GoogleAppsApplication extends Application {

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws IOException {
		Parent root = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/GoogleAppsApplication.fxml",
				GoogleAppsApplicationController.class, ResourceBundle.getBundle("messages"));
		Scene scene = new Scene(root);
		stage.setTitle(MessageHelper.getMessage("main.title"));
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Startet die Anwendungs-GUI.
	 */
	public void startApplication() {
		launch();
	}

}
