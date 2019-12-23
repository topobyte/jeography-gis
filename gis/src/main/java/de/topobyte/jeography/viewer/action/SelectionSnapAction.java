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

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionSnapAction extends ViewerAction
{

	private static final long serialVersionUID = -3911163331340305500L;

	static final Logger logger = LoggerFactory
			.getLogger(SelectionSnapAction.class);

	private static final String FILE_ICON = "res/images/stock_snap-object-points.png";

	private JeographyGIS gis;

	/**
	 * @param gis
	 *            the JeographyGIS instance to monitor with this action.
	 */
	public SelectionSnapAction(JeographyGIS gis)
	{
		super(gis.getViewer(), FILE_ICON);
		this.gis = gis;
		// icon = new ImageIcon(FILE_IMAGE, "");
	}

	@Override
	public Object getValue(String key)
	{
		// System.out.println(key);
		if (key.equals("SmallIcon")) {
			return icon;
		} else if (key.equals(Action.SELECTED_KEY)) {
			return Boolean.valueOf(gis.getSelectionAdapter().isSnapSelection());
		} else if (key.equals(Action.NAME)) {
			return "Selection's snap mode";
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle selection's snap mode";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		SelectionAdapter selectionAdapter = gis.getSelectionAdapter();
		selectionAdapter.setSnapSelection(!selectionAdapter.isSnapSelection());
		firePropertyChange(Action.SELECTED_KEY, null, null);
	}

}
