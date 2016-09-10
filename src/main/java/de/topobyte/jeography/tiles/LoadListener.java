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

package de.topobyte.jeography.tiles;

/**
 * @param <T>
 *            the type of things.
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface LoadListener<T, D>
{

	/**
	 * Notify about the loading of this thing.
	 * 
	 * @param thing
	 *            the thing that got loaded.
	 * @param data
	 *            the data loaded.
	 */
	void loaded(T thing, D data);

	/**
	 * Notify about the failure of loading this thing.
	 * 
	 * @param thing
	 *            the thing that failed loading.
	 */
	void loadFailed(T thing);

}
