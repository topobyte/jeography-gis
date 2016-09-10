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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.topobyte.jeography.core.ImageManagerSourceRam;
import de.topobyte.jeography.core.ImageSourceUrlPattern;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileResoluterUrlDisk;
import de.topobyte.jeography.viewer.config.ManagerTileConfig;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * A special test for a map viewer with special tile-management features.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PriorityViewerTest
{

	/*
	 * Change tiles when hitting the space key.
	 */

	String tileUrl1 = "http://tile.openstreetmap.org/%d/%d/%d.png";
	String tileUrl2 = "http://tiles-base.openstreetbrowser.org/tiles/basemap_base/%d/%d/%d.png";
	String[] urls = new String[] { tileUrl1, tileUrl2 };

	int currentTiles = 0;
	Viewer viewer;

	ManagerTileConfig tileConfig;

	// TODO: add clearCache under a different name to PriorityImageManager
	// PriorityImageManager<Tile, BufferedImage, Integer> manager;
	ImageManagerSourceRam<Tile, BufferedImage> manager;
	ImageSourceUrlPattern<Tile> source;

	/**
	 * Execute the test
	 * 
	 * @param args
	 *            none.
	 */
	public static void main(String[] args)
	{
		PriorityViewerTest test = new PriorityViewerTest();
		test.start();
	}

	private void start()
	{
		createManagerFromUrl(tileUrl1);
		tileConfig = new ManagerTileConfig(1, "test", manager);
		viewer = new Viewer(tileConfig, null);
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

	private void createManagerFromUrl(String tileUrl)
	{
		TileResoluterUrlDisk resolver = new TileResoluterUrlDisk(null, tileUrl);
		source = new ImageSourceUrlPattern<>(resolver, 3);
		manager = new ImageManagerSourceRam<>(1, 64, source);
	}

	void changeTiles()
	{
		currentTiles = (currentTiles + 1) % urls.length;
		setTileUrl(urls[currentTiles]);
	}

	private void setTileUrl(String tileUrl)
	{
		TileResoluterUrlDisk resolver = new TileResoluterUrlDisk(null, tileUrl);
		source.setPathResoluter(resolver);
		manager.cancelJobs();
		manager.setIgnorePendingProductions();
		manager.clearCache();
		viewer.repaint();
	}

}
