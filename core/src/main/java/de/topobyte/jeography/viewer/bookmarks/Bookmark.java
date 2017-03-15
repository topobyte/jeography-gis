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

package de.topobyte.jeography.viewer.bookmarks;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Bookmark
{

	private String name;
	private Coordinate coordinate;

	public Bookmark(String name, Coordinate coordinate)
	{
		this.name = name;
		this.coordinate = coordinate;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Coordinate getCoordinate()
	{
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}

}
