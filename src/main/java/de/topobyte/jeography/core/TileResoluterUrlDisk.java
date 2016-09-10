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

package de.topobyte.jeography.core;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileResoluterUrlDisk implements PathResoluter<Tile>,
		UrlResoluter<Tile>
{

	// private String cacheDir;
	private String cacheFileTemplate;
	private String urlTemplate;

	/**
	 * @param cacheDir
	 *            the dir to store cache images.
	 * @param urlTemplate
	 *            the url template.
	 */
	public TileResoluterUrlDisk(String cacheDir, String urlTemplate)
	{
		// this.cacheDir = cacheDir;
		cacheFileTemplate = cacheDir + "/%d_%d_%d.png";
		this.urlTemplate = urlTemplate;
	}

	@Override
	public String getCacheFile(Tile tile)
	{
		return String.format(cacheFileTemplate, tile.getZoom(), tile.getTx(),
				tile.getTy());
	}

	@Override
	public String getUrl(Tile tile)
	{
		return String.format(urlTemplate, tile.getZoom(), tile.getTx(),
				tile.getTy());
	}

}
