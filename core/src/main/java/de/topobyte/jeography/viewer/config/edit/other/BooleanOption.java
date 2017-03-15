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

package de.topobyte.jeography.viewer.config.edit.other;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BooleanOption extends JPanel
{

	private static final long serialVersionUID = 370726320917504217L;

	private JCheckBox box;

	/**
	 * Create a component that shows a checkbox with label in a boxlayout.
	 * 
	 * @param title
	 *            the label of the checkbox
	 * @param state
	 *            the initial state of the checkbox
	 */
	public BooleanOption(String title, boolean state)
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);
		box = new JCheckBox(title);
		box.setSelected(state);
		add(box);
		// SwingHelper.setBorder(panel, Color.BLUE);
		// SwingHelper.setBorder(box, Color.BLUE);
	}

	/**
	 * @return the checkbox within the component
	 */
	public JCheckBox getCheckBox()
	{
		return box;
	}

}
