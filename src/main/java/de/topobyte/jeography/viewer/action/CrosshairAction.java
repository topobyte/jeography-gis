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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CrosshairAction extends ViewerAction
{

	private static final long serialVersionUID = 5898198182135459647L;

	static final Logger logger = LoggerFactory.getLogger(CrosshairAction.class);

	private static final String FILE_IMAGE = "res/images/stock_draw-line-45.png";

	/**
	 * @param viewer
	 *            the viewer to monitor with this action.
	 */
	public CrosshairAction(Viewer viewer)
	{
		super(viewer, FILE_IMAGE);
	}

	@Override
	public Object getValue(String key)
	{
		// System.out.println(key);
		if (key.equals("SmallIcon")) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return new Boolean(getViewer().isDrawCrosshair());
		} else if (key.equals(Action.NAME)) {
			return "Crosshair";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle visibility of crosshair";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getViewer().setDrawCrosshair(!getViewer().isDrawCrosshair());
		firePropertyChange(Action.SELECTED_KEY, null, null);
	}

}
