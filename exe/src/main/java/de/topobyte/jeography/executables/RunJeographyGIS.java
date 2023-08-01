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

package de.topobyte.jeography.executables;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.bookmarks.Bookmark;
import de.topobyte.jeography.viewer.bookmarks.BookmarksIO;
import de.topobyte.jeography.viewer.config.ConfigWriter;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.shared.preferences.SharedPreferences;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RunJeographyGIS
{

	static final Logger logger = LoggerFactory.getLogger(RunJeographyGIS.class);

	private static final String HELP_MESSAGE = RunJeographyGIS.class
			.getSimpleName() + " [args]";

	public static void main(String[] args)
	{
		// @formatter:off
		Options options = new Options();
		OptionHelper.addL(options, "width", true, false, "pixels",
				"Window width");
		OptionHelper.addL(options, "height", true, false, "pixels",
				"Window height");
		OptionHelper.addL(options, "network", true, false, "yes/no",
				"start online / offline?");
		OptionHelper.addL(options, "grid", true, false, "yes/no", "show grid?");
		OptionHelper.addL(options, "tile-numbers", true, false, "yes/no",
				"show tile numbers?");
		OptionHelper.addL(options, "crosshair", true, false, "yes/no",
				"show crosshair?");
		OptionHelper.addL(options, "overlay", true, false, "yes/no",
				"show overlay?");
		OptionHelper.addL(options, "config", true, false, "file",
				"configuration to use");
		OptionHelper.addL(options, "lon", true, false, "double",
				"longitude to show");
		OptionHelper.addL(options, "lat", true, false, "double",
				"latitude to show");
		OptionHelper.addL(options, "zoom", true, false, "int",
				"zoom level to use");
		OptionHelper.addL(options, "db", true, false, "file",
				"file to use for database queries");
		OptionHelper.addL(options, "tiles", true, false, "index",
				"the ordinal index of the tile configuration to use");
		// @formatter:on

		System.setProperty("sun.java2d.uiScale",
				Double.toString(SharedPreferences.getUIScale()));

		CommandLineParser clp = new DefaultParser();

		CommandLine line = null;
		try {
			line = clp.parse(options, args);
		} catch (ParseException e) {
			System.err
					.println("Parsing command line failed: " + e.getMessage());
			new HelpFormatter().printHelp(HELP_MESSAGE, options);
			System.exit(1);
		}

		if (line == null) {
			return;
		}

		try {
			ensureConfiguration();
		} catch (IOException e) {
			logger.error("Unable to ensure configuration files existance", e);
		}

		LoadedConfiguration loadedConfiguration = Util.loadConfiguration(line);
		Configuration configuration = loadedConfiguration.getConfiguration();

		int width = configuration.getWidth(),
				height = configuration.getHeight();
		Path pathDatabase = configuration.getPathDatabase();
		boolean isOnline = configuration.isOnline();
		boolean showCrosshair = configuration.isShowCrosshair();
		boolean showGrid = configuration.isShowGrid();
		boolean showTileNumbers = configuration.isShowTileNumbers();
		boolean showOverlay = configuration.isShowOverlay();
		final boolean showGeometryManager = configuration
				.isShowGeometryManager();
		final boolean showSelectionRectDialog = configuration
				.isShowSelectionRectDialog();
		final boolean showSelectionPolyDialog = configuration
				.isShowSelectionPolyDialog();
		final boolean showMapWindowDialog = configuration
				.isShowMapWindowDialog();
		double lon = configuration.getLon();
		double lat = configuration.getLat();
		int zoom = configuration.getZoom();
		int tilesIndex = 0;

		if (line.hasOption("width")) {
			int w = Integer.valueOf(line.getOptionValue("width"));
			if (w != 0) {
				width = w;
			}
		}
		if (line.hasOption("height")) {
			int h = Integer.valueOf(line.getOptionValue("height"));
			if (h != 0) {
				height = h;
			}
		}
		if (line.hasOption("network")) {
			isOnline = line.getOptionValue("network").equals("yes");
		}
		if (line.hasOption("crosshair")) {
			showCrosshair = line.getOptionValue("crosshair").equals("yes");
		}
		if (line.hasOption("overlay")) {
			showOverlay = line.getOptionValue("overlay").equals("yes");
		}
		if (line.hasOption("grid")) {
			showGrid = line.getOptionValue("grid").equals("yes");
		}
		if (line.hasOption("tile_numbers")) {
			showTileNumbers = line.getOptionValue("tile_numbers").equals("yes");
		}
		if (line.hasOption("db")) {
			pathDatabase = Paths.get(line.getOptionValue("db"));
		}

		if (line.hasOption("lon")) {
			String value = line.getOptionValue("lon");
			lon = Double.parseDouble(value);
		}
		if (line.hasOption("lat")) {
			String value = line.getOptionValue("lat");
			lat = Double.parseDouble(value);
		}
		if (line.hasOption("zoom")) {
			String value = line.getOptionValue("zoom");
			zoom = Integer.parseInt(value);
		}
		if (line.hasOption("tiles")) {
			String value = line.getOptionValue("tiles");
			tilesIndex = Integer.parseInt(value);
		}

		String lookAndFeel = configuration.getLookAndFeel();
		if (lookAndFeel != null) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				logger.error("error while setting look and feel '" + lookAndFeel
						+ "': " + e.getClass().getSimpleName() + ", message: "
						+ e.getMessage());
			}
		}

		final JeographyGIS gis = new JeographyGIS(
				loadedConfiguration.getConfigFile(), configuration, tilesIndex,
				pathDatabase, isOnline, showGrid, showTileNumbers,
				showCrosshair, showOverlay, zoom, lon, lat);

		final int fWidth = width;
		final int fHeight = height;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				gis.create(fWidth, fHeight, showGeometryManager,
						showSelectionRectDialog, showSelectionPolyDialog,
						showMapWindowDialog);
			}
		});
	}

	private static void ensureConfiguration() throws IOException
	{
		Path directory = ConfigurationHelper.getUserConfigurationDirectory();
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}
		Path config = ConfigurationHelper.getUserConfigurationFilePath();
		if (!Files.exists(config)) {
			Configuration configuration = Configuration
					.createDefaultConfiguration();
			ConfigWriter.write(configuration, config);
		}
		Path bookmarks = ConfigurationHelper.getBookmarksFilePath();
		if (!Files.exists(bookmarks)) {
			List<Bookmark> list = new ArrayList<>();
			BookmarksIO.write(bookmarks, list);
		}
	}

}
