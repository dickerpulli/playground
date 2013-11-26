package de.tbosch.tools.googleapps.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import jfxtras.labs.dialogs.MonologFX.Type;
import jfxtras.labs.dialogs.MonologFXBuilder;

import org.springframework.stereotype.Controller;

import de.tbosch.tools.googleapps.utils.MessageHelper;

@Controller
public class AuthorizeController implements Initializable {

	@FXML
	private Hyperlink hyperlink;

	private String url;

	@FXML
	public void clickOkButton() {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		hyperlink.setText(url);
		hyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(url);
				clipboard.setContent(content);
				MonologFXBuilder.create().modal(true).type(Type.INFO)
						.message(MessageHelper.getMessage("authorize.clipboard")).build().showDialog();
			}
		});
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
