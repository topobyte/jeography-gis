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

package de.topobyte.jeography.util;

import java.util.HashMap;
import java.util.Map;

import de.topobyte.jeography.viewer.gotolocation.Location;

/**
 * This class is based on LGPL code by Matt Amos <zerebubuth@gmail.com>
 * originally part of the openstreetmap-website project. See <a href=
 * "https://github.com/openstreetmap/openstreetmap-website/blob/master/lib/short_link.rb"
 * >the original implementation</a> in Ruby.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OsmShortLinks
{

	private static final char ARRAY[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '_', '~' };

	private static final Map<Character, Integer> REVERSE = new HashMap<>();

	static {
		for (int i = 0; i < ARRAY.length; i++) {
			REVERSE.put(ARRAY[i], i);
		}
	}

	public static String encode(double lon, double lat, int zoom)
	{
		long scale = 1L << 32;
		long x = (long) ((lon + 180) / 360 * scale);
		long y = (long) ((lat + 90) / 180 * scale);
		long code = interleaveBits(x, y);

		StringBuilder buf = new StringBuilder();

		// Add eight to the zoom level, which approximates an accuracy of one
		// pixel in a tile.
		int n = (int) Math.ceil((zoom + 8) / 3d);
		for (int i = 0; i < n; i++) {
			int digit = (int) ((code >> (58 - 6 * i)) & 0x3f);
			buf.append(ARRAY[digit]);
		}

		// Append characters onto the end of the string to represent partial
		// zoom levels (characters themselves have a granularity of 3 zoom
		// levels).
		int m = (zoom + 8) % 3;
		for (int i = 0; i < m; i++) {
			buf.append('-');
		}

		return buf.toString();
	}

	// Interleaves the bits of two 32-bit numbers. The result is known
	// as a Morton code.
	private static long interleaveBits(long x, long y)
	{
		long c = 0;
		for (int i = 31; i >= 0; i--) {
			c = (c << 1) | ((x >> i) & 1);
			c = (c << 1) | ((y >> i) & 1);
		}
		return c;
	}

	public static Location decode(String str)
	{
		long x = 0, y = 0;
		int z = 0;
		int zOffset = 0;

		// Support old shortlinks with @
		str = str.replaceAll("@", "~");

		for (char c : str.toCharArray()) {
			if (c == '-' || c == '=') {
				zOffset += 1;
				continue;
			}
			Integer index = REVERSE.get(c);
			if (index == null) {
				throw new IllegalArgumentException("Invalid character: '" + c
						+ "'");
			}
			int t = index;
			for (int i = 0; i < 3; i++) {
				x <<= 1;
				if ((t & 32) != 0) {
					x |= 1;
				}
				t <<= 1;

				y <<= 1;
				if ((t & 32) != 0) {
					y |= 1;
				}
				t <<= 1;
			}
			z += 3;
		}
		x <<= 32 - z;
		y <<= 32 - z;

		double scale = 1L << 32;
		double lon = (x / scale * 360d) - 180;
		double lat = (y / scale * 180d) - 90;

		int zoom = z - 8;
		if (zOffset == 0) {
			// do nothing
		} else if (zOffset == 1) {
			zoom -= 2;
		} else if (zOffset == 2) {
			zoom -= 1;
		} else {
			throw new IllegalArgumentException("Too many '-' signs");
		}

		return new Location(lon, lat, zoom);
	}

}
