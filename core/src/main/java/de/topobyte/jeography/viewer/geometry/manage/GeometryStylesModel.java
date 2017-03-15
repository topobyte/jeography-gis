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

package de.topobyte.jeography.viewer.geometry.manage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryStylesModel implements ListModel<GeometryStyle>
{

	List<GeometryStyle> styles = new ArrayList<>();
	private List<ListDataListener> listeners = new ArrayList<>();

	/**
	 * Add this style.
	 * 
	 * @param style
	 *            the style to add.
	 */
	public void add(GeometryStyle style)
	{
		styles.add(style);
		int i = styles.indexOf(style);
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
				i, i);
		for (ListDataListener ldl : listeners) {
			ldl.intervalAdded(e);
		}
	}

	@Override
	public int getSize()
	{
		return styles.size();
	}

	@Override
	public GeometryStyle getElementAt(int index)
	{
		return styles.get(index);
	}

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
	 * Get the style by the denoted name.
	 * 
	 * @param name
	 *            the name to retrieve a style for.
	 * @return the style or null.
	 */
	public GeometryStyle getStyleByName(String name)
	{
		for (GeometryStyle style : styles) {
			if (style.getName().equals(name)) {
				return style;
			}
		}
		return null;
	}

}
