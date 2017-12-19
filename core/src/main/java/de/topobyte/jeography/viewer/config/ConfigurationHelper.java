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

import java.nio.file.Path;
import java.nio.file.Paths;

import de.topobyte.system.utils.SystemPaths;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ConfigurationHelper
{

	private static Path pathConfig = Paths.get(".config/jeography");

	/**
	 * Retrieve the path to the user's configuration file
	 * 
	 * @return the path to the user's configuration file
	 */
	public static Path getUserConfigurationFilePath()
	{
		Path jeography = SystemPaths.HOME.resolve(pathConfig);
		Path configXml = jeography.resolve("config.xml");
		return configXml;
	}

	/**
	 * Retrieve the path to the user's bookmarks file
	 * 
	 * @return the path to the user's bookmarks file
	 */
	public static Path getBookmarksFilePath()
	{
		Path jeography = SystemPaths.HOME.resolve(pathConfig);
		Path configXml = jeography.resolve("bookmarks.xml");
		return configXml;
	}

}
