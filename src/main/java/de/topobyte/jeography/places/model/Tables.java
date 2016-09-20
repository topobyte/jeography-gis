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

package de.topobyte.jeography.places.model;

import de.topobyte.jsqltables.table.ColumnClass;
import de.topobyte.jsqltables.table.ColumnExtension;
import de.topobyte.jsqltables.table.Table;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Tables
{

	public static String TABLE_NAME_PLACES = "places";

	public static String COLUMN_ID = "id";
	public static String COLUMN_TYPE = "type";
	public static String COLUMN_NAME = "name";
	public static String COLUMN_PREFIX_NAME = "name_";
	public static String COLUMN_LON = "lon";
	public static String COLUMN_LAT = "lat";

	public static String COLUMN_KEY = "key";
	public static String COLUMN_VALUE = "value";

	public static Table METADATA = new Table("metadata");
	static {
		METADATA.addColumn(ColumnClass.VARCHAR, COLUMN_KEY);
		METADATA.addColumn(ColumnClass.VARCHAR, COLUMN_VALUE);
	}

	public static Table PLACETYPES = new Table("placetypes");
	static {
		PLACETYPES.addColumn(ColumnClass.INT, COLUMN_ID,
				ColumnExtension.PRIMARY_AUTO_INCREMENT);
		PLACETYPES.addColumn(ColumnClass.VARCHAR, COLUMN_NAME);
	}

}
