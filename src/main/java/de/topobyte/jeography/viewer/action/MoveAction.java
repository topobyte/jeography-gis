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

import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MoveAction extends ViewerAction
{

	private static final long serialVersionUID = -146376865443735561L;

	/**
	 * Create an action for moving the viewport of the viewer.
	 * 
	 * @param viewer
	 *            the viewer to work with.
	 * @param name
	 *            the name of the action.
	 * @param description
	 *            the description of the action.
	 * @param filename
	 *            the file of the icon.
	 * @param dx
	 *            the amount to move in the direction of the x-axis.
	 * @param dy
	 *            the amount to move in the direction of the y-axis.
	 */
	public MoveAction(Viewer viewer, String name, String description,
			String filename, int dx, int dy)
	{
		super(viewer, filename);
		setName(name);
		setDescription(description);
		this.dx = dx;
		this.dy = dy;
	}

	private int dx, dy = 0;

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getViewer().getMapWindow().move(dx, dy);
		getViewer().repaint();
	}

}
