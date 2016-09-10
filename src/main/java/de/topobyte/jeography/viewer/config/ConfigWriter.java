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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ConfigWriter
{

	final static Logger logger = LoggerFactory.getLogger(ConfigWriter.class);

	/**
	 * Test the configuration writer
	 * 
	 * @param args
	 *            none
	 * @throws IOException
	 *             on IO failure
	 */
	public static void main(String[] args) throws IOException
	{
		BasicConfigurator.configure();

		Configuration configuration = Configuration
				.createDefaultConfiguration();

		String path = ConfigurationHelper.getUserConfigurationFilePath();
		try {
			configuration = ConfigReader.read(path);
		} catch (Exception e) {
			logger.debug("exception while reading config: " + e.getMessage());
		}

		write(configuration, System.out);
	}

	/**
	 * Write the denoted configuration to the denoted OutputStream
	 * 
	 * @param configuration
	 *            the configuration to write
	 * @param out
	 *            the destination
	 * @throws IOException
	 *             on IO failure
	 */
	public static void write(Configuration configuration, OutputStream out)
			throws IOException
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Document document = documentFactory.createDocument();

		buildDocument(documentFactory, document, configuration);

		OutputFormat pretty = OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter = new XMLWriter(pretty);
		xmlWriter.setOutputStream(out);
		xmlWriter.write(document);
	}

	private static void buildDocument(DocumentFactory documentFactory,
			Document document, Configuration configuration)
	{

		Element eConfiguration = documentFactory.createElement("configuration");
		document.add(eConfiguration);

		addOption(documentFactory, eConfiguration, "grid",
				configuration.isShowGrid());
		addOption(documentFactory, eConfiguration, "tilenumbers",
				configuration.isShowTileNumbers());
		addOption(documentFactory, eConfiguration, "crosshair",
				configuration.isShowCrosshair());
		addOption(documentFactory, eConfiguration, "overlay",
				configuration.isShowOverlay());
		addOption(documentFactory, eConfiguration, "online",
				configuration.isOnline());
		addOption(documentFactory, eConfiguration, "database",
				configuration.getFileDb());
		addOption(documentFactory, eConfiguration, "width",
				configuration.getWidth());
		addOption(documentFactory, eConfiguration, "height",
				configuration.getHeight());
		addOption(documentFactory, eConfiguration, "lon",
				configuration.getLon());
		addOption(documentFactory, eConfiguration, "lat",
				configuration.getLat());
		addOption(documentFactory, eConfiguration, "zoom",
				configuration.getZoom());
		if (configuration.getLookAndFeel() != null) {
			addOption(documentFactory, eConfiguration, "look-and-feel",
					configuration.getLookAndFeel());
		}

		for (TileConfig config : configuration.getTileConfigs()) {
			addTileConfig(documentFactory, eConfiguration, config, "tiles");
		}
		for (TileConfig config : configuration.getTileConfigsOverlay()) {
			addTileConfig(documentFactory, eConfiguration, config, "overlay");
		}
	}

	private static void addTileConfig(DocumentFactory documentFactory,
			Element eConfiguration, TileConfig tileConfig, String tileConfigType)
	{
		Element main = documentFactory.createElement(tileConfigType);
		eConfiguration.add(main);
		main.add(documentFactory.createAttribute(main, "name",
				tileConfig.getName()));
		if (tileConfig instanceof TileConfigUrlDisk) {
			TileConfigUrlDisk urldisk = (TileConfigUrlDisk) tileConfig;
			String type = urldisk.getUrl() == null ? "disk" : "urldisk";
			main.add(documentFactory.createAttribute(main, "type", type));
			if (urldisk.getUrl() != null) {
				main.add(documentFactory.createAttribute(main, "url",
						urldisk.getUrl()));
			}
			main.add(documentFactory.createAttribute(main, "path",
					urldisk.getPath()));
			if (urldisk.getUserAgent() != null) {
				main.add(documentFactory.createAttribute(main, "userAgent",
						urldisk.getUserAgent()));
			}
		}
	}

	private static void addOption(DocumentFactory documentFactory,
			Element eConfiguration, String name, boolean bool)
	{
		String value = bool ? "yes" : "no";
		addOption(documentFactory, eConfiguration, name, value);
	}

	private static void addOption(DocumentFactory documentFactory,
			Element eConfiguration, String name, double val)
	{
		String value = String.format(Locale.US, "%f", val);
		addOption(documentFactory, eConfiguration, name, value);
	}

	private static void addOption(DocumentFactory documentFactory,
			Element eConfiguration, String name, int val)
	{
		String value = String.format("%d", val);
		addOption(documentFactory, eConfiguration, name, value);
	}

	private static void addOption(DocumentFactory documentFactory,
			Element eConfiguration, String name, String value)
	{
		Element eOption = documentFactory.createElement("option");
		eOption.add(documentFactory.createAttribute(eOption, "name", name));
		// convert null to ""
		String v = value == null ? "" : value;
		eOption.add(documentFactory.createAttribute(eOption, "value", v));
		eConfiguration.add(eOption);
	}

}
