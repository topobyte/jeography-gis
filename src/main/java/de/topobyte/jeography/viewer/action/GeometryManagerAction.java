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

import javax.swing.Action;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.geometry.manage.VisibilityListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryManagerAction extends GISAction implements
		VisibilityListener
{

	private static final long serialVersionUID = 3260669993327051070L;

	/**
	 * @param gis
	 *            the JeographyGIS instance
	 */
	public GeometryManagerAction(JeographyGIS gis)
	{
		super(gis, "res/images/polygonn.png");
		getGIS().getGeometryManagerDialog().addVisibilityListener(this);
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return getGIS().getGeometryManagerDialog().isVisible();
		} else if (key.equals(Action.NAME)) {
			return "Geometry Manager";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle visibility of GeometryManager";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean visible = getGIS().getGeometryManagerDialog().isVisible();
		getGIS().getGeometryManagerDialog().setVisible(!visible);
	}

	@Override
	public void visibilityChanged(boolean visible)
	{
		firePropertyChange(Action.SELECTED_KEY, !visible, visible);
	}

}
