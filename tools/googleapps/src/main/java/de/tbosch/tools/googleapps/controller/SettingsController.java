package de.tbosch.tools.googleapps.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;

@Controller
public class SettingsController implements Initializable {

	@Autowired
	private PreferencesService preferencesService;

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private CheckBox autoconnect;

	@FXML
	public void clickOkButton() {
		preferencesService.writePref(PrefKey.USERNAME, username.getText());
		preferencesService.writePref(PrefKey.AUTOCONNECT, BooleanUtils.toStringTrueFalse(autoconnect.isSelected()));
		clickCancelButton();
	}

	@FXML
	public void clickCancelButton() {
		Stage stage = (Stage) username.getScene().getWindow();
		stage.close();
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(preferencesService.readPref(PrefKey.USERNAME));
		autoconnect.setSelected(BooleanUtils.toBoolean(preferencesService.readPref(PrefKey.AUTOCONNECT)));
	}

}
