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

package de.topobyte.jeography.viewer.geometry.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.geometry.io.FileFormat;
import de.topobyte.jeography.geometry.io.GeometrySerializer;
import de.topobyte.jeography.geometry.io.GeometrySerializerFactory;
import de.topobyte.jeography.viewer.action.SimpleAction;
import de.topobyte.swing.util.ExtensionFileFilter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ExportAction extends SimpleAction
{

	final static Logger logger = LoggerFactory.getLogger(ExportAction.class);

	private static final long serialVersionUID = 5060790684819680647L;
	private final Geometry geometry;

	/**
	 * Create a new ExportAction.
	 * 
	 * @param geometry
	 *            the geometry to export.
	 */
	public ExportAction(Geometry geometry)
	{
		super("export", "export geometry to file");
		this.geometry = geometry;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		ExtensionFileFilter filterWkb = new ExtensionFileFilter("wkb",
				"Well Known Binary");
		ExtensionFileFilter filterWkt = new ExtensionFileFilter("wkt",
				"Well Known Text");
		ExtensionFileFilter filterGeoJson = new ExtensionFileFilter("geojson",
				"GeoJSON");
		ExtensionFileFilter filterPoly = new ExtensionFileFilter("poly",
				"Poly text format");
		chooser.addChoosableFileFilter(filterWkb);
		chooser.addChoosableFileFilter(filterWkt);
		chooser.addChoosableFileFilter(filterPoly);
		chooser.addChoosableFileFilter(filterGeoJson);
		chooser.setFileFilter(filterWkb);

		int response = chooser.showSaveDialog(null);
		boolean accept = response == JFileChooser.APPROVE_OPTION;

		if (accept) {
			File file = chooser.getSelectedFile();
			ExtensionFileFilter fileFilter = (ExtensionFileFilter) chooser
					.getFileFilter();
			String extension = fileFilter.getExtension();
			String path = file.getAbsolutePath();
			if (!path.endsWith("." + extension)) {
				path = path + "." + extension;
			}
			FileFormat fileFormat = FileFormat.fromExtension(extension);
			if (fileFormat == null) {
				logger.debug("file format not supported for extension: "
						+ extension);
				return;
			}
			logger.debug("export: " + geometry);
			logger.debug("file: " + path);
			logger.debug("format: " + fileFormat);

			File outfile = new File(path);
			if (outfile.exists()) {
				// TODO: check for existence, and ask user for replacement if
				// necessary
				logger.debug("not overwriting currently, please implement");
				return;
			}

			GeometrySerializer serializer = GeometrySerializerFactory
					.getInstance(fileFormat);
			try {
				serializer.serialize(geometry, outfile);
			} catch (IOException exeption) {
				logger.debug(
						"unable to write geometry: " + exeption.getMessage());
			}
		}
	}
}
