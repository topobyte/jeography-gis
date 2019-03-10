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

import java.util.List;

import de.topobyte.jsqltables.table.ColumnClass;
import de.topobyte.jsqltables.table.ColumnExtension;
import de.topobyte.jsqltables.table.Table;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TablePlaces extends Table
{

	private List<String> languages;

	public TablePlaces(List<String> languages)
	{
		super(Tables.TABLE_NAME_PLACES);
		this.languages = languages;

		addColumn(ColumnClass.LONG, Tables.COLUMN_ID,
				ColumnExtension.PRIMARY_AUTO_INCREMENT);
		addColumn(ColumnClass.INT, Tables.COLUMN_TYPE);
		addColumn(ColumnClass.DOUBLE, Tables.COLUMN_LON);
		addColumn(ColumnClass.DOUBLE, Tables.COLUMN_LAT);
		addColumn(ColumnClass.VARCHAR, Tables.COLUMN_NAME);

		for (String language : languages) {
			addColumn(ColumnClass.VARCHAR,
					Tables.COLUMN_PREFIX_NAME + language);
		}
	}

	public List<String> getLanguages()
	{
		return languages;
	}

}
