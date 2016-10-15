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

import javax.swing.Action;

import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import de.topobyte.jeography.viewer.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MapWindowPanelAction extends BooleanAction implements
		CDockableStateListener
{

	private static final long serialVersionUID = 3970439392210144565L;

	private final JeographyGIS gis;

	/**
	 * @param gis
	 *            the JeographyGIS instance
	 */
	public MapWindowPanelAction(JeographyGIS gis)
	{
		super("MapWindowPanel", "toggle visibility of the MapWindowPanel");
		this.gis = gis;
		setIconFromResource("res/images/stock_draw-line-45.png");
		gis.getMapWindowPanelDialog().addCDockableStateListener(this);
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

	@Override
	public boolean getState()
	{
		return gis.getMapWindowPanelDialog().isVisible();
	}

	@Override
	public void toggleState()
	{
		boolean visible = gis.getMapWindowPanelDialog().isVisible();
		gis.getMapWindowPanelDialog().setVisible(!visible);
	}

}
