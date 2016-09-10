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

package de.topobyte.jeography.viewer.geometry.manage;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bric.swing.ColorPicker;

import de.topobyte.chromaticity.AwtColors;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.swing.util.ButtonPane;
import de.topobyte.swing.util.combobox.BooleanComboBoxModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class EditGeometryStyleDialog extends JDialog implements
		DocumentListener
{

	private static final long serialVersionUID = -2733578787051285160L;

	private final GeometryStyle style;
	private final GeometryStylesModel model;

	boolean accept = false;

	private JButton buttonOk;
	private JTextField fieldName;
	private JTextField fieldLineWidth;
	private JComboBox<Boolean> comboDrawNodes;
	private ColorPicker picker1;
	private ColorPicker picker2;

	private BooleanComboBoxModel drawNodesBoxModel;

	/**
	 * Create an edit dialog for the denoted style.
	 * 
	 * @param model
	 *            the list model of geometry styles.
	 * 
	 * @param style
	 *            the style to edit.
	 */
	public EditGeometryStyleDialog(GeometryStylesModel model,
			GeometryStyle style)
	{
		this.model = model;
		this.style = style;
		ColorCode colorFill = style.getColorFill();
		ColorCode colorStroke = style.getColorStroke();

		JPanel panel = new JPanel(new GridBagLayout());
		setContentPane(panel);

		List<JButton> buttonList = new ArrayList<>();
		buttonOk = new JButton("Ok");
		JButton buttonCancel = new JButton("Cancel");
		buttonList.add(buttonOk);
		buttonList.add(buttonCancel);
		ButtonPane buttons = new ButtonPane(buttonList);

		JLabel label1 = new JLabel("Fill:");
		JLabel label2 = new JLabel("Outline:");

		picker1 = new ColorPicker(true, true);

		picker2 = new ColorPicker(true, true);
		picker1.setColor(AwtColors.convert(colorFill));
		picker2.setColor(AwtColors.convert(colorStroke));

		picker1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		picker2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		fieldName = new JTextField(style.getName());
		fieldLineWidth = new JTextField("" + style.getLineWidth());
		drawNodesBoxModel = new BooleanComboBoxModel(style.isDrawNodes());
		comboDrawNodes = new JComboBox<>(drawNodesBoxModel);
		fieldName.getDocument().addDocumentListener(this);
		fieldLineWidth.getDocument().addDocumentListener(this);

		GridBagConstraints c = new GridBagConstraints();

		JPanel boxLine = new JPanel(new GridBagLayout());
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		boxLine.add(new JLabel("name:"), c);
		c.gridy = 1;
		boxLine.add(new JLabel("line width:"), c);
		c.gridy = 2;
		boxLine.add(new JLabel("draw nodes:"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		boxLine.add(fieldName, c);
		c.gridy = 1;
		boxLine.add(fieldLineWidth, c);
		c.gridy = 2;
		boxLine.add(comboDrawNodes, c);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		panel.add(boxLine, c);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		panel.add(label1, c);
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(picker1, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(label2, c);
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(picker2, c);
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.EAST;
		panel.add(buttons, c);

		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				accept = true;
				dispose();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				accept = false;
				dispose();
			}
		});

		checkValidityAndUpdateGui();
	}

	/**
	 * @return the line width for outline.
	 */
	public double getLineWidth()
	{
		String value = fieldLineWidth.getText();
		return Double.parseDouble(value);
	}

	/**
	 * @return whether to draw nodes.
	 */
	public boolean getDrawNodes()
	{
		return (Boolean) drawNodesBoxModel.getSelectedItem();
	}

	/**
	 * @return the color for fillage.
	 */
	public ColorCode getColorFill()
	{
		return AwtColors.convert(picker1.getColor());
	}

	/**
	 * @return the color for outlines.
	 */
	public ColorCode getColorOutline()
	{
		return AwtColors.convert(picker2.getColor());
	}

	/**
	 * Show the dialog in a modal fashion.
	 * 
	 * @return JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION.
	 */
	public int showDialog()
	{
		pack();
		setModal(true);
		setVisible(true);
		return accept ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
	}

	private static final Color green = new Color(0xaa00ff00, true);
	private static final Color red = new Color(0xaaff0000, true);

	private Border getBorder(boolean valid)
	{
		Border b1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border b2 = BorderFactory.createLineBorder(valid ? green : red);
		Border b3 = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		return BorderFactory.createCompoundBorder(b3,
				BorderFactory.createCompoundBorder(b2, b1));
	}

	private boolean checkValidity()
	{
		return checkName() && checkLineWidth();
	}

	private boolean checkName()
	{
		String value = fieldName.getText();
		boolean valid = value.equals(style.getName());
		valid |= model.getStyleByName(value) == null;
		valid &= !value.isEmpty();
		return valid;
	}

	private boolean checkLineWidth()
	{
		String value = fieldLineWidth.getText();
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	void checkValidityAndUpdateGui()
	{
		buttonOk.setEnabled(checkValidity());
		fieldName.setBorder(getBorder(checkName()));
		fieldLineWidth.setBorder(getBorder(checkLineWidth()));
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		checkValidityAndUpdateGui();
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		checkValidityAndUpdateGui();
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		checkValidityAndUpdateGui();
	}

}
