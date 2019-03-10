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

package de.topobyte.jeography.viewer.geometry.manage;

import de.topobyte.jeography.geometry.GeoObject;

/**
 * A class that bundles a tagged geometry with information about its' source and
 * an unique id.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryContainer
{

	int id;
	GeoObject geometry;
	GeometrySource source;

	/**
	 * Create a new Container for the denoted geometry.
	 * 
	 * @param id
	 *            the identifier for this container.
	 * @param geometry
	 *            the geometry contained.
	 * @param source
	 *            the source where this came from.
	 */
	public GeometryContainer(int id, GeoObject geometry, GeometrySource source)
	{
		this.id = id;
		this.geometry = geometry;
		this.source = source;
	}

	/**
	 * @return the identifier.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the geometry encapsulated.
	 */
	public GeoObject getGeometry()
	{
		return geometry;
	}

	/**
	 * Get the source of this container (where it came from).
	 * 
	 * @return an instance of GeometrySource
	 */
	public GeometrySource getSource()
	{
		return source;
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof GeometryContainer) {
			GeometryContainer other = (GeometryContainer) o;
			return other.id == id;
		}
		return false;
	}

}
