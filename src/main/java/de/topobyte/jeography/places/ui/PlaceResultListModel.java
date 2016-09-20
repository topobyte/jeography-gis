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

import java.util.List;

import javax.swing.SortOrder;
import javax.swing.event.ListDataEvent;

import de.topobyte.jeography.places.Dao;
import de.topobyte.jeography.places.model.Place;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PlaceResultListModel extends AbstractResultListModel<Place>
		implements UpdateableDataListModel<Place>
{

	private List<Place> results;
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
		results = dao.getPlaces(textNew, SortOrder.ASCENDING, max, 0);
		fire(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
	}

	@Override
	public Place getElementAt(int index)
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
		return results.get(index);
	}

}
