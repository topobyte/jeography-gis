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

package de.topobyte.jeography.viewer.geometry.list.panels;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

import de.topobyte.jeography.viewer.geometry.action.ExportAction;
import de.topobyte.jeography.viewer.geometry.action.InspectCollectionAction;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ContextEnabledGeometryPanel extends GeometryPanel
{

	private static final long serialVersionUID = 309875900504894560L;

	/**
	 * Create an instance of the ContextEnabledGeometryPanel
	 * 
	 * @param geometry
	 *            the geometry to display initially.
	 * @param isSource
	 *            whether dragging from the panel shall be enabled.
	 * @param isDestination
	 *            whether dragging to the panel shall be enabled.
	 */
	public ContextEnabledGeometryPanel(Geometry geometry, boolean isSource,
			boolean isDestination)
	{
		super(geometry, isSource, isDestination);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						showPreview(getData());
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					showContext(e.getX(), e.getY());
				}
			}
		});
	}

	void showPreview(Geometry geometry)
	{
		new GeometryPreview().showViewerWithFile(this, geometry, "Preview");
	}

	void showContext(int x, int y)
	{
		Geometry result = getData();

		JPopupMenu popup = new JPopupMenu();

		ExportAction exportAction = new ExportAction(result);
		JMenuItem itemExport = new JMenuItem(exportAction);
		popup.add(itemExport);

		if (result instanceof GeometryCollection) {
			JFrame frame = Components.getContainingFrame(this);
			InspectCollectionAction inspectAction = new InspectCollectionAction(
					frame, result);
			JMenuItem itemInspect = new JMenuItem(inspectAction);
			popup.add(itemInspect);
		}

		popup.show(this, x, y);
	}

}
