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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.topobyte.jeography.places.setup.DatabaseBuilder;
import de.topobyte.jeography.places.setup.DatabaseBuildingException;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.osm4j.core.access.OsmInputException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestBuildDatabase
{

	final static Logger logger = LoggerFactory
			.getLogger(TestBuildDatabase.class);

	public static void main(String[] args) throws DatabaseBuildingException,
			SQLException, QueryException, IOException, OsmInputException

	{
		Path dir = Paths.get("/raid/osm/planet/planet-derivatives/160516");

		Path pathDatabase = Paths.get("/tmp/places.sqlite");

		List<String> languages = new ArrayList<>();
		languages.add("en");
		languages.add("de");

		List<String> types = Lists.newArrayList("country", "state", "county",
				"city", "town", "village", "region", "island");

		Map<String, Path> files = new HashMap<>();
		for (String type : types) {
			String filename = String.format("place-%s.tbo", type);
			Path file = dir.resolve(filename);
			files.put(type, file);
		}

		DatabaseBuilder databaseBuilder = new DatabaseBuilder(pathDatabase,
				languages, types, files);
		databaseBuilder.build();
	}

}
