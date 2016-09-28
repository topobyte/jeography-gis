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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.tiles.BufferedImageAndBytes;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.CachePathProvider;
import de.topobyte.jeography.tiles.UrlProvider;
import de.topobyte.jeography.tiles.source.ImageProviderDisk;
import de.topobyte.jeography.tiles.source.ImageProviderHttp;

/**
 * @param <T>
 *            type argument
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageManagerHttpDisk<T> extends
		AbstractImageManagerWithMemoryCachePlus<T, BufferedImage>
{

	final static Logger logger = LoggerFactory
			.getLogger(ImageManagerHttpDisk.class);

	boolean online = true;
	ImageProviderDisk<T> diskProvider = null;
	ImageProviderHttp<T> httpProvider = null;

	/**
	 * Create a new ImageManager that provides tiles from http and backed by a
	 * disk-cache.
	 * 
	 * @param resolver
	 *            the information about how to resolve tiles.
	 */
	public <X extends CachePathProvider<T> & UrlProvider<T>> ImageManagerHttpDisk(
			X resolver)
	{
		diskProvider = new ImageProviderDisk<>(resolver);
		httpProvider = new ImageProviderHttp<>(resolver, 4, 5);

		diskProvider.addLoadListener(new LoadListenerDisk(this));
		httpProvider.addLoadListener(new LoadListenerHttp(this));
	}

	@Override
	public BufferedImage get(T thing)
	{
		BufferedImage image = memoryCache.get(thing);
		if (image != null) {
			return image;
		}
		diskProvider.provide(thing);
		return null;
	}

	private class LoadListenerDisk implements LoadListener<T, BufferedImage>
	{

		private ImageManagerHttpDisk<T> manager;

		LoadListenerDisk(ImageManagerHttpDisk<T> manager)
		{
			this.manager = manager;
		}

		@Override
		public void loadFailed(T thing)
		{
			if (online) {
				manager.httpProvider.provide(thing);
			}
		}

		@Override
		public void loaded(T thing, BufferedImage image)
		{
			manager.memoryCache.put(thing, image);
			notifyListeners(thing, image);
		}
	}

	private class LoadListenerHttp implements
			LoadListener<T, BufferedImageAndBytes>
	{

		private ImageManagerHttpDisk<T> manager;

		LoadListenerHttp(ImageManagerHttpDisk<T> manager)
		{
			this.manager = manager;
		}

		@Override
		public void loadFailed(T thing)
		{
			logger.debug("failed loading from HTTP ... giving up");
			notifyListenersFail(thing);
		}

		@Override
		public void loaded(T thing, BufferedImageAndBytes image)
		{
			manager.memoryCache.put(thing, image.getImage());
			notifyListeners(thing, image.getImage());
			manager.diskProvider.push(thing, image);
		}
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
		httpProvider.setUserAgent(userAgent);
	}

	@Override
	public void destroy()
	{
		diskProvider.stopRunning();
		httpProvider.stopRunning();
	}

}
