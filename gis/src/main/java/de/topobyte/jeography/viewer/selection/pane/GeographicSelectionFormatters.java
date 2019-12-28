// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.selection.pane;

import java.util.ArrayList;
import java.util.List;

import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelectionFormatter;

public class GeographicSelectionFormatters
{

	public static List<GeographicSelectionFormatter> FORMATTERS_BBOX = new ArrayList<>();
	static {
		FORMATTERS_BBOX.add(new GeographicSelectionFormatter(
				"lon1,lat1:lon2,lat2", "%f,%f:%f,%f"));
		FORMATTERS_BBOX.add(new GeographicSelectionFormatter(
				"lon1,lat1,lon2,lat2", "%f,%f,%f,%f"));
		FORMATTERS_BBOX
				.add(new GeographicSelectionFormatter("xml", patternForXml(6)));
	}

	public static List<GeographicSelectionFormatter> FORMATTERS_DATA = new ArrayList<>();
	static {
		FORMATTERS_DATA.add(new GeographicSelectionFormatter(
				"OpenStreetMap API bbox query",
				"http://www.openstreetmap.org/api/0.6/map?bbox=%1$.6f,%4$.6f,%3$.6f,%2$.6f"));
		FORMATTERS_DATA.add(new GeographicSelectionFormatter(
				"Overpass API bbox query",
				"http://overpass-api.de/api/interpreter?data=(node(%4$.6f,%1$.6f,%2$.6f,%3$.6f);<;>;);out;"));
	}

	private static String patternForXml(int digits)
	{
		String pattern = "lon1=\"%%1$.%df\" lon2=\"%%3$.%df\" lat1=\"%%2$.%df\" lat2=\"%%4$.%df\"";
		String realPattern = String.format(pattern, digits, digits, digits,
				digits);
		return realPattern;
	}

}
