/*
 * Main.java
 */
package de.tbosch.tools.jpasswordbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

import de.tbosch.tools.jpasswordbuilder.swing.MainView;

/**
 * The main class of the application.
 */
public class JPasswordBuilder extends Application {

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		// startSwing();
		startJavaFX(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("javafx/JPasswordBuilder.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("JPasswordBuilder");
		stage.setScene(scene);
		stage.show();
	}

	private static void startJavaFX(String[] args) {
		launch(args);
	}

	private static void startSwing() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainView().setVisible(true);
			}
		});
	}
}
