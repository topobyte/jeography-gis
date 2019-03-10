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

package de.topobyte.jeography.viewer.selection.polygonal;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

import de.topobyte.jeography.core.mapwindow.MapWindow;

/**
 * A LineEditor manages the process of editing a line / polygon.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class LineEditor
{

	private GeometryFactory factory = new GeometryFactory();
	private List<Coordinate> coordinates = new ArrayList<>();
	private List<Coordinate> coordinatesLonLat = new ArrayList<>();
	private final MapWindow window;

	/**
	 * Create a new line editor.
	 * 
	 * @param window
	 *            the window to use for transformations from pixel to geographic
	 *            space.
	 */
	public LineEditor(MapWindow window)
	{
		this.window = window;
	}

	/**
	 * Add the point (x,y) to the list of points.
	 * 
	 * @param x
	 *            the x coordinate of the new point.
	 * @param y
	 *            the y coordinate of the new point.
	 */
	public void add(int x, int y)
	{
		if (closed) {
			return;
		}
		Coordinate coord = new Coordinate(x, y);
		coordinates.add(coord);

		Coordinate lonlat = new Coordinate(window.getPositionLon(x),
				window.getPositionLat(y));
		coordinatesLonLat.add(lonlat);

		buildGeometries();
	}

	/**
	 * If possible remove the last point.
	 */
	public void removeLast()
	{
		if (coordinates.size() > 0) {
			coordinates.remove(coordinates.size() - 1);
			coordinatesLonLat.remove(coordinatesLonLat.size() - 1);
			closed = false;
			buildGeometries();
		}
	}

	private boolean closed = false;

	/**
	 * If possible, close the line to a polygon by connecting the last point
	 * with the first one.
	 */
	public void close()
	{
		if (coordinates.size() < 3) {
			return;
		}
		closed = true;
		Coordinate clone = (Coordinate) coordinates.get(0).clone();
		coordinates.add(clone);
		Coordinate cloneLonLat = (Coordinate) coordinatesLonLat.get(0).clone();
		coordinatesLonLat.add(cloneLonLat);
		buildGeometries();
	}

	/**
	 * @return whether it is currently possible to close this line to a polygon.
	 */
	public boolean canClose()
	{
		return !closed && coordinates.size() >= 3;
	}

	/**
	 * @return whether this line is closed, i.e. is a polygon.
	 */
	public boolean isClosed()
	{
		return closed;
	}

	/**
	 * @return whether this line is empty. i.e. has zero coordinates.
	 */
	public boolean isEmpty()
	{
		return coordinates.size() == 0;
	}

	private Geometry geometryPixels = null;
	private Geometry geometryDegrees = null;

	private void buildGeometries()
	{
		geometryPixels = getGeometry(coordinates);
		geometryDegrees = getGeometry(coordinatesLonLat);
	}

	/**
	 * @param coords
	 *            the coordinate sequence to build the geometry from
	 * @return the geometry represented (a point, line or polygon).
	 */
	private Geometry getGeometry(List<Coordinate> coords)
	{
		int n = coords.size();
		if (n == 0) {
			return null;
		} else if (n == 1) {
			return factory.createPoint(coords.get(0));
		} else {
			Coordinate[] array = coords.toArray(new Coordinate[0]);
			if (closed) {
				LinearRing ring = factory.createLinearRing(array);
				return factory.createPolygon(ring, null);
			}
			return factory.createLineString(array);
		}
	}

	/**
	 * @return the geometry represented (a point, line or polygon) in pixel
	 *         space.
	 */
	public Geometry getGeometryPixels()
	{
		if (geometryPixels == null) {
			buildGeometries();
		}
		return geometryPixels;
	}

	/**
	 * @return the geometry represented (a point, line or polygon) in geographic
	 *         space.
	 */
	public Geometry getGeometryDegrees()
	{
		if (geometryDegrees == null) {
			buildGeometries();
		}
		return geometryDegrees;
	}

	/**
	 * Rebuild the pixel representation of the geometry by the geographic
	 * representation.
	 */
	public void fromDegrees()
	{
		for (int i = 0; i < coordinates.size(); i++) {
			Coordinate c = coordinates.get(i);
			Coordinate ll = coordinatesLonLat.get(i);
			// convert ll to c...
			c.x = window.longitudeToX(ll.x);
			c.y = window.latitudeToY(ll.y);
		}
	}

	/**
	 * Rebuild the geographic representation of the geometry by the pixel
	 * representation.
	 */
	public void fromPixels()
	{
		for (int i = 0; i < coordinates.size(); i++) {
			Coordinate c = coordinates.get(i);
			Coordinate ll = coordinatesLonLat.get(i);
			// convert c to ll...
			ll.x = window.getPositionLon((int) c.x);
			ll.y = window.getPositionLat((int) c.y);
		}
	}

	/**
	 * Set this LineEditor's geometry to the denoted string.
	 * 
	 * @param string
	 *            the LineString to edit.
	 */
	public void setLineDegrees(LineString string)
	{
		closed = string.isClosed();
		CoordinateSequence sequence = string.getCoordinateSequence();
		for (int i = 0; i < sequence.size(); i++) {
			Coordinate coordinate = sequence.getCoordinate(i);
			Coordinate clone = (Coordinate) coordinate.clone();
			coordinates.add(new Coordinate(0, 0));
			coordinatesLonLat.add(clone);
		}
		fromDegrees();
		buildGeometries();
	}

}
