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

package de.topobyte.jeography.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.tiles.TileResoluterUrlDisk;
import de.topobyte.jeography.tiles.manager.ImageManagerSourceRam;
import de.topobyte.jeography.tiles.source.ImageSourceUrlPattern;
import de.topobyte.jeography.tiles.source.UnwrappingImageSource;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * A special test for a map viewer with special tile-management features.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CustomViewerTest
{

	/*
	 * Change tiles when hitting the space key.
	 */

	int currentTiles = 0;
	MyViewer viewer;

	/**
	 * Execute the test
	 * 
	 * @param args
	 *            none.
	 */
	public static void main(String[] args)
	{
		CustomViewerTest test = new CustomViewerTest();
		test.start();
	}

	private void start()
	{
		viewer = new MyViewer(TestConfigs.urls[0]);
		viewer.setMouseActive(true);

		JFrame frame = new JFrame("Viewer Test");
		frame.setContentPane(viewer);
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		viewer.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == ' ') {
					changeTiles();
				}
			}
		});
	}

	void changeTiles()
	{
		currentTiles = (currentTiles + 1) % TestConfigs.urls.length;
		viewer.setTileUrl(TestConfigs.urls[currentTiles]);
	}

	class MyViewer extends Viewer
	{

		private static final long serialVersionUID = 1L;

		private ImageSourceUrlPattern<Tile> source;
		private ImageManagerSourceRam<Tile, BufferedImage> manager;

		public MyViewer(String tileUrl)
		{
			super(Configuration.createDefaultConfiguration().getTileConfigs()
					.get(0), null);
			createManagerFromUrl(tileUrl);
		}

		private void createManagerFromUrl(String tileUrl)
		{
			TileResoluterUrlDisk resolver = new TileResoluterUrlDisk(null,
					tileUrl);
			source = new ImageSourceUrlPattern<>(resolver, 3);
			UnwrappingImageSource<Tile> unwrapper = new UnwrappingImageSource<>(
					source);
			manager = new ImageManagerSourceRam<>(1, 40, unwrapper);
			manager.addLoadListener(MyViewer.this);
		}

		public void setTileUrl(String tileUrl)
		{
			TileResoluterUrlDisk resolver = new TileResoluterUrlDisk(null,
					tileUrl);
			source.setPathResoluter(resolver);
			manager.cancelJobs();
			manager.setIgnorePendingProductions();
			manager.clearCache();
			viewer.repaint();
		}

		@Override
		public void paintComponent(Graphics g)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());

			// first cancel pending jobs
			manager.cancelJobs();

			// renew current tiles' cache status
			for (TileOnWindow tile : getMapWindow()) {
				manager.willNeed(tile);
			}

			// request and draw
			for (TileOnWindow tile : getMapWindow()) {
				// calculate priority
				int priority = calculatePriority(tile, getMapWindow());

				// request
				BufferedImage image = manager.get(tile, priority);

				// draw
				g.drawImage(image, tile.getDX(), tile.getDY(), null);
				g.setColor(Color.BLACK);
				g.drawRect(tile.getDX(), tile.getDY(), Tile.SIZE, Tile.SIZE);
			}
		}

		private int calculatePriority(TileOnWindow tile, MapWindow mapWindow)
		{
			int width = mapWindow.getWidth();
			int height = mapWindow.getHeight();
			int midX = width / 2;
			int midY = height / 2;
			int tX = tile.getDX() + Tile.SIZE / 2;
			int tY = tile.getDY() + Tile.SIZE / 2;
			int dX = tX - midX;
			int dY = tY - midY;
			int dist = dX * dX + dY * dY;
			return dist;
		}
	}

}
