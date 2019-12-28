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

	public static List<GeographicSelectionFormatter> FORMATTERS = new ArrayList<>();
	static {
		FORMATTERS.add(new GeographicSelectionFormatter("lon1,lat1:lon2,lat2",
				"%f,%f:%f,%f"));
		FORMATTERS.add(new GeographicSelectionFormatter("lon1,lat1,lon2,lat2",
				"%f,%f,%f,%f"));
		FORMATTERS
				.add(new GeographicSelectionFormatter("xml", patternForXml(6)));
	}

	private static String patternForXml(int digits)
	{
		String pattern = "lon1=\"%%1$.%df\" lon2=\"%%3$.%df\" lat1=\"%%2$.%df\" lat2=\"%%4$.%df\"";
		String realPattern = String.format(pattern, digits, digits, digits,
				digits);
		return realPattern;
	}

}
