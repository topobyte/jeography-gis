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

package de.topobyte.jeography.viewer.geometry.list.measure;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Puntal;

import de.topobyte.geomath.WGS84;
import de.topobyte.jeography.viewer.geometry.list.GeomList;
import de.topobyte.jeography.viewer.geometry.list.PreviewMouseAdapter;
import de.topobyte.jeography.viewer.geometry.list.TrashLabel;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryListTransferhandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MeasureList extends JPanel implements ListDataListener
{

	private static final long serialVersionUID = -5141716445948665122L;

	private JLabel label = new JLabel();
	private GeomList list = new GeomList();

	/**
	 * Create a new MeasureList
	 */
	public MeasureList()
	{
		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);
		list.setDropMode(DropMode.INSERT);

		JComponent trash = new TrashLabel("trash");

		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.0;
		c.weightx = 0.0;
		add(trash, c);

		c.gridx = 1;
		c.weightx = 1.0;
		add(label, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1.0;
		add(jsp, c);

		TransferHandler transferhandler = new GeometryListTransferhandler(list);
		list.setTransferHandler(transferhandler);
		list.setDragEnabled(true);

		PreviewMouseAdapter previewMouseAdapter = new PreviewMouseAdapter(list);
		list.addMouseListener(previewMouseAdapter);

		updateLabel();

		list.getModel().addListDataListener(this);
	}

	/*
	 * Methods of the ListDataListener implementation added to the GeomList
	 * instance
	 * 
	 * whatever changes, just recompute the overall length
	 */

	@Override
	public void intervalAdded(ListDataEvent e)
	{
		updateLabel();
	}

	@Override
	public void intervalRemoved(ListDataEvent e)
	{
		updateLabel();
	}

	@Override
	public void contentsChanged(ListDataEvent e)
	{
		updateLabel();
	}

	/*
	 * End of ListDataListener methods
	 */

	/*
	 * calculate the length of all geometries and update the label
	 */
	private void updateLabel()
	{
		List<Geometry> geometries = new ArrayList<>();
		for (int i = 0; i < list.getModel().getSize(); i++) {
			Geometry geometry = list.getModel().getElementAt(i);
			geometries.add(geometry);
		}

		double length = length(geometries);

		double distance = 0;
		for (Geometry geometry : geometries) {
			distance += geometry.getNumPoints();
		}

		String text = "";
		if (length < 1000) {
			// if < 1 km, display meters with 2 decimal places
			text = String.format("Length: %.2f m", length);
		} else {
			// if >= 1 km, display kilometers with 3 decimal places
			text = String.format("Length: %.3f km", length / 1000);
		}
		label.setText(text);
	}

	/**
	 * Compute the length of a collection of geometries by calculating the sum
	 * of each individual geometry's length.
	 * 
	 * @param geometries
	 *            the collection of geometries to calculate the total length
	 *            for.
	 * 
	 * @return the length in meters
	 */
	private double length(Collection<Geometry> geometries)
	{
		double sum = 0.0;
		for (Geometry geometry : geometries) {
			sum += length(geometry);
		}
		return sum;
	}

	/**
	 * Compute the sum of a single geometry
	 * 
	 * @param geometry
	 *            the geometry to calculate the length for.
	 * @return the length of the geometry's outline in meters.
	 */
	private double length(Geometry geometry)
	{
		List<LineString> strings = extractLines(geometry);
		double sum = 0.0;
		for (LineString string : strings) {
			sum += length(string);
		}
		return sum;
	}

	private List<LineString> extractLines(Geometry geometry)
	{
		List<LineString> strings = new ArrayList<>();
		if (geometry instanceof Puntal) {
			return strings;
		} else if (geometry instanceof LineString) {
			strings.add((LineString) geometry);
		} else if (geometry instanceof GeometryCollection) {
			GeometryCollection collection = (GeometryCollection) geometry;
			for (int i = 0; i < collection.getNumGeometries(); i++) {
				Geometry child = collection.getGeometryN(i);
				strings.addAll(extractLines(child));
			}
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			LineString exteriorRing = polygon.getExteriorRing();
			strings.add(exteriorRing);
			for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
				LineString interior = polygon.getInteriorRingN(i);
				strings.add(interior);
			}
		}
		return strings;
	}

	/**
	 * Compute the length of a single LineString.
	 * 
	 * @param string
	 *            the line to compute the length for
	 * @return the length in meters.
	 */
	private double length(LineString string)
	{
		double length = 0.0;
		Point a = string.getPointN(0);
		for (int i = 1; i < string.getNumPoints(); i++) {
			Point b = string.getPointN(i);
			length += distance(a, b);
			a = b;
		}
		return length;
	}

	/**
	 * Calculate the distance between the denoted points.
	 * 
	 * @param a
	 *            the first point.
	 * @param b
	 *            the second point.
	 * @return the distance between a and b in meters.
	 */
	private double distance(Point a, Point b)
	{
		return WGS84.haversineDistance(a.getX(), a.getY(), b.getX(), b.getY());
	}

}
