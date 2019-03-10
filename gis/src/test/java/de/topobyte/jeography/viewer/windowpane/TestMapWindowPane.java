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

package de.topobyte.jeography.viewer.windowpane;

import javax.swing.JDialog;

import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.viewer.TestUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestMapWindowPane
{

	/**
	 * Simple test for the dialog
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String args[])
	{
		JDialog dialog = new JDialog();

		MapWindow window = new SteppedMapWindow(300, 300, 14, 400, 400, 0, 0);
		MapWindowPane pane = new MapWindowPane(window);
		dialog.setContentPane(pane);

		dialog.setSize(300, 200);
		dialog.setVisible(true);

		TestUtil.exitOnClose(dialog);
	}

}
