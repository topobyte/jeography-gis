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

package de.topobyte.jeography.viewer.selection.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;
import de.topobyte.jts.utils.JtsHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BboxDragGeometryPanel extends JPanel implements DragGestureListener
{

	private static final long serialVersionUID = 2404823349194508514L;

	private BBox bbox;

	/**
	 * Public constructor for creating a GeometryPanel with drag support.
	 * 
	 * @param geometry
	 *            the geometry to display.
	 */
	public BboxDragGeometryPanel(BBox bbox)
	{
		setLayout(new BorderLayout(0, 0));
		this.bbox = bbox;
		setupDragndrop();
		setToolTipText("drag'n'drop selection as polygon");
		int s = 2;
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				new EmptyBorder(s, s, s, s)));
	}

	public void setBoundingBox(BBox bbox)
	{
		this.bbox = bbox;
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
		JComponent c = this;
		TransferHandler handler = c.getTransferHandler();
		handler.exportAsDrag(c, dge.getTriggerEvent(), TransferHandler.COPY);
	}

	public Geometry getGeometry()
	{
		if (bbox == null) {
			return new GeometryFactory().createGeometryCollection(null);
		}
		Geometry geometry = JtsHelper.toGeometry(bbox.toEnvelope());
		return geometry;
	}

}
