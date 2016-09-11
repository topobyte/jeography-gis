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

package de.topobyte.jeography.viewer.selection.rectangular;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.viewer.MouseUser;
import de.topobyte.jeography.viewer.core.PaintListener;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionAdapter implements MouseListener, MouseMotionListener,
		MouseUser, PaintListener, MapWindowChangeListener
{

	static final Logger logger = LoggerFactory
			.getLogger(SelectionAdapter.class);

	private Viewer viewer;

	private IntSelection selection = null;
	private GeographicSelection degrees = null;
	boolean snapSelection = true;

	/**
	 * Create a SelectionAdapter for the given viewer.
	 * 
	 * @param viewer
	 *            the viewer to wrap
	 */
	public SelectionAdapter(Viewer viewer)
	{
		this.viewer = viewer;

		viewer.addMouseListener(this);
		viewer.addMouseMotionListener(this);
		viewer.addPaintListener(this);

		viewer.getMapWindow().addChangeListener(this);
	}

	private boolean mouseActive = false;
	private boolean mousePressed = false;
	private Point pressPosition = null;
	private Point lastPosition = null;
	private SelectionPosition pressPad = SelectionPosition.PAD_NONE;
	private SelectionPosition lastPad = SelectionPosition.PAD_NONE;

	@Override
	public boolean getMouseActive()
	{
		return mouseActive;
	}

	@Override
	public void setMouseActive(boolean state)
	{
		mouseActive = state;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (!mouseActive)
			return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			mousePressed = true;
			pressPosition = e.getPoint();
			lastPosition = e.getPoint();
			pressPad = getSelectionPosition(e.getPoint());
			logger.debug("mouse pressed");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (!mouseActive)
			return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			mousePressed = false;
			pressPad = SelectionPosition.PAD_NONE;
			logger.debug("mouse released");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// do nothing
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (!mouseActive)
			return;
		if (mousePressed) {
			if (pressPad == SelectionPosition.PAD_NONE) {
				Point p = getDifference(pressPosition, e.getPoint());
				logger.debug(pressPosition + " " + p);
				selection = new IntSelection((int) pressPosition.getX(),
						e.getX(), (int) pressPosition.getY(), e.getY());
				degreeSelectionFromIntSelection();
				triggerPixelValuesChanged();
				viewer.repaint();
				return;
			}
			Point difference = getDifference(lastPosition, e.getPoint());
			lastPosition = e.getPoint();

			if (pressPad == SelectionPosition.PAD_RIGHT
					|| pressPad == SelectionPosition.PAD_TOP_RIGHT
					|| pressPad == SelectionPosition.PAD_BOTTOM_RIGHT) {
				int diffX = (int) difference.getX();
				int n = selection.getX2() + diffX;
				if (n >= selection.getX1()) {
					selection.setX2(n);
				} else {
					selection.setX2(selection.getX1());
					selection.setX1(n);
					if (pressPad == SelectionPosition.PAD_RIGHT) {
						pressPad = SelectionPosition.PAD_LEFT;
					} else if (pressPad == SelectionPosition.PAD_TOP_RIGHT) {
						pressPad = SelectionPosition.PAD_TOP_LEFT;
					} else {
						pressPad = SelectionPosition.PAD_BOTTOM_LEFT;
					}
				}
			} else if (pressPad == SelectionPosition.PAD_LEFT
					|| pressPad == SelectionPosition.PAD_TOP_LEFT
					|| pressPad == SelectionPosition.PAD_BOTTOM_LEFT) {
				int diffX = (int) difference.getX();
				int n = selection.getX1() + diffX;
				if (n <= selection.getX2()) {
					selection.setX1(n);
				} else {
					selection.setX1(selection.getX2());
					selection.setX2(n);
					if (pressPad == SelectionPosition.PAD_LEFT) {
						pressPad = SelectionPosition.PAD_RIGHT;
					} else if (pressPad == SelectionPosition.PAD_TOP_LEFT) {
						pressPad = SelectionPosition.PAD_TOP_RIGHT;
					} else {
						pressPad = SelectionPosition.PAD_BOTTOM_RIGHT;
					}
				}
			}
			if (pressPad == SelectionPosition.PAD_BOTTOM
					|| pressPad == SelectionPosition.PAD_BOTTOM_LEFT
					|| pressPad == SelectionPosition.PAD_BOTTOM_RIGHT) {
				int diffY = (int) difference.getY();
				int n = selection.getY2() + diffY;
				if (n >= selection.getY1()) {
					selection.setY2(n);
				} else {
					selection.setY2(selection.getY1());
					selection.setY1(n);
					if (pressPad == SelectionPosition.PAD_BOTTOM) {
						pressPad = SelectionPosition.PAD_TOP;
					} else if (pressPad == SelectionPosition.PAD_BOTTOM_LEFT) {
						pressPad = SelectionPosition.PAD_TOP_LEFT;
					} else {
						pressPad = SelectionPosition.PAD_TOP_RIGHT;
					}
				}
			} else if (pressPad == SelectionPosition.PAD_TOP
					|| pressPad == SelectionPosition.PAD_TOP_LEFT
					|| pressPad == SelectionPosition.PAD_TOP_RIGHT) {
				int diffY = (int) difference.getY();
				int n = selection.getY1() + diffY;
				if (n <= selection.getY2()) {
					selection.setY1(n);
				} else {
					selection.setY1(selection.getY2());
					selection.setY2(n);
					if (pressPad == SelectionPosition.PAD_TOP) {
						pressPad = SelectionPosition.PAD_BOTTOM;
					} else if (pressPad == SelectionPosition.PAD_TOP_LEFT) {
						pressPad = SelectionPosition.PAD_BOTTOM_LEFT;
					} else {
						pressPad = SelectionPosition.PAD_BOTTOM_RIGHT;
					}
				}
			}
			// logger.debug(String.format("%d %d", difference.x, difference.y));
			lastPad = pressPad;
			degreeSelectionFromIntSelection();
			triggerPixelValuesChanged();
			viewer.repaint();
		}
	}

	private void degreeSelectionFromIntSelection()
	{
		double lon1 = viewer.getMapWindow().getPositionLon(selection.getX1());
		double lon2 = viewer.getMapWindow().getPositionLon(selection.getX2());
		double lat1 = viewer.getMapWindow().getPositionLat(selection.getY1());
		double lat2 = viewer.getMapWindow().getPositionLat(selection.getY2());
		degrees = new GeographicSelection(lon1, lon2, lat1, lat2);
		logger.debug(degrees.toString());
		triggerGeographicValuesChanged();
	}

	private void intSelectionFromDegreeSelection()
	{
		if (degrees == null) {
			return;
		}
		double x1 = viewer.getMapWindow().longitudeToX(degrees.getX1().value());
		double x2 = viewer.getMapWindow().longitudeToX(degrees.getX2().value());
		double y1 = viewer.getMapWindow().latitudeToY(degrees.getY1().value());
		double y2 = viewer.getMapWindow().latitudeToY(degrees.getY2().value());
		selection = new IntSelection((int) Math.round(x1),
				(int) Math.round(x2), (int) Math.round(y1),
				(int) Math.round(y2));
		triggerPixelValuesChanged();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (!mouseActive)
			return;
		// SelectionPosition selectionPosition =
		// getSelectionPosition(e.getPoint());
		// logger.debug("selection position: " + selectionPosition);

		SelectionPosition newPad = getSelectionPosition(e.getPoint());
		if (newPad != lastPad) {
			lastPad = newPad;
			viewer.repaint();
		}
	}

	private Point getDifference(Point before, Point now)
	{
		int x = now.x - before.x;
		int y = now.y - before.y;
		return new Point(x, y);
	}

	private Color colorLines = new Color(0xff000000, true);
	private Color colorFill = new Color(0x66ffff00, true);
	private Color colorPads = new Color(0x66ff0000, true);
	private int maxSize = 40;

	@Override
	public void onPaint(TileMapWindow mapWindow, Graphics g)
	{
		if (selection == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g;
		GraphicsUtil.useAntialiasing(g2d, false);
		// background
		g.setColor(colorFill);
		g.fillRect(selection.getX1(), selection.getY1(), selection.getWidth(),
				selection.getHeight());

		// pads
		int padWidth = getPadWidth();
		int padHeight = getPadHeight();

		g.setColor(colorPads);
		switch (lastPad) {
		case PAD_LEFT:
			g.fillRect(selection.getX1(), selection.getY1(), padWidth,
					selection.getHeight());
			break;
		case PAD_BOTTOM:
			g.fillRect(selection.getX1(), selection.getY2() - padHeight,
					selection.getWidth(), padHeight);
			break;
		case PAD_BOTTOM_LEFT:
			g.fillRect(selection.getX1(), selection.getY2() - padHeight,
					padWidth, padHeight);
			break;
		case PAD_BOTTOM_RIGHT:
			g.fillRect(selection.getX2() - padWidth, selection.getY2()
					- padHeight, padWidth, padHeight);
			break;
		case PAD_CENTER:
			break;
		case PAD_NONE:
			break;
		case PAD_RIGHT:
			g.fillRect(selection.getX2() - padWidth, selection.getY1(),
					padWidth, selection.getHeight());
			break;
		case PAD_TOP:
			g.fillRect(selection.getX1(), selection.getY1(),
					selection.getWidth(), padHeight);
			break;
		case PAD_TOP_LEFT:
			g.fillRect(selection.getX1(), selection.getY1(), padWidth,
					padHeight);
			break;
		case PAD_TOP_RIGHT:
			g.fillRect(selection.getX2() - padWidth, selection.getY1(),
					padWidth, padHeight);
			break;
		}

		// lines
		g2d.setStroke(new BasicStroke());
		g.setColor(colorLines);
		g.drawRect(selection.getX1(), selection.getY1(), selection.getWidth(),
				selection.getHeight());
	}

	private int getPadWidth()
	{
		int padWidth = selection.getWidth() / 4;
		if (padWidth > maxSize)
			padWidth = maxSize;
		return padWidth;
	}

	private int getPadHeight()
	{
		int padHeight = selection.getHeight() / 4;
		if (padHeight > maxSize)
			padHeight = maxSize;
		return padHeight;
	}

	private SelectionPosition getSelectionPosition(Point point)
	{
		if (selection == null) {
			return SelectionPosition.PAD_NONE;
		}
		int x = (int) point.getX();
		int y = (int) point.getY();
		if (x > selection.getX2() || x < selection.getX1()
				|| y > selection.getY2() || y < selection.getY1()) {
			return SelectionPosition.PAD_NONE;
		}

		int padWidth = getPadWidth();
		int padHeight = getPadHeight();

		boolean left = x <= selection.getX1() + padWidth;
		boolean right = x >= selection.getX2() - padWidth;
		boolean top = y <= selection.getY1() + padHeight;
		boolean bottom = y >= selection.getY2() - padHeight;

		if (top) {
			if (left)
				return SelectionPosition.PAD_TOP_LEFT;
			if (right)
				return SelectionPosition.PAD_TOP_RIGHT;
			return SelectionPosition.PAD_TOP;
		}
		if (bottom) {
			if (left)
				return SelectionPosition.PAD_BOTTOM_LEFT;
			if (right)
				return SelectionPosition.PAD_BOTTOM_RIGHT;
			return SelectionPosition.PAD_BOTTOM;
		}
		if (left)
			return SelectionPosition.PAD_LEFT;
		if (right)
			return SelectionPosition.PAD_RIGHT;
		return SelectionPosition.PAD_CENTER;
	}

	private enum SelectionPosition {

		PAD_NONE,
		PAD_CENTER,
		PAD_TOP,
		PAD_BOTTOM,
		PAD_LEFT,
		PAD_RIGHT,
		PAD_TOP_LEFT,
		PAD_TOP_RIGHT,
		PAD_BOTTOM_LEFT,
		PAD_BOTTOM_RIGHT

	}

	/**
	 * @return the current selection.
	 */
	public IntSelection getSelection()
	{
		return selection;
	}

	/**
	 * @return the current selection.
	 */
	public GeographicSelection getGeographicSelection()
	{
		return degrees;
	}

	/**
	 * Set the current geographic selection
	 * 
	 * @param degrees
	 *            the new selection.
	 */
	public void setGeographicSelection(GeographicSelection degrees)
	{
		this.degrees = degrees;
		intSelectionFromDegreeSelection();
	}

	public void clearSelection()
	{
		this.selection = null;
		this.degrees = null;
		triggerPixelValuesChanged();
		triggerGeographicValuesChanged();
		viewer.repaint();
	}

	@Override
	public void changed()
	{
		// map window changed
		if (snapSelection) {
			intSelectionFromDegreeSelection();
		} else {
			if (selection != null) {
				degreeSelectionFromIntSelection();
			}
		}
	}

	/**
	 * @return whether the selection snaps to the map.
	 */
	public boolean isSnapSelection()
	{
		return snapSelection;
	}

	/**
	 * @param b
	 *            whether the selection should snap to the map.
	 */
	public void setSnapSelection(boolean b)
	{
		snapSelection = b;
		// TODO: only do this, if the mapWindow has changed in while in non-snap
		// mode.
		// to prevent unnecessary change of geographic values.
		if (snapSelection && selection != null) {
			degreeSelectionFromIntSelection();
		}
	}

	private List<SelectionChangeListener> listeners = new ArrayList<>();

	/**
	 * Add a listener that get informed on changes of the selections.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addSelectionChangeListener(SelectionChangeListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Remove a listener from the list of selection listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeSelectionChangeListener(SelectionChangeListener listener)
	{
		listeners.remove(listener);
	}

	private void triggerPixelValuesChanged()
	{
		for (SelectionChangeListener l : listeners) {
			l.pixelValuesChanged();
		}
	}

	private void triggerGeographicValuesChanged()
	{
		for (SelectionChangeListener l : listeners) {
			l.geographicValuesChanged();
		}
	}

}
