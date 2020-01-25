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

package de.topobyte.jeography.viewer.gotolocation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PatternRecognizerOsm2 implements PatternRecognizer
{

	// http://www.openstreetmap.org/?lat=%f&lon=%f&zoom=%d
	private String regex = "(https?://)?(www\\.)?openstreetmap.org/\\?lat=(.*)&lon=(.*)&zoom=(\\d*)";
	private Pattern pattern = Pattern.compile(regex);

	@Override
	public Location parse(String text)
	{
		text = text.trim();
		Matcher matcher = pattern.matcher(text);
		Location result = null;
		if (matcher.matches()) {
			try {
				String sLat = matcher.group(3);
				String sLon = matcher.group(4);
				String sZoom = matcher.group(5);
				int zoom = Integer.parseInt(sZoom);
				double lat = Double.parseDouble(sLat);
				double lon = Double.parseDouble(sLon);
				result = new Location(lon, lat, zoom);
			} catch (NumberFormatException e) {
				// return null
			}
		}
		return result;
	}

}
