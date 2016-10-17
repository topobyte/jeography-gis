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

package de.topobyte.jeography.viewer.export;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.selection.rectangular.Latitude;
import de.topobyte.jeography.viewer.selection.rectangular.Longitude;
import de.topobyte.jeography.viewer.selection.rectangular.Selection;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageExporter
{

	static final Logger logger = LoggerFactory.getLogger(ImageExporter.class);

	// private final Selection<Longitude, Latitude> selection;
	private final TileConfig tileConfig;

	private TileMapWindow mapWindow;

	private int nImages = 0;
	private int nAvailable = 0;
	private int nFailed = 0;
	private Map<Tile, BufferedImage> images = new HashMap<>();
	private ImageManager<Tile, BufferedImage> manager;

	private BufferedImage targetImage;

	/**
	 * Create a new ImageExporter instance for the specified zoom level and
	 * selection
	 * 
	 * @param zoom
	 *            the zoom level to use for exporting
	 * @param selection
	 *            the selection to export
	 * @param tileConfig
	 *            the tile configuration to use
	 */
	public ImageExporter(int zoom, Selection<Longitude, Latitude> selection,
			TileConfig tileConfig)
	{
		this(zoom,
				new BBox(selection.getX1().value(), selection.getY1().value(),
						selection.getX2().value(), selection.getY2().value()),
				tileConfig);
	}

	/**
	 * Create a new ImageExporter instance for the specified zoom level and
	 * bounding box.
	 * 
	 * @param zoom
	 *            the zoom level to use for the export.
	 * @param bbox
	 *            the bounding box to show.
	 * @param tileConfig
	 *            the tile configuration to use
	 */
	public ImageExporter(int zoom, BBox bbox, TileConfig tileConfig)
	{
		this.tileConfig = tileConfig;
		mapWindow = new SteppedMapWindow(bbox, zoom);
	}

	/**
	 * Create a new ImageExporter instance for the specified bounding box to be
	 * covered with in an image of the denoted size.
	 * 
	 * @param bbox
	 *            the bounding box to cover
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 * @param tileConfig
	 *            the tile configuration to use.
	 */
	public ImageExporter(BBox bbox, int width, int height,
			TileConfig tileConfig)
	{
		mapWindow = new SteppedMapWindow(width, height, 10, bbox.getLon1(),
				bbox.getLat1());
		mapWindow.gotoLonLat(bbox.getLon1(), bbox.getLon2(), bbox.getLat1(),
				bbox.getLat2());
		this.tileConfig = tileConfig;
	}

	/**
	 * Get the map window the is used within this instance.
	 * 
	 * @return the map window.
	 */
	public MapWindow getMapWindow()
	{
		return mapWindow;
	}

	/**
	 * Perform exporting.
	 * 
	 * @param filenameExport
	 *            the file's name to use for saving the image.
	 */
	public void export(String filenameExport)
	{
		export();
		try {
			logger.debug("closing");
			ImageIO.write(targetImage, "PNG", new File(filenameExport));
		} catch (IOException e1) {
			logger.debug("unable to write image");
		}
	}

	/**
	 * Perform exporting.
	 * 
	 * @return the image created.
	 */
	public BufferedImage export()
	{

		// get number of tiles
		Iterator<TileOnWindow> iterator = mapWindow.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			nImages += 1;
		}

		// setup image loader
		manager = tileConfig.createImageManager();

		manager.addLoadListener(new LoadListener<Tile, BufferedImage>() {

			@Override
			public void loaded(Tile thing, BufferedImage data)
			{
				put(thing, data);
			}

			@Override
			public void loadFailed(Tile thing)
			{
				fail(thing);
			}
		});

		synchronized (sync) {
			// queue images
			for (TileOnWindow tow : mapWindow) {
				logger.debug(tow.getTx() + "," + tow.getTy());
				BufferedImage i = manager.get(tow);
				if (i != null) {
					put(tow, i);
				}
			}
		}

		synchronized (sync) {
			while (!imageReady) {
				try {
					sync.wait();
				} catch (InterruptedException e) {
					// continue waiting
				}
			}
		}

		return targetImage;
	}

	private boolean imageReady = false;
	private Object sync = new Object();

	void put(Tile tile, BufferedImage image)
	{
		logger.debug("tile loaded: " + tile);
		synchronized (sync) {
			images.put(tile, image);
			nAvailable += 1;
			if (nAvailable + nFailed == nImages) {
				produce();
			}
		}
	}

	void fail(Tile tile)
	{
		logger.debug("tile failed: " + tile);
		synchronized (sync) {
			nFailed += 1;
			if (nAvailable + nFailed == nImages) {
				produce();
			}
		}
	}

	private void produce()
	{
		logger.debug("produce");
		manager.destroy();

		int width = mapWindow.getWidth();
		int height = mapWindow.getHeight();
		targetImage = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = (Graphics2D) targetImage.getGraphics();

		for (TileOnWindow tow : mapWindow) {
			logger.debug("blitting: " + tow.getTx() + "," + tow.getTy());
			BufferedImage i = images.get(tow);
			g2d.drawImage(i, null, tow.getDX(), tow.getDY());
		}

		imageReady = true;
		sync.notify();
	}

}
