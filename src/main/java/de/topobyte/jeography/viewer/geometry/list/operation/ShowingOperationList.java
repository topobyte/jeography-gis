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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.viewer.core.PaintListener;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.ImageManagerGeometry;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometrySourceNull;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyle;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowingOperationList extends OperationList implements
		PaintListener
{

	private static final long serialVersionUID = -2692520265323153740L;

	private final OperationEvaluator evaluator;
	private final Viewer viewer;
	private GeometryStyle style;
	private ImageManagerGeometry manager;

	@Override
	protected Geometry operationResult(List<Geometry> geometries)
	{
		return evaluator.operationResult(geometries);
	}

	/**
	 * Create a new operation list that displays its results on a viewer.
	 * 
	 * @param evaluator
	 *            the evaluator to use for generating results.
	 * @param viewer
	 *            the viewer to display results on.
	 */
	public ShowingOperationList(OperationEvaluator evaluator, Viewer viewer)
	{
		this.evaluator = evaluator;
		this.viewer = viewer;

		style = new GeometryStyle();

		manager = new ImageManagerGeometry(viewer.getMapWindow());
		manager.addLoadListener(viewer);

		viewer.addPaintListener(this);
	}

	@Override
	public void onPaint(TileMapWindow mapWindow, Graphics g)
	{
		for (TileOnWindow tow : mapWindow) {
			BufferedImage imageOverlay = manager.get(tow);
			if (imageOverlay != null)
				g.drawImage(imageOverlay, tow.getDX(), tow.getDY(), null);
		}
	}

	@Override
	protected void resultUpdated()
	{
		super.resultUpdated();

		Geometry geometry = getResult();
		List<GeometryContainer> geometries = new ArrayList<>();
		GeoObject tg = new GeoObject(geometry);
		GeometryContainer gc = new GeometryContainer(0, tg,
				new GeometrySourceNull());
		geometries.add(gc);
		manager.setGeometries(style, geometries);

		viewer.repaint();
	}

	/**
	 * Get the evaluator that is used to generate results.
	 * 
	 * @return the operation's evaluator.
	 */
	public OperationEvaluator getEvaluator()
	{
		return evaluator;
	}

}
