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

package de.topobyte.jeography.places.setup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.places.Dao;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.JdbcConnection;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.osm4j.core.access.OsmReader;
import de.topobyte.osm4j.core.dataset.InMemoryListDataSet;
import de.topobyte.osm4j.core.dataset.ListDataSetLoader;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmIoUtils;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DatabaseBuilder
{

	final static Logger logger = LoggerFactory.getLogger(DatabaseBuilder.class);

	private Path pathDatabase;
	private List<String> languages;
	private List<String> types;
	private Map<String, Path> files;

	private Connection jdbcConnection;
	private IConnection connection;
	private Dao dao;

	private Map<String, Integer> typeToId = new HashMap<>();

	public DatabaseBuilder(Path pathDatabase, List<String> languages,
			List<String> types, Map<String, Path> files)
	{
		this.pathDatabase = pathDatabase;
		this.languages = languages;
		this.types = types;
		this.files = files;
	}

	public void build() throws DatabaseBuildingException, SQLException,
			QueryException, IOException, OsmInputException
	{
		logger.info("loading driver");
		loadDriver();
		logger.info("initializing connection");
		initConnection();
		logger.info("initializing schema");
		initSchema();
		logger.info("initializing DAO");
		initDao();
		logger.info("adding metadata");
		addMetadata();
		logger.info("inserting types");
		insertTypes();
		logger.info("inserting data");
		insertData();
		logger.info("building search table");
		buildSearchTable();
		logger.info("closing connection");
		closeConnection();
		logger.info("done");
	}

	private void loadDriver() throws DatabaseBuildingException
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new DatabaseBuildingException("sqlite driver not found", e);
		}
	}

	private void initConnection() throws DatabaseBuildingException, SQLException
	{
		/*
		 * Setup database connection
		 */

		logger.debug("configuring output connection");
		jdbcConnection = null;
		try {
			jdbcConnection = DriverManager
					.getConnection("jdbc:sqlite:" + pathDatabase);
			jdbcConnection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DatabaseBuildingException(
					"unable to create jdbc connection", e);
		}
		connection = null;
		try {
			connection = new JdbcConnection(jdbcConnection);
		} catch (SQLException e) {
			throw new DatabaseBuildingException(
					"unable to create jdbc connection", e);
		}

		jdbcConnection.setAutoCommit(false);
	}

	private void initSchema() throws QueryException
	{
		Dao.createSchema(connection, languages);
	}

	private void initDao() throws QueryException
	{
		dao = new Dao(connection);
	}

	private void addMetadata() throws QueryException
	{
		/*
		 * Add metadata
		 */

		dao.addMetadata("schema-version", "1");
	}

	private void insertTypes() throws QueryException
	{
		/*
		 * Insert place types
		 */

		for (String type : types) {
			int id = dao.addType(type);
			logger.info("type " + id + " " + type);
			typeToId.put(type, id);
		}
	}

	private void insertData()
			throws IOException, OsmInputException, QueryException
	{
		/*
		 * Read and insert data
		 */

		for (String type : types) {
			int typeId = typeToId.get(type);
			Path file = files.get(type);

			OsmReader reader = OsmIoUtils.setupOsmReader(
					Files.newInputStream(file), FileFormat.TBO, false);
			InMemoryListDataSet data = ListDataSetLoader.read(reader, true,
					true, true);
			logger.info(type + ": " + data.getNodes().size());
			for (OsmNode node : data.getNodes()) {
				Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);
				String name = tags.get("name");
				Map<String, String> altNames = new HashMap<>();
				for (String language : languages) {
					String langName = tags.get("name:" + language);
					if (langName == null) {
						continue;
					}
					altNames.put(language, langName);
				}
				if (name == null && altNames.isEmpty()) {
					continue;
				}
				if (name == null) {
					// Make sure that name != null. From all alternative names
					// choose the first non-null name in order of the languages
					// array.
					for (String language : languages) {
						String altName = altNames.get(language);
						if (altName == null) {
							continue;
						}
						name = altName;
						break;
					}
				}
				dao.addPlace(typeId, name, altNames, node.getLongitude(),
						node.getLatitude());
			}
		}
	}

	private void buildSearchTable() throws QueryException
	{
		dao.populateSearchTable();
	}

	private void closeConnection() throws SQLException
	{
		/*
		 * Close connection
		 */

		jdbcConnection.commit();
		jdbcConnection.close();
	}

}
