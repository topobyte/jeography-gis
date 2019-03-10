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

import de.topobyte.adt.geo.BBox;
import de.topobyte.geomath.WGS84;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileUtil
{

	/**
	 * Get the bounding box that the given tile covers.
	 * 
	 * @param tile
	 *            the tile to compute a bounding box for.
	 * @return a BBox instance.
	 */
	public static BBox getBoundingBox(Tile tile)
	{
		double lat1 = WGS84.merc2lat(tile.getTy(), 1 << tile.getZoom());
		double lon1 = WGS84.merc2lon(tile.getTx(), 1 << tile.getZoom());
		double lat2 = WGS84.merc2lat(tile.getTy() + 1, 1 << tile.getZoom());
		double lon2 = WGS84.merc2lon(tile.getTx() + 1, 1 << tile.getZoom());
		return new BBox(lon1, lat1, lon2, lat2);
	}

	/**
	 * Get the bounding box that is covered by the given tile coordinates.
	 * Fractional tile-numbers are possible here to allow the computation of
	 * bounding boxes across partly covered tiles.
	 * 
	 * @param txStart
	 *            the start of the box in fractional tile-coordinates.
	 * @param txEnd
	 *            the end of the box in fractional tile-coordinates.
	 * @param tyStart
	 *            the start of the box in fractional tile-coordinates.
	 * @param tyEnd
	 *            the end of the box in fractional tile-coordinates.
	 * @param zoom
	 *            the zoom level the tile coordinates are to be resolved on
	 * @return a BBox instance.
	 */
	public static BBox getBoundingBox(double txStart, double txEnd,
			double tyStart, double tyEnd, int zoom)
	{
		double lat1 = WGS84.merc2lat(tyStart, 1 << zoom);
		double lon1 = WGS84.merc2lon(txStart, 1 << zoom);
		double lat2 = WGS84.merc2lat(tyEnd, 1 << zoom);
		double lon2 = WGS84.merc2lon(txEnd, 1 << zoom);
		return new BBox(lon1, lat1, lon2, lat2);
	}

	/**
	 * Check if a tile is valid, i.e. its coordinates are within the range
	 * [0..2^zoom].
	 * 
	 * @param tile
	 *            the tile to check
	 * @return whether the specified tile is valid.
	 */
	public static boolean isValid(Tile tile)
	{
		int zoom = tile.getZoom();
		int max = 1 << zoom;
		int tx = tile.getTx();
		int ty = tile.getTy();
		if (tx < 0 || ty < 0 || tx >= max || ty >= max) {
			return false;
		}
		return true;
	}

	/**
	 * Create an {@link Iterable} that contains all {@link Tile}s that are valid
	 * as of {@link #isValid(Tile)}.
	 */
	public static <T extends Tile> Iterable<T> valid(Iterable<T> iterable)
	{
		List<T> results = new ArrayList<>();
		for (T tile : iterable) {
			if (isValid(tile)) {
				results.add(tile);
			}
		}
		return results;
	}

}
