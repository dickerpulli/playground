package de.tbosch.tools.googleapps.controller;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.annotation.PreDestroy;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.GoogleAppsThread;
import de.tbosch.tools.googleapps.gui.JXTrayIcon;
import de.tbosch.tools.googleapps.gui.SettingsFrame;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.utils.MessageHelper;

/**
 * Controller for the tray icon functionalities.
 * 
 * @author thomas.bosch
 */
@Controller
public class TrayiconController {

	@Autowired
	private JXTrayIcon trayIcon;

	@Autowired
	@Qualifier("imageOnline")
	private Image imageOnline;

	@Autowired
	@Qualifier("imageOffline")
	private Image imageOffline;

	@Autowired
	private GoogleAppsThread googleAppsThread;

	@Autowired
	private GoogleAppsService googleAppsService;

	@Autowired
	private PreferencesService preferencesService;

	@PreDestroy
	public void preDestroy() {
		trayIcon.dispose();
		SystemTray.getSystemTray().remove(trayIcon);
	}

	/**
	 * Register menu and all actions on the trayicon.
	 */
	public void registerTrayIcon() {
		// Autosize the icon image
		trayIcon.setImageAutoSize(true);

		// Add tray icon to the system tray
		SystemTray systemTray = SystemTray.getSystemTray();
		try {
			systemTray.add(trayIcon);
		}
		catch (AWTException e) {
			throw new RuntimeException(e);
		}

		// Adds the popup menu to trayicon
		registerPopupMenu();

		// register events on trayicon itself
		registerEvents();

		// sets the image of the icon
		initIcon();
	}

	/**
	 * Initializes the image of the tray icon.
	 */
	private void initIcon() {
		setTrayiconTooltip();
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
				}
				else if (SwingUtilities.isLeftMouseButton(e)) {
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
		final JPopupMenu popup = new JPopupMenu(MessageHelper.getMessage("menu.label"));

		// Sub menu for choosing active project
		// JMenu menu = new JMenu(MessageHelper.getMessage("menu.projects.label"));
		//
		// Control item to add a new project
		// JMenuItem itemAdd = new JMenuItem(MessageHelper.getMessage("menu.projects.manageProjects"));
		// itemAdd.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// guiController.manageProjects();
		// }
		//
		// });
		// menu.add(itemAdd);
		//
		// // Separator
		// menu.addSeparator();
		//
		// // Fill in all known project to choose from
		// List<Project> projects = projectService.getAllProjects();
		// for (final Project project : projects) {
		// JMenuItem item = new JMenuItem(projectService.getFullName(project));
		// item.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// projectService.setActiveProject(project.getId());
		// refreshProjects();
		// }
		//
		// });
		// if (project.isActive()) {
		// item.setBackground(Color.DARK_GRAY);
		// item.setForeground(Color.WHITE);
		// }
		// menu.add(item);
		// }
		// popup.add(menu);
		//
		// // Separator
		// popup.addSeparator();

		// Connect to Google Apps
		JMenuItem connectItem = new JMenuItem(MessageHelper.getMessage("menu.item.connect"));
		connectItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					googleAppsService.getAndSaveCalendar();
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(popup, MessageHelper.getMessage("error.io") + ": " + e1.getMessage(),
							MessageHelper.getMessage("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
				}
				catch (ServiceException e1) {
					JOptionPane.showMessageDialog(popup,
							MessageHelper.getMessage("error.service") + ": " + e1.getMessage(),
							MessageHelper.getMessage("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		popup.add(connectItem);

		// Connect to Google Apps
		JMenuItem settingsItem = new JMenuItem(MessageHelper.getMessage("menu.item.settings"));
		settingsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsFrame(preferencesService).setVisible(true);
			}

		});
		popup.add(settingsItem);

		// Separator
		popup.addSeparator();

		// Exit Item to shutdown the program
		JMenuItem exitItem = new JMenuItem(MessageHelper.getMessage("menu.item.exit"));
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				googleAppsThread.interrupt();
			}

		});
		popup.add(exitItem);

		trayIcon.setJPopupMenu(popup);
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
		// Start or stop active timelot
		// Timeslot activeTimeslot = timeslotService.getActiveTimeslot();
		// if (activeTimeslot == null) {
		// Project activeProject = projectService.getActiveProject();
		// if (activeProject == null) {
		// JOptionPane.showMessageDialog(null, MessageHelper.getMessage("error.not_active_project_found"),
		// MessageHelper.getMessage("title.error"), JOptionPane.ERROR_MESSAGE);
		// } else {
		// timeslotService.startTimeslot(activeProject.getId());
		// }
		// } else {
		// timeslotService.endTimeslot(activeTimeslot.getId());
		// }

		// Reset the icon image
		initIcon();
	}

	/**
	 * Sets the project name and timeslot data as tooltip on tray icon.
	 */
	private void setTrayiconTooltip() {
		// Project activeProject = projectService.getActiveProject();
		// if (activeProject != null) {
		// String fullName = projectService.getFullName(activeProject);
		// Timeslot activeTimeslot = timeslotService.getActiveTimeslot();
		// if (activeTimeslot != null) {
		// fullName += " : "
		// + DateUtils.getDifferenceAsString(activeTimeslot.getStarttime(), activeTimeslot.getEndtime(),
		// true);
		// }
		// trayIcon.setToolTip(fullName);
		// }
	}
}
