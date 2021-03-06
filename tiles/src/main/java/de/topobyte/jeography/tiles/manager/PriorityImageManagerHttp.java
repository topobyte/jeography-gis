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

package de.topobyte.jeography.tiles.manager;

import java.awt.image.BufferedImage;

import de.topobyte.jeography.tiles.UrlProvider;
import de.topobyte.jeography.tiles.source.ImageSourceUrlPattern;
import de.topobyte.jeography.tiles.source.UnwrappingImageSource;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PriorityImageManagerHttp<T>
		extends ImageManagerSourceRam<T, BufferedImage>
{

	boolean online = true;

	private ImageSourceUrlPattern<T> sourceUrl;

	/**
	 * Create an ImageSource based ImageManager implementation that does in-RAM
	 * caching.
	 * 
	 * @param nThreads
	 *            the number of threads to use for providing images.
	 * @param cacheSize
	 *            the number of data elements to store in the RAM cache.
	 * @param source
	 *            the ImageSource to use for creation of unknown requests.
	 */
	public <X extends UrlProvider<T>> PriorityImageManagerHttp(int nThreads,
			int cacheSize, X resolver)
	{
		super(nThreads, cacheSize, null);

		sourceUrl = new ImageSourceUrlPattern<>(resolver, 3);

		imageSource = new UnwrappingImageSource<>(sourceUrl);
	}

	/**
	 * Set whether an internet connection shall be used to retrieve tiles.
	 * 
	 * @param state
	 *            the network state.
	 */
	public void setNetworkState(boolean state)
	{
		online = state;
		sourceUrl.setOnline(state);
	}

	/**
	 * @return whether an internet connection shall be used to retrieve tiles.
	 */
	public boolean getNetworkState()
	{
		return online;
	}

	/**
	 * Set the user-agent to use during HTTP-requests.
	 * 
	 * @param userAgent
	 *            the user agent to use.
	 */
	public void setUserAgent(String userAgent)
	{
		sourceUrl.setUserAgent(userAgent);
	}

}
