package de.tbosch.tools.googleapps.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

import jfxtras.labs.dialogs.MonologFX.Type;
import jfxtras.labs.dialogs.MonologFXBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gdata.util.ServiceException;
import com.sun.javafx.collections.ObservableListWrapper;

import de.tbosch.tools.googleapps.model.GCalendarEventEntry;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

@Controller
public class GoogleAppsApplicationController implements Initializable {

	@Autowired
	private GoogleAppsService googleAppsService;

	@FXML
	private Button connectButton;

	@FXML
	private Button disconnectButton;

	@FXML
	private Button refreshButton;

	@FXML
	private ListView<GCalendarEventEntry> calendarList;

	// @FXML
	// private ListView<?> emailList;

	@PostConstruct
	public void postContruct() {
		googleAppsService.addUpdateListener(new UpdateListener() {

			@Override
			public void updated() {
				if (calendarList != null) {
					calendarList.setItems(new ObservableListWrapper<GCalendarEventEntry>(googleAppsService
							.getCalendarEventsFromNowOn()));
				}
			}
		});
	}

	@FXML
	public void clickSettingsButton() throws IOException {
		Parent parent = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/Settings.fxml", SettingsController.class);
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(MessageHelper.getMessage("settings.title"));
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void clickConnectButton() {
		try {
			googleAppsService.connect();
			googleAppsService.updateCalendar();
			initialize(null, null);
		} catch (IOException e) {
			MonologFXBuilder.create().modal(true).type(Type.ERROR)
					.message(MessageHelper.getMessage("error.io") + ": " + e.getMessage())
					.titleText(MessageHelper.getMessage("error.title"));
		} catch (ServiceException e) {
			MonologFXBuilder.create().modal(true).type(Type.ERROR)
					.message(MessageHelper.getMessage("error.service") + ": " + e.getMessage())
					.titleText(MessageHelper.getMessage("error.title"));
		}
	}

	@FXML
	public void clickDisconnectButton() {
		googleAppsService.disconnect();
		initialize(null, null);
	}

	@FXML
	public void clickUpdateButton() throws IOException, ServiceException {
		if (googleAppsService.isConnected()) {
			googleAppsService.updateCalendar();
		}
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (googleAppsService.isConnected()) {
			connectButton.setDisable(true);
			disconnectButton.setDisable(false);
			refreshButton.setDisable(false);
		} else {
			connectButton.setDisable(false);
			disconnectButton.setDisable(true);
			refreshButton.setDisable(true);
		}
		calendarList.setItems(new ObservableListWrapper<GCalendarEventEntry>(googleAppsService
				.getCalendarEventsFromNowOn()));
	}

}
