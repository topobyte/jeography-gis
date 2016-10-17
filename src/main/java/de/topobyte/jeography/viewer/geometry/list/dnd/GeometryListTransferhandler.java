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

package de.topobyte.jeography.viewer.geometry.list.dnd;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.geometry.list.GeomList;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListTransferhandler
		extends GeometryDestinationSourceTransferHandler
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometryListTransferhandler.class);

	private GeomList list;

	/**
	 * @param list
	 *            the list this handler is for.
	 */
	public GeometryListTransferhandler(GeomList list)
	{
		this.list = list;
	}

	private static final long serialVersionUID = 5730318813430359667L;

	private int[] selectedIndices = null;

	@Override
	public Collection<Geometry> getGeometries()
	{
		selectedIndices = list.getSelectedIndices();
		List<Geometry> geometries = new ArrayList<>();
		for (int i : selectedIndices) {
			Geometry geometry = list.getModel().getElementAt(i);
			if (geometry != null) {
				geometries.add(geometry);
			}
		}
		return geometries;
	}

	@Override
	public int getSourceActions(JComponent c)
	{
		logger.debug("getSourceActions");
		return COPY_OR_MOVE;
	}

	@Override
	public void handle(Collection<Geometry> geometries, TransferSupport ts)
	{
		logger.debug("handling geometries due to drop");
		JList.DropLocation dropLocation = (JList.DropLocation) ts
				.getDropLocation();
		int index = dropLocation.getIndex();
		for (Geometry g : geometries) {
			list.getModel().add(g, index);
		}
	}

	@Override
	public void reorder(TransferSupport ts)
	{
		logger.debug("reorder");
	}

	@Override
	public void exportDone(JComponent c, Transferable t, int action)
	{
		super.exportDone(c, t, action);
		logger.debug("export done");
		if (action == NONE) {
			logger.debug("none");
		}
		if (action == MOVE) {
			logger.debug("moved");
			List<Integer> indices = new ArrayList<>();
			for (int i : selectedIndices) {
				indices.add(i);
			}
			Collections.reverse(indices);
			for (int i : indices) {
				list.getModel().remove(i);
			}
		}
	}

}
