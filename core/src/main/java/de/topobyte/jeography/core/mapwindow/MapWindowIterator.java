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

package de.topobyte.jeography.core.mapwindow;

import java.util.Iterator;

import de.topobyte.jeography.core.TileOnWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
class MapWindowIterator implements Iterator<TileOnWindow>
{

	private SteppedMapWindow mapWindow;
	private int i = 0;
	private int nx = 0;
	private int ny = 0;
	private int n = 0;

	public MapWindowIterator(SteppedMapWindow mapWindow)
	{
		this.mapWindow = mapWindow;
		nx = mapWindow.getNumTilesX();
		ny = mapWindow.getNumTilesY();
		n = nx * ny;
	}

	@Override
	public boolean hasNext()
	{
		return i < n;
	}

	@Override
	public TileOnWindow next()
	{
		int x = i % nx;
		int y = i / nx;
		int tx = mapWindow.tx + x;
		int ty = mapWindow.ty + y;
		int dx = -mapWindow.xoff + x * mapWindow.tileWidth;
		int dy = -mapWindow.yoff + y * mapWindow.tileHeight;
		i += 1;

		int ntiles = 1 << mapWindow.zoom;
		// TODO: using loops here is simple, yet a bit of a overhead
		while (tx < 0) {
			tx = ntiles + tx;
		}
		while (tx >= ntiles) {
			tx = tx - ntiles;
		}
		return new TileOnWindow(mapWindow.zoom, tx, ty, dx, dy);
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException(
				"you can't remove tiles from a MapWindow");
	}

}
