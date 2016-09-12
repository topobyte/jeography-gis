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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Location
{

	private boolean hasZoom;
	private int zoom;
	private double lon;
	private double lat;

	public Location(double lon, double lat)
	{
		this.lon = lon;
		this.lat = lat;
		hasZoom = false;
	}

	public Location(double lon, double lat, int zoom)
	{
		this.lon = lon;
		this.lat = lat;
		this.zoom = zoom;
		hasZoom = true;
	}

	public boolean hasZoom()
	{
		return hasZoom;
	}

	public int getZoom()
	{
		return zoom;
	}

	public double getLon()
	{
		return lon;
	}

	public double getLat()
	{
		return lat;
	}

	@Override
	public String toString()
	{
		if (hasZoom) {
			return String.format("%f, %f, %d", lon, lat, zoom);
		} else {
			return String.format("%f, %f", lon, lat);
		}
	}

}
