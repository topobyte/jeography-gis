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

package de.topobyte.jeography.viewer.tools.preview;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.PaintListener;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.ImageManagerGeometry;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometrySourceNull;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyle;
import de.topobyte.jeography.viewer.util.ActionUtil;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryPreview
{

	final static Logger logger = LoggerFactory.getLogger(GeometryPreview.class);

	/**
	 * Show a preview window showing the given geometry on a default background
	 * of map images.
	 * 
	 * @param component
	 *            the parent component for the dialog.
	 * @param geometry
	 *            the geometry.
	 * @param title
	 *            the title for the dialog.
	 * @return the dialog displayed.
	 */
	public JDialog showViewerWithFile(Component component, Geometry geometry,
			String title)
	{
		List<Geometry> list = new ArrayList<>();
		list.add(geometry);
		return showViewerWithFile(component, list, title);
	}

	/**
	 * Show a preview window showing the given geometries on a default
	 * background of map images.
	 * 
	 * @param component
	 *            the parent component for the dialog.
	 * @param geometries
	 *            the set of geometries.
	 * @param title
	 *            the title for the dialog.
	 * @return the dialog displayed
	 */
	public JDialog showViewerWithFile(Component component,
			Collection<Geometry> geometries, String title)
	{
		Set<GeometryContainer> tgs = new HashSet<>();
		for (Geometry geometry : geometries) {
			GeoObject taggedGeometry = new GeoObject(geometry);
			tgs.add(new GeometryContainer(1, taggedGeometry,
					new GeometrySourceNull()));
		}
		return showViewerWithFile(component, tgs, title);
	}

	/**
	 * Show a preview window showing the given geometries on a default
	 * background of map images.
	 * 
	 * @param component
	 *            the parent component for the dialog.
	 * @param tgs
	 *            the set of geometries.
	 * @param title
	 *            the title for the dialog.
	 * @return the dialog displayed
	 */
	public JDialog showViewerWithFile(Component component,
			Set<GeometryContainer> tgs, String title)
	{
		JDialog frame = new JDialog(Components.getContainingFrame(component),
				title);
		showViewerWithFile(frame.getRootPane(), null, tgs);
		frame.setVisible(true);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	public void showViewerWithFile(JRootPane root, Configuration configuration,
			Set<GeometryContainer> tgs)
	{
		Configuration config = configuration == null ? Configuration
				.createDefaultConfiguration() : configuration;
		TileConfig tileConfig = config.getTileConfigs().get(0);
		final Viewer viewer = new Viewer(tileConfig, null);

		root.setContentPane(viewer);

		viewer.setMouseActive(true);
		viewer.setDrawBorder(false);
		viewer.setDrawCrosshair(false);
		viewer.setDrawOverlay(false);
		viewer.setDrawTileNumbers(false);

		InputMap inputMap = viewer
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = viewer.getActionMap();
		ActionUtil.setupMovementActions(viewer, inputMap, actionMap);
		ActionUtil.setupZoomActions(viewer, inputMap, actionMap);

		ImageManagerGeometry manager = new ImageManagerGeometry(
				viewer.getMapWindow());
		GeometryStyle style = new GeometryStyle();
		ArrayList<GeometryContainer> bag = new ArrayList<>();
		for (GeometryContainer tg : tgs) {
			bag.add(tg);
		}
		manager.setGeometries(style, bag);
		manager.addLoadListener(viewer);

		Envelope envelope = new Envelope();
		for (GeometryContainer gc : bag) {
			envelope.expandToInclude(gc.getGeometry().getGeometry()
					.getEnvelopeInternal());
		}
		final BBox box = new BBox(envelope);
		logger.debug("showing region: " + box);

		OverlayPainter painter = new OverlayPainter(manager);
		viewer.addPaintListener(painter);

		viewer.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e)
			{
				viewer.getMapWindow().gotoLonLat(box.getLon1(), box.getLon2(),
						box.getLat1(), box.getLat2());
			}
		});
	}

	class OverlayPainter implements PaintListener
	{

		private ImageManager<Tile, BufferedImage> manager;

		public OverlayPainter(ImageManager<Tile, BufferedImage> manager)
		{
			this.manager = manager;
		}

		@Override
		public void onPaint(TileMapWindow mapWindow, Graphics g)
		{
			for (TileOnWindow tow : mapWindow) {
				BufferedImage imageOverlay = manager.get(tow);
				if (imageOverlay != null)
					g.drawImage(imageOverlay, tow.getDX(), tow.getDY(), null);
			}
		}

	}

}
