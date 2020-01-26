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

package de.topobyte.jeography.viewer.bookmarks;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.melon.casting.CastUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Bookmarks extends JPanel
{

	final static Logger logger = LoggerFactory.getLogger(Bookmarks.class);

	private static final long serialVersionUID = 535219091704824790L;

	private JeographyGIS gis;

	private BookmarksModel model;

	public Bookmarks(JeographyGIS gis)
	{
		this.gis = gis;

		setLayout(new BorderLayout());

		JList<Bookmark> list = new JList<>();
		JScrollPane jsp = new JScrollPane(list);
		add(jsp, BorderLayout.CENTER);

		model = new BookmarksModel();

		boolean gotBookmarks = loadConfigBoomarks();
		if (!gotBookmarks) {
			loadDefaultBookmarks();
		}

		list.setCellRenderer(new BookmarksRenderer());
		list.setModel(model);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JList<Bookmark> list = CastUtil.cast(e.getSource());
				if (e.getClickCount() == 2) {
					int index = list.locationToIndex(e.getPoint());
					activate(index);
				}
			}
		});
	}

	private boolean loadConfigBoomarks()
	{
		Path pathInput = ConfigurationHelper.getBookmarksFilePath();
		try {
			InputStream input = Files.newInputStream(pathInput);
			List<Bookmark> bookmarks = BookmarksIO.read(input);
			input.close();
			model.addAll(bookmarks, 0);
			return true;
		} catch (IOException e) {
			logger.warn("Error while reading bookmarks from config dir", e);
			return false;
		}
	}

	private boolean loadDefaultBookmarks()
	{
		try {
			InputStream input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("res/bookmarks.xml");
			List<Bookmark> bookmarks = BookmarksIO.read(input);
			input.close();
			model.addAll(bookmarks, 0);
			return true;
		} catch (IOException e) {
			logger.warn("Error while reading bookmarks from resources", e);
			return false;
		}
	}

	protected void activate(int index)
	{
		Bookmark bookmark = model.getElementAt(index);
		Coordinate c = bookmark.getCoordinate();

		Viewer viewer = gis.getViewer();
		viewer.getMapWindow().gotoLonLat(c.x, c.y);
		viewer.repaint();
	}

	public BookmarksModel getModel()
	{
		return model;
	}

}
