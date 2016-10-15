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

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.action.GISAction;
import de.topobyte.jeography.viewer.selection.download.DownloadDialog;
import de.topobyte.jeography.viewer.selection.rectangular.Latitude;
import de.topobyte.jeography.viewer.selection.rectangular.Longitude;
import de.topobyte.jeography.viewer.selection.rectangular.Selection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DownloadAction extends GISAction
{

	private static final long serialVersionUID = 8870407480951831801L;

	final static Logger logger = LoggerFactory.getLogger(DownloadAction.class);

	private final SelectionAdapter selectionAdapter;

	/**
	 * Create this action with the given SelectionAdapter as a source for the
	 * download area.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 * 
	 * @param selectionAdapter
	 *            the adapter to get the selection from.
	 */
	public DownloadAction(JeographyGIS gis, SelectionAdapter selectionAdapter)
	{
		super(gis, "res/images/16/document-save.png");
		this.name = "download tiles";
		this.description = "download the tiles covered by the current selection";

		this.selectionAdapter = selectionAdapter;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Selection<Longitude, Latitude> selection = selectionAdapter
				.getGeographicSelection();

		if (selection == null) {
			return;
		}

		logger.debug("downloading tiles");
		logger.debug("geo: " + selection.toString());
		logger.debug("zoom: " + getGIS().getViewer().getZoomLevel());

		JFrame frame = getMainFrame();
		DownloadDialog dialog = new DownloadDialog(getGIS(), frame,
				"download options");
		dialog.pack();
		dialog.setVisible(true);
	}

}
