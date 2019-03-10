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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;

import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.melon.casting.CastUtil;
import de.topobyte.swing.util.dnd.DestinationTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class GeometryDestinationTransferHandler extends TransferHandler
		implements DestinationTransferHandler, GeometryDestination
{

	private static final long serialVersionUID = 460417237371558219L;

	final static Logger logger = LoggerFactory
			.getLogger(GeometryDestinationTransferHandler.class);

	@Override
	public boolean canImport(TransferSupport ts)
	{
		logger.debug("canImport()");
		DataFlavor[] flavors = ts.getDataFlavors();
		boolean take = false;
		for (int i = 0; i < flavors.length; i++) {
			final DataFlavor curFlavor = flavors[i];
			if (curFlavor.equals(GeometryTransferable.flavorJSG)) {
				take = true;
				break;
			}
			if (curFlavor.equals(GeometryTransferable.flavorWKB)) {
				take = true;
				break;
			}
			if (curFlavor.equals(GeometryTransferable.flavorWKT)) {
				take = true;
				break;
			}
		}
		return take;
	}

	@Override
	public boolean importData(TransferSupport ts)
	{
		logger.debug("importData()");

		Transferable tr = ts.getTransferable();
		boolean handleable = false;

		if (tr.isDataFlavorSupported(GeometryTransferable.flavorJSG)) {
			logger.debug("trying JSG-flavor");
			try {
				Object data = tr
						.getTransferData(GeometryTransferable.flavorJSG);
				InputStream is = (InputStream) data;
				ObjectInputStream ois = new ObjectInputStream(is);
				Collection<Geometry> geoms = CastUtil.cast(ois.readObject());
				handle(geoms, ts);
			} catch (UnsupportedFlavorException e) {
				logger.debug("unable to get transfer data: " + e.getMessage());
			} catch (IOException e) {
				logger.debug("unable to get transfer data: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				logger.debug("unable to read from object input stream: "
						+ e.getMessage());
			}
			handleable = true;
		}
		// TODO: implement reading WKB
		if (!handleable
				&& tr.isDataFlavorSupported(GeometryTransferable.flavorWKB)) {
			logger.debug("trying WKB-flavor");
			try {
				Object data = tr
						.getTransferData(GeometryTransferable.flavorWKB);
				Collection<Geometry> geoms = CastUtil.cast(data);
				handle(geoms, ts);
			} catch (UnsupportedFlavorException e) {
				logger.debug(
						"unable to get transfer data (UnsupportedFlavorException): "
								+ e.getMessage());
			} catch (IOException e) {
				logger.debug("unable to get transfer data (IOException): "
						+ e.getMessage());
			}
			handleable = true;
		}
		// TODO: implement reading WKT
		if (!handleable
				&& tr.isDataFlavorSupported(GeometryTransferable.flavorWKT)) {
			logger.debug("trying WKT-flavor");
			handleable = true;
			try {
				Object data = tr
						.getTransferData(GeometryTransferable.flavorWKT);
				Collection<Geometry> geoms = CastUtil.cast(data);
				handle(geoms, ts);
			} catch (UnsupportedFlavorException e) {
				logger.debug(
						"unable to get transfer data (UnsupportedFlavorException): "
								+ e.getMessage());
			} catch (IOException e) {
				logger.debug("unable to get transfer data (IOException): "
						+ e.getMessage());
			}
		}
		return handleable;
	}

}
