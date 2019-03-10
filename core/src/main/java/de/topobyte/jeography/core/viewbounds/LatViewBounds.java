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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class LatViewBounds implements ViewBounds
{

	private double minLat;
	private double maxLat;

	public LatViewBounds(double minLat, double maxLat)
	{
		this.minLat = minLat;
		this.maxLat = maxLat;
	}

	@Override
	public BoundsInfo checkBounds(double lon, double lat)
	{
		if (lat > maxLat) {
			return BoundsInfo.LAT_OUT_OF_BOUNDS;
		} else if (lat < minLat) {
			return BoundsInfo.LAT_OUT_OF_BOUNDS;
		}
		return BoundsInfo.OK;
	}

	@Override
	public double fixLon(double lon)
	{
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
