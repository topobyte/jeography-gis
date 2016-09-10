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

package de.topobyte.jeography.tiles.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <K>
 *            the key type.
 * @param <V>
 *            the value type.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MemoryCache<K, V>
{

	private int size = 20;
	private Map<K, V> map = new HashMap<>();
	private List<K> keys = new ArrayList<>();

	/**
	 * Create a memory cache.
	 * 
	 * @param size
	 *            the number of elements to store.
	 */
	public MemoryCache(int size)
	{
		this.size = size;
	}

	public synchronized int getSize()
	{
		return size;
	}

	public synchronized void setSize(int size)
	{
		this.size = size;
		while (keys.size() > size) {
			K removed = keys.remove(0);
			map.remove(removed);
		}
	}

	/**
	 * Put key, value into the cache.
	 * 
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 */
	public synchronized void put(K key, V value)
	{
		if (map.containsKey(key)) {
			// replace...
			map.put(key, value);
			// TODO: rearrange in queue
		} else {
			map.put(key, value);
			keys.add(key);
			if (keys.size() > size) {
				K removed = keys.remove(0);
				map.remove(removed);
			}
		}
	}

	/**
	 * Get the stored element.
	 * 
	 * @param key
	 *            the key to retrieve a value for.
	 * @return the value.
	 */
	public synchronized V get(K key)
	{
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}

	/**
	 * Remove and get the stored element if any.
	 * 
	 * @param key
	 *            the key to remove the value for.
	 * @return the removed element or null.
	 */
	public synchronized V remove(K key)
	{
		if (map.containsKey(key)) {
			return map.remove(key);
		}
		return null;
	}

	/**
	 * Clear this cache. Removes all elements.
	 */
	public synchronized void clear()
	{
		keys.clear();
		map.clear();
	}

}
