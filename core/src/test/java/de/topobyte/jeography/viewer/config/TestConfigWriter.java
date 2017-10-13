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
import java.io.InputStream;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.melon.io.StreamUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestConfigWriter
{

	final static Logger logger = LoggerFactory
			.getLogger(TestConfigWriter.class);

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
		Configuration configuration = Configuration
				.createDefaultConfiguration();

		Path path = ConfigurationHelper.getUserConfigurationFilePath();
		InputStream input = StreamUtil.bufferedInputStream(path);

		try {
			configuration = ConfigReader.read(input);
		} catch (Exception e) {
			logger.debug("exception while reading config: " + e.getMessage());
		}

		input.close();

		ConfigWriter.write(configuration, System.out);
	}

}
