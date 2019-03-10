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

package de.topobyte.jeography.viewer.geometry.list;

import javax.swing.JDialog;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.TestUtil;
import de.topobyte.jeography.viewer.config.Configuration;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestShowingGeometryList
{

	/**
	 * Test for the list
	 * 
	 * @param args
	 *            none
	 * @throws Exception
	 *             on failure
	 */
	public static void main(String[] args) throws Exception
	{
		int zoom = 6;
		double lon = 10.9, lat = 51.5;

		Configuration configuration = Configuration
				.createDefaultConfiguration();

		JeographyGIS gis = new JeographyGIS(null, configuration, 0, null, true,
				false, false, false, false);

		gis.getViewer().getMapWindow().gotoLonLat(lon, lat);
		gis.getViewer().getMapWindow().zoom(zoom);
		gis.getViewer().repaint();

		JDialog dialog = new JDialog();
		TestUtil.exitOnClose(dialog);

		ShowingGeometryList showingGeometryList = new ShowingGeometryList(
				gis.getViewer());
		dialog.setContentPane(showingGeometryList);
		dialog.pack();
		dialog.setVisible(true);
	}

}
