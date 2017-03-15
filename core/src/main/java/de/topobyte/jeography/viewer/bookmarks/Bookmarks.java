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

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.vividsolutions.jts.geom.Coordinate;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.melon.casting.CastUtil;
import de.topobyte.swing.util.list.ArrayListModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Bookmarks extends JPanel
{

	private static final long serialVersionUID = 535219091704824790L;

	private JeographyGIS gis;

	private ArrayListModel<Bookmark> model;

	public Bookmarks(JeographyGIS gis)
	{
		this.gis = gis;

		setLayout(new BorderLayout());

		JList<Bookmark> list = new JList<>();
		JScrollPane jsp = new JScrollPane(list);
		add(jsp, BorderLayout.CENTER);

		model = new ArrayListModel<>();
		model.add(
				new Bookmark("Berlin",
						new Coordinate(13.368988037109375, 52.52958999943302)),
				0);
		model.add(new Bookmark("Düsseldorf", new Coordinate(6.777, 51.225)), 1);
		model.add(
				new Bookmark("Cottbus",
						new Coordinate(14.329948425292969, 51.758490455733785)),
				1);

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

	protected void activate(int index)
	{
		Bookmark bookmark = model.getElementAt(index);
		Coordinate c = bookmark.getCoordinate();

		Viewer viewer = gis.getViewer();
		viewer.getMapWindow().gotoLonLat(c.x, c.y);
		viewer.repaint();
	}

}
