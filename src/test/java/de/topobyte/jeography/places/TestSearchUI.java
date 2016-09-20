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

package de.topobyte.jeography.places;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;

import de.topobyte.jeography.places.model.Place;
import de.topobyte.jeography.places.ui.PlaceActivationListener;
import de.topobyte.jeography.places.ui.SearchUI;
import de.topobyte.luqe.iface.QueryException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestSearchUI
{

	public static void main(String args[]) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			QueryException
	{
		Path pathDatabase = Paths.get("/tmp/places.sqlite");

		Class.forName("org.sqlite.JDBC").newInstance();
		String url = "jdbc:sqlite:" + pathDatabase;
		Connection connex = DriverManager.getConnection(url);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SearchUI searchUI = new SearchUI(connex);
		frame.setContentPane(searchUI);

		frame.pack();
		frame.setVisible(true);

		searchUI.addPlaceActivationListener(new PlaceActivationListener() {

			@Override
			public void placeActivated(Place place)
			{
				System.out.println(String.format("activated: %s, %f %f",
						place.getName(), place.getLon(), place.getLat()));
			}
		});
	}

}