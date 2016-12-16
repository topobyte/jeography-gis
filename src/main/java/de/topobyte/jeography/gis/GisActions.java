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

package de.topobyte.jeography.gis;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.action.OperationAction;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.operation.Operation;
import de.topobyte.jeography.viewer.util.ActionUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GisActions
{

	public static void setupActions(JFrame frame, final JeographyGIS gis,
			JToolBar toolbar, JMenuBar menuBar, List<TileConfig> tileConfigs,
			List<TileConfig> overlayConfigs)
	{
		final Viewer viewer = gis.getViewer();

		Actions actions = new Actions(gis, viewer, frame);

		setupToolbar(toolbar, actions);

		GisMenu gisMenu = new GisMenu(menuBar, actions, gis, viewer,
				tileConfigs, overlayConfigs);
		gisMenu.setupMenu();

		/* key bindings */

		JPanel source = gis.getMainPanel();

		InputMap inputMap = source
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = source.getActionMap();

		ActionUtil.setupMovementActions(viewer, inputMap, actionMap);
		ActionUtil.setupZoomActions(viewer, inputMap, actionMap);

		inputMap.put(KeyStroke.getKeyStroke('a'), "a");
		inputMap.put(KeyStroke.getKeyStroke('s'), "s");
		inputMap.put(KeyStroke.getKeyStroke('d'), "d");
		inputMap.put(KeyStroke.getKeyStroke('f'), "f");

		actionMap.put("a", actions.mma1);
		actionMap.put("s", actions.mma2);
		actionMap.put("d", actions.mma3);
		actionMap.put("f", actions.mma4);

		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK),
				"ctrl f1");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.CTRL_MASK),
				"ctrl f2");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK),
				"ctrl f3");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK),
				"ctrl f4");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_MASK),
				"ctrl f5");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.CTRL_MASK),
				"ctrl f6");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F7, InputEvent.CTRL_MASK),
				"ctrl f7");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F8, InputEvent.CTRL_MASK),
				"ctrl f8");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK),
				"ctrl f9");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.CTRL_MASK),
				"ctrl f10");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_MASK),
				"ctrl f11");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.CTRL_MASK),
				"ctrl f12");

		actionMap.put("ctrl f1", new OperationAction(gis, Operation.UNION));
		actionMap.put("ctrl f2",
				new OperationAction(gis, Operation.INTERSECTION));
		actionMap.put("ctrl f3",
				new OperationAction(gis, Operation.DIFFERENCE));
		actionMap.put("ctrl f5", actions.gla);
	}

	private static void setupToolbar(JToolBar toolbar, Actions actions)
	{
		JToggleButton buttonMma1 = new JToggleButton(actions.mma1);
		JToggleButton buttonMma2 = new JToggleButton(actions.mma2);
		JToggleButton buttonMma3 = new JToggleButton(actions.mma3);
		JToggleButton buttonMma4 = new JToggleButton(actions.mma4);
		JToggleButton buttonGrid = new JToggleButton(actions.grid);
		JToggleButton buttonTileNumbers = new JToggleButton(
				actions.tileNumbers);
		JToggleButton buttonCrosshair = new JToggleButton(actions.crosshair);
		JToggleButton buttonOverlay = new JToggleButton(actions.overlay);
		JToggleButton buttonGeometryInfo = new JToggleButton(
				actions.geometryInfo);
		JToggleButton buttonSnap = new JToggleButton(actions.snap);
		JToggleButton buttonSnapPolygonal = new JToggleButton(
				actions.snapPolygonal);
		JToggleButton buttonGM = new JToggleButton(actions.gma);
		JToggleButton buttonSRP = new JToggleButton(actions.srpa);
		JToggleButton buttonSPP = new JToggleButton(actions.sppa);
		JToggleButton buttonMWP = new JToggleButton(actions.mpa);
		JButton buttonAddBookmark = new JButton(actions.addBookmark);

		buttonMma1.setText(null);
		buttonMma2.setText(null);
		buttonMma3.setText(null);
		buttonMma4.setText(null);
		buttonGrid.setText(null);
		buttonTileNumbers.setText(null);
		buttonCrosshair.setText(null);
		buttonOverlay.setText(null);
		buttonGeometryInfo.setText(null);
		buttonSnap.setText(null);
		buttonSnapPolygonal.setText(null);
		buttonGM.setText(null);
		buttonSRP.setText(null);
		buttonSPP.setText(null);
		buttonMWP.setText(null);
		buttonAddBookmark.setText(null);

		toolbar.add(actions.network);
		toolbar.addSeparator();
		toolbar.add(buttonMma1);
		toolbar.add(buttonMma2);
		toolbar.add(buttonMma3);
		toolbar.add(buttonMma4);
		toolbar.addSeparator();
		toolbar.add(buttonGrid);
		toolbar.add(buttonTileNumbers);
		toolbar.add(buttonCrosshair);
		toolbar.add(actions.zoomIn);
		toolbar.add(actions.zoomOut);
		toolbar.add(buttonOverlay);
		toolbar.add(buttonSnap);
		toolbar.add(buttonSnapPolygonal);
		toolbar.add(buttonGeometryInfo);
		toolbar.addSeparator();
		toolbar.add(buttonMWP);
		toolbar.add(buttonSRP);
		toolbar.add(buttonSPP);
		toolbar.add(buttonGM);
		toolbar.addSeparator();
		toolbar.add(buttonAddBookmark);
	}

}
