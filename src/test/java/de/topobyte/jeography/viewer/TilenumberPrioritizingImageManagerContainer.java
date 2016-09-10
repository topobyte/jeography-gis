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

package de.topobyte.jeography.viewer;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.manager.PriorityImageManager;

/**
 * This is a container for tile-based ImageManagers that will introduce a
 * priorization based on tile numbers.
 * 
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TilenumberPrioritizingImageManagerContainer<D> implements
		ImageManager<Tile, D>
{

	private PriorityImageManager<Tile, D, Integer> manager;

	/**
	 * Create a container for the specified ImageManager
	 * 
	 * @param manager
	 *            the manager to contain
	 */
	public TilenumberPrioritizingImageManagerContainer(
			PriorityImageManager<Tile, D, Integer> manager)
	{
		this.manager = manager;
	}

	@Override
	public D get(Tile tile)
	{
		int p = tile.getTy() * (1 << tile.getZoom()) + tile.getTx();
		return manager.get(tile, p);
	}

	@Override
	public void addLoadListener(LoadListener<Tile, D> listener)
	{
		manager.addLoadListener(listener);
	}

	@Override
	public void removeLoadListener(LoadListener<Tile, D> listener)
	{
		manager.removeLoadListener(listener);
	}

	@Override
	public void destroy()
	{
		manager.destroy();
	}

	@Override
	public void setCacheHintMinimumSize(int size)
	{
		// no cache -> no-op
	}

	@Override
	public void willNeed(Tile thing)
	{
		// do nothing here
	}

}
