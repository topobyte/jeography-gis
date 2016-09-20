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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SortOrder;

import de.topobyte.jeography.places.model.Place;
import de.topobyte.jeography.places.model.TablePlaces;
import de.topobyte.jeography.places.model.Tables;
import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.index.Indexes;
import de.topobyte.jsqltables.query.Select;
import de.topobyte.jsqltables.query.order.OrderDirection;
import de.topobyte.jsqltables.query.order.SingleOrder;
import de.topobyte.jsqltables.query.where.Comparison;
import de.topobyte.jsqltables.query.where.SingleCondition;
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

		connection.execute(Indexes.createStatement(Tables.TABLE_NAME_PLACES,
				"places_name", Tables.COLUMN_NAME));
	}

	private IConnection connection;
	private QueryBuilder qb = new QueryBuilder(new SqliteDialect());

	private TablePlaces tablePlaces;
	private List<String> languages;

	private Map<Integer, String> types;

	private int idxPlacesId;
	private int idxPlacesName;
	private int idxPlacesType;
	private int idxPlacesLon;
	private int idxPlacesLat;

	private int idxTypesId = Tables.PLACETYPES
			.getColumnIndexSafe(Tables.COLUMN_ID);
	private int idxTypesName = Tables.PLACETYPES
			.getColumnIndexSafe(Tables.COLUMN_NAME);

	public Dao(IConnection connection) throws QueryException
	{
		this.connection = connection;

		languages = new ArrayList<>();

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

		idxPlacesId = tablePlaces.getColumnIndexSafe(Tables.COLUMN_ID);
		idxPlacesName = tablePlaces.getColumnIndexSafe(Tables.COLUMN_NAME);
		idxPlacesType = tablePlaces.getColumnIndexSafe(Tables.COLUMN_TYPE);
		idxPlacesLon = tablePlaces.getColumnIndexSafe(Tables.COLUMN_LON);
		idxPlacesLat = tablePlaces.getColumnIndexSafe(Tables.COLUMN_LAT);

		types = getTypes();
	}

	public int addType(String name) throws QueryException
	{
		IPreparedStatement stmt = connection.prepareStatement(qb
				.insert(Tables.PLACETYPES));
		stmt.setString(idxTypesName, name);
		IResultSet results = stmt.executeQuery();
		return results.getInt(1);
	}

	private Map<Integer, String> getTypes() throws QueryException
	{
		Map<Integer, String> idToType = new HashMap<>();
		Select select = new Select(Tables.PLACETYPES);
		IPreparedStatement stmt = connection.prepareStatement(select.sql());
		IResultSet results = stmt.executeQuery();
		while (results.next()) {
			int id = results.getInt(idxTypesId);
			String name = results.getString(idxTypesName);
			idToType.put(id, name);
		}
		return idToType;
	}

	public long addPlace(int type, String name, Map<String, String> altNames,
			double lon, double lat) throws QueryException
	{
		IPreparedStatement stmt = connection.prepareStatement(qb
				.insert(tablePlaces));

		stmt.setInt(idxPlacesType, type);
		stmt.setString(idxPlacesName, name);

		for (String language : tablePlaces.getLanguages()) {
			int idx = tablePlaces.getColumnIndexSafe(Tables.COLUMN_PREFIX_NAME
					+ language);
			stmt.setString(idx, altNames.get(language));
		}

		stmt.setDouble(idxPlacesLon, lon);
		stmt.setDouble(idxPlacesLat, lat);

		IResultSet results = stmt.executeQuery();
		return results.getLong(1);
	}

	public List<Place> getPlaces(String query, SortOrder order, int max,
			int offset) throws QueryException
	{
		List<Place> list = new ArrayList<>();

		Select select = new Select(tablePlaces);
		select.where(new SingleCondition(select.getMainTable(),
				Tables.COLUMN_NAME, Comparison.LIKE));
		select.order(new SingleOrder(select.getMainTable(), Tables.COLUMN_NAME,
				OrderDirection.ASC));

		IPreparedStatement stmt = connection.prepareStatement(select.sql());
		stmt.setString(1, "%" + query + "%");

		IResultSet results = stmt.executeQuery();
		while (results.next()) {
			long id = results.getLong(idxPlacesId);
			int typeId = results.getInt(idxPlacesType);
			String type = types.get(typeId);
			String name = results.getString(idxPlacesName);
			Map<String, String> altNames = new HashMap<>();
			double lon = results.getDouble(idxPlacesLon);
			double lat = results.getDouble(idxPlacesLat);
			list.add(new Place(id, type, name, altNames, lon, lat));
			for (String language : languages) {
				int idx = tablePlaces
						.getColumnIndexSafe(Tables.COLUMN_PREFIX_NAME
								+ language);
				String altName = results.getString(idx);
				if (altName == null) {
					continue;
				}
				altNames.put(language, altName);
			}
		}

		return list;
	}

}
