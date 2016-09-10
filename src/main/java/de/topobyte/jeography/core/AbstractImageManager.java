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

package de.topobyte.jeography.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 *            type of keys
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractImageManager<T, D> implements ImageManager<T, D>
{

	private List<LoadListener<T, D>> listeners = new ArrayList<>();

	@Override
	public void addLoadListener(LoadListener<T, D> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeLoadListener(LoadListener<T, D> listener)
	{
		listeners.remove(listener);
	}

	/**
	 * Notify all listeners about a loaded thing.
	 * 
	 * @param thing
	 *            the loaded thing
	 * @param image
	 *            the image provided
	 */
	protected void notifyListeners(T thing, D image)
	{
		for (LoadListener<T, D> listener : listeners) {
			listener.loaded(thing, image);
		}
	}

	/**
	 * Notify listeners that the given image has failed to load.
	 * 
	 * @param thing
	 *            which thing has failed to load.
	 */
	protected void notifyListenersFail(T thing)
	{
		for (LoadListener<T, D> listener : listeners) {
			listener.loadFailed(thing);
		}
	}

}
