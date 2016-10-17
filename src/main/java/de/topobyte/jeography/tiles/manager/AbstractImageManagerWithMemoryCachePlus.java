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

import de.topobyte.jeography.tiles.cache.MemoryCachePlus;

/**
 * @param <T>
 *            type of keys
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractImageManagerWithMemoryCachePlus<T, D>
		extends AbstractImageManager<T, D>
{

	protected int desiredCacheSize;

	protected MemoryCachePlus<T, D> memoryCache;

	public AbstractImageManagerWithMemoryCachePlus()
	{
		this(150);
	}

	public AbstractImageManagerWithMemoryCachePlus(int desiredCacheSize)
	{
		this.desiredCacheSize = desiredCacheSize;
		memoryCache = new MemoryCachePlus<>(desiredCacheSize);
	}

	@Override
	public void willNeed(T thing)
	{
		memoryCache.refresh(thing);
	}

	@Override
	public void setCacheHintMinimumSize(int size)
	{
		if (memoryCache.getSize() < size) {
			memoryCache.setSize(size);
		} else if (size < desiredCacheSize) {
			memoryCache.setSize(desiredCacheSize);
		}
	}

}
