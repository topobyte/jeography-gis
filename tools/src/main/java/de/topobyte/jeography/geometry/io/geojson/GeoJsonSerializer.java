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

package de.topobyte.jeography.geometry.io.geojson;

import java.io.IOException;
import java.io.OutputStream;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeoJsonSerializer
{

	public static void write(OutputStream os, Geometry geometry)
			throws IOException
	{
		StringBuilder builder = new StringBuilder();
		write(builder, geometry);
		String string = builder.toString();
		byte[] bytes = string.getBytes();
		os.write(bytes);
	}

	private static void write(StringBuilder builder, Geometry geometry)
	{
		builder.append("{\n");
		if (geometry instanceof Polygon) {
			builder.append("  \"type\": \"Polygon\",\n");
			builder.append("  \"coordinates\": [\n");
			Polygon polygon = (Polygon) geometry;
			writeInternal(builder, polygon);
			builder.append("]\n");
		} else if (geometry instanceof MultiPolygon) {
			builder.append("  \"type\": \"MultiPolygon\",\n");
			builder.append("  \"coordinates\": [\n");
			MultiPolygon multipolygon = (MultiPolygon) geometry;
			writeInternal(builder, multipolygon);
			builder.append("]\n");
		}
		builder.append("}\n");
	}

	private static void writeInternal(StringBuilder builder,
			MultiPolygon multipolygon)
	{
		int n = multipolygon.getNumGeometries();
		for (int i = 0; i < n; i++) {
			builder.append("[ ");
			Polygon polygon = (Polygon) multipolygon.getGeometryN(i);
			writeInternal(builder, polygon);
			builder.append(" ]");
			builder.append(i < n - 1 ? ",\n" : "\n");
		}
	}

	private static void writeInternal(StringBuilder builder, Polygon polygon)
	{
		LineString exterior = polygon.getExteriorRing();
		writeInternal(builder, exterior);
		int nHoles = polygon.getNumInteriorRing();
		builder.append(nHoles > 0 ? ",\n" : "\n");
		for (int i = 0; i < nHoles; i++) {
			LineString interior = polygon.getInteriorRingN(i);
			writeInternal(builder, interior);
			builder.append(i < nHoles - 1 ? ",\n" : "\n");
		}
	}

	private static void writeInternal(StringBuilder builder, LineString string)
	{
		builder.append("[ ");
		int numPoints = string.getNumPoints();
		for (int i = 0; i < numPoints; i++) {
			Point point = string.getPointN(i);
			// TODO use a formatter
			String p = String.format("[%f, %f]", point.getX(), point.getY());
			builder.append(p);
			if (i < numPoints - 1) {
				builder.append(", ");
			}
		}
		builder.append(" ]");
	}

	/*
	 * { "type": "MultiPolygon", "coordinates": [ [[[102.0, 2.0], [103.0, 2.0],
	 * [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]], [[[100.0, 0.0], [101.0, 0.0],
	 * [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]], [[100.2, 0.2], [100.8, 0.2],
	 * [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]] ] }
	 */

}
