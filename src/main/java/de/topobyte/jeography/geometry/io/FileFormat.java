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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public enum FileFormat {

	/**
	 * Polygon filter file format (osmosis)
	 */
	POLY,
	/**
	 * WKB: Well known binary
	 */
	WKB,
	/**
	 * WKT: Well known text
	 */
	WKT,
	/**
	 * GeoJSON
	 */
	GEOJSON;

	static Map<String, FileFormat> map = new HashMap<>();
	static Map<FileFormat, String> reverseMap = new HashMap<>();

	private static void put(String extension, FileFormat format)
	{
		map.put(extension, format);
		reverseMap.put(format, extension);
	}

	static {
		put("wkb", WKB);
		put("wkt", WKT);
		put("poly", POLY);
		put("geojson", GEOJSON);
	}

	/**
	 * Get the FileFormat to use for the denoted extension.
	 * 
	 * @param extension
	 *            the file extension.
	 * @return the FileFormat to use.
	 */
	public static FileFormat fromExtension(String extension)
	{
		String lower = extension.toLowerCase();
		FileFormat format = map.get(lower);
		return format;
	}

	/**
	 * Get the default file-extension to use for the denoted format.
	 * 
	 * @param format
	 *            the file format.
	 * @return the extension to use.
	 */
	public static String getExtension(FileFormat format)
	{
		return reverseMap.get(format);
	}

}
