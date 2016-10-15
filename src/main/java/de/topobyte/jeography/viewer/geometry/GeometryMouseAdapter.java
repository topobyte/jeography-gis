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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.StyledGeometry;
import de.topobyte.jts.indexing.GeometryTesselationMap;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryMouseAdapter extends MouseAdapter
{

	static final Logger logger = LoggerFactory
			.getLogger(GeometryMouseAdapter.class);

	private final JeographyGIS gis;
	private GeometryTesselationMap<GeometryContainer> map;

	/**
	 * @param gis
	 *            the JeographyGIS instance.
	 * @param sg
	 *            the set of geometries to monitor.
	 */
	public GeometryMouseAdapter(JeographyGIS gis, StyledGeometry sg)
	{
		this.gis = gis;
		map = new GeometryTesselationMap<>();
		if (sg == null) {
			return;
		}
		Collection<GeometryContainer> geometries = sg.getGeometries();
		for (GeometryContainer gc : geometries) {
			map.add(gc.getGeometry().getGeometry(), gc);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		if (!gis.isShowGeometryInfo()) {
			return;
		}
		logger.debug("checking mouse click for layer " + this);
		MapWindow mapWindow = gis.getViewer().getMapWindow();
		double lon = mapWindow.getPositionLon(event.getX());
		double lat = mapWindow.getPositionLat(event.getY());
		Point point = new GeometryFactory()
				.createPoint(new Coordinate(lon, lat));
		Set<GeometryContainer> intersect = map.test(point);
		List<GeometryContainer> intersectionContainers = new ArrayList<>();
		for (GeometryContainer gc : intersect) {
			System.out
					.println("intersection with: " + gc.getSource().getInfo());
			intersectionContainers.add(gc);
		}
		if (intersectionContainers.size() > 0) {
			showInfo(intersectionContainers);
		}
	}

	private void showInfo(List<GeometryContainer> intersectionContainers)
	{
		String newLine = System.getProperty("line.separator");
		StringBuilder strb = new StringBuilder();
		Iterator<GeometryContainer> iterator = intersectionContainers
				.iterator();
		while (iterator.hasNext()) {
			GeometryContainer gc = iterator.next();
			strb.append(gc.getSource().getInfo());
			if (iterator.hasNext()) {
				strb.append(newLine);
			}
		}
		String message = strb.toString();
		JOptionPane.showMessageDialog(gis.getMainPanel(), message,
				"Interesection", JOptionPane.INFORMATION_MESSAGE);
	}

}
