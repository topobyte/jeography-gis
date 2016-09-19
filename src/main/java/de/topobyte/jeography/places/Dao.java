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

package de.topobyte.jeography.places;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.topobyte.jeography.places.model.TablePlaces;
import de.topobyte.jeography.places.model.Tables;
import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.table.QueryBuilder;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.IPreparedStatement;
import de.topobyte.luqe.iface.IResultSet;
import de.topobyte.luqe.iface.QueryException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Dao
{

	public static void createSchema(IConnection connection,
			List<String> languages) throws QueryException
	{
		QueryBuilder qb = new QueryBuilder(new SqliteDialect());

		String createTypes = qb.create(Tables.PLACETYPES, true);

		TablePlaces tablePlaces = new TablePlaces(languages);
		String createPlaces = qb.create(tablePlaces, true);

		connection.execute(createTypes);
		connection.execute(createPlaces);
	}

	private IConnection connection;
	private QueryBuilder qb = new QueryBuilder(new SqliteDialect());

	private TablePlaces tablePlaces;

	public Dao(IConnection connection) throws QueryException
	{
		this.connection = connection;

		// Get available languages from database table names
		List<String> languages = new ArrayList<>();

		String query = String.format("pragma table_info(%s);",
				Tables.TABLE_NAME_PLACES);
		IPreparedStatement stmt = connection.prepareStatement(query);
		IResultSet results = stmt.executeQuery();
		while (results.next()) {
			String colName = results.getString(2);
			String prefix = Tables.COLUMN_PREFIX_NAME;
			if (colName.startsWith(prefix)) {
				languages.add(colName.substring(prefix.length()));
			}
		}

		tablePlaces = new TablePlaces(languages);
	}

	public int addType(String name) throws QueryException
	{
		IPreparedStatement stmt = connection.prepareStatement(qb
				.insert(Tables.PLACETYPES));
		int idxName = Tables.PLACETYPES.getColumnIndexSafe(Tables.COLUMN_NAME);
		stmt.setString(idxName, name);
		IResultSet results = stmt.executeQuery();
		return results.getInt(1);
	}

	public long addPlace(int type, String name, Map<String, String> altNames,
			double lon, double lat) throws QueryException
	{
		IPreparedStatement stmt = connection.prepareStatement(qb
				.insert(tablePlaces));
		int idxName = tablePlaces.getColumnIndexSafe(Tables.COLUMN_NAME);
		int idxType = tablePlaces.getColumnIndexSafe(Tables.COLUMN_TYPE);
		int idxLon = tablePlaces.getColumnIndexSafe(Tables.COLUMN_LON);
		int idxLat = tablePlaces.getColumnIndexSafe(Tables.COLUMN_LAT);

		stmt.setInt(idxType, type);
		stmt.setString(idxName, name);

		for (String language : tablePlaces.getLanguages()) {
			int idx = tablePlaces.getColumnIndexSafe(Tables.COLUMN_PREFIX_NAME
					+ language);
			stmt.setString(idx, altNames.get(language));
		}

		stmt.setDouble(idxLon, lon);
		stmt.setDouble(idxLat, lat);

		IResultSet results = stmt.executeQuery();
		return results.getLong(1);
	}

}
