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

package de.topobyte.jeography.viewer.selection.download;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;
import de.topobyte.jeography.viewer.geometry.manage.EventJDialog;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.swing.util.ButtonPane;
import de.topobyte.swing.util.Components;
import de.topobyte.swing.util.ElementWrapper;
import de.topobyte.swing.util.SelectFolderWidget;
import de.topobyte.swing.util.combobox.ListComboBoxModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DownloadDialog extends EventJDialog
{

	private static final long serialVersionUID = 5617532574377870576L;

	static final Logger logger = LoggerFactory.getLogger(DownloadDialog.class);

	private final JeographyGIS gis;
	private GeographicSelection selection;

	private List<JCheckBox> checkBoxes = new ArrayList<>();
	private ListComboBoxModel<TileConfigUrlDisk> model;

	private boolean useDefaultLocation = true;
	private SelectFolderWidget panelLocation;

	/**
	 * Create a new DownloadDialog.
	 * 
	 * @param gis
	 *            the JeographyGIS instance.
	 * @param parent
	 *            the parent frame.
	 * @param title
	 *            the title
	 */
	public DownloadDialog(JeographyGIS gis, JFrame parent, String title)
	{
		super(parent, title);
		this.gis = gis;
		selection = gis.getSelectionAdapter().getGeographicSelection();
		construct();
	}

	private void construct()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		/*
		 * ComboBox for tile configurations
		 */

		JLabel labelSelectedTiles = new JLabel("Select tileset: ");

		List<TileConfig> tileConfigs = gis.getConfiguration().getTileConfigs();
		List<TileConfigUrlDisk> httpConfigs = new ArrayList<>();
		for (TileConfig config : tileConfigs) {
			if (config instanceof TileConfigUrlDisk) {
				httpConfigs.add((TileConfigUrlDisk) config);
			}
		}

		model = new ListComboBoxModel<TileConfigUrlDisk>(httpConfigs) {

			@Override
			public String toString(TileConfigUrlDisk element)
			{
				return element.getName();
			}

		};
		JComboBox<ElementWrapper<TileConfigUrlDisk>> box = new JComboBox<>(
				model);

		/*
		 * location setup
		 */
		JRadioButton radioDefault = new JRadioButton("default location");
		JRadioButton radioCustom = new JRadioButton("custom location");
		radioDefault.setSelected(true);

		ButtonGroup groupLocation = new ButtonGroup();
		groupLocation.add(radioDefault);
		groupLocation.add(radioCustom);

		File location = new File("/tmp/tiles/download");
		panelLocation = new SelectFolderWidget(location);
		panelLocation.setEnabled(false);

		radioDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				useDefaultLocation = true;
				panelLocation.setEnabled(false);
			}
		});
		radioCustom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				useDefaultLocation = false;
				panelLocation.setEnabled(true);
			}
		});

		/*
		 * CheckBoxes for zoom levels
		 */

		BBox boundingBox = selection.toBoundingBox();

		int s = 1;
		int n = 18;
		JPanel panel = new JPanel(new GridBagLayout());
		for (int i = 0; i < n; i++) {
			int x = s + i;
			c.gridy = i;
			c.gridx = 0;
			panel.add(new JLabel("" + x), c);
			c.gridx = 1;
			JCheckBox checkBox = new JCheckBox();
			checkBoxes.add(checkBox);
			panel.add(checkBox, c);
			c.gridx = 2;
			JLabel label = new JLabel();
			panel.add(label, c);

			TileMapWindow mapWindow = new SteppedMapWindow(boundingBox, x);

			int nX = mapWindow.getNumTilesX();
			int nY = mapWindow.getNumTilesY();
			int nT = nX * nY;
			label.setText(nT + " tiles");
		}

		/*
		 * buttons
		 */

		JButton buttonDownload = new JButton("download");
		JButton buttonCancel = new JButton("cancel");

		ArrayList<JButton> buttons = new ArrayList<>();
		buttons.add(buttonDownload);
		buttons.add(buttonCancel);
		ButtonPane buttonPane = new ButtonPane(buttons);

		/*
		 * layout
		 */

		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = GridBagConstraints.RELATIVE;

		c.gridx = 0;
		add(labelSelectedTiles, c);
		c.gridx = 1;
		add(box, c);

		c.gridx = 0;
		c.gridwidth = 2;
		add(radioDefault, c);
		add(radioCustom, c);

		c.gridx = 0;
		c.gridwidth = 2;
		add(panelLocation, c);

		c.gridx = 0;
		c.gridwidth = 2;
		add(panel, c);

		c.gridx = 0;
		c.gridwidth = 2;
		add(buttonPane, c);

		/*
		 * button actions
		 */

		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		buttonDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				startDownload();
			}
		});
	}

	void startDownload()
	{
		logger.debug("downloading...");

		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < checkBoxes.size(); i++) {
			int zoom = i + 1;
			if (checkBoxes.get(i).isSelected()) {
				values.add(zoom);
			}
		}

		TilePathProvider resoluter = null;
		if (!useDefaultLocation) {

			final File directory = panelLocation.getSelectedFolder();
			directory.mkdirs();
			final String pattern = "%d_%d_%d.png";

			resoluter = new TilePathProvider() {

				@Override
				public File getPath(Tile tile)
				{
					String filename = String.format(pattern, tile.getZoom(),
							tile.getTx(), tile.getTy());
					return new File(directory, filename);
				}
			};
		}

		TileConfigUrlDisk config = model.getSelectedElement();
		TileDownloader tileDownloader = new TileDownloader(config, resoluter,
				selection.toBoundingBox(), values);

		JFrame frame = Components.getContainingFrame(this);
		DownloadProgressWindow progressWindow = new DownloadProgressWindow(
				frame, tileDownloader);
		progressWindow.pack();
		progressWindow.setVisible(true);
		progressWindow.toFront();

		tileDownloader.download();
	}

}
