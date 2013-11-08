package de.tbosch.tools.googleapps.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

@Controller
public class GoogleAppsApplicationController {

	@FXML
	public void clickSettingsButton() throws IOException {
		Parent parent = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/Settings.fxml", SettingsController.class,
				ResourceBundle.getBundle("messages"));
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(MessageHelper.getMessage("settings.title"));
		stage.setScene(scene);
		stage.show();
	}
}
