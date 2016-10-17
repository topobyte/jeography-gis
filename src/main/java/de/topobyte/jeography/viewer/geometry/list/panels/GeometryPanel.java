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

import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryDestinationTransferHandler;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryTransferable;
import de.topobyte.swing.util.dnd.panel.DndPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryPanel extends DndPanel<Geometry>
{

	private static final long serialVersionUID = 309875900504894560L;

	de.topobyte.jeography.viewer.geometry.list.renderer.GeometryPanel panel;

	/**
	 * Create an instance of a panel representing the denoted geometry.
	 * 
	 * @param geometry
	 *            the geometry to represent.
	 * @param isSource
	 *            whether dragging from this panel shall be enabled.
	 * @param isDestination
	 *            whether dragging from this panel shall be enabled.
	 */
	public GeometryPanel(Geometry geometry, boolean isSource,
			boolean isDestination)
	{
		super(geometry, isSource, isDestination);
		panel.setup(geometry);
	}

	@Override
	public JComponent getComponent()
	{
		return panel;
	}

	@Override
	public List<DataFlavor> getSupportedFlavors()
	{
		List<DataFlavor> flavors = new ArrayList<>();
		flavors.add(GeometryTransferable.flavorJSG);
		flavors.add(GeometryTransferable.flavorWKB);
		flavors.add(GeometryTransferable.flavorWKT);
		flavors.add(GeometryTransferable.flavorPlainText);
		return flavors;
	}

	@Override
	public void setup(Geometry data)
	{
		setData(data);
		if (panel == null) {
			panel = new de.topobyte.jeography.viewer.geometry.list.renderer.GeometryPanel();
		}
		panel.setup(data);
	}

	@Override
	public Object getTransferData(DataFlavor flavor, Geometry t)
	{
		ArrayList<Geometry> geometries = new ArrayList<>();
		geometries.add(t);
		GeometryTransferable transferable = new GeometryTransferable(
				geometries);
		return transferable.getTransferData(flavor);
	}

	@Override
	public boolean importData(TransferSupport ts)
	{
		GeometryDestinationTransferHandler handler = new GeometryDestinationTransferHandler() {

			private static final long serialVersionUID = 8865272531594205545L;

			@Override
			public void reorder(TransferSupport tsInner)
			{
				// ignore
			}

			@Override
			public void handle(Collection<Geometry> geometries,
					TransferSupport tsInner)
			{
				setup(geometries.iterator().next());
			}
		};
		boolean imported = handler.importData(ts);
		ts.setDropAction(TransferHandler.MOVE);
		return imported;
	}

}
