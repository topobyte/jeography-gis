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

package de.topobyte.jeography.geometry.io.poly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;

import de.topobyte.jts.utils.PolygonHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PolyfileReader
{

	/**
	 * Read a polygon from an inputstream from which to read *.poly style data.
	 * 
	 * @param in
	 *            the inputstream to read data from.
	 * @return a geometry.
	 * @throws IOException
	 *             if reading the stream fails.
	 */
	public static Geometry read(InputStream in) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		/* String filename = */br.readLine();
		// System.out.println(filename);
		Set<PolyPolygon> polygons = new HashSet<>();
		PolyPolygon p;
		while ((p = PolyPolygon.readPolygon(br)) != null) {
			polygons.add(p);
		}
		// System.out.println(String.format("loaded %d polygons",
		// polygons.size()));

		Set<LinearRing> rings = new HashSet<>();
		for (PolyPolygon polygon : polygons) {
			rings.add(polygon.toLinearRing());
		}
		MultiPolygon mp = PolygonHelper.multipolygonFromRings(rings, true);
		// System.out.println(String.format("Multipolygon has %d polygons",
		// mp.getNumGeometries()));
		// System.out.println(String.format("Multipolygon has %d nested geoms",
		// PolygonHelper.countGeometries(mp)));

		return mp;
	}

}
