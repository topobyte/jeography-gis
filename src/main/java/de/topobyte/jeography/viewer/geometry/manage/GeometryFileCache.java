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

package de.topobyte.jeography.viewer.geometry.manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.geometry.io.PolygonLoader;
import de.topobyte.melon.casting.CastUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryFileCache
{

	private static int idFactory = 1;

	/**
	 * TODO: no caching performed currently
	 * 
	 * @param file
	 *            the file to retrieve the geometries from.
	 * @return the set of tagged geometries.
	 */
	public Set<GeometryContainer> read(File file)
	{
		Set<GeometryContainer> geometries = new HashSet<>();

		String name = file.getName();
		if (name.endsWith(".jsg") || name.endsWith(".ser")
				|| name.endsWith(".wkt") || name.endsWith(".wkb")) {
			try {
				int id = idFactory++;
				Geometry geometry = PolygonLoader
						.readPolygon(file.getAbsolutePath());
				GeoObject tg = new GeoObject(geometry);
				GeometrySourceJSG source = new GeometrySourceJSG(name);
				GeometryContainer geometryContainer = new GeometryContainer(id,
						tg, source);
				geometries.add(geometryContainer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		} else if (name.endsWith(".tgs")) {
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(new FileInputStream(file));
				List<GeoObject> elements = CastUtil.cast(ois.readObject());
				int n = 0;
				for (GeoObject geometry : elements) {
					int id = idFactory++;
					GeometrySourceTGS source = new GeometrySourceTGS(name, n++);
					GeometryContainer geometryContainer = new GeometryContainer(
							id, geometry, source);
					geometries.add(geometryContainer);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return geometries;
	}

}
