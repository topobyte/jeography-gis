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

import javax.swing.SortOrder;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.places.Dao;
import de.topobyte.jeography.places.model.Place;
import de.topobyte.luqe.iface.QueryException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SearchWorker implements Runnable
{

	final static Logger logger = LoggerFactory.getLogger(SearchWorker.class);

	private Object wait = new Object();

	private String last = null;

	private SearchUI ui;
	private Dao dao;

	public SearchWorker(SearchUI ui, Dao dao)
	{
		this.ui = ui;
		this.dao = dao;
	}

	public void kickOff()
	{
		synchronized (wait) {
			wait.notify();
		}
	}

	@Override
	public void run()
	{
		while (true) {
			logger.debug("start of loop");
			String query = ui.getQuery();
			boolean bothNull = query == null && last == null;
			boolean nonNull = query != null && last != null;
			if (bothNull || (nonNull && query.equals(last))) {
				try {
					logger.debug("waiting");
					synchronized (wait) {
						wait.wait();
					}
				} catch (InterruptedException e) {
					continue;
				}
				continue;
			}
			// At this point, not both can be null, and their are not equal
			if (query == null) {
				logger.debug("new query is null");
				updateWithResults(null, new ArrayList<Place>());
				last = null;
				continue;
			}

			logger.debug("new query is: " + query);
			try {
				List<Place> results = dao.getPlaces(query, SortOrder.ASCENDING,
						SearchConfig.MAX_RESULTS, 0);
				logger.debug("number of results: " + results.size());
				updateWithResults(query, results);
				last = query;
			} catch (QueryException e) {
				logger.error("error while querying database", e);
			}
		}
	}

	private void updateWithResults(final String query, final List<Place> results)
	{
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				ui.updateWithResults(query, results);
			}

		});
	}

}
