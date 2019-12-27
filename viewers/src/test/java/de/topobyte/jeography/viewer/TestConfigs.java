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

package de.topobyte.jeography.viewer;

import java.util.ArrayList;
import java.util.List;

import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.TileConfigUrl;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestConfigs
{

	public static String tileUrl1 = "http://tile.openstreetmap.org/%d/%d/%d.png";
	public static String tileUrl2 = "https://maps.wikimedia.org/osm/%d/%d/%d.png";
	public static String[] urls = new String[] { tileUrl1, tileUrl2 };

	public static List<TileConfig> configs = new ArrayList<>();
	static {
		for (int i = 0; i < urls.length; i++) {
			TileConfigUrl config = new TileConfigUrl(i, "name " + i, urls[i]);
			config.setUserAgent("Jeography GIS");
			configs.add(config);
		}
	}

}
