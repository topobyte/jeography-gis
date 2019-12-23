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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowToolbarAction extends GISAction
{

	private static final long serialVersionUID = 3945889967966238110L;

	/**
	 * Constructor
	 * 
	 * @param gis
	 *            the app this is about.
	 */
	public ShowToolbarAction(JeographyGIS gis)
	{
		super(gis, null, "Toolbar");
		setDescription("toggle toolbar visibility");
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SELECTED_KEY)) {
			return Boolean.valueOf(getGIS().isShowToolBar());
		}
		return super.getValue(key);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getGIS().setShowToolBar(!getGIS().isShowToolBar());
		firePropertyChange(Action.SELECTED_KEY, null, null);
	}

}
