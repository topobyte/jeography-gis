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

package de.topobyte.jeography.viewer.config.edit.selectable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface Selectable
{

	/**
	 * @return whether this component is currently selected;
	 */
	public boolean isSelected();

	/**
	 * Set whether this component is currently selected;
	 * 
	 * @param state
	 *            selected or not selected
	 */
	public void setSelected(boolean state);

	/**
	 * Add a new listener that informs you about selection changes.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addSelectionListener(SelectionListener listener);

	/**
	 * Remove the denoted listener from the list of listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeSelectionListener(SelectionListener listener);

}
