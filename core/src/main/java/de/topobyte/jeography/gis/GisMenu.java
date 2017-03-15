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

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.action.DialogAction;
import de.topobyte.jeography.viewer.action.OperationAction;
import de.topobyte.jeography.viewer.action.OverlayTileConfigAction;
import de.topobyte.jeography.viewer.action.TileConfigAction;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.operation.Operation;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingBufferUnionList;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingTranslateList;
import de.topobyte.jeography.viewer.geometry.list.operation.transform.ShowingTransformList;
import de.topobyte.swing.util.EmptyIcon;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GisMenu
{

	private JMenuBar menuBar;
	private Actions actions;
	private JeographyGIS gis;
	private Viewer viewer;
	private List<TileConfig> tileConfigs;
	private List<TileConfig> overlayConfigs;

	public GisMenu(JMenuBar menuBar, Actions actions, JeographyGIS gis,
			Viewer viewer, List<TileConfig> tileConfigs,
			List<TileConfig> overlayConfigs)
	{
		this.menuBar = menuBar;
		this.actions = actions;
		this.gis = gis;
		this.viewer = viewer;
		this.tileConfigs = tileConfigs;
		this.overlayConfigs = overlayConfigs;
	}

	public void setupMenu()
	{
		JMenu menuFile = new JMenu("File");
		add(menuFile, 'F');
		menuFile.add(new JMenuItem(actions.network));
		menuFile.add(new JMenuItem(actions.configure));
		menuFile.add(new JMenuItem(actions.quit));

		JMenu menuView = new JMenu("View");
		add(menuView, 'V');
		addCheckbox(menuView, actions.ssa);
		addCheckbox(menuView, actions.sta);
		addCheckbox(menuView, actions.grid, KeyEvent.VK_F5);
		addCheckbox(menuView, actions.tileNumbers, KeyEvent.VK_F6);
		addCheckbox(menuView, actions.crosshair, KeyEvent.VK_F7);
		addCheckbox(menuView, actions.overlay, KeyEvent.VK_F8);
		addCheckbox(menuView, actions.geometryInfo, KeyEvent.VK_F9);
		addCheckbox(menuView, actions.fullscreen, KeyEvent.VK_F11);
		addItem(menuView, actions.zoomIn);
		addItem(menuView, actions.zoomOut);

		JMenu menuMap = new JMenu("Map");
		add(menuMap, 'M');
		addItem(menuMap, actions.gta, InputEvent.CTRL_MASK, KeyEvent.VK_G);
		addItem(menuMap, actions.sa, InputEvent.CTRL_MASK, KeyEvent.VK_F);
		addItem(menuMap, actions.addBookmark, InputEvent.CTRL_MASK,
				KeyEvent.VK_D);

		JMenu menuTiles = new JMenu("Tiles");
		add(menuTiles, 'T');
		for (TileConfig config : tileConfigs) {
			menuTiles.add(new TileConfigAction(viewer, config));
		}

		JMenu menuOverlays = new JMenu("Overlays");
		menuOverlays.setMnemonic('O');
		menuBar.add(menuOverlays);
		for (TileConfig config : overlayConfigs) {
			menuOverlays.add(new OverlayTileConfigAction(viewer, config));
		}

		JMenu menuWindows = new JMenu("Windows");
		add(menuWindows, 'W');
		addCheckbox(menuWindows, actions.gma);
		addCheckbox(menuWindows, actions.srpa, InputEvent.CTRL_MASK,
				KeyEvent.VK_S);
		addCheckbox(menuWindows, actions.sppa, InputEvent.CTRL_MASK,
				KeyEvent.VK_P);
		addCheckbox(menuWindows, actions.mpa, InputEvent.CTRL_MASK,
				KeyEvent.VK_M);

		addItem(menuWindows, actions.gia);
		addItem(menuWindows, actions.gsa);
		addItem(menuWindows, actions.gla);
		addItem(menuWindows, actions.mla);

		JMenu menuOperationsAdd = new JMenu("operations");
		menuOperationsAdd.setIcon(new EmptyIcon(24));
		menuWindows.add(menuOperationsAdd);

		JPanel source = gis.getMainPanel();
		menuOperationsAdd.add(new OperationAction(gis, Operation.UNION));
		menuOperationsAdd.add(new OperationAction(gis, Operation.INTERSECTION));
		menuOperationsAdd.add(new OperationAction(gis, Operation.DIFFERENCE));
		menuOperationsAdd.add(new OperationAction(gis, Operation.COLLECTION));
		menuOperationsAdd.add(new OperationAction(gis, Operation.HULL));

		menuOperationsAdd.add(new DialogAction(source,
				"res/images/geometryOperation/union.png", "Union Buffered",
				"union geometries and create a buffer of the result") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingBufferUnionList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(source,
				"res/images/geometryOperation/collection.png", "Translate",
				"collect and translate geometries") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingTranslateList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(source,
				"res/images/geometryOperation/collection.png", "Transform",
				"collect and transform geometries") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingTransformList(viewer);
			}
		});

		JMenu menuHelp = new JMenu("Help");
		add(menuHelp, 'H');
		addItem(menuHelp, actions.manual);
		addItem(menuHelp, actions.about);

		menuBar.updateUI();
	}

	private void add(JMenu menu, char mnemonic)
	{
		menu.setMnemonic(mnemonic);
		menuBar.add(menu);
	}

	private void addItem(JMenu menu, Action action)
	{
		menu.add(new JMenuItem(action));
	}

	private void addItem(JMenu menu, Action action, KeyStroke keyStroke)
	{
		JMenuItem item = new JMenuItem(action);
		item.setAccelerator(keyStroke);
		menu.add(item);
	}

	private void addItem(JMenu menu, Action action, int modifiers, int keyCode)
	{
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
		addItem(menu, action, keyStroke);
	}

	private void addCheckbox(JMenu menu, Action action)
	{
		menu.add(new JCheckBoxMenuItem(action));
	}

	private void addCheckbox(JMenu menu, Action action, KeyStroke keyStroke)
	{
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
		item.setAccelerator(keyStroke);
		menu.add(item);
	}

	private void addCheckbox(JMenu menu, Action action, int keyCode)
	{
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);
		addCheckbox(menu, action, keyStroke);
	}

	private void addCheckbox(JMenu menu, Action action, int modifiers,
			int keyCode)
	{
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
		addCheckbox(menu, action, keyStroke);
	}

}
