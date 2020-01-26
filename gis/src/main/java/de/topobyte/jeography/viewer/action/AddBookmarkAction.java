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

package de.topobyte.jeography.viewer.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.bookmarks.Bookmark;
import de.topobyte.jeography.viewer.bookmarks.Bookmarks;
import de.topobyte.jeography.viewer.bookmarks.BookmarksIO;
import de.topobyte.jeography.viewer.bookmarks.BookmarksModel;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class AddBookmarkAction extends GISAction
{

	private static final long serialVersionUID = -7407120947945201632L;

	final static Logger logger = LoggerFactory
			.getLogger(AddBookmarkAction.class);

	/**
	 * Create the action
	 * 
	 * @param gis
	 *            the JeographyGIS instance to work on.
	 */
	public AddBookmarkAction(JeographyGIS gis)
	{
		super(gis, "res/images/stock_bookmark.png", "add bookmark");
		setDescription("Add a bookmark with the current position");
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Bookmarks bookmarks = getGIS().getBookmarks();
		BookmarksModel model = bookmarks.getModel();

		TileMapWindow mapWindow = getGIS().getViewer().getMapWindow();
		double lon = mapWindow.getCenterLon();
		double lat = mapWindow.getCenterLat();

		// TODO: get name via dialog
		String name = "Test";

		int size = model.getSize();
		model.add(new Bookmark(name, new Coordinate(lon, lat)), size);

		Path file = ConfigurationHelper.getBookmarksFilePath();

		// TODO: copying into another list is kind of stupid
		List<Bookmark> list = new ArrayList<>();
		for (int i = 0; i < model.getSize(); i++) {
			list.add(model.getElementAt(i));
		}

		try {
			BookmarksIO.write(file, list);
		} catch (IOException e) {
			logger.error("Error while writing bookmarks file", e);
			// TODO: display error message
		}
	}

}
