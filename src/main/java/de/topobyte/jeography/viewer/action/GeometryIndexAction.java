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

import de.topobyte.jeography.viewer.geometry.list.index.IndexList;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryIndexAction extends SimpleAction
{

	private static final long serialVersionUID = 8831096225965762349L;

	private final JComponent source;

	/**
	 * Public constructor.
	 * 
	 * @param source
	 *            the component to use for determining the frame to use as a
	 *            parent for the dialog to display
	 */
	public GeometryIndexAction(JComponent source)
	{
		this.source = source;

		setName("Geometry Index creator");
		setDescription("create a spatial index from a collection of geometries");
		setIconFromResource("res/images/polygonn.png");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		IndexList list = new IndexList();

		JFrame frame = Components.getContainingFrame(source);
		JDialog dialog = new JDialog(frame, name);
		dialog.setContentPane(list);
		dialog.pack();
		dialog.setVisible(true);
	}

}
