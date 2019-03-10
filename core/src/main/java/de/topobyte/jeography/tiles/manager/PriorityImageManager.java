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

package de.topobyte.jeography.tiles.manager;

/**
 * An extension of the ImageManager interface that allows priority in requests.
 * 
 * @param <T>
 *            the type of keys.
 * @param <D>
 *            the type of data.
 * @param <P>
 *            the type of priority elements.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface PriorityImageManager<T, D, P extends Comparable<P>>
		extends ImageManager<T, D>
{

	/**
	 * Get data for the specified thing with the specified priority.
	 * 
	 * @param thing
	 *            the thing to get data for.
	 * @param priority
	 *            the priority of the query.
	 * @return the data for the thing.
	 */
	public D get(T thing, P priority);

	/**
	 * Cancel all jobs in the queue so that new jobs get done faster.
	 */
	public void cancelJobs();

	/**
	 * Tell this manager to ignore all data that are currently in production due
	 * to request that happened before calling this method. This may be used if
	 * some configuration has changed within the underlying ImageSource to make
	 * sure that things don't get mixed up.
	 */
	public void setIgnorePendingProductions();

}
