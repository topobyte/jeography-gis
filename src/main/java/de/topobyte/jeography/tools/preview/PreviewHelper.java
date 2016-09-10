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

package de.topobyte.jeography.tools.preview;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.TileUtil;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.config.TileConfig;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PreviewHelper
{

	private Graphics2D graphics;
	private TileMapWindow mapWindow;

	public PreviewHelper(BufferedImage image, TileMapWindow mapWindow)
	{
		this.mapWindow = mapWindow;
		this.graphics = image.createGraphics();
	}

	public PreviewHelper(Graphics2D graphics, TileMapWindow mapWindow)
	{
		this.mapWindow = mapWindow;
		this.graphics = graphics;
	}

	private static int counter = 0;
	private static int total = 0;

	private static Object sync = new Object();
	private static Set<Tile> queriedTiles = new HashSet<>();
	private static Map<Tile, BufferedImage> map = new HashMap<>();

	public void prepareImage()
	{
		Configuration configuration = Configuration
				.createDefaultConfiguration();
		String configFile = ConfigurationHelper.getUserConfigurationFilePath();
		System.out.println("default user config file: " + configFile);
		try {
			configuration = ConfigReader.read(configFile);
		} catch (Exception e) {
			System.out.println("unable to read configuration: "
					+ e.getMessage());
			System.out.println("using default configuration");
		}

		TileConfig tileConfig = configuration.getTileConfigs().get(0);
		ImageManager<Tile, BufferedImage> imageManager = tileConfig
				.createImageManager();

		imageManager.addLoadListener(new LoadListener<Tile, BufferedImage>() {

			@Override
			public void loaded(Tile tile, BufferedImage image)
			{
				synchronized (sync) {
					counter++;
					map.put(tile, image);
					sync.notify();
				}
			}

			@Override
			public void loadFailed(Tile tile)
			{
				synchronized (sync) {
					counter++;
					sync.notify();
				}
			}
		});

		for (TileOnWindow tow : mapWindow) {
			if (!TileUtil.isValid(tow)) {
				continue;
			}
			if (!queriedTiles.contains(tow)) {
				queriedTiles.add(tow);
				total += 1;
			}
		}

		for (TileOnWindow tow : mapWindow) {
			if (!TileUtil.isValid(tow)) {
				continue;
			}
			BufferedImage tileImage = imageManager.get(tow);
			if (tileImage == null) {
				continue;
			}
			synchronized (sync) {
				counter++;
				map.put(tow, tileImage);
				sync.notify();
			}
		}

		while (true) {
			synchronized (sync) {
				if (counter < total) {
					while (true) {
						try {
							sync.wait();
							break;
						} catch (InterruptedException e) {
							// continue
						}
					}
				} else {
					break;
				}
			}
		}
		System.out.println("drawing, base layer");
		for (TileOnWindow tow : mapWindow) {
			if (!TileUtil.isValid(tow)) {
				continue;
			}
			BufferedImage tileImage = map.get(tow);
			if (tileImage == null) {
				continue;
			}
			graphics.drawImage(tileImage, tow.getDX(), tow.getDY(), null);
		}

	}

}
