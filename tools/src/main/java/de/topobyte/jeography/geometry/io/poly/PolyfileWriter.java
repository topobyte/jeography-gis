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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PolyfileWriter
{

	final static Logger logger = LoggerFactory.getLogger(PolyfileWriter.class);

	static String newline = System.getProperty("line.separator");

	/**
	 * Write <code>geometry</code> to a stream in the *.poly format.
	 * 
	 * @param os
	 *            the stream to write to.
	 * @param name
	 *            the name of the geometry in the .poly file.
	 * @param geometry
	 *            the geometry to write.
	 * @throws IOException
	 *             if writing to the stream fails.
	 */
	public static void write(OutputStream os, String name, Geometry geometry)
			throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		if (geometry instanceof Polygon) {
			write(bw, name);
			write(bw, (Polygon) geometry, 1);
			writeEnd(bw);
			bw.close();
		} else if (geometry instanceof MultiPolygon) {
			write(bw, name);
			write(bw, (MultiPolygon) geometry);
			writeEnd(bw);
			bw.close();
		} else {
			// TODO: throw exception
		}
	}

	private static void write(BufferedWriter bw, String name) throws IOException
	{
		bw.write(name);
		bw.write(newline);
	}

	private static void writeEnd(BufferedWriter bw) throws IOException
	{
		bw.write("END");
		bw.write(newline);
	}

	private static void write(BufferedWriter bw, LineString lineString,
			int index, boolean subtract) throws IOException
	{
		if (subtract)
			bw.write("!");
		bw.write(String.format("%d%s", index, newline));
		for (int i = 0; i < lineString.getNumPoints(); i++) {
			Point point = lineString.getPointN(i);
			double lon = point.getX();
			double lat = point.getY();
			String line = String.format("    %.9e    %.9e%s", lon, lat,
					newline);
			bw.write(line);
		}
		bw.write(String.format("END%s", newline));
	}

	private static int write(BufferedWriter bw, Polygon polygon, int index)
			throws IOException
	{
		logger.debug("polygon");
		int idx = index;
		LineString exterior = polygon.getExteriorRing();
		write(bw, exterior, idx++, false);
		for (int k = 0; k < polygon.getNumInteriorRing(); k++) {
			logger.debug("hole");
			LineString interior = polygon.getInteriorRingN(k);
			write(bw, interior, idx++, true);
		}
		return idx;
	}

	private static void write(BufferedWriter bw, MultiPolygon mp)
			throws IOException
	{
		logger.debug("multipolygon");
		int index = 1;
		for (int i = 0; i < mp.getNumGeometries(); i++) {
			Geometry g = mp.getGeometryN(i);
			if (g instanceof Polygon) {
				index = write(bw, (Polygon) g, index);
			}
		}
	}

}
