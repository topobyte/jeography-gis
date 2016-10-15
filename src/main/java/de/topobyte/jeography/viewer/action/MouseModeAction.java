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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.MouseMode;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MouseModeAction extends ViewerAction
		implements PropertyChangeListener
{

	private static final long serialVersionUID = 7666117851502031731L;

	final static Logger logger = LoggerFactory.getLogger(MouseModeAction.class);

	// private static final String FILE_IMAGE =
	// "/usr/share/icons/gnome/24x24/stock/image/stock_guides.png";

	private JeographyGIS gis;
	private MouseMode mouseMode;

	/**
	 * @param gis
	 *            the JeographyGIS instance to monitor with this action.
	 * @param mouseMode
	 *            one of the mouse modes to represent with this action.
	 * 
	 */
	public MouseModeAction(JeographyGIS gis, MouseMode mouseMode)
	{
		super(gis.getViewer(), null);
		this.gis = gis;
		this.mouseMode = mouseMode;
		switch (mouseMode) {
		case NAVIGATE:
			setIconFromResource("res/images/stock_zoom-shift.png");
			break;
		case SELECT:
			setIconFromResource("res/images/stock_draw-rectangle.png");
			break;
		case POLYSELECT:
			setIconFromResource("res/images/polygon.png");
			break;
		case DRAG:
			setIconFromResource("res/images/polygonn.png");
			break;
		}
		gis.addPropertyChangeListener(this);
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return new Boolean(gis.getMouseMode() == mouseMode);
		} else if (key.equals(Action.NAME)) {
			return mouseMode.toString();
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "mouse mode: " + mouseMode;
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		gis.setMouseMode(mouseMode);
		firePropertyChange(Action.SELECTED_KEY, false, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		String propertyName = evt.getPropertyName();
		logger.debug("property: " + propertyName);
		if (propertyName.equals("mouseMode")) {
			logger.debug("mousemode: " + evt.getOldValue() + "->"
					+ evt.getNewValue());
			if (mouseMode == evt.getOldValue()
					|| mouseMode == evt.getNewValue()) {
				firePropertyChange(Action.SELECTED_KEY, true, false);
			}
		}
	}

}
