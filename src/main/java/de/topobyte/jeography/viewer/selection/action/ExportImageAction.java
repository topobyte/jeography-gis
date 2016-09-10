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
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.action.GISAction;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.export.ImageExporter;
import de.topobyte.jeography.viewer.selection.rectangular.Latitude;
import de.topobyte.jeography.viewer.selection.rectangular.Longitude;
import de.topobyte.jeography.viewer.selection.rectangular.Selection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ExportImageAction extends GISAction
{

	private static final long serialVersionUID = 9126814144430505371L;

	final static Logger logger = LoggerFactory
			.getLogger(ExportImageAction.class);

	private final SelectionAdapter selectionAdapter;

	/**
	 * Create this action with the given SelectionAdapter as a source for
	 * created images.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 * 
	 * @param selectionAdapter
	 *            the adapter to get the selection from.
	 */
	public ExportImageAction(JeographyGIS gis, SelectionAdapter selectionAdapter)
	{
		super(gis, "res/images/16/stock_insert_image.png");
		this.name = "export image";
		this.description = "export the current selection as an image";

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

		// IntSelection iselection = selectionAdapter.getSelection();
		logger.debug("exporting image");
		logger.debug("geo: " + selection.toString());
		// logger.debug("int: " + iselection.toString());
		logger.debug("zoom: " + getGIS().getViewer().getZoomLevel());

		TileConfig tileConfig = getGIS().getViewer().getTileConfig();

		// TODO: ask for zoom
		int zoom = getGIS().getViewer().getZoomLevel();

		ImageExporter imageExporter = new ImageExporter(zoom, selection,
				tileConfig);

		// TODO: ask for filename
		try {
			File tempFile = File.createTempFile("image", ".png");
			String path = tempFile.getCanonicalPath();
			logger.debug("writing to: " + path);
			imageExporter.export(path);
		} catch (IOException e1) {
			logger.debug("unable to create temporary file");
		}
	}

}
