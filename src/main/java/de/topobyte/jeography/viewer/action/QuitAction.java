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
import javax.swing.JFrame;

import de.topobyte.jeography.executables.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class QuitAction extends GISAction
{

	private static final long serialVersionUID = -6668467091115019208L;

	private final JFrame frame;

	/**
	 * Default constructor for this action.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this action is about
	 * @param frame
	 *            the frame to close on exit.
	 */
	public QuitAction(JeographyGIS gis, JFrame frame)
	{
		super(gis, "res/images/gtk-quit.png");
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JeographyGIS.showReallyExitDialog(frame);
	}

	@Override
	public Object getValue(String key)
	{
		if (key == Action.SMALL_ICON) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return "Quit";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "Quit";
		}
		return null;
	}

	@Override
	public void putValue(String key, Object value)
	{
		//
	}

	@Override
	public void setEnabled(boolean b)
	{
		//
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

}
