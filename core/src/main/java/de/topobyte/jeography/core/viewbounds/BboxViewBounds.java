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

package de.topobyte.jeography.core.viewbounds;

import de.topobyte.adt.geo.BBox;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BboxViewBounds implements ViewBounds
{

	private double minLon;
	private double maxLon;
	private double minLat;
	private double maxLat;

	public BboxViewBounds(BBox bbox)
	{
		minLon = bbox.getLon1();
		maxLon = bbox.getLon2();
		minLat = bbox.getLat2();
		maxLat = bbox.getLat1();
	}

	@Override
	public BoundsInfo checkBounds(double lon, double lat)
	{
		boolean lonOk = lon >= minLon && lon <= maxLon;
		boolean latOk = lat >= minLat && lat <= maxLat;
		if (lonOk && latOk) {
			return BoundsInfo.OK;
		} else if (!lonOk && !latOk) {
			return BoundsInfo.LON_LAT_OUT_OF_BOUNDS;
		} else if (!lonOk) {
			return BoundsInfo.LON_OUT_OF_BOUNDS;
		} else /* if (!latOk) */ {
			return BoundsInfo.LAT_OUT_OF_BOUNDS;
		}
	}

	@Override
	public double fixLon(double lon)
	{
		if (lon > maxLon) {
			return maxLon;
		} else if (lon < minLon) {
			return minLon;
		}
		return lon;
	}

	@Override
	public double fixLat(double lat)
	{
		if (lat > maxLat) {
			return maxLat;
		} else if (lat < minLat) {
			return minLat;
		}
		return lat;
	}

}
