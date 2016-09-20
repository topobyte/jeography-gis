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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class TestBuildDatabase
{

	final static Logger logger = LoggerFactory
			.getLogger(TestBuildDatabase.class);

	public static void main(String[] args) throws IOException,
			OsmInputException, QueryException, SQLException
	{
		Path dir = Paths.get("/raid/osm/planet/planet-derivatives/160516");
		String[] types = { "country", "state", "county", "city", "town",
				"village", "region", "island" };

		Path pathDatabase = Paths.get("/tmp/places.sqlite");

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			logger.error("sqlite driver not found", e);
			System.exit(1);
		}

		/*
		 * Setup database connection
		 */

		logger.debug("configuring output connection");
		Connection jdbcConnection = null;
		try {
			jdbcConnection = DriverManager.getConnection("jdbc:sqlite:"
					+ pathDatabase);
			jdbcConnection.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("unable to create jdbc connection", e);
			System.exit(1);
		}
		JdbcConnection connection = null;
		try {
			connection = new JdbcConnection(jdbcConnection);
		} catch (SQLException e) {
			logger.error("unable to create jdbc connection", e);
			System.exit(1);
		}

		List<String> languages = new ArrayList<>();
		languages.add("en");
		languages.add("de");

		Dao.createSchema(connection, languages);

		Dao dao = new Dao(connection);

		/*
		 * Add metadata
		 */

		dao.addMetadata("schema-version", "1");

		/*
		 * Insert place types
		 */

		Map<String, Integer> typeToId = new HashMap<>();

		for (String type : types) {
			int id = dao.addType(type);
			System.out.println("type " + id + " " + type);
			typeToId.put(type, id);
		}

		/*
		 * Read and insert data
		 */

		for (String type : types) {
			int typeId = typeToId.get(type);

			String filename = String.format("place-%s.tbo", type);
			Path file = dir.resolve(filename);

			OsmReader reader = OsmIoUtils.setupOsmReader(
					Files.newInputStream(file), FileFormat.TBO, false);
			InMemoryListDataSet data = ListDataSetLoader.read(reader, true,
					true, true);
			System.out.println(type + ": " + data.getNodes().size());
			for (OsmNode node : data.getNodes()) {
				Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);
				String name = tags.get("name");
				Map<String, String> altNames = new HashMap<>();
				for (String language : languages) {
					String langName = tags.get("name:" + language);
					altNames.put(language, langName);
				}
				dao.addPlace(typeId, name, altNames, node.getLongitude(),
						node.getLatitude());
			}
		}

		/*
		 * Close connection
		 */

		jdbcConnection.commit();
		jdbcConnection.close();
	}

}
