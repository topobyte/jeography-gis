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

package de.topobyte.jeography.viewer.geometry.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.swing.util.list.ArrayListModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListModel extends ArrayListModel<Geometry>
{

	private static final long serialVersionUID = -3594464507636535851L;

	static final Logger logger2 = LoggerFactory
			.getLogger(GeometryListModel.class);

	/**
	 * Default constructor
	 */
	public GeometryListModel()
	{
		// default constructor
	}

}
