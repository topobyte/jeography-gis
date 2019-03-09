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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.melon.io.StreamUtil;

public class Util
{

	final static Logger logger = LoggerFactory.getLogger(Util.class);

	public static LoadedConfiguration loadConfiguration(CommandLine line)
	{
		Path configFile = null;
		Configuration configuration = Configuration
				.createDefaultConfiguration();

		if (line.hasOption("config")) {
			String argConfigFile = line.getOptionValue("config");
			configFile = Paths.get(argConfigFile);
			try (InputStream configInput = StreamUtil
					.bufferedInputStream(configFile)) {
				configuration = ConfigReader.read(configInput);
			} catch (Exception e) {
				logger.warn(
						"unable to read configuration specified at command-line",
						e);
				logger.warn("using default configuration");
			}
		} else {
			configFile = ConfigurationHelper.getUserConfigurationFilePath();
			logger.debug("default user config file: " + configFile);
			try (InputStream configInput = StreamUtil
					.bufferedInputStream(configFile)) {
				configuration = ConfigReader.read(configInput);
			} catch (FileNotFoundException e) {
				logger.warn(
						"no configuration file found, using default configuration");
			} catch (Exception e) {
				logger.warn("unable to read configuration at user home", e);
				logger.warn("using default configuration");
			}
		}

		return new LoadedConfiguration(configFile, configuration);
	}

}
