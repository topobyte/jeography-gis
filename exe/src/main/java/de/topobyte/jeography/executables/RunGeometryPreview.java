// Copyright 2017 Sebastian Kuerten
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.geometry.io.PolygonLoader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometrySourceJSG;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RunGeometryPreview
{

	static final Logger logger = LoggerFactory
			.getLogger(RunGeometryPreview.class);

	private static final String HELP_MESSAGE = "RunGeometryPreview [args] file1 [file2, ..., fileN]";

	/**
	 * Create a standalone preview for geometries.
	 * 
	 * @param args
	 *            at least on file to display.
	 */
	public static void main(String args[])
	{
		Options options = new Options();
		OptionHelper.addL(options, "config", true, false, "file",
				"configuration to use");

		CommandLine line = null;
		try {
			line = new DefaultParser().parse(options, args);
		} catch (ParseException e) {
			System.out
					.println("unable to parse command line: " + e.getMessage());
			new HelpFormatter().printHelp(HELP_MESSAGE, options);
			System.exit(1);
		}
		if (line == null) {
			return;
		}

		LoadedConfiguration loadedConfiguration = Util.loadConfiguration(line);
		Configuration configuration = loadedConfiguration.getConfiguration();

		String[] list = line.getArgs();
		if (list.length == 0) {
			new HelpFormatter().printHelp(HELP_MESSAGE, options);
			System.exit(1);
		}

		List<String> filenames = new ArrayList<>();
		for (int i = 0; i < list.length; i++) {
			String filename = list[i];
			filenames.add(filename);
		}

		List<Geometry> geometries = new ArrayList<>();
		for (String filename : filenames) {
			try {
				Geometry geometry = PolygonLoader.readPolygon(filename);
				geometries.add(geometry);
				continue;
			} catch (IOException e) {
				// go on
			}

			try {
				EntityFile entityFile = SmxFileReader.read(filename);
				geometries.add(entityFile.getGeometry());
				continue;
			} catch (ParserConfigurationException | SAXException
					| IOException e) {
				// go on
			}

			logger.info("unable to load geometry: " + filename);
		}

		int id = 1;
		Set<GeometryContainer> tgs = new HashSet<>();
		for (Geometry geometry : geometries) {
			GeoObject taggedGeometry = new GeoObject(geometry);
			GeometrySourceJSG source = new GeometrySourceJSG(
					String.format("%d", id));
			GeometryContainer geometryContainer = new GeometryContainer(id++,
					taggedGeometry, source);
			tgs.add(geometryContainer);
		}

		JFrame frame = new JFrame();
		frame.setTitle("GeometryPreview showing " + tgs.size() + " geometries");
		GeometryPreview preview = new GeometryPreview();
		preview.showViewerWithFile(frame.getRootPane(), configuration, tgs);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

}
