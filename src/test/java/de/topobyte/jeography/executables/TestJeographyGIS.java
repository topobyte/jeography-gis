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

package de.topobyte.jeography.executables;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.geometry.manage.GeometryManager;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRule;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRuleTag;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRules;
import de.topobyte.jeography.viewer.geometry.manage.GeometryRulesModel;
import de.topobyte.jeography.viewer.geometry.manage.GeometryTree;
import de.topobyte.jeography.viewer.geometry.manage.GeometryTreeModel;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;
import de.topobyte.jeography.viewer.selection.polygonal.Selection;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * A test class for the main UI
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestJeographyGIS
{

	static final Logger logger = LoggerFactory
			.getLogger(TestJeographyGIS.class);

	public static void main(String[] args) throws ParseException
	{
		final int zoom = 6;
		final double lon = 10.9, lat = 51.5;
		final int width = 800, height = 600;
		final boolean showGeometryManager = true;
		final boolean showSelectionRectDialog = false;
		final boolean showSelectionPolyDialog = false;
		final boolean showMapWindowDialog = false;

		String configFile = ConfigurationHelper.getUserConfigurationFilePath();

		Configuration configuration = Configuration
				.createDefaultConfiguration();

		logger.debug("default user config file: " + configFile);
		try {
			configuration = ConfigReader.read(configFile);
		} catch (Exception e) {
			logger.info("unable to read configuration: " + e.getMessage());
			logger.info("using default configuration");
		}

		final JeographyGIS gis = new JeographyGIS(configFile, configuration, 0,
				null, true, false, false, false, false);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				gis.create(width, height, showGeometryManager,
						showSelectionRectDialog, showSelectionPolyDialog,
						showMapWindowDialog);

				gis.getViewer().getMapWindow().gotoLonLat(lon, lat);
				gis.getViewer().getMapWindow().zoom(zoom);
				gis.getViewer().repaint();
			}
		});

		/*
		 * Some testing stuff...
		 */

		/* Geometry Manager */

		String filename1 = "/tmp/berlin/Mitte/boroughs/10_Mitte.smx";
		String filename2 = "/tmp/berlin/Mitte/boroughs/10_Moabit.smx";

		GeometryManager geometryManager = gis.getGeometryManager();
		geometryManager
				.showTab(de.topobyte.jeography.viewer.geometry.manage.Tab.FILES);

		GeometryTree tree = gis.getGeometryManager().getTree();
		GeometryTreeModel treeModel = tree.getModel();

		Node root = (Node) treeModel.getByNamespace("");
		treeModel.addDirectory(root, "test");
		treeModel.addDirectory(root, "bar");

		Node test = (Node) treeModel.getByNamespace("test");
		treeModel.addFile(new File(filename1), test);
		Node bar = (Node) treeModel.getByNamespace("bar");
		treeModel.addFile(new File(filename2), bar);

		GeometryRules rules = gis.getGeometryManager().getRules();
		GeometryRulesModel rulesModel = rules.getModel();
		rulesModel.add(new GeometryRule("foo", "test", "Red",
				new ArrayList<GeometryRuleTag>()));
		rulesModel.add(new GeometryRule("xxx", "bar", "Blue",
				new ArrayList<GeometryRuleTag>()));

		/* Rectangular Selection */
		SelectionAdapter selectionAdapter = gis.getSelectionAdapter();
		selectionAdapter.setGeographicSelection(new GeographicSelection(
				5.3173828125, 15.3369140625, 55.4040698270061,
				46.980252355218816));

		/* Polygonal Selection */

		Selection selection = gis.getPolygonalSelectionAdapter().getSelection();

		Geometry geom1 = new WKTReader()
				.read("POLYGON ((8.1298828125 52.77618568896172, 7.58056640625 51.63165734944997, 9.33837890625 51.481382896100975, 9.84375 52.536273041459474, 8.1298828125 52.77618568896172))");
		Geometry geom2 = new WKTReader()
				.read("POLYGON ((9.1845703125 52.816043191549326, 10.48095703125 52.54963607438229, 9.82177734375 51.658926648800524, 11.865234375 52.496159531097106, 11.00830078125 53.05442186546102, 9.1845703125 52.816043191549326))");
		selection.add((LineString) geom1.getBoundary());
		selection.add((LineString) geom2.getBoundary());

		// gis.setMouseMode(MouseMode.DRAG);

		// String name = "transform";
		// ShowingOperationList list = new ShowingOperationList(
		// new UnionBufferEvaluator(), gis.getViewer());
		// ShowingOperationList list = new
		// ShowingBufferUnionList(gis.getViewer());
		// ShowingTransformList list = new ShowingTransformList(
		// gis.getViewer());
		// JDialog dialog = new JDialog(frame, name);
		// dialog.setContentPane(list);
		// dialog.pack();
		// dialog.setVisible(true);
	}

}
