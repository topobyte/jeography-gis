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

package de.topobyte.jeography.viewer.selection.download;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.CachePathProvider;
import de.topobyte.jeography.tiles.TileUrlAndCachePathProvider;
import de.topobyte.jeography.tiles.UrlProvider;
import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;
import de.topobyte.util.async.Executer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileDownloader
{

	final static Logger logger = LoggerFactory.getLogger(TileDownloader.class);

	// private final TileConfig config;
	final BBox boundingBox;
	final List<Integer> zoomLevels;

	private UrlProvider<Tile> urlResoluter;
	private CachePathProvider<Tile> pathResoluter;

	private int numberOfTilesToDownload = 0;

	private int tilesCompleted = 0;
	private int tilesGivenUp = 0;

	private List<DownloadProgressListener> progressListeners = new ArrayList<>();

	private TilePathProvider customResoluter;

	/**
	 * Create a new TileDownloader instance.
	 * 
	 * @param config
	 *            the config to use for resolving URLs and CacheFile paths.
	 * @param customResoluter
	 * @param boundingBox
	 *            the bounding box to retrieve tiles for.
	 * @param zoomLevels
	 *            the zoom levels to retrieve tiles for.
	 */
	public TileDownloader(TileConfigUrlDisk config,
			TilePathProvider customResoluter, BBox boundingBox,
			List<Integer> zoomLevels)
	{
		this.customResoluter = customResoluter;
		// this.config = config;
		this.boundingBox = boundingBox;
		this.zoomLevels = zoomLevels;

		TileUrlAndCachePathProvider resoluter = new TileUrlAndCachePathProvider(
				config.getPath(), config.getUrl());
		urlResoluter = resoluter;
		pathResoluter = resoluter;

		for (int zoom : zoomLevels) {
			TileMapWindow mapWindow = new SteppedMapWindow(boundingBox, zoom);
			numberOfTilesToDownload += mapWindow.getNumTilesX()
					* mapWindow.getNumTilesY();
		}
	}

	/**
	 * @return the number of tiles already downloaded
	 */
	public int getNumberOfTilesToDownload()
	{
		return numberOfTilesToDownload;
	}

	/**
	 * @return the number of tiles completed successfully.
	 */
	public int getTilesCompleted()
	{
		return tilesCompleted;
	}

	/**
	 * @return the number of tiles that we have given up to download upon.
	 */
	public int getTilesGivenUp()
	{
		return tilesGivenUp;
	}

	Executer executer;

	/**
	 * Begin download process.
	 */
	public void download()
	{
		executer = new Executer(10, 3);

		if (numberOfTilesToDownload == 0) {
			logger.debug("nothing to download");
			return;
		}

		Runnable task = new Runnable() {

			@Override
			public void run()
			{
				for (int zoom : zoomLevels) {
					TileMapWindow mapWindow = new SteppedMapWindow(boundingBox,
							zoom);
					for (TileOnWindow tow : mapWindow) {
						if (cancelled) {
							return;
						}
						queue(tow);
					}
				}
				executer.finish();
			}
		};

		Thread thread = new Thread(task);
		thread.start();
	}

	boolean cancelled = false;

	/**
	 * cancel the download process.
	 */
	public void cancel()
	{
		cancelled = true;
		executer.cancel();
	}

	/**
	 * pause or resume the download process.
	 */
	public void pauseResume()
	{
		executer.pauseOrResume();
	}

	/**
	 * @param listener
	 *            the listener to add.
	 */
	public void addProgressListener(DownloadProgressListener listener)
	{
		progressListeners.add(listener);
	}

	/**
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeProgressListener(DownloadProgressListener listener)
	{
		progressListeners.remove(listener);
	}

	private void notifyProgressListeners()
	{
		for (DownloadProgressListener l : progressListeners) {
			l.progress();
		}
	}

	private Object sync = new Object();

	void complete(TileOnWindow tow)
	{
		logger.debug("completed: " + tow.toString());
		synchronized (sync) {
			tilesCompleted += 1;
			checkComplete();
			notifyProgressListeners();
		}
	}

	void fail(TileOnWindow tow)
	{
		logger.debug("failed: " + tow.toString());
		synchronized (sync) {
			tilesGivenUp += 1;
			checkComplete();
			notifyProgressListeners();
		}
	}

	private void checkComplete()
	{
		if (tilesCompleted + tilesGivenUp == numberOfTilesToDownload) {
			logger.debug("finished all");
		}
	}

	void queue(final TileOnWindow tow)
	{
		final String url = urlResoluter.getUrl(tow);
		final String cacheFile;
		if (customResoluter == null) {
			cacheFile = pathResoluter.getCacheFile(tow);
		} else {
			File file = customResoluter.getPath(tow);
			cacheFile = file.getAbsolutePath();
		}

		executer.queue(new Runnable() {

			@Override
			public void run()
			{

				File file = new File(cacheFile);
				if (file.exists()) {
					complete(tow);
					return;
				}

				try {
					URL URL = new URL(url);
					URLConnection connection = URL.openConnection();
					InputStream is = connection.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream(
							40000);
					int b;
					while ((b = is.read()) != -1) {
						baos.write(b);
					}
					is.close();
					byte[] bytes = baos.toByteArray();
					// System.out.println(bytes.length);

					// ByteArrayInputStream bais = new
					// ByteArrayInputStream(bytes);
					// BufferedImage image = ImageIO.read(bais);

					FileOutputStream fos = new FileOutputStream(cacheFile);
					fos.write(bytes);
					fos.close();

					complete(tow);
				} catch (MalformedURLException e) {
					logger.debug("malformed URL");
					fail(tow);
				} catch (IOException e) {
					logger.debug("download failed: " + e.getMessage());
					fail(tow);
				}
			}
		});
	}

}
