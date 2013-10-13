package de.tbosch.tools.jpasswordbuilder.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import de.tbosch.tools.jpasswordbuilder.builder.PasswordService;

public class JPasswordBuilderController {

	@FXML
	private TextField password;

	@FXML
	private TextField suffix;

	@FXML
	public void createButtonAction() {
		password.setText(PasswordService.getPasswort(suffix.getText()));
	}

}
