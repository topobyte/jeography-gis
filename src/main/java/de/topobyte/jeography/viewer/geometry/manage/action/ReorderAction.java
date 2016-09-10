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

package de.topobyte.jeography.viewer.geometry.manage.action;

import javax.swing.Action;

import de.topobyte.jeography.viewer.action.SimpleAction;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class ReorderAction extends SimpleAction
{

	private static final long serialVersionUID = 1340496537750134922L;

	/**
	 * The value for an 'up' action
	 */
	public static final int UP = 0;
	/**
	 * The value for an 'down' action
	 */
	public static final int DOWN = 1;

	private static final String fileUp = "res/images/go-up.png";
	private static final String fileDown = "res/images/go-down.png";

	/**
	 * The direction of this action
	 */
	protected int direction;

	/**
	 * Create a new ReorderAction.
	 * 
	 * @param direction
	 *            one of UP or DOWN.
	 */
	public ReorderAction(int direction)
	{
		super();
		this.direction = direction;
		if (direction == UP) {
			this.setIconFromResource(fileUp);
		} else {
			this.setIconFromResource(fileDown);
		}
	}

	@Override
	public Object getValue(String key)
	{
		// System.out.println(key);
		if (key.equals("SmallIcon")) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return direction == UP ? "up" : "down";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "move an item " + (direction == UP ? "up" : "down");
		}
		return null;
	}

}
