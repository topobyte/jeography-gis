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

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.topobyte.adt.geo.BBox;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BboxPanel extends JPanel
{

	private static final long serialVersionUID = 6078194746110457046L;

	private BBox bbox;
	private JLabel label = new JLabel();

	public BboxPanel(BBox bbox)
	{
		this.bbox = bbox;
		add(label);
		updateText();
	}

	public BBox getBoundingBox()
	{
		return bbox;
	}

	public void setBoundingBox(BBox bbox)
	{
		this.bbox = bbox;
		updateText();
	}

	private void updateText()
	{
		if (bbox == null) {
			label.setText("none");
		} else {
			label.setText(bbox.toString());
		}
	}

}
