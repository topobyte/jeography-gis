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

package de.topobyte.jeography.viewer.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jts.indexing.GeometryTesselationMap;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryTester
{

	private List<Geometry> geometries;
	private GeometryTesselationMap<Geometry> tree;

	/**
	 * @param geometries
	 *            the geometries to test for.
	 */
	public void setGeometries(Collection<GeometryContainer> geometries)
	{
		this.geometries = new ArrayList<>();

		for (GeometryContainer geometryContainer : geometries) {
			Geometry geometry = geometryContainer.getGeometry().getGeometry();
			this.geometries.add(geometry);
		}

		buildRTree();
	}

	private void buildRTree()
	{
		tree = new GeometryTesselationMap<>();

		for (Geometry geometry : geometries) {
			tree.add(geometry, geometry);
		}
	}

	/**
	 * Get the geometries at the denoted position.
	 * 
	 * @param lon
	 *            the longitude.
	 * @param lat
	 *            the latitude.
	 * @return the collection of geometries at this point.
	 */
	public Collection<Geometry> test(double lon, double lat)
	{
		Point point = new GeometryFactory()
				.createPoint(new Coordinate(lon, lat));

		Set<Geometry> intersects = tree.intersecting(point);
		return intersects;
	}

}
