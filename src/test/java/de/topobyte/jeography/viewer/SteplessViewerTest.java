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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import de.topobyte.jeography.core.OverlayPoint;
import de.topobyte.jeography.viewer.core.SteplessViewer;
import de.topobyte.jeography.viewer.core.SteplessViewer.TileDrawMode;

/**
 * A test for the stepless map viewer.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SteplessViewerTest
{

	/*
	 * Change tiles when hitting the space key.
	 */

	int currentTiles = 0;
	SteplessViewer viewer;

	/**
	 * Execute the test
	 * 
	 * @param args
	 *            none.
	 */
	public static void main(String[] args)
	{
		SteplessViewerTest test = new SteplessViewerTest();
		test.start();
	}

	private void start()
	{
		viewer = new SteplessViewer(TestConfigs.configs.get(0), null);
		viewer.setMouseActive(true);
		viewer.setDrawCrosshair(false);
		viewer.setDrawBorder(false);
		viewer.setDrawTileNumbers(false);
		viewer.setMode(TileDrawMode.SCALE_SMOOTH);

		Set<OverlayPoint> ps = new HashSet<>();
		ps.add(new OverlayPoint(Constants.DEFAULT_LON, Constants.DEFAULT_LAT));
		viewer.setOverlayPoints(ps);

		JFrame frame = new JFrame("Stepless Viewer Test");
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
		currentTiles = (currentTiles + 1) % TestConfigs.configs.size();
		viewer.setTileConfig(TestConfigs.configs.get(currentTiles));
	}

}
