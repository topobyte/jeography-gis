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

package de.topobyte.jeography.viewer.gotolocation;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.swing.util.BorderHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SupportedFormatsDialog extends JDialog
{

	private static final long serialVersionUID = 4225144550694153999L;

	public SupportedFormatsDialog(Frame owner)
	{
		super(owner);
		init();
	}

	public SupportedFormatsDialog(Window owner)
	{
		super(owner);
		init();
	}

	private void init()
	{
		setTitle("Supported formats");

		JPanel panel = new JPanel(new GridBagLayout());
		setContentPane(panel);

		GridBagConstraintsEditor c = new GridBagConstraintsEditor();
		c.anchor(GridBagConstraints.LINE_START);
		c.gridPos(0, 0);

		JLabel label = new JLabel("The following formats can be parsed:");
		BorderHelper.addEmptyBorder(label, 5, 5, 10, 5);
		panel.add(label, c.getConstraints());

		String[] formats = {
				"lat,lon → e.g. 33.810398,-117.921046",
				"[http[s]://][www.]openstreetmap.org/#map=15/33.810398/-117.921046",
				"[http[s]://][www.]openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"[http[s]://]osm.org/go/21ABCD--", };
		for (String format : formats) {
			JLabel l = new JLabel(format);
			BorderHelper.addEmptyBorder(l, 5, 5, 5, 5);
			c.gridY(c.getConstraints().gridy + 1);
			panel.add(l, c.getConstraints());
		}
	}
}
