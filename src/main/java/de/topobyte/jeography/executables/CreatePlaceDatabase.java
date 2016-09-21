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

package de.topobyte.jeography.executables;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.access.OsmOutputStream;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.tbo.access.TboWriter;
import de.topobyte.osm4j.utils.AbstractExecutableSingleInputFile;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CreatePlaceDatabase extends AbstractExecutableSingleInputFile
{

	final static Logger logger = LoggerFactory
			.getLogger(CreatePlaceDatabase.class);

	private static final String OPTION_OUTPUT = "output";

	public static void main(String[] args) throws DatabaseBuildingException,
			SQLException, QueryException, IOException, OsmInputException
	{
		CreatePlaceDatabase task = new CreatePlaceDatabase();

		task.setup(args);

		task.execute();
	}

	private Path pathDatabase;

	@Override
	protected String getHelpMessage()
	{
		return CreatePlaceDatabase.class.getSimpleName() + " [args]";
	}

	public CreatePlaceDatabase()
	{
		// @formatter:off
		OptionHelper.addL(options, OPTION_OUTPUT, true, true, "database output file");
		// @formatter:on
	}

	@Override
	protected void setup(String[] args)
	{
		super.setup(args);
		pathDatabase = Paths.get(line.getOptionValue(OPTION_OUTPUT));
	}

	public void execute() throws DatabaseBuildingException, SQLException,
			QueryException, IOException, OsmInputException
	{
		List<String> languages = new ArrayList<>();
		languages.add("en");
		languages.add("de");

		List<String> types = Lists.newArrayList("country", "state", "county",
				"city", "town", "village", "region", "island");

		/*
		 * Build temporary data
		 */

		Map<String, Path> files = new HashMap<>();
		for (String type : types) {
			String prefix = String.format("place-creation-%s-", type);
			File file = File.createTempFile(prefix, ".tbo");
			file.deleteOnExit();
			files.put(type, file.toPath());
			logger.info(String.format("temporary file for type=%s: '%s'", type,
					file));
		}

		Map<String, OutputStream> streams = new HashMap<>();
		Map<String, OsmOutputStream> outputs = new HashMap<>();

		for (String type : types) {
			Path file = files.get(type);
			OutputStream os = Files.newOutputStream(file);
			TboWriter writer = new TboWriter(os, false);
			streams.put(type, os);
			outputs.put(type, writer);
		}

		OsmIteratorInput input = getOsmFileInput().createIterator(true, false);
		for (EntityContainer container : input.getIterator()) {
			if (container.getType() != EntityType.Node) {
				continue;
			}
			OsmNode node = (OsmNode) container.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);
			String place = tags.get("place");
			if (place == null) {
				continue;
			}
			OsmOutputStream output = outputs.get(place);
			if (output == null) {
				continue;
			}
			output.write(node);
		}

		for (String type : types) {
			outputs.get(type).complete();
			streams.get(type).close();
		}

		/*
		 * Build database
		 */

		DatabaseBuilder databaseBuilder = new DatabaseBuilder(pathDatabase,
				languages, types, files);
		databaseBuilder.build();
	}

}
