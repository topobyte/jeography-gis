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

import de.topobyte.jeography.tiles.LoadListener;

/**
 * @param <T>
 *            the type of things this manager provides images for.
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface ImageManager<T, D>
{

	/**
	 * Get the image for the given thing.
	 * 
	 * @param thing
	 *            the thing to get an image for.
	 * @return an instance of BufferedImage
	 */
	public D get(T thing);

	/**
	 * Notify the manager that the specified thing will be needed soon. This
	 * methods should be called before actually calling {@code get} subsequently
	 * for more than one item so that the manager may be able to reorganize its
	 * structures to not throwing out things out of its cache that are known to
	 * be used soon.
	 * 
	 * @param thing
	 *            the thing you need to use soon.
	 */
	public void willNeed(T thing);

	/**
	 * @param listener
	 *            the listener to add.
	 */
	public void addLoadListener(LoadListener<T, D> listener);

	/**
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeLoadListener(LoadListener<T, D> listener);

	/**
	 * Destroy this manager and release all resources occupied
	 */
	public void destroy();

	/**
	 * Let the implementation optimize the cache by having a minimum size.
	 */
	public void setCacheHintMinimumSize(int size);

}
