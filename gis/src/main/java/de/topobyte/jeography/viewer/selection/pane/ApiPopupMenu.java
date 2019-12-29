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

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelectionFormatter;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.jeography.viewer.statusbar.MouseOverClipboardAdapter;

public class ApiPopupMenu extends JPopupMenu
{

	final static Logger logger = LoggerFactory.getLogger(ApiPopupMenu.class);

	private static final long serialVersionUID = 1L;

	public ApiPopupMenu(RectPane pane, SelectionAdapter selectionAdapter,
			List<GeographicSelectionFormatter> formatters)
	{
		for (GeographicSelectionFormatter formatter : formatters) {
			final PatternClipboardMenuItem item = new PatternClipboardMenuItem(
					selectionAdapter, formatter);

			add(item);
			item.addMouseListener(new MouseOverClipboardAdapter(item, pane,
					item::getClipboardText));
		}

		JMenuItem item = new JMenuItem(
				"Download data from Overpass API and examine");
		add(item);

		item.addActionListener(event -> {
			downloadAndExamine(
					selectionAdapter.getGeographicSelection().toBoundingBox());
		});
	}

	private void downloadAndExamine(BBox bbox)
	{
		TaskDownloadAndExamineOverpass task = new TaskDownloadAndExamineOverpass(
				bbox);
		new Thread(task).start();
	}

}
