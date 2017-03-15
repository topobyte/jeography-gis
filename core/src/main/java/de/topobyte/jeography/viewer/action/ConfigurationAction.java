// Copyright 2016 Sebastian Kuerten
//
// This file is part of jeography.
//
// jeography is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// jeography is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with jeography. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.jeography.viewer.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.config.ConfigWriter;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.edit.ConfigurationEditor;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ConfigurationAction extends GISAction
{

	private static final long serialVersionUID = -4879240469021570017L;

	final static Logger logger = LoggerFactory
			.getLogger(ConfigurationAction.class);

	private static final String FILE_IMAGE = "res/images/preferences-other.png";

	private String configFile;

	private JDialog dialog;
	private ConfigurationEditor configurationEditor;

	/**
	 * Create a new action instance.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 */
	public ConfigurationAction(JeographyGIS gis)
	{
		super(gis, FILE_IMAGE, "settings");
		setDescription("configure the settings for this application");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		logger.debug("configure settings");
		configFile = getGIS().getConfigFile();
		Configuration config = getGIS().getConfiguration();
		showDialog(config);
	}

	private void showDialog(Configuration configuration)
	{
		configurationEditor = new ConfigurationEditor(configuration) {

			private static final long serialVersionUID = 1L;

			@Override
			public void ok()
			{
				ConfigurationAction.this.ok();
			}

			@Override
			public void cancel()
			{
				ConfigurationAction.this.cancel();
			}

		};

		JFrame frame = getMainFrame();
		dialog = new JDialog(frame, "Settings: " + configFile);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		dialog.setContentPane(configurationEditor);

		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e)
			{
				closing();
			}
		});

		dialog.setModal(true);
		dialog.setSize(500, 500);

		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}

	/**
	 * Called when dialog is submitted.
	 */
	protected void ok()
	{
		logger.debug("OK");
		logger.debug("writing to: " + configFile);
		Configuration configuration = configurationEditor.getConfiguration();

		OutputStream out = null;
		boolean success = false;

		File file = new File(configFile);
		File directory = file.getParentFile();
		if (directory != null) {
			if (!directory.exists()) {
				directory.mkdirs();
			}
		}

		try {
			out = new FileOutputStream(configFile);
			// out = System.out;
			ConfigWriter.write(configuration, out);
			success = true;
		} catch (FileNotFoundException e) {
			showError("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			showError("IOException: " + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// do nothing
			}
			getGIS().setConfiguration(configuration);
		}
		if (success) {
			dialog.dispose();
		}

		setLookAndFeel(configuration.getLookAndFeel());
		setDockingFramesTheme(configuration.getDockingFramesTheme());
	}

	private void setLookAndFeel(String lookAndFeel)
	{
		try {
			if (lookAndFeel == null) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) {
			logger.error("error while setting look and feel '" + lookAndFeel
					+ "': " + e.getClass().getSimpleName() + ", message: "
					+ e.getMessage());
		}
		for (Window window : JFrame.getWindows()) {
			SwingUtilities.updateComponentTreeUI(window);
			// window.pack();
		}
	}

	private void setDockingFramesTheme(String dockingFramesTheme)
	{
		getGIS().setDockingFramesTheme(dockingFramesTheme);
	}

	private void showError(String message)
	{
		JOptionPane.showMessageDialog(dialog, message, "error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Called when dialog has been canceled.
	 */
	protected void cancel()
	{
		logger.debug("CANCEL");
		dialog.dispose();
	}

	/**
	 * Called when dialog is closing.
	 */
	protected void closing()
	{
		logger.debug("CLOSING");
		dialog.dispose();
	}

}
