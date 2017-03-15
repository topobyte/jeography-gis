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

package de.topobyte.jeography.viewer.config;

import java.awt.image.BufferedImage;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.viewer.core.PaintListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface TileConfig
{

	/**
	 * A unique id that can be used to identify this TileConfig instance.
	 * 
	 * @return the id.
	 */
	public int getId();

	/**
	 * A descriptive name of this configuration.
	 * 
	 * @return the name.
	 */
	public String getName();

	/**
	 * Create an ImageManager for tiles from this TileConfig instance.
	 * 
	 * @return the newly created ImageManager.
	 */
	public ImageManager<Tile, BufferedImage> createImageManager();

	public PaintListener createGlobalManager();

}
