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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.TreeCellEditor;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryCellEditor extends AbstractCellEditor implements
		TreeCellEditor
{

	// public class CellContainer extends Container {
	//
	// public CellContainer() {
	// setLayout(null);
	// }
	//
	// @Override
	// public Dimension getPreferredSize() {
	// return new Dimension(200, 20);
	// }
	//
	// public void doLayout() {
	// int width = getWidth();
	// int height = getHeight();
	//
	// int offset = 20;
	//
	// textField.setBounds(
	// offset, 0, width - offset, height);
	//
	// }
	//
	// public void paint(Graphics g) {
	// int width = getWidth();
	// int height = getHeight();
	//
	// // Border selection color
	// Color background = new Color(0x000000);
	// if (background != null) {
	// g.setColor(background);
	// g.drawRect(0, 0, width - 1, height - 1);
	// }
	// super.paint(g);
	// }
	//
	// }

	private static final long serialVersionUID = -3728440816616354346L;

	// private Container container = new CellContainer();

	private JTextField textField = new JTextField();

	/**
	 * Default constructor
	 */
	public GeometryCellEditor()
	{

		// container.add(textField);

		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					stopCellEditing();
				}
			}
		});
	}

	@Override
	public Object getCellEditorValue()
	{
		return textField.getText();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		if (anEvent instanceof MouseEvent) {
			return ((MouseEvent) anEvent).getClickCount() >= 3;
		}
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopCellEditing()
	{
		super.stopCellEditing();
		System.out.println("stop: " + textField.getText());
		fireEditingStopped();
		return true;
	}

	@Override
	public void cancelCellEditing()
	{
		super.cancelCellEditing();
		fireEditingCanceled();
		System.out.println("cancel: " + textField.getText());
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row)
	{
		// TODO Auto-generated method stub
		textField.setText(value.toString());
		int width = tree.getWidth();
		Container jsp = tree.getParent();
		if (jsp instanceof JViewport) {
			width = jsp.getWidth();
		}
		Dimension dim = textField.getPreferredSize();
		textField.setPreferredSize(new Dimension(width, (int) dim.getHeight()));
		// return container;
		return textField;
	}

}
