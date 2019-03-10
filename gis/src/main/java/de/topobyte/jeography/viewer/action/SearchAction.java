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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.places.model.Place;
import de.topobyte.jeography.places.ui.PlaceActivationListener;
import de.topobyte.jeography.places.ui.SearchUI;
import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.JdbcConnection;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SearchAction extends GISAction
{

	private static final long serialVersionUID = 976554850069652664L;

	final static Logger logger = LoggerFactory.getLogger(SearchAction.class);

	/**
	 * Create an action for showing the search dialog
	 * 
	 * @param gis
	 *            the JeographyGIS instance this action is for.
	 */
	public SearchAction(JeographyGIS gis)
	{
		super(gis, "res/images/edit-find.png");
		setName("search");
		setDescription("search for places");
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Path pathDatabase = getGIS().getConfiguration().getPathDatabase();

		if (!Files.exists(pathDatabase)) {
			logger.error(
					String.format("File does not exists '%s'", pathDatabase));
			// TODO: display error dialog
			return;
		}

		if (!Files.isRegularFile(pathDatabase)) {
			logger.error(
					String.format("Not a regular file '%s'", pathDatabase));
			// TODO: display error dialog
			return;
		}

		try {
			Class.forName("org.sqlite.JDBC").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			logger.error("unable to load SQLite driver", e);
			return;
		}

		String url = "jdbc:sqlite:" + pathDatabase;

		Connection connex = null;
		try {
			connex = DriverManager.getConnection(url);
		} catch (SQLException e) {
			logger.error("unable to create connection", e);
			return;
		}

		IConnection connection;
		try {
			connection = new JdbcConnection(connex);
		} catch (SQLException e) {
			logger.error("unable to create jdbc connection wrapper", e);
			return;
		}

		SearchUI searchUI;
		try {
			searchUI = new SearchUI(connection);
		} catch (QueryException e) {
			logger.error("unable to initialize search UI", e);
			return;
		}

		JFrame frame = getMainFrame();

		JDialog dialog = new JDialog(frame);
		dialog.setTitle("Search");
		dialog.setContentPane(searchUI);
		dialog.pack();

		dialog.setVisible(true);

		searchUI.addPlaceActivationListener(new PlaceActivationListener() {

			@Override
			public void placeActivated(Place place)
			{
				logger.info(String.format("activated: %s, %f %f",
						place.getName(), place.getLon(), place.getLat()));

				Viewer viewer = getGIS().getViewer();
				viewer.getMapWindow().gotoLonLat(place.getLon(),
						place.getLat());
				viewer.repaint();
			}
		});
	}

}
