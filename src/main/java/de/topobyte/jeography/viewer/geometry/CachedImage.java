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

import java.awt.image.BufferedImage;

import de.topobyte.jeography.core.mapwindow.MapWindow;

/**
 * A cached image is an instance of BufferedImage together with the information
 * of it's location in form of a mapWindow.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CachedImage
{

	private BufferedImage image;
	private MapWindow mapWindow;

	/**
	 * Default constructor.
	 * 
	 * @param image
	 *            the image.
	 * @param mapWindow
	 *            the position information.
	 */
	public CachedImage(BufferedImage image, MapWindow mapWindow)
	{
		this.image = image;
		this.mapWindow = mapWindow;
	}

	/**
	 * @return the image represented.
	 */
	public BufferedImage getImage()
	{
		return image;
	}

	/**
	 * @return the positional information.
	 */
	public MapWindow getMapWindow()
	{
		return mapWindow;
	}

	/**
	 * @param image
	 *            the image to represent.
	 */
	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	/**
	 * @param mapWindow
	 *            the positional information.
	 */
	public void setMapWindow(MapWindow mapWindow)
	{
		this.mapWindow = mapWindow;
	}

}
