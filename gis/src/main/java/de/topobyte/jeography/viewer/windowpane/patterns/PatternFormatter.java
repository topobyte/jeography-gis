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
public class PatternFormatter implements CoordinateFormatter
{

	private String name;
	private String pattern;
	private CoordinateOrder coordinateOrder;

	public PatternFormatter(String name, String pattern,
			CoordinateOrder coordinateOrder)
	{
		this.name = name;
		this.pattern = pattern;
		this.coordinateOrder = coordinateOrder;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPattern()
	{
		return pattern;
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	public CoordinateOrder getCoordinateOrder()
	{
		return coordinateOrder;
	}

	public void setCoordinateOrder(CoordinateOrder coordinateOrder)
	{
		this.coordinateOrder = coordinateOrder;
	}

	@Override
	public String format(double lon, double lat)
	{
		if (coordinateOrder == CoordinateOrder.LAT_LON) {
			return String.format(pattern, lat, lon);
		} else if (coordinateOrder == CoordinateOrder.LON_LAT) {
			return String.format(pattern, lon, lat);
		}
		return null;
	}

}
