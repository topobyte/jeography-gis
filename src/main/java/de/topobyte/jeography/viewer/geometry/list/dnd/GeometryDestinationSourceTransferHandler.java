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

import java.util.Collection;

import javax.swing.JComponent;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.swing.util.dnd.DestinationSourceTransferHandler;

/**
 * An implementation of DestinationSourceTransferHandler that implements a
 * drag-source/drag-destination for geometries from/to a single component based
 * on GeometrySourceTransferHandler / GeometryDestinationTransferHandler.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class GeometryDestinationSourceTransferHandler
		extends DestinationSourceTransferHandler
		implements GeometrySource, GeometryDestination
{

	private static final long serialVersionUID = 5062007241222686797L;

	TheGeometryDestinationTransferHandler dest = new TheGeometryDestinationTransferHandler();
	TheGeometrySourceTransferHandler src = new TheGeometrySourceTransferHandler();

	/**
	 * Public constructor.
	 */
	public GeometryDestinationSourceTransferHandler()
	{
		setDestinationHandler(dest);
		setSourceHandler(src);
	}

	@Override
	public abstract int getSourceActions(JComponent c);

	@Override
	public boolean importData(TransferSupport ts)
	{
		boolean reorder = src.isDragWithinSameComponent();

		if (reorder && ts.getDropAction() == MOVE) {
			reorder(ts);
			return false;
		}

		return super.importData(ts);
	}

	class TheGeometryDestinationTransferHandler
			extends GeometryDestinationTransferHandler
	{

		private static final long serialVersionUID = 249269940459908811L;

		@Override
		public void handle(Collection<Geometry> geometries, TransferSupport ts)
		{
			GeometryDestinationSourceTransferHandler.this.handle(geometries,
					ts);
		}

		@Override
		public void reorder(TransferSupport ts)
		{
			GeometryDestinationSourceTransferHandler.this.reorder(ts);
		}

	}

	class TheGeometrySourceTransferHandler extends GeometrySourceTransferHandler
	{

		private static final long serialVersionUID = -2717744536346756915L;

		@Override
		public Collection<Geometry> getGeometries()
		{
			return GeometryDestinationSourceTransferHandler.this
					.getGeometries();
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return GeometryDestinationSourceTransferHandler.this
					.getSourceActions(c);
		}

	}

}
