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

package de.topobyte.jeography.viewer.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ConfigReader
{

	final static Logger logger = LoggerFactory.getLogger(ConfigReader.class);

	/**
	 * Read a configuration from the given fileame.
	 * 
	 * @param filename
	 *            the filename to read the xml from.
	 * @return the configuration read.
	 * @throws SAXException
	 *             on parsing failure
	 * @throws ParserConfigurationException
	 *             on failure.
	 * @throws IOException
	 *             on failure.
	 * @throws FileNotFoundException
	 *             on failure.
	 */
	public static Configuration read(String filename)
			throws ParserConfigurationException, SAXException,
			FileNotFoundException, IOException
	{
		SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
		ConfigHandler configHandler = new ConfigHandler();
		sax.parse(new FileInputStream(filename), configHandler);

		for (TileConfig config : configHandler.configuration.getTileConfigs()) {
			logger.info("tiles: " + config.toString());
		}
		for (TileConfig config : configHandler.configuration
				.getTileConfigsOverlay()) {
			logger.info("overlay: " + config.toString());
		}

		return configHandler.configuration;
	}

}

class ConfigHandler extends DefaultHandler
{

	final static Logger logger = LoggerFactory.getLogger(ConfigHandler.class);

	Configuration configuration = new Configuration();

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes)
	{
		if (qName.equals("option")) {
			String name = attributes.getValue("name");
			String value = attributes.getValue("value");
			if (name == null || value == null) {
				return;
			}
			parseOption(name, value);
		} else if (qName.equals("tiles")) {
			String name = attributes.getValue("name");
			String type = attributes.getValue("type");
			if (name == null || type == null) {
				return;
			}
			if (type.equals("urldisk")) {
				String url = attributes.getValue("url");
				String path = attributes.getValue("path");
				String userAgent = attributes.getValue("userAgent");
				if (url == null || path == null) {
					return;
				}
				TileConfigUrlDisk c = new TileConfigUrlDisk(configuration
						.getTileConfigs().size() + 1, name, url, path);
				if (userAgent != null) {
					c.setUserAgent(userAgent);
				}
				configuration.getTileConfigs().add(c);
			} else if (type.equals("disk")) {
				String path = attributes.getValue("path");
				if (path == null) {
					return;
				}
				TileConfigDisk c = new TileConfigDisk(configuration
						.getTileConfigs().size() + 1, name, path);
				configuration.getTileConfigs().add(c);
			}
		} else if (qName.equals("overlay")) {
			String name = attributes.getValue("name");
			String type = attributes.getValue("type");
			if (type.equals("urldisk")) {
				String url = attributes.getValue("url");
				String path = attributes.getValue("path");
				if (url == null || path == null) {
					return;
				}
				String userAgent = attributes.getValue("userAgent");
				TileConfigUrlDisk c = new TileConfigUrlDisk(configuration
						.getTileConfigsOverlay().size() + 1, name, url, path);
				if (userAgent != null) {
					c.setUserAgent(userAgent);
				}
				configuration.getTileConfigsOverlay().add(c);
			} else if (type.equals("disk")) {
				String path = attributes.getValue("path");
				if (path == null) {
					return;
				}
				configuration.getTileConfigsOverlay().add(
						new TileConfigUrlDisk(configuration
								.getTileConfigsOverlay().size() + 1, name,
								null, path));
			}
		}
	}

	private void parseOption(String name, String value)
	{
		if (name.equals("grid")) {
			configuration.setShowGrid(parseBoolean(value));
		} else if (name.equals("tilenumbers")) {
			configuration.setShowTileNumbers(parseBoolean(value));
		} else if (name.equals("crosshair")) {
			configuration.setShowCrosshair(parseBoolean(value));
		} else if (name.equals("overlay")) {
			configuration.setShowOverlay(parseBoolean(value));
		} else if (name.equals("online")) {
			configuration.setOnline(parseBoolean(value));
		} else if (name.equals("tilenumbers")) {
			configuration.setShowGrid(parseBoolean(value));
		} else if (name.equals("database")) {
			configuration.setPathDatabase(Paths.get(value));
		} else if (name.equals("width")) {
			configuration.setWidth(parseInteger(value));
		} else if (name.equals("height")) {
			configuration.setHeight(parseInteger(value));
		} else if (name.equals("zoom")) {
			configuration.setZoom(parseInteger(value));
		} else if (name.equals("lon")) {
			configuration.setLon(parseDouble(value));
		} else if (name.equals("lat")) {
			configuration.setLat(parseDouble(value));
		} else if (name.equals("look-and-feel")) {
			configuration.setLookAndFeel(value);
		} else {
			logger.debug(String.format("unhandled option: %s:%s", name, value));
		}
	}

	private boolean parseBoolean(String value)
	{
		return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
	}

	private int parseInteger(String value)
	{
		return Integer.parseInt(value);
	}

	private double parseDouble(String value)
	{
		return Double.parseDouble(value);
	}

}
