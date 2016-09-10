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

import de.topboyte.interactiveview.ZoomChangedListener;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ZoomAction extends ViewerAction implements ZoomChangedListener
{

	private static final long serialVersionUID = 365467138876660750L;

	static final Logger logger = LoggerFactory.getLogger(ZoomAction.class);

	private static String FILE_IN = "res/images/zoom-in.png";
	private static String FILE_OUT = "res/images/zoom-out.png";
	private boolean in;

	/**
	 * Create a new ZoomAction.
	 * 
	 * @param viewer
	 *            the viewer this action is about.
	 * @param in
	 *            true to zoom in, false to zoom out.
	 */
	public ZoomAction(Viewer viewer, boolean in)
	{
		super(viewer, in ? FILE_IN : FILE_OUT);
		this.in = in;

		viewer.getMapWindow().addZoomListener(this);
	}

	@Override
	public Object getValue(String key)
	{
		if (key == Action.SMALL_ICON) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return String.format("zoom %s", in ? "in" : "out");
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return String.format("zoom %s", in ? "in" : "out");
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (in) {
			getViewer().getMapWindow().zoomIn();
		} else {
			getViewer().getMapWindow().zoomOut();
		}
		getViewer().repaint();
	}

	@Override
	public boolean isEnabled()
	{
		return (in && getViewer().getZoomLevel() < getViewer()
				.getMaxZoomLevel())
				|| (!in && getViewer().getZoomLevel() > getViewer()
						.getMinZoomLevel());
	}

	@Override
	public void zoomChanged()
	{
		firePropertyChange("enabled", null, null);
	}

}
