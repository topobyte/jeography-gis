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

package de.topobyte.jeography.tools.bboxaction.bboxchooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.nio.file.Path;

import javax.accessibility.AccessibleContext;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.melon.io.StreamUtil;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BboxChooser extends JPanel
{

	static final Logger logger = LoggerFactory.getLogger(BboxChooser.class);

	private static final long serialVersionUID = 2362483528400638190L;

	public static final int APPROVE_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	public static final int ERROR_OPTION = 2;

	private BBox bbox;

	private JToolBar toolBar;
	private Viewer viewer;
	private SelectionAdapter selectionAdapter;

	private boolean focussed = false;

	public BboxChooser(BBox bbox)
	{
		this.bbox = bbox;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		/*
		 * viewer
		 */

		Configuration configuration = Configuration
				.createDefaultConfiguration();

		Path configFile = ConfigurationHelper.getUserConfigurationFilePath();
		logger.debug("default user config file: " + configFile);
		try {
			InputStream configInput = StreamUtil
					.bufferedInputStream(configFile);
			configuration = ConfigReader.read(configInput);
			IOUtils.closeQuietly(configInput);
		} catch (Exception e) {
			logger.info("unable to read configuration: " + e.getMessage());
			logger.info("using default configuration");
		}

		TileConfig tileConfig = configuration.getTileConfigs().get(0);

		viewer = new Viewer(tileConfig, null);
		viewer.setMouseActive(true);
		viewer.setDrawCrosshair(false);
		viewer.setDrawTileNumbers(false);
		viewer.setDrawBorder(false);
		viewer.setPreferredSize(new Dimension(600, 500));

		selectionAdapter = new SelectionAdapter(viewer);
		if (bbox != null) {
			selectionAdapter.setGeographicSelection(
					new GeographicSelection(bbox.getLon1(), bbox.getLon2(),
							bbox.getLat1(), bbox.getLat2()));
		}
		selectionAdapter.setMouseActive(true);
		viewer.setMouseActive(false);

		/*
		 * toolbar
		 */
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		PanAction panAction = new PanAction(this);
		SelectAction selectAction = new SelectAction(this);
		toolBar.add(panAction);
		toolBar.add(selectAction);

		/*
		 * layout
		 */
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		c.weightx = 1.0;
		c.weighty = 0.0;
		add(toolBar, c);

		c.weightx = 1.0;
		c.weighty = 1.0;
		add(viewer, c);

		if (bbox != null) {

			viewer.addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e)
				{
					if (focussed) {
						return;
					}
					viewer.getMapWindow().gotoLonLat(
							BboxChooser.this.bbox.getLon1(),
							BboxChooser.this.bbox.getLon2(),
							BboxChooser.this.bbox.getLat1(),
							BboxChooser.this.bbox.getLat2());
					focussed = true;
				}

			});
		}
	}

	public void setBbox(BBox bbox)
	{
		this.bbox = bbox;
	}

	public BBox getBbox()
	{
		return bbox;
	}

	public Viewer getViewer()
	{
		return viewer;
	}

	public SelectionAdapter getSelectionAdapter()
	{
		return selectionAdapter;
	}

	/*
	 * Show stuff
	 */

	private JDialog dialog;
	private int returnValue;

	public int showDialog(Component parent)
	{
		dialog = createDialog(parent);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				returnValue = CANCEL_OPTION;
			}
		});
		returnValue = ERROR_OPTION;

		dialog.setVisible(true);
		dialog.dispose();
		dialog = null;
		return returnValue;
	}

	protected JDialog createDialog(Component parent) throws HeadlessException
	{
		String title = "choose a bounding box";
		putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY,
				title);

		JDialog dialog;
		Window window = Components.getContainingWindow(parent);
		if (window instanceof Frame) {
			dialog = new JDialog((Frame) window, title, true);
		} else {
			dialog = new JDialog((Dialog) window, title, true);
		}
		dialog.setComponentOrientation(this.getComponentOrientation());

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(this, BorderLayout.CENTER);

		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = UIManager.getLookAndFeel()
					.getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				dialog.getRootPane().setWindowDecorationStyle(
						JRootPane.FILE_CHOOSER_DIALOG);
			}
		}

		JPanel panel = new JPanel();
		JButton buttonOk = new JButton("Select");
		JButton buttonCancel = new JButton("Cancel");
		panel.add(buttonOk);
		panel.add(buttonCancel);
		contentPane.add(panel, BorderLayout.SOUTH);

		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ok();
			}
		});
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				cancel();
			}
		});

		// String buttonText1 = "Select";
		// String buttonText2 = "Cancel";
		// Object[] options = { buttonText1, buttonText2 };
		// JOptionPane optionPane = new JOptionPane(this,
		// JOptionPane.QUESTION_MESSAGE,
		// JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		// contentPane.add(optionPane, BorderLayout.CENTER);

		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		return dialog;
	}

	protected void cancel()
	{
		returnValue = CANCEL_OPTION;
		dialog.setVisible(false);
	}

	protected void ok()
	{
		GeographicSelection selection = selectionAdapter
				.getGeographicSelection();
		if (selection == null) {
			bbox = null;
		} else {
			bbox = selection.toBoundingBox();
		}
		returnValue = APPROVE_OPTION;
		dialog.setVisible(false);
	}

}
