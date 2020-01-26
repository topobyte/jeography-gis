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

package de.topobyte.jeography.viewer.geometry.list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.action.ExportAction;
import de.topobyte.jeography.viewer.geometry.action.InspectCollectionAction;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PreviewMouseAdapter extends MouseAdapter
{

	final static Logger logger = LoggerFactory
			.getLogger(PreviewMouseAdapter.class);

	private final GeomList geomList;

	/**
	 * Public constructor for adapter.
	 * 
	 * @param geomList
	 *            the list to react on.
	 */
	public PreviewMouseAdapter(GeomList geomList)
	{
		this.geomList = geomList;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		logger.debug("click");
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2) {
				int index = geomList.locationToIndex(e.getPoint());
				Geometry geometry = geomList.getModel().getElementAt(index);
				GeometryPreview geometryPreview = new GeometryPreview();
				geometryPreview.showViewerWithFile(geomList, geometry,
						"Preview");
			}
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			int index = geomList.locationToIndex(e.getPoint());
			showContext(index, e.getX(), e.getY());
		}
	}

	void showContext(int index, int x, int y)
	{
		JPopupMenu popup = new JPopupMenu();
		Geometry geometry = geomList.getModel().getElementAt(index);

		ExportAction exportAction = new ExportAction(geometry);
		JMenuItem itemExport = new JMenuItem(exportAction);
		popup.add(itemExport);

		if (geometry instanceof GeometryCollection) {
			JFrame frame = Components.getContainingFrame(geomList);
			InspectCollectionAction inspectAction = new InspectCollectionAction(
					frame, geometry);
			JMenuItem itemInspect = new JMenuItem(inspectAction);
			popup.add(itemInspect);
		}

		popup.show(geomList, x, y);
	}

}
