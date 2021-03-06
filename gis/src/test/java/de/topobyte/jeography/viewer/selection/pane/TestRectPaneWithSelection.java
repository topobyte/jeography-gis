// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.selection.pane;

import javax.swing.JDialog;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.TestUtil;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestRectPaneWithSelection
{

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
		SelectionAdapter selectionAdapter = new SelectionAdapter(
				gis.getViewer());
		selectionAdapter.setGeographicSelection(
				new GeographicSelection(13.15, 13.66, 52.64, 52.39));

		RectPane pane = new RectPane(gis, selectionAdapter);

		JDialog dialog = new JDialog();

		dialog.setContentPane(pane);

		dialog.pack();
		dialog.setVisible(true);

		TestUtil.exitOnClose(dialog);
	}

}
