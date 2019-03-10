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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileOnWindow extends Tile
{

	private static final long serialVersionUID = 4122776265368269212L;

	public int dx, dy;

	/**
	 * Create new TileOnWindow instance.
	 * 
	 * @param zoom
	 *            the zoomlevel.
	 * @param tx
	 *            the x coordinate.
	 * @param ty
	 *            the y coordinate.
	 * @param dx
	 *            x offset of the tile on the window.
	 * @param dy
	 *            y offset of the tile on the window.
	 */
	public TileOnWindow(int zoom, int tx, int ty, int dx, int dy)
	{
		super(zoom, tx, ty);
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * @return the offset of the tile in the window.
	 */
	public int getDX()
	{
		return dx;
	}

	/**
	 * @return the offset of the tile in the window.
	 */
	public int getDY()
	{
		return dy;
	}

}
