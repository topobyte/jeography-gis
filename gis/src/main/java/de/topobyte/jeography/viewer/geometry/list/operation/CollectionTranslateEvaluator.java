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

package de.topobyte.jeography.viewer.geometry.list.operation;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.util.GeometryEditor;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CollectionTranslateEvaluator implements OperationEvaluator
{

	private double x;
	private double y;

	/**
	 * An operation that collects a set of geometries and translates the result
	 * by the denoted shifting offsets.
	 * 
	 * @param x
	 *            the amount to shift parallel to the X-axis.
	 * @param y
	 *            the amount to shift parallel to the Y-axis.
	 */
	public CollectionTranslateEvaluator(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the amount to shift along the x-axis.
	 * 
	 * @param x
	 *            the amount to shift.
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * Set the amount to shift along the y-axis.
	 * 
	 * @param y
	 *            the amount to shift.
	 */
	public void setY(double y)
	{
		this.y = y;
	}

	/**
	 * @return the amount to shift along the x-axis.
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @return the amount to shift along the x-axis.
	 */
	public double getY()
	{
		return y;
	}

	@Override
	public Geometry operationResult(List<Geometry> geometries)
	{
		GeometryFactory factory = new GeometryFactory();
		GeometryCollection collection = factory
				.createGeometryCollection(geometries.toArray(new Geometry[0]));

		GeometryEditor editor = new GeometryEditor(factory);
		Geometry translated = editor.edit(collection, new TranslateOperation());

		return translated;
	}

	private class TranslateOperation extends GeometryEditor.CoordinateOperation
	{

		public TranslateOperation()
		{
			// nothing to do here
		}

		@Override
		public Coordinate[] edit(Coordinate[] coords, Geometry geometry)
		{
			List<Coordinate> result = new ArrayList<>();
			for (Coordinate c : coords) {
				Coordinate cn = new Coordinate(c.x + getX(), c.y + getY());
				result.add(cn);
			}
			return result.toArray(new Coordinate[0]);
		}

	}

}
