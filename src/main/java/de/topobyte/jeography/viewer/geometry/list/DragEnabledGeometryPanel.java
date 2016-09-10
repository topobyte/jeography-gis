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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;
import de.topobyte.jeography.viewer.geometry.list.renderer.GeometryPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DragEnabledGeometryPanel extends GeometryPanel implements
		DragGestureListener
{

	private static final long serialVersionUID = 2738726613621310738L;

	/**
	 * Public constructor for creating a GeometryPanel with drag support.
	 * 
	 * @param geometry
	 *            the geometry to display.
	 */
	public DragEnabledGeometryPanel(Geometry geometry)
	{
		super(geometry);
		setupDragndrop();
	}

	private void setupDragndrop()
	{
		Transferhandler transferhandler = new Transferhandler();
		setTransferHandler(transferhandler);

		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this,
				TransferHandler.COPY, this);
	}

	class Transferhandler extends GeometrySourceTransferHandler
	{

		private static final long serialVersionUID = 8527201136284573954L;

		@Override
		public Collection<Geometry> getGeometries()
		{
			List<Geometry> list = new ArrayList<>();
			list.add(getGeometry());
			return list;
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return COPY;
		}
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		System.out.println("drag gesture");
		JComponent c = this;
		TransferHandler handler = c.getTransferHandler();
		handler.exportAsDrag(c, dge.getTriggerEvent(), TransferHandler.COPY);
	}

	@Override
	public Geometry getGeometry()
	{
		// override to avoid synthetic accessor function
		return super.getGeometry();
	}

}
