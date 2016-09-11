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

import java.awt.Color;

import javax.swing.JPanel;

import de.topobyte.jeography.viewer.MouseUser;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.zoom.ZoomMode;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractViewer extends JPanel implements MouseUser
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

}
