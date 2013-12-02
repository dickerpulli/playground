package de.tbosch.tools.googleapps.controller;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

import jfxtras.labs.dialogs.MonologFX.Type;
import jfxtras.labs.dialogs.MonologFXBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.collections.ObservableListWrapper;

import de.tbosch.tools.googleapps.exception.GoogleAppsException;
import de.tbosch.tools.googleapps.model.GCalendarEvent;
import de.tbosch.tools.googleapps.model.GEmail;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.listeners.ConnectionStatusListener;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

@Controller
public class GoogleAppsApplicationController implements Initializable {

	public static final Log LOG_STATUS = LogFactory.getLog(StatusbarWriter.class);

	@Autowired
	private GoogleAppsService googleAppsService;

	@FXML
	private Button connectButton;

	@FXML
	private Button disconnectButton;

	@FXML
	private Button refreshButton;

	@FXML
	private TextField status;

	@FXML
	private ListView<GCalendarEvent> calendarList;

	@FXML
	private ListView<GEmail> emailList;

	@PostConstruct
	public void postContruct() {
		googleAppsService.addUpdateListener(new UpdateListener() {
			@Override
			public void updated() {
				PlatformImpl.runLater(new Runnable() {
					@Override
					public void run() {
						calendarList.setItems(new ObservableListWrapper<GCalendarEvent>(googleAppsService
								.getCalendarEventsFromNowOn()));
						emailList.setItems(new ObservableListWrapper<GEmail>(googleAppsService.getEmails()));
						if (LOG_STATUS.isInfoEnabled()) {
							LOG_STATUS.info(MessageHelper.getMessage("msg.updated"));
						}
					}
				});
			}
		});
		googleAppsService.addConnectionStatusListener(new ConnectionStatusListener() {
			@Override
			public void changed(boolean connected) {
				PlatformImpl.runLater(new Runnable() {
					@Override
					public void run() {
						initialize(null, null);
					}
				});
				if (connected) {
					if (LOG_STATUS.isInfoEnabled()) {
						LOG_STATUS.info(MessageHelper.getMessage("msg.connected"));
					}
				} else {
					if (LOG_STATUS.isInfoEnabled()) {
						LOG_STATUS.info(MessageHelper.getMessage("msg.disconnected"));
					}
				}
			}
		});
		Logger logger = Logger.getLogger(StatusbarWriter.class);
		logger.addAppender(new WriterAppender(new SimpleLayout(), new StatusbarWriter()));
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
			if (LOG_STATUS.isInfoEnabled()) {
				LOG_STATUS.info(MessageHelper.getMessage("msg.updating"));
			}
			googleAppsService.connect();
			googleAppsService.updateCalendar();
			googleAppsService.updateEmails();
		} catch (GoogleAppsException e) {
			googleAppsService.disconnect();
			MonologFXBuilder.create().modal(true).type(Type.ERROR)
					.message(MessageHelper.getMessage("error.service") + ": " + e.getMessage())
					.titleText(MessageHelper.getMessage("error.title")).build().showDialog();
		}
	}

	@FXML
	public void clickDisconnectButton() {
		googleAppsService.disconnect();
	}

	@FXML
	public void clickUpdateButton() throws GoogleAppsException {
		if (googleAppsService.isConnected()) {
			if (LOG_STATUS.isInfoEnabled()) {
				LOG_STATUS.info(MessageHelper.getMessage("msg.updating"));
			}
			googleAppsService.updateCalendar();
			googleAppsService.updateEmails();
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
		calendarList
				.setItems(new ObservableListWrapper<GCalendarEvent>(googleAppsService.getCalendarEventsFromNowOn()));
		emailList.setItems(new ObservableListWrapper<GEmail>(googleAppsService.getEmails()));
	}

	/**
	 * A Writer that writes Log output to the status text field in the GUI.
	 */
	private class StatusbarWriter extends Writer {

		/**
		 * @see java.io.Writer#write(char[], int, int)
		 */
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			if (status != null) {
				String text = "";
				for (int i = off; i < len; i++) {
					text += cbuf[i];
				}
				status.setText(text);
			}
		}

		/**
		 * @see java.io.Writer#flush()
		 */
		@Override
		public void flush() throws IOException {
			// nop
		}

		/**
		 * @see java.io.Writer#close()
		 */
		@Override
		public void close() throws IOException {
			// nop
		}

	}

}
