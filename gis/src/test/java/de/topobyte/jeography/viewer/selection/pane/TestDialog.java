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

package de.topobyte.jeography.viewer.selection.pane;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.LinearRing;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.TestUtil;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.selection.polygonal.PolySelectionAdapter;
import de.topobyte.jeography.viewer.selection.polygonal.Selection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.jts.utils.JtsHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestDialog
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
		SelectionAdapter selectionAdapter = new SelectionAdapter(
				gis.getViewer());
		PolySelectionAdapter polySelectionAdapter = new PolySelectionAdapter(
				gis.getViewer());

		Dialog dialog = new Dialog(null, "Selection Panel", gis,
				selectionAdapter, polySelectionAdapter);
		dialog.setSize(500, 400);
		dialog.pack();
		dialog.setVisible(true);
		TestUtil.exitOnClose(dialog);

		dialog.showTab(Tab.RectPane);

		Selection selection = polySelectionAdapter.getSelection();
		List<Double> xs = new ArrayList<>();
		List<Double> ys = new ArrayList<>();
		xs.add(11.2335);
		xs.add(11.2720);
		xs.add(11.9476);
		xs.add(11.8762);
		ys.add(48.2466);
		ys.add(47.9752);
		ys.add(47.9900);
		ys.add(48.3416);
		LinearRing ring = JtsHelper.toLinearRing(xs, ys, false);
		selection.add(ring);
	}

}
