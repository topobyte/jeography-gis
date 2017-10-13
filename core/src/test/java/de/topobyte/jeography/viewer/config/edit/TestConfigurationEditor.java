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

package de.topobyte.jeography.viewer.config.edit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.melon.io.StreamUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestConfigurationEditor
{

	final static Logger logger = LoggerFactory
			.getLogger(TestConfigurationEditor.class);

	/**
	 * A test for the ConfigurationEditor
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args) throws IOException
	{
		final JFrame frame = new JFrame("Configuration Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

		ConfigurationEditor configurationEditor = new ConfigurationEditor(
				configuration) {

			private static final long serialVersionUID = 1L;

			@Override
			public void ok()
			{
				frame.dispose();
			}

			@Override
			public void cancel()
			{
				frame.dispose();
			}

		};
		frame.setContentPane(configurationEditor);

		frame.setSize(500, 500);
		frame.setVisible(true);
	}

}
