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

import java.io.Serializable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Tile implements Serializable
{

	private static final long serialVersionUID = -2268865981792560407L;

	/**
	 * The size of a tile.
	 */
	public static int SIZE = 256;
	int zoom, tx, ty;

	/**
	 * Default constructor.
	 * 
	 * @param zoom
	 *            the zoom level.
	 * @param tx
	 *            the x coordinate.
	 * @param ty
	 *            the y coordinate.
	 */
	public Tile(int zoom, int tx, int ty)
	{
		this.zoom = zoom;
		this.tx = tx;
		this.ty = ty;
	}

	@Override
	public int hashCode()
	{
		return zoom * tx * ty;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Tile) {
			Tile other = (Tile) o;
			return other.zoom == zoom && other.tx == tx && other.ty == ty;
		}
		return false;
	}

	/**
	 * @return the zoom level.
	 */
	public int getZoom()
	{
		return zoom;
	}

	/**
	 * @return the x number of this tile.
	 */
	public int getTx()
	{
		return tx;
	}

	/**
	 * @return the y number of this tile
	 */
	public int getTy()
	{
		return ty;
	}

}
