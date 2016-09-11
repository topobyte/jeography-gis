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
public class ManagerTileConfig implements TileConfig
{

	private final int id;
	private final String name;
	private final ImageManager<Tile, BufferedImage> manager;

	/**
	 * Create a new instance of TileConfig that simply presents a single manager
	 * instance.
	 * 
	 * @param id
	 *            the id to use.
	 * @param name
	 *            the name for this configuration.
	 * @param manager
	 *            the manager that this configuration should provide.
	 */
	public ManagerTileConfig(int id, String name,
			ImageManager<Tile, BufferedImage> manager)
	{
		this.id = id;
		this.name = name;
		this.manager = manager;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ImageManager<Tile, BufferedImage> createImageManager()
	{
		return manager;
	}

	@Override
	public PaintListener createGlobalManager()
	{
		return null;
	}

}
