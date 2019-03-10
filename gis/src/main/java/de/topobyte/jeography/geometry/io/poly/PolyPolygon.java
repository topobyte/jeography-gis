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
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
class PolyPolygon
{

	private List<Point> points = new ArrayList<>();

	public List<Point> getPoints()
	{
		return points;
	}

	public LinearRing toLinearRing()
	{
		GeometryFactory factory = new GeometryFactory();
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();

		CoordinateSequence seq = csf.create(points.size(), 2);

		int n = 0;
		for (Point p : points) {
			seq.setOrdinate(n, 0, p.lon);
			seq.setOrdinate(n, 1, p.lat);
			n++;

		}

		LinearRing shell = new LinearRing(seq, factory);
		return shell;
	}

	public static PolyPolygon readPolygon(BufferedReader br) throws IOException
	{
		PolyPolygon polygon = new PolyPolygon();
		String line = br.readLine();
		if (line.equals("END")) {
			return null;
		}
		// line is name
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			if (line.equals("END"))
				break;
			Point point = readPoint(line);
			polygon.getPoints().add(point);
		}
		return polygon;
	}

	public static Point readPoint(String line)
	{
		String trimmed = line.trim();
		String[] split = trimmed.split(" ");
		int i = 0;
		double lon = 0;
		double lat = 0;
		for (String s : split) {
			if (!s.equals("")) {
				double d = Double.parseDouble(s);
				if (i == 0)
					lon = d;
				if (i == 1)
					lat = d;
				i++;
			}
		}
		if (i == 2) {
			// System.out.println(String.format("%f, %f", lon, lat));
		}
		return new Point(lon, lat);
	}

}

class Point
{

	double lon, lat;

	Point(double lon, double lat)
	{
		this.lon = lon;
		this.lat = lat;
	}

}
