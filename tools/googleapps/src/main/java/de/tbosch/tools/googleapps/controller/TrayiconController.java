package de.tbosch.tools.googleapps.controller;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import de.tbosch.tools.googleapps.GoogleAppsThread;
import de.tbosch.tools.googleapps.exception.GoogleAppsException;
import de.tbosch.tools.googleapps.gui.GoogleAppsApplication;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;
import de.tbosch.tools.googleapps.utils.MessageHelper;

/**
 * Controller for the tray icon functionalities.
 * 
 * @author thomas.bosch
 */
@Controller
public class TrayiconController {

	private static final Log LOG = LogFactory.getLog(TrayiconController.class);

	private TrayIcon trayIcon;

	@Autowired
	@Qualifier("imageOnline")
	private Image imageOnline;

	@Autowired
	@Qualifier("imageOffline")
	private Image imageOffline;

	private GoogleAppsApplication app;

	@Autowired
	private GoogleAppsThread googleAppsThread;

	@Autowired
	private GoogleAppsService googleAppsService;

	@Autowired
	private PreferencesService preferencesService;

	@PostConstruct
	public void postContruct() {
		googleAppsService.addUpdateListener(new UpdateListener() {

			@Override
			public void updated() {
				System.out.println("updated");
			}
		});
		app = new GoogleAppsApplication();
	}

	@PreDestroy
	public void preDestroy() {
		SystemTray.getSystemTray().remove(trayIcon);
	}

	/**
	 * Register menu and all actions on the trayicon.
	 */
	public void registerTrayIcon() {
		trayIcon = new TrayIcon(imageOffline);

		// Autosize the icon image
		trayIcon.setImageAutoSize(true);

		// Add tray icon to the system tray
		SystemTray systemTray = SystemTray.getSystemTray();
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}

		// Adds the popup menu to trayicon
		registerPopupMenu();

		// register events on trayicon itself
		registerEvents();

		// start GUI
		app.startApplication(false);

		// connect
		connect();

		// sets the image of the icon
		initIcon();
	}

	/**
	 * Connect to Google if its possible.
	 */
	private void connect() {
		boolean connect = BooleanUtils.toBoolean(preferencesService.readPref(PrefKey.AUTOCONNECT));
		if (connect) {
			try {
				googleAppsService.connect();
				googleAppsService.updateCalendar();
				googleAppsService.updateEmails();
			} catch (GoogleAppsException e) {
				throw new IllegalStateException("Error while connecting at startup", e);
			}
		}
	}

	/**
	 * Initializes the image of the tray icon.
	 */
	private void initIcon() {
		setTrayiconTooltip();
	}

	/**
	 * Sets the image for the tray icon. Online or offline image.
	 * 
	 * @param online
	 *            online image?
	 */
	public void setIconImage(boolean online) {
		if (trayIcon != null) {
			if (online) {
				if (trayIcon.getImage().equals(imageOffline)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("set tray icon image to online");
					}
					trayIcon.setImage(imageOnline);
				}
			} else {
				if (trayIcon.getImage().equals(imageOnline)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("set tray icon image to offline");
					}
					trayIcon.setImage(imageOffline);
				}
			}
		}
	}

	/**
	 * Registers all events on the trayicon.
	 */
	private void registerEvents() {
		trayIcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseButtonClicked();
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseButtonClicked();
				}
			}

		});
		trayIcon.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				setTrayiconTooltip();
			}
		});
	}

	/**
	 * Registers a popup menu on trayicon
	 */
	private void registerPopupMenu() {
		// Create a new popup menu for options and controls
		final PopupMenu popup = new PopupMenu(MessageHelper.getMessage("menu.label"));

		// Connect to Google Apps
		MenuItem settingsItem = new MenuItem(MessageHelper.getMessage("menu.item.settings"));
		settingsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				app.startApplication(true);
			}

		});
		popup.add(settingsItem);

		// Separator
		popup.addSeparator();

		// Exit Item to shutdown the program
		MenuItem exitItem = new MenuItem(MessageHelper.getMessage("menu.item.exit"));
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				googleAppsThread.interrupt();
			}

		});
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
	}

	/**
	 * If the right mouse button is clicked
	 */
	private void rightMouseButtonClicked() {

	}

	/**
	 * If the left mouse button is clicked
	 */
	private void leftMouseButtonClicked() {
		// Reset the icon image
		initIcon();
	}

	/**
	 * Sets the project name and timeslot data as tooltip on tray icon.
	 */
	private void setTrayiconTooltip() {
		trayIcon.setToolTip(MessageHelper.getMessage("msg.connection")
				+ (googleAppsService.isConnected() ? "online" : "offline"));
	}

}
