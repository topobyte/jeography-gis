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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class BooleanAction extends SimpleAction
{

	private static final long serialVersionUID = 7648212308884524179L;

	/**
	 * @return whether this action is enabled.
	 */
	public abstract boolean getState();

	/**
	 * implement what happens when the state is toggled.
	 */
	public abstract void toggleState();

	/**
	 * Create a new boolean action.
	 * 
	 * @param name
	 *            the name of the action.
	 * @param description
	 *            the description of the action.
	 */
	public BooleanAction(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals(Action.SELECTED_KEY)) {
			return getState();
		}
		return super.getValue(key);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		toggleState();
		firePropertyChange(Action.SELECTED_KEY, null, null);
	}

}
