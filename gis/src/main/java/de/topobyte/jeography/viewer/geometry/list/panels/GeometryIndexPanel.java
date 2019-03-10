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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jsi.GenericSpatialIndex;
import de.topobyte.swing.util.dnd.panel.SimpleSerializingDndPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryIndexPanel
		extends SimpleSerializingDndPanel<GenericSpatialIndex<Geometry>>
{

	private static final long serialVersionUID = 8806826692383521733L;

	final static Logger logger = LoggerFactory
			.getLogger(GeometryIndexPanel.class);

	private static final DataFlavor flavor = new DataFlavor("geometry/index",
			"Index of geometries");

	/**
	 * Create an instance of the GeometryIndexPanel, a panel that represents a
	 * spatial index.
	 * 
	 * @param index
	 *            the index to represent.
	 * @param isSource
	 *            whether dragging from this panel shall be enabled.
	 * @param isDestination
	 *            whether dragging from this panel shall be enabled.
	 */
	public GeometryIndexPanel(GenericSpatialIndex<Geometry> index,
			boolean isSource, boolean isDestination)
	{
		super(flavor, index, isSource, isDestination);
	}

	@Override
	public String getLabelString(GenericSpatialIndex<Geometry> index)
	{
		int size = index.size();
		String string = String.format("index with %d elements", size);
		logger.info(string);
		return string;
	}

}
