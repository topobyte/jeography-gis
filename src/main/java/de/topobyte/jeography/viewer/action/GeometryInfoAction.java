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

import de.topobyte.jeography.viewer.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryInfoAction extends BooleanAction
{

	private static final long serialVersionUID = 8677899982048514674L;

	private final JeographyGIS gis;

	/**
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 */
	public GeometryInfoAction(JeographyGIS gis)
	{
		super(
				"show geometry information",
				"toggle whether to display information about the geometry at the current mouse position on click");
		this.gis = gis;
		setIconFromResource("res/images/polygonn_info.png");
	}

	@Override
	public boolean getState()
	{
		return gis.isShowGeometryInfo();
	}

	@Override
	public void toggleState()
	{
		gis.setShowGeometryInfo(!gis.isShowGeometryInfo());
	}

}
