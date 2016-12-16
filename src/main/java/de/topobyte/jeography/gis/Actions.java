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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.MouseMode;
import de.topobyte.jeography.viewer.action.AboutAction;
import de.topobyte.jeography.viewer.action.AddBookmarkAction;
import de.topobyte.jeography.viewer.action.ConfigurationAction;
import de.topobyte.jeography.viewer.action.CrosshairAction;
import de.topobyte.jeography.viewer.action.FullscreenAction;
import de.topobyte.jeography.viewer.action.GeometryIndexAction;
import de.topobyte.jeography.viewer.action.GeometryInfoAction;
import de.topobyte.jeography.viewer.action.GeometryListAction;
import de.topobyte.jeography.viewer.action.GeometryManagerAction;
import de.topobyte.jeography.viewer.action.GeometrySelectionAction;
import de.topobyte.jeography.viewer.action.GotoAction;
import de.topobyte.jeography.viewer.action.GridAction;
import de.topobyte.jeography.viewer.action.ManualAction;
import de.topobyte.jeography.viewer.action.MapWindowPanelAction;
import de.topobyte.jeography.viewer.action.MeasurePanelAction;
import de.topobyte.jeography.viewer.action.MouseModeAction;
import de.topobyte.jeography.viewer.action.NetworkStateAction;
import de.topobyte.jeography.viewer.action.OverlayAction;
import de.topobyte.jeography.viewer.action.PolygonalSelectionSnapAction;
import de.topobyte.jeography.viewer.action.QuitAction;
import de.topobyte.jeography.viewer.action.SearchAction;
import de.topobyte.jeography.viewer.action.SelectionPolyPanelAction;
import de.topobyte.jeography.viewer.action.SelectionRectPanelAction;
import de.topobyte.jeography.viewer.action.SelectionSnapAction;
import de.topobyte.jeography.viewer.action.ShowStatusbarAction;
import de.topobyte.jeography.viewer.action.ShowToolbarAction;
import de.topobyte.jeography.viewer.action.TileNumberAction;
import de.topobyte.jeography.viewer.action.ZoomAction;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Actions
{

	MouseModeAction mma1;
	MouseModeAction mma2;
	MouseModeAction mma3;
	MouseModeAction mma4;
	List<MouseModeAction> mmActions;
	SelectionSnapAction snap;
	PolygonalSelectionSnapAction snapPolygonal;
	ConfigurationAction configure;
	NetworkStateAction network;
	ShowStatusbarAction ssa;
	ShowToolbarAction sta;
	GridAction grid;
	TileNumberAction tileNumbers;
	CrosshairAction crosshair;
	ZoomAction zoomIn;
	ZoomAction zoomOut;
	OverlayAction overlay;
	GeometryInfoAction geometryInfo;
	QuitAction quit;
	GeometryManagerAction gma;
	SelectionRectPanelAction srpa;
	SelectionPolyPanelAction sppa;
	MapWindowPanelAction mpa;
	GeometryListAction gla;
	GeometryIndexAction gia;
	GeometrySelectionAction gsa;
	MeasurePanelAction mla;
	FullscreenAction fullscreen;
	GotoAction gta;
	SearchAction sa;
	AddBookmarkAction addBookmark;
	ManualAction manual;
	AboutAction about;

	public Actions(JeographyGIS gis, Viewer viewer, JFrame frame)
	{
		mma1 = new MouseModeAction(gis, MouseMode.NAVIGATE);
		mma2 = new MouseModeAction(gis, MouseMode.SELECT);
		mma3 = new MouseModeAction(gis, MouseMode.POLYSELECT);
		mma4 = new MouseModeAction(gis, MouseMode.DRAG);

		mmActions = new ArrayList<>();
		mmActions.add(mma1);
		mmActions.add(mma2);
		mmActions.add(mma3);
		mmActions.add(mma4);

		snap = new SelectionSnapAction(gis);
		snapPolygonal = new PolygonalSelectionSnapAction(gis);

		configure = new ConfigurationAction(gis);
		network = new NetworkStateAction(viewer);
		ssa = new ShowStatusbarAction(gis);
		sta = new ShowToolbarAction(gis);
		grid = new GridAction(viewer);
		tileNumbers = new TileNumberAction(viewer);
		crosshair = new CrosshairAction(viewer);
		zoomIn = new ZoomAction(viewer, true);
		zoomOut = new ZoomAction(viewer, false);
		overlay = new OverlayAction(viewer);
		geometryInfo = new GeometryInfoAction(gis);
		quit = new QuitAction(gis, frame);
		gma = new GeometryManagerAction(gis);
		srpa = new SelectionRectPanelAction(gis);
		sppa = new SelectionPolyPanelAction(gis);
		mpa = new MapWindowPanelAction(gis);
		gla = new GeometryListAction(gis);
		gia = new GeometryIndexAction(gis.getMainPanel());
		gsa = new GeometrySelectionAction(gis.getMainPanel());
		mla = new MeasurePanelAction(gis.getMainPanel());
		fullscreen = new FullscreenAction(gis);
		gta = new GotoAction(gis);
		sa = new SearchAction(gis);
		addBookmark = new AddBookmarkAction(gis);

		manual = new ManualAction(gis);
		about = new AboutAction(gis);
	}

}
