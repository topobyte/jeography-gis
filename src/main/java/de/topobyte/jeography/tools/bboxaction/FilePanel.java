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

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class FilePanel extends JPanel
{

	private static final long serialVersionUID = 6078194746110457046L;

	private File file;
	private JLabel label = new JLabel();

	public FilePanel(File file)
	{
		this.file = file;
		add(label);
		updateText();
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
		updateText();
	}

	private void updateText()
	{
		if (file == null) {
			label.setText("none");
		} else {
			label.setText(file.toString());
		}
	}

}
