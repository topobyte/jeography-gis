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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryTransferable implements Transferable
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometryTransferable.class);

	/**
	 * A flavor for well serialized geometries.
	 */
	public static DataFlavor flavorJSG = new DataFlavor("application/jsg",
			"Java Serialized Geometry");
	/**
	 * A flavor for well known binary.
	 */
	public static DataFlavor flavorWKB = new DataFlavor("application/wkb",
			"Well Known Binary");
	/**
	 * A flavor for well known text.
	 */
	public static DataFlavor flavorWKT = new DataFlavor("text/wkt",
			"Well Known Text");

	/**
	 * A flavor for plain text.
	 */
	public static DataFlavor flavorPlainText = DataFlavor
			.getTextPlainUnicodeFlavor();

	private final Collection<Geometry> geometries;

	/**
	 * Create a Transferable for the denoted geometries.
	 * 
	 * @param geometries
	 *            the geometries to transfer.
	 */
	public GeometryTransferable(Collection<Geometry> geometries)
	{
		this.geometries = geometries;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		logger.debug("getTransferDataFlavors()");
		List<DataFlavor> flavorList = new ArrayList<>();
		flavorList.add(flavorJSG);
		flavorList.add(flavorWKB);
		flavorList.add(flavorWKT);
		flavorList.add(flavorPlainText);
		return flavorList.toArray(new DataFlavor[0]);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
	{
		logger.debug("getTransferData() " + flavor);
		if (flavor.equals(flavorJSG)) {
			logger.debug("returning JSG flavor");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(geometries);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] bytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			return bais;
		}
		// TODO: implement writing WKB
		if (flavor.equals(flavorWKB)) {
			logger.debug("returning WKB flavor");
			return geometries;
		}
		// TODO: implement writing WKT
		if (flavor.equals(flavorWKT)) {
			logger.debug("returning WKT flavor");
			return geometries;
		}
		if (flavor.equals(flavorPlainText)) {
			return buildStringInputStream();
		}
		return null;
	}

	private InputStream buildStringInputStream()
	{
		String text = buildText();
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(text.getBytes("unicode"));
		} catch (UnsupportedEncodingException e) {
			logger.debug("unable to create string: " + e.getMessage());
		}
		return bais;
	}

	private String buildText()
	{
		WKTWriter writer = new WKTWriter();
		Iterator<Geometry> iterator = geometries.iterator();
		StringBuilder strb = new StringBuilder();
		while (iterator.hasNext()) {
			Geometry g = iterator.next();
			String text = writer.write(g);
			strb.append(text);
			if (iterator.hasNext()) {
				strb.append(String.format(";%n"));
			}
		}
		return strb.toString();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		logger.debug("isDataFlavorSupported() " + flavor);
		return flavor.equals(flavorJSG) || flavor.equals(flavorWKB)
				|| flavor.equals(flavorWKT) || flavor.equals(flavorPlainText);
	}

}
