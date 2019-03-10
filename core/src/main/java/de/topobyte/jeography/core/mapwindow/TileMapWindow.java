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

import de.topobyte.jeography.core.TileOnWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface TileMapWindow extends MapWindow, Iterable<TileOnWindow>
{

	public int getZoomLevel();

	/**
	 * Get the window's center's tile coordinate.
	 * 
	 * @return the center's tile coordinate.
	 */
	public double getCenterX();

	/**
	 * Get the window's center's tile coordinate.
	 * 
	 * @return the center's tile coordinate.
	 */
	public double getCenterY();

	/**
	 * Get the tile coordinate at the given x in view space.
	 * 
	 * @param px
	 *            the position in view space.
	 * @return the position in tile space.
	 */
	public double getPositionX(int px);

	/**
	 * Get the tile coordinate at the given y in view space.
	 * 
	 * @param py
	 *            the position in view space.
	 * @return the position in tile space.
	 */
	public double getPositionY(int py);

	public boolean setTileSize(int tileSize);

	public int getTileWidth();

	public int getTileHeight();

	public int getNumTilesX();

	public int getNumTilesY();

	public int minimumCacheSize();

	/**
	 * Add the given listener to the set of tile size change listeners. The
	 * listener will be notified on tile size events only.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addTileSizeListener(MapWindowTileSizeListener listener);

}
