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

package de.topobyte.jeography.viewer.selection.pane;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.geometry.manage.EventJDialog;
import de.topobyte.jeography.viewer.selection.polygonal.PolySelectionAdapter;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Dialog extends EventJDialog
{

	private static final long serialVersionUID = -7335715034238308994L;

	private JTabbedPane tabbed = new JTabbedPane();

	private RectPane rectPane;
	private PolyPane polyPane;

	/**
	 * Create a new dialog.
	 * 
	 * @param parent
	 *            the parent frame or null.
	 * @param title
	 *            the title of the dialog.
	 * @param gis
	 *            the JeographyGIS instance this is for.
	 * @param selectionAdapter
	 *            the selectionAdapter to display information for.
	 * @param polySelectionAdapter
	 *            the polygonal selectionAdapter to display information for.
	 */
	public Dialog(JFrame parent, String title, JeographyGIS gis,
			SelectionAdapter selectionAdapter,
			PolySelectionAdapter polySelectionAdapter)
	{
		super(parent, title);

		setContentPane(tabbed);

		rectPane = new RectPane(gis, selectionAdapter);
		tabbed.add("rectangular", rectPane);

		polyPane = new PolyPane(gis, polySelectionAdapter);
		tabbed.add("polygonal", polyPane);
	}

	/**
	 * Show the denoted tab.
	 * 
	 * @param tab
	 *            the tab to show.
	 */
	public void showTab(Tab tab)
	{
		int index = 0;
		switch (tab) {
		case RectPane:
			index = 0;
			break;
		case PolyPane:
			index = 1;
			break;
		}
		tabbed.setSelectedIndex(index);
	}

}
