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

package de.topobyte.jeography.places.model;

import java.util.Map;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Place
{

	private String name;
	private Map<String, String> altNames;
	private double lon;
	private double lat;

	public Place(String name, Map<String, String> altNames, double lon,
			double lat)
	{
		this.name = name;
		this.altNames = altNames;
		this.lon = lon;
		this.lat = lat;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, String> getAltNames()
	{
		return altNames;
	}

	public void setAltNames(Map<String, String> altNames)
	{
		this.altNames = altNames;
	}

	public double getLon()
	{
		return lon;
	}

	public void setLon(double lon)
	{
		this.lon = lon;
	}

	public double getLat()
	{
		return lat;
	}

	public void setLat(double lat)
	{
		this.lat = lat;
	}

}
