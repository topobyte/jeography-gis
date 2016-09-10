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
import java.io.File;

import de.topobyte.jeography.core.PaintListener;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.tiles.TileResoluterDisk;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.manager.ImageManagerDisk;

/**
 * Information about a storage directory.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileConfigDisk implements TileConfig
{

	private int id;
	private String name;
	private String path;

	/**
	 * Create a new TileConfig
	 * 
	 * @param id
	 *            an artificial id to use.
	 * @param name
	 *            the name of this configuration.
	 * @param path
	 *            where to store temporary images
	 */
	public TileConfigDisk(int id, String name, String path)
	{
		this.id = id;
		this.name = name;
		this.path = path;
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

	/**
	 * @return the path for temporary images.
	 */
	public String getPath()
	{
		return path;
	}

	@Override
	public String toString()
	{
		return "name: " + getName() + ", path: " + getPath();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof TileConfigDisk) {
			TileConfigDisk other = (TileConfigDisk) o;
			return other.id == id;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public ImageManager<Tile, BufferedImage> createImageManager()
	{

		File pathDir = new File(getPath());
		if (!pathDir.exists()) {
			pathDir.mkdirs();
		}
		if (!pathDir.exists() && pathDir.isDirectory()) {
			return null;
		}

		TileResoluterDisk tileResoluterBase = new TileResoluterDisk(getPath());
		ImageManagerDisk<Tile> manager = new ImageManagerDisk<>(
				tileResoluterBase);
		return manager;
	}

	@Override
	public PaintListener createGlobalManager()
	{
		return null;
	}

}
