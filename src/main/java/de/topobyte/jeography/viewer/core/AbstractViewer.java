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

package de.topobyte.jeography.viewer.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.TransferHandler;

import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.tiles.TileConfigListener;
import de.topobyte.jeography.viewer.MouseUser;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.zoom.ZoomMode;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractViewer extends JPanel implements MouseUser,
		ComponentListener, MouseMotionListener, MouseListener,
		MouseWheelListener
{

	private static final long serialVersionUID = -3023462611876276320L;

	protected Color colorBackground = new Color(255, 255, 255, 255);
	protected Color colorBorder = new Color(0, 0, 0, 255);
	protected Color colorTilenumbers = new Color(0, 0, 0, 255);
	protected Color colorCrosshair = new Color(127, 0, 0, 255);

	protected boolean mouseActive = false;
	protected ZoomMode zoomMode = ZoomMode.ZOOM_AND_KEEP_POINT;

	protected boolean drawBorder = true;
	protected boolean drawCrosshair = true;
	protected boolean drawOverlay = true;
	protected boolean drawTileNumbers = true;

	protected TileConfig tileConfig;
	protected TileConfig overlayTileConfig;

	protected abstract MapWindow getMapWindow();

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

	public ZoomMode getZoomMode()
	{
		return zoomMode;
	}

	public void setZoomMode(ZoomMode zoomMode)
	{
		this.zoomMode = zoomMode;
	}

	/**
	 * @return whether a border is drawn around tiles.
	 */
	public boolean isDrawBorder()
	{
		return drawBorder;
	}

	/**
	 * @return whether the crosshair is shown.
	 */
	public boolean isDrawCrosshair()
	{
		return drawCrosshair;
	}

	/**
	 * @return whether the overlay will be drawn.
	 */
	public boolean isDrawOverlay()
	{
		return drawOverlay;
	}

	/**
	 * @return whether to draw each tile's number.
	 */
	public boolean isDrawTileNumbers()
	{
		return drawTileNumbers;
	}

	/**
	 * Set whether a border shall be drawn around tiles.
	 * 
	 * @param drawBorder
	 *            whether to draw a border around tiles.
	 */
	public void setDrawBorder(boolean drawBorder)
	{
		this.drawBorder = drawBorder;
		dispatchRepaint();
	}

	/**
	 * Set whether a crosshair shall be drawn in the middle of the viewport.
	 * 
	 * @param drawCrosshair
	 *            whether to draw a crosshair.
	 */
	public void setDrawCrosshair(boolean drawCrosshair)
	{
		this.drawCrosshair = drawCrosshair;
		dispatchRepaint();
	}

	/**
	 * Set whether the overlay will be drawn.
	 * 
	 * @param drawOverlay
	 *            whether to draw an overlay.
	 */
	public void setDrawOverlay(boolean drawOverlay)
	{
		this.drawOverlay = drawOverlay;
		dispatchRepaint();
	}

	/**
	 * Set whether the tile's numbers will be drawn.
	 * 
	 * @param drawTileNumbers
	 *            whether to draw each tile's number.
	 */
	public void setDrawTileNumbers(boolean drawTileNumbers)
	{
		this.drawTileNumbers = drawTileNumbers;
		dispatchRepaint();
	}

	/**
	 * Get the color of the background
	 * 
	 * @return a color.
	 */
	public Color getColorBackground()
	{
		return colorBackground;
	}

	/**
	 * Set the color of the background
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColorBackground(Color color)
	{
		this.colorBackground = color;
	}

	/**
	 * Get the color of the tile borders
	 * 
	 * @return a color.
	 */
	public Color getColorBorder()
	{
		return colorBorder;
	}

	/**
	 * Set the color of the tile borders
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColorBorder(Color color)
	{
		this.colorBorder = color;
	}

	/**
	 * Get the color of the tile number font
	 * 
	 * @return a color.
	 */
	public Color getColorTilenumbers()
	{
		return colorTilenumbers;
	}

	/**
	 * Set the color of the tile number font
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColorTilenumbers(Color color)
	{
		this.colorTilenumbers = color;
	}

	/**
	 * Get the color of the crosshair
	 * 
	 * @return a color.
	 */
	public Color getColorCrosshair()
	{
		return colorCrosshair;
	}

	/**
	 * Set the color of the crosshair
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColorCrosshair(Color color)
	{
		this.colorCrosshair = color;
	}

	/**
	 * Get the current configuration.
	 * 
	 * @return the current tile configuration.
	 */
	public TileConfig getTileConfig()
	{
		return tileConfig;
	}

	/**
	 * Get the current overlay configuration.
	 * 
	 * @return the current overlay configuration.
	 */
	public TileConfig getOverlayTileConfig()
	{
		return overlayTileConfig;
	}

	/*
	 * Repaint management
	 */

	boolean shallRepaint = true;
	Object repaintLock = new Object();

	protected void dispatchRepaint()
	{
		synchronized (repaintLock) {
			shallRepaint = true;
			repaintLock.notify();
		}
	}

	// this is to dispatch repainting into a separate thread.
	// events that normally generate a repaint will use dispatchRepaint instead.
	protected class Repainter implements Runnable
	{

		Repainter()
		{
			// do nothing
		}

		@Override
		public void run()
		{
			while (true) {
				synchronized (repaintLock) {
					shallRepaint = false;
				}
				repaint();
				synchronized (repaintLock) {
					if (!shallRepaint) {
						try {
							repaintLock.wait();
						} catch (InterruptedException e) {
							// do nothing
						}
					}
				}
			}
		}
	}

	/*
	 * Drag gesture detection
	 */

	private DragGestureRecognizer recognizer;
	private DragGestureListener currentDragGestureListener = null;
	private DragGestureListener dragGestureListener = null;

	/**
	 * Set whether dragging the mouse starts an drag event.
	 * 
	 * @param drag
	 *            whether to allow dragging.
	 */
	public void setDragging(boolean drag)
	{
		if (drag) {
			if (dragGestureListener != null) {
				uninstallDragSource();
				DragSource dragSource = new DragSource();
				currentDragGestureListener = dragGestureListener;
				recognizer = dragSource.createDefaultDragGestureRecognizer(
						this, TransferHandler.COPY, dragGestureListener);
			}
		} else {
			uninstallDragSource();
		}
	}

	private void uninstallDragSource()
	{
		if (recognizer != null) {
			recognizer.removeDragGestureListener(currentDragGestureListener);
			recognizer.setComponent(null);
			recognizer = null;
		}
	}

	/**
	 * Set the DragGestureListener to use.
	 * 
	 * @param listener
	 *            the listener to use after invoking setDragging();
	 */
	public void setDragGestureListener(DragGestureListener listener)
	{
		dragGestureListener = listener;
	}

	/*
	 * Tile config listeners
	 */

	private Set<TileConfigListener> listeners = new HashSet<>();
	private Set<TileConfigListener> listenersOverlay = new HashSet<>();

	/**
	 * Add this listener to the set of listeners.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addTileConfigListener(TileConfigListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Add this listener to the set of listeners.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addOverlayTileConfigListener(TileConfigListener listener)
	{
		listenersOverlay.add(listener);
	}

	protected void triggerTileConfigListeners()
	{
		for (TileConfigListener listener : listeners) {
			listener.tileConfigChanged();
		}
	}

	protected void triggerOverlayTileConfigListeners()
	{
		for (TileConfigListener listener : listenersOverlay) {
			listener.tileConfigChanged();
		}
	}

	/*
	 * Paint listeners
	 */

	protected List<PaintListener> paintListeners = new ArrayList<>();

	/**
	 * Add the given PaintListener to the list of paintListeners
	 * 
	 * @param paintListener
	 *            the listener to add.
	 */
	public void addPaintListener(PaintListener paintListener)
	{
		synchronized (paintListeners) {
			paintListeners.add(paintListener);
		}
	}

	/**
	 * Remove the given PaintListener from the list of paintListeners.
	 * 
	 * @param paintListener
	 *            the listener to remove.
	 */
	public void removePaintListener(PaintListener paintListener)
	{
		synchronized (paintListeners) {
			paintListeners.remove(paintListener);
		}
	}

	/*
	 * Mouse listeners
	 */

	protected Collection<MouseListener> mouseListeners = new ArrayList<>();

	/**
	 * @param listeners
	 *            the collection of mouse listeners to notify about mouse
	 *            events.
	 */
	public void setMouseListeners(Collection<MouseListener> listeners)
	{
		mouseListeners = listeners;
	}

	/*
	 * ComponentListener implementation
	 */

	@Override
	public void componentResized(ComponentEvent e)
	{
		// Invoked when the component's size changes.
		// Do nothing by default
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		// Invoked when the component's position changes.
		// Do nothing by default
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
		// Invoked when the component has been made visible.
		// Do nothing by default
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
		// Invoked when the component has been made invisible.
		// Do nothing by default
	}

	/*
	 * Mouse logic
	 */

	private Point pointPress;
	private boolean mousePressed = false;

	/*
	 * MouseMotionListener implementation
	 */

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (mouseActive && mousePressed) {
			Point currentPoint = e.getPoint();
			int dx = pointPress.x - currentPoint.x;
			int dy = pointPress.y - currentPoint.y;
			pointPress = currentPoint;
			// down right movement is negative for both
			// System.out.println(String.format("%d %d", dx, dy));
			getMapWindow().move(dx, dy);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// do nothing
	}

	/*
	 * MouseListener implementation
	 */

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) {
			pointPress = e.getPoint();
			mousePressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) {
			mousePressed = false;
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

	/*
	 * MouseWheelListener
	 */

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// do nothing
	}

	/*
	 * Drawing stuff
	 */

	protected void drawCrosshair(Graphics2D g)
	{
		GraphicsUtil.useAntialiasing(g, false);
		g.setStroke(new BasicStroke(1.0f));
		g.setColor(colorCrosshair);
		g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		int d = 20;
		g.drawArc(getWidth() / 2 - d / 2, getHeight() / 2 - d / 2, d, d, 0, 90);
		g.drawArc(getWidth() / 2 - d / 2, getHeight() / 2 - d / 2, d, d, 180,
				90);
	}

}
