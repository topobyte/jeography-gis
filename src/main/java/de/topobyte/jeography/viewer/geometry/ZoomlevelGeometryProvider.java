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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

import de.topobyte.chromaticity.ColorCode;
import de.topobyte.geomath.WGS84;
import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jgs.transform.CoordinateTransformer;
import de.topobyte.jgs.transform.IdentityCoordinateTransformer;
import de.topobyte.jts.drawing.DrawMode;
import de.topobyte.jts.drawing.GeometryDrawer;
import de.topobyte.jts.drawing.awt.GeometryDrawerGraphics;
import de.topobyte.jts.utils.transform.CoordinateGeometryTransformer;
import de.topobyte.jts2awt.Jts2Awt;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ZoomlevelGeometryProvider
{

	final static Logger logger = LoggerFactory
			.getLogger(ZoomlevelGeometryProvider.class);

	private Geometry originalGeometry;
	private Geometry geometry;

	/**
	 * @param geometry
	 *            the geometry to provide zoomed instances for.
	 */
	public ZoomlevelGeometryProvider(Geometry geometry)
	{
		originalGeometry = geometry;

		CoordinateGeometryTransformer cgt = new CoordinateGeometryTransformer(
				new CoordinateTransformer() {

					@Override
					public double getY(double y)
					{
						return WGS84.lat2merc(y, 1.0);
					}

					@Override
					public double getX(double x)
					{
						return WGS84.lon2merc(x, 1.0);
					}
				});

		this.geometry = cgt.transform(geometry);
	}

	private Map<Integer, Geometry> instances = new HashMap<>();
	private Map<Integer, Shape> shapeInstances = new HashMap<>();
	private Map<Integer, CachedImage> cacheImageInstances = new HashMap<>();

	/**
	 * @return the goemetry this provider was created with.
	 */
	public Geometry getOriginalGeometry()
	{
		return originalGeometry;
	}

	/**
	 * Get the simplified geometry for the given zoom level.
	 * 
	 * @param zoom
	 *            the zoom level to generate an instance for.
	 * @return the generated simplified instance of the geometry.
	 */
	public Geometry getGeometryForZoomlevel(int zoom)
	{
		Geometry cached = instances.get(zoom);
		if (cached != null)
			return cached;
		double distance = 1.0 / 256 / (1 << zoom);
		Geometry simplified = geometry;
		try {
			// simplified = DouglasPeuckerSimplifier.simplify(geometry,
			// distance);
			simplified = TopologyPreservingSimplifier.simplify(geometry,
					distance);
		} catch (IllegalArgumentException e) {
			logger.warn("Unable to simplify geometry. IllegalArgumentException: "
					+ e.getMessage());
		}
		instances.put(zoom, simplified);
		return simplified;
	}

	/**
	 * Get the simplified shape for the given zoom level.
	 * 
	 * @param zoom
	 *            the zoom level to generate an instance for.
	 * @return the generated simplified instance of the shape.
	 */
	public Shape getShapeForZoomlevel(int zoom)
	{
		Shape cached = shapeInstances.get(zoom);
		if (cached != null)
			return cached;
		Geometry g = getGeometryForZoomlevel(zoom);
		Shape shape = Jts2Awt.toShape(g, new IdentityCoordinateTransformer());
		return shape;
	}

	/**
	 * Get a CachedImage for this zoomlevel
	 * 
	 * @param zoom
	 *            the zoom level.
	 * @return the CachedImage instance.
	 */
	public CachedImage getImageForZoomlevel(final int zoom)
	{
		CachedImage cached = cacheImageInstances.get(zoom);
		if (cached != null)
			return cached;

		Geometry geom = getGeometryForZoomlevel(zoom);

		CoordinateTransformer ct = new CoordinateTransformer() {

			@Override
			public double getX(double x)
			{
				return x * (1 << zoom) * 256;
			}

			@Override
			public double getY(double y)
			{
				return y * (1 << zoom) * 256;
			}

		};
		CoordinateGeometryTransformer transfomer = new CoordinateGeometryTransformer(
				ct);
		Geometry transformed = transfomer.transform(geom);

		Envelope envelope = transformed.getEnvelopeInternal();
		double w = envelope.getWidth();
		double h = envelope.getHeight();
		logger.debug("size of envelope: " + w + ", " + h);

		int width = (int) Math.ceil(w);
		int height = (int) Math.ceil(h);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		final Graphics2D g2d = image.createGraphics();

		g2d.setColor(new Color(0x33000000, true));
		g2d.fillRect(0, 0, width, height);

		double minX = envelope.getMinX();
		double minY = envelope.getMinY();
		logger.debug(envelope.toString());
		int tx = (int) Math.floor(minX / 256.0);
		int ty = (int) Math.floor(minY / 256.0);
		int xoff = (int) (minX - tx * 256);
		int yoff = (int) (minY - ty * 256);
		logger.debug("tx, ty, xoff, yoff: " + tx + ", " + ty + ", " + xoff
				+ ", " + yoff);

		MapWindow mi = new SteppedMapWindow(width, height, zoom, tx, ty, xoff,
				yoff);
		// MapImage mi = new MapImage(new
		// BBox(originalGeometry.getEnvelopeInternal()), width,
		// height);

		// double plat = mi.getPositionLat(0);
		// double plon = mi.getPositionLon(0);
		// logger.debug("plon, plat: " + plon + ", " + plat);

		GeometryDrawer pd = new GeometryDrawerGraphics(mi, width, height) {

			@Override
			public Graphics2D getGraphics()
			{
				return g2d;
			}
		};

		pd.setColorForeground(new ColorCode(0xff000000, true));
		pd.setColorBackground(new ColorCode(0x33ff0000, true));
		pd.drawGeometry(originalGeometry, DrawMode.FILL_OUTLINE);

		cached = new CachedImage(image, mi);
		cacheImageInstances.put(zoom, cached);
		return cached;
	}

}
