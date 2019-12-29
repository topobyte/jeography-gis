// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.selection.pane;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.viewer.osmocrat.OsmocratFrame;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.osm4j.core.access.OsmReader;
import de.topobyte.osm4j.core.dataset.InMemoryListDataSet;
import de.topobyte.osm4j.core.dataset.ListDataSetLoader;
import de.topobyte.osm4j.xml.dynsax.OsmXmlReader;
import de.topobyte.overpass.OverpassUtil;

public class TaskDownloadAndExamineOverpass implements Runnable
{

	final static Logger logger = LoggerFactory
			.getLogger(TaskDownloadAndExamineOverpass.class);

	private BBox bbox;

	public TaskDownloadAndExamineOverpass(BBox bbox)
	{
		this.bbox = bbox;
	}

	@Override
	public void run()
	{
		try {
			downloadAndExamineInternal();
		} catch (IOException | OsmInputException e) {
			// TODO show error dialog
			e.printStackTrace();
		}
	}

	private void downloadAndExamineInternal()
			throws MalformedURLException, IOException, OsmInputException
	{
		logger.info("downloading: " + bbox);
		OverpassUtil.cache(bbox);
		logger.info("done");
		Path cacheFile = OverpassUtil.cacheFile(bbox);
		InputStream input = Files.newInputStream(cacheFile);

		// Create a reader and read all data into a data set
		OsmReader reader = new OsmXmlReader(input, false);
		InMemoryListDataSet data = ListDataSetLoader.read(reader, true, true,
				true);
		OsmocratFrame osmocrat = new OsmocratFrame(data);
		osmocrat.show();
	}

}
