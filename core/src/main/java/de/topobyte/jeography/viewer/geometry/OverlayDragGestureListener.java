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

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OverlayDragGestureListener implements DragGestureListener
{

	final static Logger logger = LoggerFactory
			.getLogger(OverlayDragGestureListener.class);

	private JeographyGIS gis;

	/**
	 * Public constructor
	 * 
	 * @param gis
	 *            the gis to retrieve draggable geometries from.
	 */
	public OverlayDragGestureListener(JeographyGIS gis)
	{
		this.gis = gis;
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		logger.debug("drag");
		InputEvent event = dge.getTriggerEvent();
		logger.debug("event: " + event);
		if (event instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) event;
			double lon = gis.getViewer().getMapWindow()
					.getPositionLon(e.getX());
			double lat = gis.getViewer().getMapWindow()
					.getPositionLat(e.getY());

			List<Geometry> geometries = new ArrayList<>();
			Collection<GeometryTester> testers = gis.getOverlayManager()
					.getGeometryTesters();
			for (GeometryTester tester : testers) {
				Collection<Geometry> hit = tester.test(lon, lat);
				geometries.addAll(hit);
			}
			logger.debug("number of geometries: " + geometries.size());
			if (geometries.size() > 0) {
				logger.debug("starting drag");
				JComponent c = gis.getViewer();
				TransferHandler handler = new Transferhandler(geometries);
				c.setTransferHandler(handler);
				handler.exportAsDrag(c, dge.getTriggerEvent(),
						TransferHandler.COPY);
			}
		}
	}

	class Transferhandler extends GeometrySourceTransferHandler
	{

		private static final long serialVersionUID = 5117588589342814680L;

		private final Collection<Geometry> geometries;

		public Transferhandler(Collection<Geometry> geometries)
		{
			this.geometries = geometries;
		}

		@Override
		public Collection<Geometry> getGeometries()
		{
			List<Geometry> list = new ArrayList<>();
			list.addAll(geometries);
			return list;
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return COPY;
		}
	}

}
