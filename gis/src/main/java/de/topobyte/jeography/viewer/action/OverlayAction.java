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
public class OverlayAction extends ViewerAction
{

	private static final long serialVersionUID = 1560915120204782902L;

	static final Logger logger = LoggerFactory.getLogger(OverlayAction.class);

	private static final String FILE = "res/images/stock_styles.png";

	/**
	 * Constructor
	 * 
	 * @param viewer
	 *            the viewer this action is about
	 */
	public OverlayAction(Viewer viewer)
	{
		super(viewer, FILE);
	}

	private String text = "Overlay";

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return text;
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle visibility of overlay";
		} else if (key.equals(Action.SELECTED_KEY)) {
			return new Boolean(getViewer().isDrawOverlay());
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		getViewer().setDrawOverlay(!getViewer().isDrawOverlay());
		firePropertyChange(Action.SELECTED_KEY, null, null);
	}

}
