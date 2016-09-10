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

package de.topobyte.jeography.tools.bboxaction;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class SelectFileAction extends AbstractAction implements Action
{

	private static final long serialVersionUID = 5673802417623778337L;

	private File file;
	private final Component component;

	public SelectFileAction(File file, Component component)
	{
		this.file = file;
		this.component = component;
		putValue(Action.NAME, "select");
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser(file.getParentFile());
		int ret = fc.showOpenDialog(component);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			fileSelected(file);
		}
	}

	public abstract void fileSelected(File file);

}
