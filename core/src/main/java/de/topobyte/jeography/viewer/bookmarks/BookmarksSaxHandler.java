// Copyright 2017 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.bookmarks;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.topobyte.xml.dynsax.Child;
import de.topobyte.xml.dynsax.ChildType;
import de.topobyte.xml.dynsax.Data;
import de.topobyte.xml.dynsax.DynamicSaxHandler;
import de.topobyte.xml.dynsax.Element;
import de.topobyte.xml.dynsax.ParsingException;

class BookmarksSaxHandler extends DynamicSaxHandler
{

	static BookmarksSaxHandler createInstance()
	{
		return new BookmarksSaxHandler();
	}

	private BookmarksSaxHandler()
	{
		setRoot(createRoot(), true);
	}

	private List<Bookmark> bookmarks = new ArrayList<>();

	public List<Bookmark> getBookmarks()
	{
		return bookmarks;
	}

	private Element root, bookmark;

	private static final String NAME_BOOKMARKS = "bookmarks";
	private static final String NAME_BOOKMARK = "bookmark";

	private static final String ATTR_NAME = "name";
	private static final String ATTR_LON = "lon";
	private static final String ATTR_LAT = "lat";

	private Element createRoot()
	{
		root = new Element(NAME_BOOKMARKS, false);

		bookmark = new Element(NAME_BOOKMARK, false);
		bookmark.addAttribute(ATTR_NAME);
		bookmark.addAttribute(ATTR_LON);
		bookmark.addAttribute(ATTR_LAT);

		root.addChild(new Child(bookmark, ChildType.LIST, false));

		return root;
	}

	@Override
	public void emit(Data data) throws ParsingException
	{
		if (data.getElement() == root) {
			List<Data> list = data.getList(NAME_BOOKMARK);
			if (list == null) {
				return;
			}

			for (Data child : list) {
				String name = child.getAttribute(ATTR_NAME);
				String sLon = child.getAttribute(ATTR_LON);
				String sLat = child.getAttribute(ATTR_LAT);
				double lon = Double.parseDouble(sLon);
				double lat = Double.parseDouble(sLat);
				bookmarks.add(new Bookmark(name, new Coordinate(lon, lat)));
			}
		}
	}

}
