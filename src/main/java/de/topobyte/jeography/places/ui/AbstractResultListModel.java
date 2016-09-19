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

package de.topobyte.jeography.places.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractResultListModel<E> implements ListModel<E>
{

	private List<ListDataListener> listeners = new ArrayList<>();

	@Override
	public void addListDataListener(ListDataListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
		listeners.remove(l);
	}

	/**
	 * Propagate the given event to all registered ListDataListeners.
	 * 
	 * @param e
	 *            the event to propagate.
	 */
	public void fire(ListDataEvent e)
	{
		for (ListDataListener l : listeners) {
			l.contentsChanged(e);
		}
	}

}
