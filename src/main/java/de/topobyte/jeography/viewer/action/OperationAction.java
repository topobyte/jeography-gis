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

import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationListFactory;
import de.topobyte.jeography.viewer.geometry.list.operation.Operations;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OperationAction extends SimpleAction
{

	private static final long serialVersionUID = 3670557874516545808L;

	private final Operations operation;
	private final JComponent source;

	/**
	 * Public constructor.
	 * 
	 * @param operation
	 *            the operation to open a new dialog for.
	 * @param source
	 *            the component to use for determining the frame to use as a
	 *            parent for the dialog to display
	 */
	public OperationAction(Operations operation, JComponent source)
	{
		this.operation = operation;
		this.source = source;

		String newIcon = null;

		switch (operation) {
		case DIFFERENCE:
			setName("Difference");
			setDescription("difference operation");
			newIcon = "res/images/geometryOperation/difference.png";
			break;
		case INTERSECTION:
			setName("Intersection");
			setDescription("intersection operation");
			newIcon = "res/images/geometryOperation/intersection.png";
			break;
		case UNION:
			setName("Union");
			setDescription("union operation");
			newIcon = "res/images/geometryOperation/union.png";
			break;
		case COLLECTION:
			setName("Collection");
			setDescription("collection operation");
			newIcon = "res/images/geometryOperation/collection.png";
			break;
		case HULL:
			setName("Convex Hull");
			setDescription("convex hull operation");
			newIcon = "res/images/geometryOperation/collection.png";
			break;
		}

		setIconFromResource(newIcon);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		OperationList list = OperationListFactory
				.createOperationList(operation);

		JFrame frame = Components.getContainingFrame(source);
		JDialog dialog = new JDialog(frame, name);
		dialog.setContentPane(list);
		dialog.pack();
		dialog.setVisible(true);
	}

}
