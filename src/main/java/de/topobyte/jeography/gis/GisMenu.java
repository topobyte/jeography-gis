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

import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.action.DialogAction;
import de.topobyte.jeography.viewer.action.OperationAction;
import de.topobyte.jeography.viewer.action.OverlayTileConfigAction;
import de.topobyte.jeography.viewer.action.TileConfigAction;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.Operations;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingBufferUnionList;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingTranslateList;
import de.topobyte.jeography.viewer.geometry.list.operation.transform.ShowingTransformList;
import de.topobyte.jeography.viewer.util.EmptyIcon;

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
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		menuFile.add(new JMenuItem(actions.network));
		menuFile.add(new JMenuItem(actions.configure));
		menuFile.add(new JMenuItem(actions.quit));

		JMenu menuView = new JMenu("View");
		menuView.setMnemonic('V');
		menuBar.add(menuView);
		menuView.add(new JCheckBoxMenuItem(actions.ssa));
		menuView.add(new JCheckBoxMenuItem(actions.sta));
		menuView.add(new JCheckBoxMenuItem(actions.grid));
		menuView.add(new JCheckBoxMenuItem(actions.tileNumbers));
		menuView.add(new JCheckBoxMenuItem(actions.crosshair));
		menuView.add(actions.zoomIn);
		menuView.add(actions.zoomOut);
		menuView.add(new JCheckBoxMenuItem(actions.overlay));

		JMenu menuMap = new JMenu("Map");
		menuMap.setMnemonic('M');
		menuBar.add(menuMap);
		menuMap.add(actions.gta);

		JMenu menuTiles = new JMenu("Tiles");
		menuTiles.setMnemonic('T');
		menuBar.add(menuTiles);
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
		menuWindows.setMnemonic('W');
		menuBar.add(menuWindows);
		menuWindows.add(new JCheckBoxMenuItem(actions.gma));
		menuWindows.add(new JCheckBoxMenuItem(actions.srpa));
		menuWindows.add(new JCheckBoxMenuItem(actions.mpa));

		menuWindows.add(new JMenuItem(actions.gia));
		menuWindows.add(new JMenuItem(actions.gsa));
		menuWindows.add(new JMenuItem(actions.gla));
		menuWindows.add(new JMenuItem(actions.mla));

		JMenu menuOperationsAdd = new JMenu("operations");
		menuOperationsAdd.setIcon(new EmptyIcon(24));
		menuWindows.add(menuOperationsAdd);

		menuOperationsAdd.add(new OperationAction(Operations.UNION, gis));
		menuOperationsAdd
				.add(new OperationAction(Operations.INTERSECTION, gis));
		menuOperationsAdd.add(new OperationAction(Operations.DIFFERENCE, gis));
		menuOperationsAdd.add(new OperationAction(Operations.COLLECTION, gis));
		menuOperationsAdd.add(new OperationAction(Operations.HULL, gis));

		menuOperationsAdd.add(new DialogAction(gis,
				"res/images/geometryOperation/union.png", "Union Buffered",
				"union geometries and create a buffer of the result") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingBufferUnionList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(gis,
				"res/images/geometryOperation/collection.png", "Translate",
				"collect and translate geometries") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingTranslateList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(gis,
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
		menuHelp.setMnemonic('H');
		menuBar.add(menuHelp);
		menuHelp.add(new JMenuItem(actions.manual));
		menuHelp.add(new JMenuItem(actions.about));

		menuBar.updateUI();
	}

}
