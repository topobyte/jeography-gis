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

import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class DialogAction extends SimpleAction
{

	private static final long serialVersionUID = 3670557874516545808L;

	private final JComponent source;

	/**
	 * Public constructor.
	 * 
	 * @param source
	 *            the component to use for determining the frame to use as a
	 *            parent for the dialog to display
	 * @param icon
	 *            the icon to use in menus etc.
	 * @param title
	 *            the title of the action.
	 * @param description
	 *            the description of the action.
	 */
	public DialogAction(JComponent source, String icon, String title,
			String description)
	{
		this.source = source;

		setIconFromResource(icon);
		setName(title);
		setDescription(description);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JComponent panel = createDialog();

		JFrame frame = Components.getContainingFrame(source);
		JDialog dialog = new JDialog(frame, name);
		dialog.setContentPane(panel);
		dialog.pack();
		dialog.setVisible(true);
	}

	/**
	 * This is the method an extending class has to implement to provide a
	 * Dialog to be displayed.
	 * 
	 * @return the component that will be shown in a dialog.
	 */
	protected abstract JComponent createDialog();

}
