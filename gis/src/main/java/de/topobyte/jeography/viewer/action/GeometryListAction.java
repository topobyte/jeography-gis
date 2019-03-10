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

package de.topobyte.jeography.viewer.action;

import java.awt.event.ActionEvent;

import de.topobyte.jeography.viewer.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListAction extends GISAction
{

	private static final long serialVersionUID = 8148758442262262122L;

	public GeometryListAction(JeographyGIS gis)
	{
		super(gis, null);

		setName("Geometry list");
		setDescription("show a list of geometries to display as overlay");
		setIconFromResource("res/images/polygonn.png");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getGIS().createGeometryList();
	}

}
