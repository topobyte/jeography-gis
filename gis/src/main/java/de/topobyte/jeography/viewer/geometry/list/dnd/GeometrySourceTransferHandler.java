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
import java.util.Collection;

import javax.swing.JComponent;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.swing.util.dnd.SourceAwareTransferHandler;
import de.topobyte.swing.util.dnd.SourceTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class GeometrySourceTransferHandler
		extends SourceAwareTransferHandler
		implements SourceTransferHandler, GeometrySource
{

	private static final long serialVersionUID = 460417237371558219L;

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySourceTransferHandler.class);

	@Override
	public Transferable createTransferable(JComponent c)
	{
		super.createTransferable(c);
		logger.debug("createTransferable");
		Collection<Geometry> geometries = getGeometries();
		return new GeometryTransferable(geometries);
	}

	@Override
	public void exportDone(JComponent c, Transferable t, int action)
	{
		super.exportDone(c, t, action);
	}

}
