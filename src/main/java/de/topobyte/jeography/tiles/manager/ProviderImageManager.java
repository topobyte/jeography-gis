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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.source.ImageProvider;

/**
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ProviderImageManager<D> extends
		AbstractImageManagerWithMemoryCache<Tile, D>
{

	final static Logger logger = LoggerFactory
			.getLogger(ProviderImageManager.class);

	private ImageProvider<Tile, D> imageProvider;

	/**
	 * Create a new ImageManager that serves tiles from an ImageProvider.
	 * 
	 * @param imageProvider
	 *            the provider that produces images.
	 */
	public ProviderImageManager(ImageProvider<Tile, D> imageProvider)
	{
		this(imageProvider, 150);
	}

	/**
	 * Create a new ImageManager that serves tiles from an ImageProvider.
	 * 
	 * @param imageProvider
	 *            the provider that produces images.
	 * @param cacheSize
	 *            the number of images to cache in main memory.
	 */
	public ProviderImageManager(ImageProvider<Tile, D> imageProvider,
			int cacheSize)
	{
		super(cacheSize);
		this.imageProvider = imageProvider;
		imageProvider.addLoadListener(new LoadListenerImpl());
	}

	@Override
	public D get(Tile thing)
	{
		D image = memoryCache.get(thing);
		if (image != null) {
			return image;
		}
		if (imageProvider != null) {
			imageProvider.provide(thing);
		}
		return null;
	}

	private class LoadListenerImpl implements LoadListener<Tile, D>
	{

		public LoadListenerImpl()
		{
			// empty constructor.
		}

		@Override
		public void loaded(Tile thing, D image)
		{
			memoryCache.put(thing, image);
			notifyListeners(thing, image);
		}

		@Override
		public void loadFailed(Tile thing)
		{
			// do nothing
		}

	}

	@Override
	public void destroy()
	{
		// do nothing
	}

	/**
	 * Ensure that the denoted tile will be removed from the cache.
	 * 
	 * @param tile
	 *            the tile to remove.
	 */
	public void unchache(Tile tile)
	{
		memoryCache.remove(tile);
	}

	/**
	 * Clear the underlying cache.
	 */
	public void clearCache()
	{
		memoryCache.clear();
	}

	/**
	 * Get the ImageProvider this Manager used to produce tiles.
	 * 
	 * @return the ImageProvider in use.
	 */
	public ImageProvider<Tile, D> getImageProvider()
	{
		return imageProvider;
	}

	@Override
	public void willNeed(Tile thing)
	{
		// do nothing here
	}

}
