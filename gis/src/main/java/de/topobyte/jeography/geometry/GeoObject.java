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

package de.topobyte.jeography.geometry;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeoObject implements Serializable
{

	private static final long serialVersionUID = -8475481080494882669L;

	private Geometry geometry;
	private Map<String, String> tags;

	/**
	 * Create a new tagged geometry instance.
	 * 
	 * @param geometry
	 *            the associated geometry.
	 */
	public GeoObject(Geometry geometry)
	{
		this.setGeometry(geometry);
		setTags(new HashMap<String, String>());
	}

	/**
	 * Add this tag to the set of tags.
	 * 
	 * @param key
	 *            the key of the tag.
	 * @param value
	 *            the value of the tag.
	 */
	public void addTag(String key, String value)
	{
		getTags().put(key, value);
	}

	/**
	 * @param geometry
	 *            the geometry to associate.
	 */
	public void setGeometry(Geometry geometry)
	{
		this.geometry = geometry;
	}

	/**
	 * @return the associated geometry
	 */
	public Geometry getGeometry()
	{
		return geometry;
	}

	/**
	 * @param tags
	 *            the tags for this instance.
	 */
	public void setTags(Map<String, String> tags)
	{
		this.tags = tags;
	}

	/**
	 * @return the tags of this instance.
	 */
	public Map<String, String> getTags()
	{
		return tags;
	}

}
