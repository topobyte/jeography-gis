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

package de.topobyte.jeography.geometry.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

import de.topobyte.jeography.geometry.io.poly.PolyfileReader;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryLoader
{

	final static Logger logger = LoggerFactory.getLogger(GeometryLoader.class);

	/**
	 * Load a geometry from an input source trying different formats.
	 * 
	 * @param file
	 *            the file to read from.
	 * @return the read geometry or null on failure.
	 * @throws IOException
	 *             if the file cannot be read.
	 */
	public static Geometry readGeometry(File file) throws IOException
	{
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(file);
		try {
			Geometry geometry = readGeometry(fis);
			fis.close();
			return geometry;
		} catch (IOException e) {
			throw (e);
		} finally {
			fis.close();
		}
	}

	public static Geometry readGeometry(File file, FileFormat format)
			throws IOException
	{
		InputStream is = new FileInputStream(file);
		try {
			Geometry geometry = readGeometry(is, format);
			return geometry;
		} catch (IOException e) {
			throw (e);
		} finally {
			is.close();
		}
	}

	public static Geometry readGeometry(InputStream is, FileFormat format)
			throws IOException
	{
		switch (format) {
		default:
			throw new IllegalArgumentException("Unknown format: " + format);
		case POLY:
			return PolyfileReader.read(is);
		case WKB:
			WKBReader wkbReader = new WKBReader();
			try {
				return wkbReader.read(new InputStreamInStream(is));
			} catch (ParseException e) {
				throw new IOException("Error while parsing WKB", e);
			}
		case WKT:
			WKTReader wktReader = new WKTReader();
			try {
				return wktReader.read(new InputStreamReader(is));
			} catch (ParseException e) {
				throw new IOException("Error while parsing WKT", e);
			}
		case GEOJSON:
			throw new IllegalArgumentException(
					"GeoJSON is not yet implemented");
		}
	}

	public static Geometry readGeometry(InputStream is) throws IOException
	{
		// Read InputStream completely into a byte buffer so that we can read it
		// multiple times. Using BufferedInputStream's mark() and reset() has
		// been tried as well, but it fails if the stream gets closed by any of
		// the attempted reading methods, which is out of our control here.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		{
			byte[] b = new byte[4096];
			while (true) {
				int r = is.read(b);
				if (r < 0) {
					break;
				}
				baos.write(b, 0, r);
			}
		}
		byte[] buffer = baos.toByteArray();

		Geometry geometry = null;
		for (FileFormat format : FileFormat.values()) {
			logger.debug("Trying fileformat: " + format);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			try {
				geometry = readGeometry(bais, format);
				if (geometry != null) {
					break;
				}
			} catch (Exception e) {
				logger.debug("Unable to parse using this format: "
						+ e.getClass().getSimpleName());
				// continue
			}
		}
		if (geometry != null) {
			return geometry;
		}
		throw new IOException(
				"File could not be parsed using any known format");
	}

}
