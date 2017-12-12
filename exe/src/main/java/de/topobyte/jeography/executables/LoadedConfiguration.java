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

import java.nio.file.Path;

import de.topobyte.jeography.viewer.config.Configuration;

public class LoadedConfiguration
{

	private Path configFile;
	private Configuration configuration;

	public LoadedConfiguration(Path configFile, Configuration configuration)
	{
		this.configFile = configFile;
		this.configuration = configuration;
	}

	public Path getConfigFile()
	{
		return configFile;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

}
