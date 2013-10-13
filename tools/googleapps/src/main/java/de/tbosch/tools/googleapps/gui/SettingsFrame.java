package de.tbosch.tools.googleapps.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.utils.MessageHelper;

public class SettingsFrame extends JFrame {

	private final PreferencesService preferencesService;

	private static final long serialVersionUID = 1L;

	public SettingsFrame(PreferencesService preferencesService) {
		this.preferencesService = preferencesService;
		setTitle(MessageHelper.getMessage("dialog.settings.title"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		init();
	}

	private void init() {
		GridLayout layout = new GridLayout(3, 2);
		setLayout(layout);
		JLabel passwordLabel = new JLabel(MessageHelper.getMessage("dialog.settings.label.password"));
		JLabel usernameLabel = new JLabel(MessageHelper.getMessage("dialog.settings.label.username"));
		final JPasswordField passwordTextField = new JPasswordField(preferencesService.readPassword(), 20);
		final JTextField usernameTextField = new JTextField(preferencesService.readUsername(), 20);
		JButton saveButton = new JButton(MessageHelper.getMessage("dialog.settings.button.save"));
		JButton cancelButton = new JButton(MessageHelper.getMessage("dialog.settings.button.cancel"));
		final JFrame thisFrame = this;
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.dispose();
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preferencesService.writePassword(new String(passwordTextField.getPassword()));
				preferencesService.writeUsername(usernameTextField.getText());
				thisFrame.dispose();
			}
		});
		add(usernameLabel);
		add(usernameTextField);
		add(passwordLabel);
		add(passwordTextField);
		add(saveButton);
		add(cancelButton);
		pack();
	}

}