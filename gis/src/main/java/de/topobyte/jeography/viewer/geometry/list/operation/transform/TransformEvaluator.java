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

package de.topobyte.jeography.viewer.geometry.list.operation.transform;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.GeometryEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.list.operation.OperationEvaluator;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TransformEvaluator implements OperationEvaluator
{

	final static Logger logger = LoggerFactory
			.getLogger(TransformEvaluator.class);

	double tx = 0.0, ty = 0.0; // translation
	double theta = 0.0; // rotation
	double shx = 0.0, shy = 0.0; // shear
	double scx = 1.0, scy = 1.0; // scale

	/**
	 * @return the translation value in the x direction
	 */
	public double getTx()
	{
		return tx;
	}

	/**
	 * @return the translation value in the y direction
	 */
	public double getTy()
	{
		return ty;
	}

	/**
	 * @return the rotation
	 */
	public double getTheta()
	{
		return theta;
	}

	/**
	 * @return the shear in the x direction
	 */
	public double getShx()
	{
		return shx;
	}

	/**
	 * @return the shear in the y direction
	 */
	public double getShy()
	{
		return shy;
	}

	/**
	 * @return the scale factor in the x dimension
	 */
	public double getScx()
	{
		return scx;
	}

	/**
	 * @return the scale factor in the y dimension
	 */
	public double getScy()
	{
		return scy;
	}

	/**
	 * @param tx
	 *            the translation amount in the x direction
	 */
	public void setTx(double tx)
	{
		this.tx = tx;
	}

	/**
	 * @param ty
	 *            the translation amount in the y direction
	 */
	public void setTy(double ty)
	{
		this.ty = ty;
	}

	/**
	 * @param theta
	 *            the rotation
	 */
	public void setTheta(double theta)
	{
		this.theta = theta;
	}

	/**
	 * @param shx
	 *            the shear in the x direction
	 */
	public void setShx(double shx)
	{
		this.shx = shx;
	}

	/**
	 * @param shy
	 *            the shear in the y direction
	 */
	public void setShy(double shy)
	{
		this.shy = shy;
	}

	/**
	 * @param scx
	 *            the scale factor in the x dimension
	 */
	public void setScx(double scx)
	{
		this.scx = scx;
	}

	/**
	 * @param scy
	 *            the scale factor in the y dimension
	 */
	public void setScy(double scy)
	{
		this.scy = scy;
	}

	Point centroid = new GeometryFactory().createPoint(new Coordinate(0, 0));

	@Override
	public Geometry operationResult(List<Geometry> geometries)
	{
		GeometryFactory factory = new GeometryFactory();
		GeometryCollection collection = factory
				.createGeometryCollection(geometries.toArray(new Geometry[0]));

		GeometryEditor editor = new GeometryEditor(factory);

		centroid = collection.getCentroid();
		if (centroid == null) {
			centroid = factory.createPoint(new Coordinate(0, 0));
		}
		logger.debug("centroid: " + centroid);

		Geometry translated = editor.edit(collection, new TranslateOperation());

		return translated;
	}

	private class TranslateOperation extends GeometryEditor.CoordinateOperation
	{

		private AffineTransform t = new AffineTransform();

		public TranslateOperation()
		{
			t.translate(centroid.getX(), centroid.getY());
			t.translate(tx, ty);
			t.scale(scx, scy);
			t.shear(shx, shy);
			t.rotate(theta);
			t.translate(-centroid.getX(), -centroid.getY());
		}

		@Override
		public Coordinate[] edit(Coordinate[] coords, Geometry geometry)
		{
			List<Coordinate> result = new ArrayList<>();
			for (Coordinate c : coords) {
				Point2D transformed = t.transform(new Point2D.Double(c.x, c.y),
						null);
				Coordinate cn = new Coordinate(transformed.getX(),
						transformed.getY());
				result.add(cn);
			}
			return result.toArray(new Coordinate[0]);
		}

	}

}
