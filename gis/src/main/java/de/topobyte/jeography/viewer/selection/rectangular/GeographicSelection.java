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

package de.topobyte.jeography.viewer.selection.rectangular;

import de.topobyte.adt.geo.BBox;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeographicSelection extends Selection<Longitude, Latitude>
{

	/**
	 * Create a new geographic selection from the given coordinates.
	 * 
	 * @param x1
	 *            the first longitude
	 * @param x2
	 *            the second longitude
	 * @param y1
	 *            the first latitude
	 * @param y2
	 *            the seconds latitude.
	 */
	public GeographicSelection(Longitude x1, Longitude x2, Latitude y1,
			Latitude y2)
	{
		super(x1, x2, y1, y2);
	}

	/**
	 * Create a new geographic selection from the given coordinates.
	 * 
	 * @param x1
	 *            the first longitude
	 * @param x2
	 *            the second longitude
	 * @param y1
	 *            the first latitude
	 * @param y2
	 *            the seconds latitude.
	 */
	public GeographicSelection(double x1, double x2, double y1, double y2)
	{
		super(new Longitude(x1), new Longitude(x2), new Latitude(y1),
				new Latitude(y2));
	}

	/**
	 * @return a bounding box equivalent to this selection.
	 */
	public BBox toBoundingBox()
	{
		return new BBox(getX1().value(), getY1().value(), getX2().value(),
				getY2().value());
	}

}
