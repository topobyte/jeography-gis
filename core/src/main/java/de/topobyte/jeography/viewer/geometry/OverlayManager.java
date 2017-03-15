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

package de.topobyte.jeography.viewer.geometry;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.core.PaintListener;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.manage.GeometryManager;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRule;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRuleModelListener;
import de.topobyte.jeography.viewer.geometry.manage.StyledGeometry;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OverlayManager implements GeometryRuleModelListener
{

	final static Logger logger = LoggerFactory.getLogger(OverlayManager.class);

	final GeometryManager geometryManager;
	final JeographyGIS gis;
	final OverlayPainter painter;

	private class AssociatedInformation
	{

		public AssociatedInformation()
		{
			// do nothing
		}

		ImageManagerGeometry imageManager;
		GeometryMouseAdapter mouseAdapter;
		GeometryTester geometryTester;
	}

	private class RuleMap
	{

		public RuleMap()
		{
			// do nothing
		}

		List<GeometryRule> list = new ArrayList<>();
		Map<GeometryRule, AssociatedInformation> map = new HashMap<>();

		Collection<ImageManagerGeometry> getManagers()
		{
			List<ImageManagerGeometry> managers = new ArrayList<>();
			for (GeometryRule rule : list) {
				AssociatedInformation info = map.get(rule);
				managers.add(info.imageManager);
			}
			Collections.reverse(managers);
			return managers;
		}

		Collection<MouseListener> getMouseListeners()
		{
			List<MouseListener> listeners = new ArrayList<>();
			for (GeometryRule rule : list) {
				AssociatedInformation info = map.get(rule);
				listeners.add(info.mouseAdapter);
			}
			Collections.reverse(listeners);
			return listeners;
		}

		Collection<GeometryTester> getGeometryTesters()
		{
			List<GeometryTester> listeners = new ArrayList<>();
			for (GeometryRule rule : list) {
				AssociatedInformation info = map.get(rule);
				listeners.add(info.geometryTester);
			}
			Collections.reverse(listeners);
			return listeners;
		}

		void clear()
		{
			list.clear();
			for (GeometryRule rule : map.keySet()) {
				AssociatedInformation ai = map.get(rule);
				free(ai);
			}
			map.clear();
		}

		public void remove(GeometryRule rule)
		{
			AssociatedInformation ai = map.get(rule);
			map.remove(rule);
			list.remove(rule);
			free(ai);
		}

		public void add(GeometryRule rule, StyledGeometry sg)
		{
			TileMapWindow mapWindow = gis.getViewer().getMapWindow();

			AssociatedInformation info = new AssociatedInformation();
			info.imageManager = new ImageManagerGeometry(mapWindow);
			info.imageManager.setGeometries(sg.getStyle(), sg.getGeometries());
			info.mouseAdapter = new GeometryMouseAdapter(gis, sg);
			info.geometryTester = new GeometryTester();
			info.geometryTester.setGeometries(sg.getGeometries());

			list.add(rule);
			map.put(rule, info);
		}

		public void replace(GeometryRule rule, StyledGeometry sg)
		{
			if (map.containsKey(rule)) {
				AssociatedInformation ai = map.get(rule);
				free(ai);
			}

			TileMapWindow mapWindow = gis.getViewer().getMapWindow();

			AssociatedInformation info = new AssociatedInformation();
			info.imageManager = new ImageManagerGeometry(mapWindow);
			info.geometryTester = new GeometryTester();
			if (sg != null) {
				info.imageManager.setGeometries(sg.getStyle(),
						sg.getGeometries());
				info.geometryTester.setGeometries(sg.getGeometries());
			}
			info.mouseAdapter = new GeometryMouseAdapter(gis, sg);

			map.put(rule, info);
		}

		private void free(AssociatedInformation ai)
		{
			System.out.println("free");
			ai.imageManager.destroy();
		}

		public void reorder(List<GeometryRule> rules)
		{
			list.clear();

			list.addAll(rules);
		}
	}

	private RuleMap ruleMap = new RuleMap();

	/**
	 * A class that functions as a layer between GeometryManager and Viewer to
	 * reflect updates and changes in GeometryManager by configuring the
	 * viewer's overlays correctly.
	 * 
	 * @param geometryManager
	 *            the geometry manager.
	 * @param gis
	 *            the gis instance.
	 */
	public OverlayManager(GeometryManager geometryManager, JeographyGIS gis)
	{
		this.geometryManager = geometryManager;
		this.gis = gis;

		painter = new OverlayPainter();
		gis.getViewer().addPaintListener(painter);
	}

	@Override
	public void ruleAdded(GeometryRule rule)
	{
		StyledGeometry styledGeometry = geometryManager.getStyledGeometry(rule);
		ruleMap.add(rule, styledGeometry);
		reorderGeometryAdapterFromGeometryManager();
	}

	@Override
	public void ruleRemoved(GeometryRule rule)
	{
		ruleMap.remove(rule);
		updateViewer();
	}

	@Override
	public void ruleChanged(GeometryRule rule)
	{
		rebuildGeometryAdapters(rule);
	}

	@Override
	public void rulesChanged()
	{
		rebuildGeometryAdapterFromGeometryManager();
	}

	@Override
	public void rulesReordered()
	{
		reorderGeometryAdapterFromGeometryManager();
	}

	private void reorderGeometryAdapterFromGeometryManager()
	{
		List<GeometryRule> rules = geometryManager.getRules().getModel()
				.getRules();

		ruleMap.reorder(rules);

		updateViewer();
	}

	private void rebuildGeometryAdapterFromGeometryManager()
	{
		List<GeometryRule> rules = geometryManager.getRules().getModel()
				.getRules();

		ruleMap.clear();

		for (GeometryRule rule : rules) {
			StyledGeometry styledGeometry = geometryManager
					.getStyledGeometry(rule);
			ruleMap.add(rule, styledGeometry);
		}

		updateViewer();
	}

	private void rebuildGeometryAdapters(GeometryRule rule)
	{
		StyledGeometry styledGeometry = geometryManager.getStyledGeometry(rule);
		ruleMap.replace(rule, styledGeometry);
		updateViewer();
	}

	private void updateViewer()
	{
		// gis.getViewer().setGeometryOverlayImageManagers(ruleMap.getManagers());
		painter.setGeometryOverlayImageManagers(ruleMap.getManagers());
		gis.getViewer().setMouseListeners(ruleMap.getMouseListeners());
		gis.getViewer().repaint();
	}

	class OverlayPainter implements PaintListener
	{

		private Collection<ImageManagerGeometry> geometryOverlayManagers = new ArrayList<>();

		@Override
		public void onPaint(TileMapWindow mapWindow, Graphics g)
		{
			for (TileOnWindow tow : mapWindow) {
				for (ImageManager<Tile, BufferedImage> imageManager : geometryOverlayManagers) {
					BufferedImage imageOverlay = imageManager.get(tow);
					if (imageOverlay != null) {
						drawImage(g, mapWindow, imageOverlay, tow);
					}
				}
			}
		}

		private void drawImage(Graphics g, TileMapWindow mapWindow,
				BufferedImage image, TileOnWindow tow)
		{
			int tw = mapWindow.getTileWidth();
			int th = mapWindow.getTileHeight();

			if (image.getWidth() == tw && image.getHeight() == th) {
				g.drawImage(image, tow.dx, tow.dy, null);
			} else {
				Image scaled = image.getScaledInstance(tw, th,
						BufferedImage.SCALE_SMOOTH);
				g.drawImage(scaled, tow.dx, tow.dy, null);
			}
		}

		public void setGeometryOverlayImageManagers(
				Collection<ImageManagerGeometry> managers)
		{
			Viewer viewer = gis.getViewer();
			for (ImageManagerGeometry manager : geometryOverlayManagers) {
				manager.removeLoadListener(viewer);
				manager.removeUpdateListener(viewer);
			}
			geometryOverlayManagers = managers;
			for (ImageManagerGeometry manager : geometryOverlayManagers) {
				manager.addLoadListener(viewer);
				manager.addUpdateListener(viewer);
			}
		}

	}

	/**
	 * @return the collection of GeometryTesters of the OverlayManager
	 */
	public Collection<GeometryTester> getGeometryTesters()
	{
		return ruleMap.getGeometryTesters();
	}

}
