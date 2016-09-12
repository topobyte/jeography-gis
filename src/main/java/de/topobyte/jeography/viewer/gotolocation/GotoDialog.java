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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.swing.util.DocumentAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GotoDialog extends JDialog
{

	private static final long serialVersionUID = -2940670294533100693L;

	private static final String MESSAGE_NOTHING_YET = "nothing yet";
	private static final String MESSAGE_UNABLE_TO_PARSE = "unable to parse";

	private JTextField field;
	private JButton button;
	private JLabel recognized;

	public GotoDialog()
	{
		setTitle("Go To");

		JPanel panel = new JPanel(new GridBagLayout());
		add(panel);

		JLabel text = new JLabel("Paste something here:");
		field = new JTextField();
		recognized = new JLabel(MESSAGE_NOTHING_YET);
		button = new JButton("Go");

		field.setColumns(50);

		GridBagConstraintsEditor c = new GridBagConstraintsEditor();
		c.fill(GridBagConstraints.BOTH);
		c.anchor(GridBagConstraints.LINE_START);

		// Label and input field
		c.gridWidth(2);

		// first row: label
		c.weight(1.0, 0.0);
		c.gridPos(0, 0);
		panel.add(text, c.getConstraints());

		// next row: input
		c.weight(1.0, 1.0);
		c.gridPos(0, 1);
		panel.add(field, c.getConstraints());

		// next row: recognized + button
		c.gridWidth(1);

		c.weight(1.0, 0.0);
		c.gridPos(0, 2);
		panel.add(recognized, c.getConstraints());
		c.weightX(0);
		c.gridPos(1, 2);
		panel.add(button, c.getConstraints());

		field.getDocument().addDocumentListener(new DocumentAdapter() {

			@Override
			public void update(DocumentEvent e)
			{
				tryToParse();
			}

		});
	}

	private List<PatternRecognizer> recognizers = new ArrayList<>();
	{
		recognizers.add(new PatternRecognizerOsm());
	}

	private void tryToParse()
	{
		String text = field.getText();

		Location location = null;

		boolean valid = false;
		if (!text.isEmpty()) {
			for (PatternRecognizer recognizer : recognizers) {
				location = recognizer.parse(text);
				if (location != null) {
					valid = true;
					break;
				}
			}
		}

		button.setEnabled(valid);

		if (text.isEmpty()) {
			recognized.setText(MESSAGE_NOTHING_YET);
		} else if (!valid) {
			recognized.setText(MESSAGE_UNABLE_TO_PARSE);
		} else {
			recognized.setText(location.toString());
		}
	}

}