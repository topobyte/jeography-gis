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
import de.topobyte.jsqltables.query.LimitOffset;
import de.topobyte.jsqltables.query.Select;
import de.topobyte.jsqltables.query.TableReference;
import de.topobyte.jsqltables.query.order.OrderDirection;
import de.topobyte.jsqltables.query.order.SingleOrder;
import de.topobyte.jsqltables.query.select.AllColumn;
import de.topobyte.jsqltables.query.where.Comparison;
import de.topobyte.jsqltables.query.where.Condition;
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

		String createMeta = qb.create(Tables.METADATA, true);
		String createTypes = qb.create(Tables.PLACETYPES, true);
		String createSearchMap = qb.create(Tables.SEARCH_MAP, true);

		TablePlaces tablePlaces = new TablePlaces(languages);
		String createPlaces = qb.create(tablePlaces, true);

		connection.execute(createMeta);
		connection.execute(createTypes);
		connection.execute(createPlaces);
		connection.execute(createSearchMap);

		connection.execute(Indexes.createStatement(Tables.TABLE_NAME_PLACES,
				"places_name", Tables.COLUMN_NAME));

		connection.execute(Indexes.createStatement(Tables.TABLE_NAME_PLACES,
				"places_id", Tables.COLUMN_ID));

		connection.execute(Indexes.createStatement(Tables.SEARCH_MAP,
				"map_index", Tables.COLUMN_FTS_ID, Tables.COLUMN_ID));

		connection.execute("create virtual table " + Tables.TABLE_NAME_FTS
				+ " using fts4(" + Tables.SEARCH.getColumn(1).getName()
				+ " TEXT);");
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

	private int idxMetaKey = Tables.METADATA
			.getColumnIndexSafe(Tables.COLUMN_KEY);
	private int idxMetaValue = Tables.METADATA
			.getColumnIndexSafe(Tables.COLUMN_VALUE);

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
		results.close();

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
		int id = results.getInt(1);
		results.close();
		return id;
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
		results.close();
		return idToType;
	}

	public void addMetadata(String key, String value) throws QueryException
	{
		IPreparedStatement stmt = connection.prepareStatement(qb
				.insert(Tables.METADATA));

		stmt.setString(idxMetaKey, key);
		stmt.setString(idxMetaValue, value);

		IResultSet results = stmt.executeQuery();
		results.close();
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
		long id = results.getLong(1);
		results.close();

		List<String> names = new ArrayList<>();

		if (name != null) {
			names.add(name);
		}
		for (String language : tablePlaces.getLanguages()) {
			String altName = altNames.get(language);
			if (altName == null) {
				continue;
			}
			names.add(altName);
		}

		insertSearchNames(id, names);

		return id;
	}

	private void insertSearchNames(long id, List<String> names)
			throws QueryException
	{
		IPreparedStatement s2 = connection.prepareStatement(qb
				.insert(Tables.SEARCH));

		IPreparedStatement s3 = connection.prepareStatement(qb
				.insert(Tables.SEARCH_MAP));

		for (String n : names) {
			s2.setString(1, n);
			IResultSet results = s2.executeQuery();
			long ftsId = results.getLong(1);
			results.close();

			s3.setLong(1, id);
			s3.setLong(2, ftsId);
			s3.executeQuery().close();
		}
	}

	public List<Place> getPlaces(String query, SortOrder order, int max,
			int offset) throws QueryException
	{
		List<Place> list = new ArrayList<>();

		Select select = new Select(tablePlaces);
		TableReference map = select.join(Tables.SEARCH_MAP, Tables.COLUMN_ID,
				Tables.COLUMN_ID);
		TableReference search = select.join(map, Tables.SEARCH,
				Tables.COLUMN_FTS_ID, "rowid");

		select.distinct();
		select.addSelectColumn(new AllColumn(select.getMainTable()));

		Condition condition = new SingleCondition(search, Tables.COLUMN_NAME,
				Comparison.LIKE);

		select.where(condition);
		select.order(new SingleOrder(select.getMainTable(), Tables.COLUMN_NAME,
				OrderDirection.ASC));
		select.limit(new LimitOffset(max, offset));

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
		results.close();

		return list;
	}

}
