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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infomatiq.jsi.Rectangle;
import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.core.ImageManager;
import de.topobyte.jeography.core.LoadListener;
import de.topobyte.jeography.core.MemoryCache;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyle;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyleChangeListener;
import de.topobyte.jsi.GenericRTree;
import de.topobyte.jsijts.JsiAndJts;

/**
 * TODO: extend GenericImageManager which has been extracted from here
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageManagerGeometry implements ImageManager<Tile, BufferedImage>,
		LoadListener<Tile, BufferedImage>, GeometryStyleChangeListener
{

	final static Logger logger = LoggerFactory
			.getLogger(ImageManagerGeometry.class);

	private GeometryStyle style;
	private List<ZoomlevelGeometryProvider> geometries;
	private GenericRTree<ZoomlevelGeometryProvider> tree;
	private MemoryCache<Tile, BufferedImage> cache;

	private int desiredCacheSize = 100;

	private ImageProviderGeometry provider = null;

	private Set<LoadListener<Tile, BufferedImage>> listeners = new HashSet<>();

	private int nthreads = 2;

	private int tileWidth, tileHeight;

	@Override
	protected void finalize()
	{
		logger.debug("finalize");
	}

	/**
	 * Default constructor.
	 * 
	 * @param mapWindow
	 */
	public ImageManagerGeometry(final TileMapWindow mapWindow)
	{
		style = null;
		geometries = new ArrayList<>();
		buildRTree();
		cache = new MemoryCache<>(desiredCacheSize);

		tileWidth = mapWindow.getTileWidth();
		tileHeight = mapWindow.getTileHeight();

		provider = new ImageProviderGeometry(new GeometryStyle(), tree,
				nthreads, tileWidth, tileHeight);
		provider.addLoadListener(this);

		mapWindow.addChangeListener(new MapWindowChangeListener() {

			@Override
			public void changed()
			{
				if (tileWidth == mapWindow.getTileWidth()
						&& tileHeight == mapWindow.getTileHeight()) {
					return;
				}
				tileWidth = mapWindow.getTileWidth();
				tileHeight = mapWindow.getTileHeight();
				provider.setTileWidth(tileWidth);
				provider.setTileHeight(tileHeight);
				cache.clear();

				setCacheHintMinimumSize(mapWindow.minimumCacheSize());
			}
		});
	}

	@Override
	public BufferedImage get(Tile tile)
	{
		BufferedImage image = cache.get(tile);
		if (image != null) {
			return image;
		}

		provider.provide(tile);

		return null;
	}

	/**
	 * @param style
	 *            the style to use for painting
	 * @param geometries
	 *            the geometries to display.
	 */
	public void setGeometries(GeometryStyle style,
			Collection<GeometryContainer> geometries)
	{
		provider.removeLoadListener(this);
		provider.stopRunning();

		if (this.style != null) {
			this.style.removeChangeListener(this);
		}
		this.style = style;
		style.addChangeListener(this);

		this.geometries = new ArrayList<>();

		for (GeometryContainer geometryContainer : geometries) {
			Geometry geometry = geometryContainer.getGeometry().getGeometry();
			ZoomlevelGeometryProvider zgp = new ZoomlevelGeometryProvider(
					geometry);
			this.geometries.add(zgp);
		}

		// replace provider to prevent that _old_ tiles that are
		// being loaded in a running thread get inserted into the
		// _current_ cache...
		// TODO: still possible, if notification system is currently
		// working on a notification

		buildRTree();
		provider = new ImageProviderGeometry(style, tree, nthreads, tileWidth,
				tileHeight);
		provider.addLoadListener(this);
		// provider.setTree(tree);
		cache.clear();
	}

	private void buildRTree()
	{
		tree = new GenericRTree<>();

		for (ZoomlevelGeometryProvider zgp : geometries) {
			Rectangle rectangle = JsiAndJts.toRectangle(zgp
					.getOriginalGeometry());
			tree.add(rectangle, zgp);
		}
	}

	@Override
	public void loaded(Tile tile, BufferedImage image)
	{
		if (image.getWidth() != tileWidth || image.getHeight() != tileHeight) {
			// TODO: this is a quick and dirty hack to circumvent wrongly sized
			// tiles to end up in the cache. To solve this correctly, we should
			// definitely also stop pending productions and use a settingsId
			// similar to the implementation in the ImageManagerSourceRam
			return;
		}
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

	@Override
	public void changedName()
	{
		// nothing to do here.
	}

	@Override
	public void changedAttributes()
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

	/**
	 * Destroy this ImageManger.
	 */
	@Override
	public void destroy()
	{
		logger.debug("logging off from style: " + style);
		style.removeChangeListener(this);
		provider.stopRunning();
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
