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

import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import de.topobyte.jeography.viewer.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionPolyPanelAction extends GISAction implements
		CDockableStateListener
{

	private static final long serialVersionUID = -5487694506653254293L;

	/**
	 * @param gis
	 *            the JeographyGIS instance
	 */
	public SelectionPolyPanelAction(JeographyGIS gis)
	{
		super(gis, "res/images/polygon.png");
		getGIS().getSelectionPolyPanelDialog().addCDockableStateListener(this);
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return getGIS().getSelectionPolyPanelDialog().isVisible();
		} else if (key.equals(Action.NAME)) {
			return "Poly Selection Panel";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle visibility of Poly Selection Panel";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean visible = getGIS().getSelectionPolyPanelDialog().isVisible();
		getGIS().getSelectionPolyPanelDialog().setVisible(!visible);
	}

	@Override
	public void extendedModeChanged(CDockable dockable, ExtendedMode mode)
	{
		// ignore
	}

	@Override
	public void visibilityChanged(CDockable dockable)
	{
		boolean visible = dockable.isVisible();
		firePropertyChange(Action.SELECTED_KEY, !visible, visible);
	}

}
