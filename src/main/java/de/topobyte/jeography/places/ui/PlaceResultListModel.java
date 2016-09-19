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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SortOrder;
import javax.swing.event.ListDataEvent;

import de.topobyte.jeography.places.Dao;
import de.topobyte.jeography.places.model.Place;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.swing.util.DefaultElementWrapper;
import de.topobyte.swing.util.ElementWrapper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PlaceResultListModel extends
		AbstractResultListModel<ElementWrapper<Place>> implements
		UpdateableDataListModel<ElementWrapper<Place>>
{

	private List<ElementWrapper<Place>> results;
	private static final int max = 100;

	private IConnection connection;
	private Dao dao;

	public PlaceResultListModel(IConnection connection) throws QueryException
	{
		this.connection = connection;
		dao = new Dao(connection);
	}

	@Override
	public void update(String textNew) throws QueryException
	{
		List<Place> list = dao.getPlaces(textNew, SortOrder.ASCENDING, max, 0);

		List<ElementWrapper<Place>> newResults = new ArrayList<>();
		for (Place poi : list) {
			newResults.add(new ElementWrapperImpl(poi));
		}
		results = newResults;

		fire(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
	}

	@Override
	public ElementWrapper<Place> getElementAt(int index)
	{
		if (results.size() <= index) {
			return null;
		}
		return results.get(index);
	}

	@Override
	public int getSize()
	{
		if (results == null) {
			return 0;
		}
		return results.size() > max ? max : results.size();
	}

	@Override
	public Place getObject(int index)
	{
		return results.get(index).getElement();
	}

	public String toString(Place element)
	{
		StringBuilder buffer = new StringBuilder();
		Set<String> names = new HashSet<>();
		names.add(element.getName());
		buffer.append(element.getName());

		int n = 0;
		for (String name : element.getAltNames().values()) {
			if (!names.contains(name)) {
				if (n++ == 0) {
					buffer.append(" (");
				} else {
					buffer.append(", ");
				}
				names.add(name);
				buffer.append(name);
			}
		}
		if (n > 0) {
			buffer.append(")");
		}

		return buffer.toString();
	}

	private class ElementWrapperImpl extends DefaultElementWrapper<Place>
	{

		public ElementWrapperImpl(Place element)
		{
			super(element);
		}

		@Override
		public String toString()
		{
			return PlaceResultListModel.this.toString(element);
		}

	}

}
