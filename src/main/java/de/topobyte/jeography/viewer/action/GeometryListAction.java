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

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.ShowingGeometryList;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListAction extends SimpleAction
{

	private static final long serialVersionUID = 8148758442262262122L;

	private final Viewer viewer;
	private final JComponent source;

	/**
	 * Public constructor.
	 * 
	 * @param viewer
	 *            the viewer to show geometries on.
	 * 
	 * @param source
	 *            the component to use for determining the frame to use as a
	 *            parent for the dialog to display
	 */
	public GeometryListAction(Viewer viewer, JComponent source)
	{
		this.viewer = viewer;
		this.source = source;

		setName("Geometry list");
		setDescription("show a list of geometries to display as overlay");
		setIconFromResource("res/images/polygonn.png");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ShowingGeometryList list = new ShowingGeometryList(viewer);

		JFrame frame = Components.getContainingFrame(source);
		JDialog dialog = new JDialog(frame, name);
		dialog.setContentPane(list);
		dialog.pack();
		dialog.setVisible(true);
	}

}
