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
import de.topobyte.jeography.executables.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionRectPanelAction extends GISAction implements
		CDockableStateListener
{

	private static final long serialVersionUID = 3260669993327051070L;

	/**
	 * @param gis
	 *            the JeographyGIS instance
	 */
	public SelectionRectPanelAction(JeographyGIS gis)
	{
		super(gis, "res/images/stock_draw-rectangle.png");
		getGIS().getSelectionRectPanelDialog().addCDockableStateListener(this);
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return getGIS().getSelectionRectPanelDialog().isVisible();
		} else if (key.equals(Action.NAME)) {
			return "Rect Selection Panel";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle visibility of Rect Selection Panel";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean visible = getGIS().getSelectionRectPanelDialog().isVisible();
		getGIS().getSelectionRectPanelDialog().setVisible(!visible);
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
