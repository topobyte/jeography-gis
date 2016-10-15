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

package de.topobyte.jeography.viewer.selection.action;

import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.action.GISAction;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DismissAction extends GISAction
{

	private static final long serialVersionUID = 8870407480951831801L;

	final static Logger logger = LoggerFactory.getLogger(DismissAction.class);

	private final SelectionAdapter selectionAdapter;

	/**
	 * Create this action with the given SelectionAdapter as a source.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 * 
	 * @param selectionAdapter
	 *            the adapter to get the selection from.
	 */
	public DismissAction(JeographyGIS gis, SelectionAdapter selectionAdapter)
	{
		super(gis, "res/images/16/edit-clear.png");
		this.name = "clear selection";
		this.description = "remove the current selection";

		this.selectionAdapter = selectionAdapter;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		selectionAdapter.clearSelection();
	}

}
