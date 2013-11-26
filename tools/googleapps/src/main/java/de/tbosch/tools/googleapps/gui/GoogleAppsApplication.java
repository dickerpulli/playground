package de.tbosch.tools.googleapps.gui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.javafx.application.PlatformImpl;

import de.tbosch.tools.googleapps.controller.GoogleAppsApplicationController;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

public class GoogleAppsApplication {

	private static final Log LOG = LogFactory.getLog(GoogleAppsApplication.class);

	private Stage window;

	/**
	 * Starts the application GUI.
	 * 
	 * @param show
	 *            Sets, if the application gui is shown after this method.
	 * 
	 * @throws IOException
	 */
	public void startApplication(final boolean show) {
		if (window == null) {
			PlatformImpl.startup(new Runnable() {

				@Override
				public void run() {
					Parent root;
					try {
						root = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/GoogleAppsApplication.fxml",
								GoogleAppsApplicationController.class);
					} catch (IOException e) {
						throw new IllegalStateException("JavaFX scene cannot be created", e);
					}
					Scene scene = new Scene(root);
					window = new Stage();
					window.setTitle(MessageHelper.getMessage("main.title"));
					window.setScene(scene);
					window.setOnCloseRequest(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent arg0) {
							window.hide();
						}
					});
					if (show) {
						window.show();
					}
				}
			});
		} else if (!window.isShowing()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					if (show) {
						window.show();
					}
				}
			});
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Window already showing");
			}
		}
	}
}
