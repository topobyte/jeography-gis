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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.jeography.viewer.util.Borders;
import de.topobyte.jeography.viewer.util.DefaultComponentTraversalPolicy;
import de.topobyte.swing.util.BorderHelper;
import de.topobyte.swing.util.ComponentPanel;
import de.topobyte.swing.util.DocumentAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GotoDialog extends JDialog
{

	private static final long serialVersionUID = -2940670294533100693L;

	private static final String MESSAGE_NOTHING_YET = "nothing yet";
	private static final String MESSAGE_UNABLE_TO_PARSE = "unable to parse";

	private ComponentPanel<JTextField> field;
	private ComponentPanel<JButton> button;
	private JLabel recognized;
	private ComponentPanel<JButton> buttonHelp;

	// The parsed location
	private Location location = null;

	private GotoListener listener;

	public GotoDialog(Frame owner)
	{
		super(owner);
		setTitle("Go To");

		JPanel panel = new JPanel(new GridBagLayout());
		setContentPane(panel);

		JLabel text = new JLabel("Paste something here:");
		field = new ComponentPanel<>(new JTextField());
		recognized = new JLabel(MESSAGE_NOTHING_YET);
		button = new ComponentPanel<>(new JButton("Go"));
		buttonHelp = new ComponentPanel<>(new JButton("?"));

		BorderHelper.addEmptyBorder(text, 5);
		BorderHelper.addEmptyBorder(recognized, 5);
		BorderHelper.addEmptyBorder(field, 5);
		BorderHelper.addEmptyBorder(button, 5);
		BorderHelper.addEmptyBorder(buttonHelp, 5);

		field.getComponent().setColumns(50);
		field.getComponent().setBorder(Borders.validityBorder(false));
		button.setEnabled(false);

		GridBagConstraintsEditor c = new GridBagConstraintsEditor();
		c.fill(GridBagConstraints.BOTH);
		c.anchor(GridBagConstraints.LINE_START);

		// Label and input field

		// first row: label
		c.gridWidth(1);

		c.weight(1.0, 0.0);
		c.gridPos(0, 0);
		panel.add(text, c.getConstraints());
		c.weight(0.0, 0.0);
		c.gridPos(1, 0);
		panel.add(buttonHelp, c.getConstraints());

		// next row: input
		c.gridWidth(2);

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

		field.getComponent().getDocument()
				.addDocumentListener(new DocumentAdapter() {

					@Override
					public void update(DocumentEvent e)
					{
						tryToParse();
					}

				});

		setFocusTraversalPolicy(new DefaultComponentTraversalPolicy(
				field.getComponent()));

		ActionListener fireAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (location != null) {
					fireListener();
				}
			}

		};

		button.getComponent().addActionListener(fireAction);
		field.getComponent().addActionListener(fireAction);

		buttonHelp.getComponent().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				SupportedFormatsDialog dialog = new SupportedFormatsDialog(
						getOwner());
				dialog.pack();
				dialog.setVisible(true);
			}
		});
	}

	public void setGotoListener(GotoListener listener)
	{
		this.listener = listener;
	}

	protected void fireListener()
	{
		if (listener == null) {
			return;
		}
		listener.gotoLocation(location);
	}

	private List<PatternRecognizer> recognizers = new ArrayList<>();
	{
		recognizers.add(new PatternRecognizerLonLat());
		recognizers.add(new PatternRecognizerOsm());
		recognizers.add(new PatternRecognizerOsm2());
	}

	private void tryToParse()
	{
		String text = field.getComponent().getText();

		location = null;

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
		field.getComponent().setBorder(Borders.validityBorder(valid));

		if (text.isEmpty()) {
			recognized.setText(MESSAGE_NOTHING_YET);
		} else if (!valid) {
			recognized.setText(MESSAGE_UNABLE_TO_PARSE);
		} else {
			recognized.setText(location.toString());
		}
	}

}
