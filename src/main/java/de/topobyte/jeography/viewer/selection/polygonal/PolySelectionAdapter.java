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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.jeography.core.PaintListener;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.viewer.MouseUser;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jgs.transform.IdentityCoordinateTransformer;
import de.topobyte.jts.drawing.DrawMode;
import de.topobyte.jts.drawing.awt.GeometryDrawerGraphics;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PolySelectionAdapter implements MouseListener,
		MouseMotionListener, MouseUser, PaintListener, MapWindowChangeListener,
		SelectionChangeListener
{

	private final Viewer viewer;

	private Selection selection;
	private GeometryCollection geometries;

	/**
	 * @param viewer
	 *            the viewer to wrap
	 */
	public PolySelectionAdapter(Viewer viewer)
	{
		this.viewer = viewer;
		selection = new Selection(viewer.getMapWindow());

		selection.addSelectionChangeListener(this);

		viewer.addMouseListener(this);
		viewer.addMouseMotionListener(this);
		viewer.addPaintListener(this);

		viewer.getMapWindow().addChangeListener(this);

		buildSelection();
	}

	/**
	 * @return the selection wrapped by this adapter.
	 */
	public Selection getSelection()
	{
		return selection;
	}

	private void buildSelection()
	{
		geometries = selection.getGeometries();
	}

	@Override
	public void changed()
	{
		if (snap) {
			selection.fromDegrees();
			buildSelection();
		}
	}

	@Override
	public void onPaint(final TileMapWindow mapWindow, final Graphics g)
	{

		Graphics2D g2d = (Graphics2D) g;

		g2d.setStroke(new BasicStroke());
		GraphicsUtil.useAntialiasing(g2d, true);

		GeometryDrawerGraphics pd = new GeometryDrawerGraphics(
		// viewer.getMapWindow(),
				new IdentityCoordinateTransformer(), 0, 0) {

			@Override
			public Graphics2D getGraphics()
			{
				return (Graphics2D) g;
			}
		};

		pd.setColorForeground(new ColorCode(0xff000000, true));
		pd.setColorBackground(new ColorCode(0x66ff0000, true));
		pd.drawGeometry(geometries, DrawMode.FILL_OUTLINE);

		g.setColor(new Color(0xddff0000, true));

		for (int i = 0; i < geometries.getNumGeometries(); i++) {
			Geometry geometry = geometries.getGeometryN(i);
			if (geometry instanceof Point) {
				Point point = (Point) geometry;
				drawPoint(point, g);
			}
			if (geometry instanceof LineString) {
				LineString string = (LineString) geometry;
				drawLine(string, g);
			}
			if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LineString exteriorRing = polygon.getExteriorRing();
				drawLine(exteriorRing, g);
			}
		}
	}

	private void drawCoordinate(Coordinate coordinate, Graphics g)
	{
		Color colorLine = new Color(0xff000000, true);
		Color colorFill = new Color(0x33ffff00, true);
		double x = coordinate.x;
		double y = coordinate.y;
		int size = 10;
		g.setColor(colorFill);
		g.fillRect((int) x - size / 2, (int) y - size / 2, size, size);
		g.setColor(colorLine);
		g.drawRect((int) x - size / 2, (int) y - size / 2, size, size);
	}

	private void drawPoint(Point point, Graphics g)
	{
		Coordinate coordinate = point.getCoordinate();
		drawCoordinate(coordinate, g);
	}

	private void drawLine(LineString string, Graphics g)
	{
		for (int n = 0; n < string.getNumPoints(); n++) {
			Coordinate coordinate = string.getCoordinateN(n);
			drawCoordinate(coordinate, g);
		}
	}

	@Override
	public boolean getMouseActive()
	{
		// TODO Auto-generated method stub
		return false;
	}

	private boolean mouseActive = false;

	@Override
	public void setMouseActive(boolean state)
	{
		mouseActive = state;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (!mouseActive) {
			return;
		}

		int clicks = e.getClickCount();
		int x = e.getX();
		int y = e.getY();

		if (clicks == 1) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				selection.add(x, y);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				selection.remove();
			}
			buildSelection();
		} else if (clicks == 2) {
			selection.close();
			buildSelection();
		}
		viewer.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	private boolean snap = true;

	/**
	 * @return whether this selection snaps to the viewer's map window.
	 */
	public boolean isSnapSelection()
	{
		return snap;
	}

	/**
	 * @param b
	 *            whether this selection should snap to the viewer's map window.
	 */
	public void setSnapSelection(boolean b)
	{
		snap = b;
		// TODO: only do this, if the mapWindow has changed while in non-snap
		// mode.
		// to prevent unnecessary change of geographic values.
		if (snap) {
			selection.fromPixels();
		}
	}

	@Override
	public void geographicValuesChanged()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pixelValuesChanged()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged()
	{
		buildSelection();
		viewer.repaint();
	}

}
