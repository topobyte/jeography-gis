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

import java.util.List;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryRule
{

	final String name;
	final String namespace;
	final String style;
	final List<GeometryRuleTag> filters;

	/**
	 * @param name
	 *            the name of this rule.
	 * @param namespace
	 *            a slash-seperated name
	 * @param style
	 *            a style to use
	 * @param filters
	 *            a list of tags to use for filtering
	 */
	public GeometryRule(String name, String namespace, String style,
			List<GeometryRuleTag> filters)
	{
		this.name = name;
		this.namespace = namespace;
		this.style = style;
		this.filters = filters;
	}

	/**
	 * @return the name of this rule.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the namespace used
	 */
	public String getNamespace()
	{
		return namespace;
	}

	/**
	 * @return the style used.
	 */
	public String getStyle()
	{
		return style;
	}

	/**
	 * @return the list of tags to filter the geometries.
	 */
	public List<GeometryRuleTag> getFilters()
	{
		return filters;
	}

}
