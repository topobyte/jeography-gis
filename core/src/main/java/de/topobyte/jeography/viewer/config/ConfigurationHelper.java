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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ConfigurationHelper
{

	/**
	 * Retrieve the path to the user's configuration file
	 * 
	 * @return the path to the user's configuration file
	 */
	public static String getUserConfigurationFilePath()
	{
		String home = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");

		String configFile = home + sep + ".config" + sep + "jeography" + sep
				+ "config.xml";

		return configFile;
	}

}
