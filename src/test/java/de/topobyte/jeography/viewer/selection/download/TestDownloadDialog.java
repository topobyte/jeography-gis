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

package de.topobyte.jeography.viewer.selection.download;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestDownloadDialog
{

	/**
	 * Test the dialog
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		Configuration configuration = Configuration
				.createDefaultConfiguration();
		JeographyGIS gis = null;
		try {
			gis = new JeographyGIS(null, configuration, 0, null, true, false,
					false, false, false);
		} catch (Exception e) {
			return;
		}
		SelectionAdapter selectionAdapter = gis.getSelectionAdapter();
		selectionAdapter.setGeographicSelection(new GeographicSelection(
				12.969360, 13.840027, 52.703019, 52.305120));

		DownloadDialog dialog = new DownloadDialog(gis, null, "download dialog");
		dialog.setSize(500, 400);
		dialog.pack();
		dialog.setVisible(true);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event)
			{
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent event)
			{
				System.exit(0);
			}
		});
	}

}
