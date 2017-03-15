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

import java.awt.Graphics;

import de.topobyte.jeography.core.mapwindow.TileMapWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface PaintListener
{

	/**
	 * Called when a paint has been done.
	 * 
	 * @param mapWindow
	 *            the associated mapWindow.
	 * @param g
	 *            the graphics object emitted.
	 */
	public void onPaint(TileMapWindow mapWindow, Graphics g);

}
