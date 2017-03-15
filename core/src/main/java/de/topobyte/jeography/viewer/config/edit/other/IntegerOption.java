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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class IntegerOption implements TwoComponentOption
{

	private JLabel label = new JLabel();
	private JTextField input = new JTextField();

	/**
	 * Create a new option for integer values
	 * 
	 * @param labelText
	 *            the text for the label
	 * @param value
	 *            the initial value in the input field.
	 */
	public IntegerOption(String labelText, int value)
	{
		label.setText(labelText);
		input.setText(String.format("%d", value));
	}

	/**
	 * @return the label.
	 */
	public JLabel getLabel()
	{
		return label;
	}

	/**
	 * @return the textfield.
	 */
	public JTextField getInput()
	{
		return input;
	}

	@Override
	public JComponent getFirstComponent()
	{
		return label;
	}

	@Override
	public JComponent getSecondComponent()
	{
		return input;
	}

	/**
	 * @return the current value
	 */
	public int getValue()
	{
		return Integer.parseInt(input.getText());
	}

}
