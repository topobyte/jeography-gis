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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;

import de.topobyte.jeography.geometry.io.geojson.GeoJsonSerializer;
import de.topobyte.jeography.geometry.io.poly.PolyfileWriter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometrySerializerFactory
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerFactory.class);

	/**
	 * Retrieve a GeometrySerializer for the denoted format.
	 * 
	 * @param format
	 *            the format to get a serializer for.
	 * @return the serializer instance.
	 */
	public static GeometrySerializer getInstance(FileFormat format)
	{
		switch (format) {
		case WKB:
			return new GeometrySerializerWKB();
		case WKT:
			return new GeometrySerializerWKT();
		case POLY:
			return new GeometrySerializerPoly();
		case GEOJSON:
			return new GeometrySerializerGeoJSON();
		}
		return null;
	}

}

class GeometrySerializerJSG implements GeometrySerializer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerJSG.class);

	@Override
	public void serialize(Geometry geometry, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(geometry);
		oos.close();
	}

}

class GeometrySerializerPoly implements GeometrySerializer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerJSG.class);

	@Override
	public void serialize(Geometry geometry, File file) throws IOException
	{
		PolyfileWriter.write(new FileOutputStream(file), "geometry", geometry);
	}

}

class GeometrySerializerWKB implements GeometrySerializer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerWKB.class);

	@Override
	public void serialize(Geometry geometry, File file) throws IOException
	{
		WKBWriter wkbWriter = new WKBWriter();
		FileOutputStream stream = new FileOutputStream(file);
		wkbWriter.write(geometry, new OutputStreamOutStream(stream));
		stream.close();
	}

}

class GeometrySerializerWKT implements GeometrySerializer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerWKT.class);

	@Override
	public void serialize(Geometry geometry, File file) throws IOException
	{
		WKTWriter wktWriter = new WKTWriter();
		FileWriter fileWriter = new FileWriter(file);
		wktWriter.write(geometry, fileWriter);
		fileWriter.close();
	}

}

class GeometrySerializerGeoJSON implements GeometrySerializer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometrySerializerGeoJSON.class);

	@Override
	public void serialize(Geometry geometry, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream os = new BufferedOutputStream(fos);
		GeoJsonSerializer.write(os, geometry);
		os.close();
	}

}
