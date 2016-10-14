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

package de.topobyte.jeography.viewer.windowpane.patterns;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Formatters
{

	public static PatternFormatter FORMATTER_XML = new PatternFormatter("XML",
			"lat=\"%.5f\" lon=\"%.5f\"", CoordinateOrder.LAT_LON);
	public static PatternFormatter FORMATTER_FP_LAT_LON = new PatternFormatter(
			"Coordinates (lat, lon)", "%.6f, %.6f", CoordinateOrder.LAT_LON);
	public static PatternFormatter FORMATTER_FP_LON_LAT = new PatternFormatter(
			"Coordinates (lon, lat)", "%.6f, %.6f", CoordinateOrder.LON_LAT);
	public static DegreeFormatter FORMATTER_DEGREES = new DegreeFormatter();

}
