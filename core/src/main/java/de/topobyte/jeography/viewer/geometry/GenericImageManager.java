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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.cache.MemoryCache;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.source.ImageProvider;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GenericImageManager implements ImageManager<Tile, BufferedImage>,
		LoadListener<Tile, BufferedImage>
{

	final static Logger logger = LoggerFactory
			.getLogger(GenericImageManager.class);

	private int desiredCacheSize = 100;

	private MemoryCache<Tile, BufferedImage> cache;
	private ImageProvider<Tile, BufferedImage> imageProvider;

	private Set<LoadListener<Tile, BufferedImage>> listeners = new HashSet<>();

	@Override
	protected void finalize()
	{
		logger.debug("finalize");
	}

	/**
	 * Default constructor.
	 * 
	 * @param imageProvider
	 *            the imageProvider to use for producing tiles
	 */
	public GenericImageManager(ImageProvider<Tile, BufferedImage> imageProvider)
	{
		this.imageProvider = imageProvider;
		cache = new MemoryCache<>(desiredCacheSize);

		imageProvider.addLoadListener(this);
		// getImageProvider().addLoadListener(this);
	}

	@Override
	public BufferedImage get(Tile tile)
	{
		BufferedImage image = cache.get(tile);
		if (image != null) {
			return image;
		}

		getImageProvider().provide(tile);

		return null;
	}

	/**
	 * @return the image provider that is producing the tiles.
	 */
	public ImageProvider<Tile, BufferedImage> getImageProvider()
	{
		return imageProvider;
	}

	@Override
	public void loaded(Tile tile, BufferedImage image)
	{
		cache.put(tile, image);
		logger.debug("loaded: " + tile);
		notifyListeners(tile, image);
	}

	@Override
	public void loadFailed(Tile tile)
	{
		// TODO: what to do here?
		logger.debug("failed: " + tile);
	}

	@Override
	public void addLoadListener(LoadListener<Tile, BufferedImage> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeLoadListener(LoadListener<Tile, BufferedImage> listener)
	{
		listeners.remove(listener);
	}

	private void notifyListeners(Tile thing, BufferedImage image)
	{
		for (LoadListener<Tile, BufferedImage> listener : listeners) {
			listener.loaded(thing, image);
		}
	}

	/**
	 * Reset the image-manger. The cache will be cleared and listeners will be
	 * informed about the fact that they should consider repainting things
	 * generated from tiles of this image-manager.
	 */
	public void reset()
	{
		cache.clear();
		notifyUpdateListeners();
	}

	private List<ImageManagerUpdateListener> updateListeners = new ArrayList<>();

	/**
	 * @param l
	 *            the listener to add.
	 */
	public void addUpdateListener(ImageManagerUpdateListener l)
	{
		updateListeners.add(l);
	}

	/**
	 * @param l
	 *            the listener to remove.
	 */
	public void removeUpdateListener(ImageManagerUpdateListener l)
	{
		updateListeners.remove(l);
	}

	private void notifyUpdateListeners()
	{
		for (ImageManagerUpdateListener listener : updateListeners) {
			listener.updated();
		}
	}

	@Override
	public void destroy()
	{
		// do nothing
	}

	@Override
	public void setCacheHintMinimumSize(int size)
	{
		if (cache.getSize() < size) {
			cache.setSize(size);
		} else if (size < desiredCacheSize) {
			cache.setSize(desiredCacheSize);
		}
	}

	@Override
	public void willNeed(Tile thing)
	{
		// do nothing here
	}

}
