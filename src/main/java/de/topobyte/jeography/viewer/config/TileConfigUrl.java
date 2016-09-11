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
import de.topobyte.jeography.tiles.TileResoluterUrl;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.manager.PriorityImageManagerHttp;
import de.topobyte.jeography.viewer.core.PaintListener;

/**
 * Information about a url of tiles.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileConfigUrl implements TileConfig
{

	private int id;
	private String name;
	private String url;
	private String userAgent;

	/**
	 * Create a new TileConfig
	 * 
	 * @param id
	 *            an artificial id to use.
	 * @param name
	 *            the name of this configuration.
	 * @param url
	 *            an url pattern containing printf-style %d's for zoom, x, y
	 */
	public TileConfigUrl(int id, String name, String url)
	{
		this.id = id;
		this.name = name;
		this.url = url;
	}

	/**
	 * Set the user agent to use for the connections opened.
	 * 
	 * @param userAgent
	 *            the user agent to use for HTTP requests.
	 */
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;

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
	 * @return the url pattern.
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return the user agent to use when retrieving tiles.
	 */
	public String getUserAgent()
	{
		return userAgent;
	}

	@Override
	public String toString()
	{
		return "name: " + getName() + ", url: " + getUrl();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof TileConfigUrl) {
			TileConfigUrl other = (TileConfigUrl) o;
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
		TileResoluterUrl tileResoluterBase = new TileResoluterUrl(getUrl());
		PriorityImageManagerHttp<Tile> manager = new PriorityImageManagerHttp<>(
				4, 150, tileResoluterBase);

		if (userAgent != null) {
			manager.setUserAgent(userAgent);
		}
		return manager;
	}

	@Override
	public PaintListener createGlobalManager()
	{
		return null;
	}

}
