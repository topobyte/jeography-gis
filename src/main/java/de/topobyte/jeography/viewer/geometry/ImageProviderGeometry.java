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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import com.infomatiq.jsi.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.util.GeometryTransformer;

import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.geomath.WGS84;
import de.topobyte.jeography.core.ImageProvider;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyle;
import de.topobyte.jgs.transform.CoordinateTransformer;
import de.topobyte.jsi.GenericRTree;
import de.topobyte.jts.drawing.DrawMode;
import de.topobyte.jts.drawing.GeometryDrawer;
import de.topobyte.jts.drawing.awt.GeometryDrawerGraphics;
import de.topobyte.jts.utils.transform.CoordinateGeometryTransformer;
import de.topobyte.mercator.image.MercatorTileImage;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageProviderGeometry extends ImageProvider<Tile, BufferedImage>
{

	private GenericRTree<ZoomlevelGeometryProvider> tree;
	private GeometryStyle style;
	private int tileWidth;
	private int tileHeight;

	/**
	 * @param style
	 *            the style to use for painting.
	 * @param tree
	 *            the RTree of geometries.
	 * @param nthreads
	 *            the number of threads to use for production of tiles.
	 * @param tileHeight
	 * @param tileWidth
	 */
	public ImageProviderGeometry(GeometryStyle style,
			GenericRTree<ZoomlevelGeometryProvider> tree, int nthreads,
			int tileWidth, int tileHeight)
	{
		super(nthreads);
		this.style = style;
		this.tree = tree;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	@Override
	protected void finalize()
	{
		System.out.println("ImageProviderGeometry finalize");
	}

	@Override
	public BufferedImage load(Tile tile)
	{
		BufferedImage image = new BufferedImage(tileWidth, tileHeight,
				BufferedImage.TYPE_4BYTE_ABGR);

		final Graphics2D g2d = image.createGraphics();
		GraphicsUtil.useAntialiasing(g2d, true);
		// g2d.setColor(new Color(0x33000000, true));
		// g2d.fillRect(0, 0, Tile.SIZE - 10, Tile.SIZE - 10);

		MercatorTileImage mit = new MercatorTileImage(tile.getZoom(),
				tile.getTx(), tile.getTy(), tileWidth, tileHeight);
		GeometryDrawer pd = new GeometryDrawerGraphics(mit, 0, 0) {

			@Override
			public Graphics2D getGraphics()
			{
				return g2d;
			}
		};

		pd.setColorForeground(style.getColorStroke());
		pd.setColorBackground(style.getColorFill());
		pd.setLineWidth(style.getLineWidth());

		Rectangle viewRect = new Rectangle((float) mit.getLon1(),
				(float) mit.getLat1(), (float) mit.getLon2(),
				(float) mit.getLat2());

		Set<ZoomlevelGeometryProvider> intersects = tree.intersects(viewRect);
		// TODO: check for real intersection?
		for (ZoomlevelGeometryProvider zgp : intersects) {
			Geometry geometryMercator = zgp.getGeometryForZoomlevel(mit
					.getTileZoom());
			GeometryTransformer transformer = new CoordinateGeometryTransformer(
					new CoordinateTransformer() {

						@Override
						public double getY(double y)
						{
							return WGS84.merc2lat(y, 1.0);
						}

						@Override
						public double getX(double x)
						{
							return WGS84.merc2lon(x, 1.0);
						}
					});
			Geometry geometry = transformer.transform(geometryMercator);

			pd.drawGeometry(geometry, DrawMode.FILL_OUTLINE);

			if (style.isDrawNodes()) {
				Coordinate[] coordinates = geometry.getCoordinates();
				for (Coordinate c : coordinates) {
					pd.drawCircle(c.x, c.y, 3.0, DrawMode.FILL_OUTLINE);
				}
			}
			Coordinate[] coordinates = geometry.getCoordinates();
			int i = 0;
			for (Coordinate c : coordinates) {
				i++;
				// pd.drawString(c.x, c.y, "" + i, 12, -4, -12);
			}
		}

		return image;
	}

	/**
	 * @param tree
	 *            use this tree in consequent productions of images.
	 */
	public void setTree(GenericRTree<ZoomlevelGeometryProvider> tree)
	{
		this.tree = tree;
	}

	public void setTileWidth(int tileWidth)
	{
		this.tileWidth = tileWidth;
	}

	public void setTileHeight(int tileHeight)
	{
		this.tileHeight = tileHeight;
	}

}
