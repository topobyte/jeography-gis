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

package de.topobyte.jeography.viewer.geometry.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import de.topobyte.jeography.viewer.action.SimpleAction;
import de.topobyte.jeography.viewer.geometry.list.GeomList;
import de.topobyte.jeography.viewer.geometry.list.GeometryList;
import de.topobyte.jts.utils.GeometryCollectionIterator;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class InspectCollectionAction extends SimpleAction
{

	private static final long serialVersionUID = -1042348930648339830L;

	final static Logger logger = LoggerFactory
			.getLogger(InspectCollectionAction.class);

	private final JFrame parent;
	private final Geometry geometry;

	/**
	 * Create a new ExportAction.
	 * 
	 * @param parent
	 *            the parent frame to show the dialog in.
	 * 
	 * @param geometry
	 *            the geometry to export.
	 */
	public InspectCollectionAction(JFrame parent, Geometry geometry)
	{
		super("inspect", "inspect components of this geometry collection");
		this.parent = parent;
		this.geometry = geometry;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		GeometryList geometryList = new GeometryList();
		GeomList geomList = geometryList.getList();

		List<Geometry> geometries = new ArrayList<>();
		if (geometry instanceof GeometryCollection) {
			GeometryCollection gc = (GeometryCollection) geometry;
			for (Geometry g : new GeometryCollectionIterator(gc)) {
				geometries.add(g);
			}
		}

		geomList.getModel().addAll(geometries, 0);

		JDialog dialog = new JDialog(parent);
		dialog.setContentPane(geometryList);
		dialog.pack();
		dialog.setSize(200, 300);
		dialog.setVisible(true);
	}

}
