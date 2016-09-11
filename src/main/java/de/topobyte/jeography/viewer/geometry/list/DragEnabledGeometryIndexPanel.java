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

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryIndexSourceTransferHandler;
import de.topobyte.jsi.GenericSpatialIndex;
import de.topobyte.swing.util.dnd.DestinationSourceTransferHandler;
import de.topobyte.swing.util.dnd.DestinationTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DragEnabledGeometryIndexPanel extends JPanel implements
		DragGestureListener
{

	private static final long serialVersionUID = -4507519038851053131L;

	final static Logger logger = LoggerFactory
			.getLogger(DragEnabledGeometryIndexPanel.class);

	private GenericSpatialIndex<Geometry> geometryIndex;

	private JLabel label;

	/**
	 * Create a new IndexPanle representing the denoted index.
	 * 
	 * @param geometryIndex
	 *            the index to represent.
	 */
	public DragEnabledGeometryIndexPanel(
			GenericSpatialIndex<Geometry> geometryIndex)
	{
		setupDragndrop();

		label = new JLabel();
		setup(geometryIndex);

		add(label);
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		TransferHandler handler = this.getTransferHandler();
		handler.exportAsDrag(this, dge.getTriggerEvent(), TransferHandler.COPY);
	}

	private void setupDragndrop()
	{
		SourceTransferhandler sourceHandler = new SourceTransferhandler();
		DestinationTransferhandler destHandler = new DestinationTransferhandler();
		DestinationSourceTransferHandler transferHandler = new DestinationSourceTransferHandler();

		transferHandler.setSourceHandler(sourceHandler);
		transferHandler.setDestinationHandler(destHandler);
		setTransferHandler(transferHandler);

		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this,
				TransferHandler.COPY, this);
	}

	class DestinationTransferhandler implements DestinationTransferHandler
	{

		private static final long serialVersionUID = 8088826407050808720L;

		@Override
		public boolean canImport(TransferSupport ts)
		{
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean importData(TransferSupport ts)
		{
			// TODO Auto-generated method stub
			return false;
		}

	}

	class SourceTransferhandler extends GeometryIndexSourceTransferHandler
	{

		private static final long serialVersionUID = 1320181642257143993L;

		@Override
		public int getSourceActions(JComponent c)
		{
			return COPY;
		}

		@Override
		public GenericSpatialIndex<Geometry> getGeometryIndex()
		{
			return DragEnabledGeometryIndexPanel.this.getGeometryIndex();
		}
	}

	/**
	 * Setup the panel to represent the denoted geometryIndex.
	 * 
	 * @param geometryIndex
	 *            the index to represent.
	 */
	public void setup(GenericSpatialIndex<Geometry> geometryIndex)
	{
		this.geometryIndex = geometryIndex;

		int size = geometryIndex.size();
		label.setText(String.format("Index for %d geometries", size));
	}

	/**
	 * @return the geometryIndex this panel represents.
	 */
	public GenericSpatialIndex<Geometry> getGeometryIndex()
	{
		return geometryIndex;
	}

}
