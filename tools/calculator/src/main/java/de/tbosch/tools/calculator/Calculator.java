package de.tbosch.tools.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Calculator extends Application {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Parent parent = FXMLLoader.load(this.getClass().getResource(
				"gui/Calculator.fxml"));
		Scene scene = new Scene(parent);
		stage.setTitle("Calculator");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Main-Method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
