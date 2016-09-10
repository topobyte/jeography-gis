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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

import de.topobyte.jeography.geometry.io.poly.PolyfileReader;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PolygonLoader
{

	/**
	 * Load a polygon from a file trying different formats.
	 * 
	 * @param filename
	 *            the file to read from.
	 * @return the read polygon or null on failure.
	 * @throws IOException
	 *             if the file cannot be read.
	 */
	public static Geometry readPolygon(String filename) throws IOException
	{
		Geometry geom = null;
		File file = new File(filename);
		if (!file.exists()) {
			throw new IOException("file not found");
		}
		FileInputStream fis = new FileInputStream(filename);
		try {
			geom = PolyfileReader.read(fis);
		} catch (Exception e) {
			// ignore and try different format
		} finally {
			fis.close();
		}
		if (geom == null) {
			WKBReader reader = new WKBReader();
			try {
				fis = new FileInputStream(filename);
				geom = reader.read(new InputStreamInStream(fis));
			} catch (ParseException e) {
				// ignore
			} finally {
				fis.close();
			}
		}
		if (geom == null) {
			WKTReader reader = new WKTReader();
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(new File(filename));
				geom = reader.read(fileReader);
			} catch (ParseException e) {
				// ignore
			} finally {
				fileReader.close();
			}
		}
		if (geom == null) {
			System.out.println("unable to load...");
			throw new IOException("unable to load file: " + filename);
		}
		return geom;
	}

}
