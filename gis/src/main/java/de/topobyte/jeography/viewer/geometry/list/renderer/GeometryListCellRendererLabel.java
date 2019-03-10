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

package de.topobyte.jeography.viewer.geometry.list.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A renderer for the geometry list that simply displays a label for each entry.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListCellRendererLabel implements ListCellRenderer
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometryListCellRendererLabel.class);

	private JLabel label = new JLabel();

	/**
	 * Public constructor for the rendering component.
	 */
	public GeometryListCellRendererLabel()
	{
		label.setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{

		label.setComponentOrientation(list.getComponentOrientation());

		Color bg = null;
		Color fg = null;

		boolean selected = isSelected;

		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert()
				&& dropLocation.getIndex() == index) {

			bg = UIManager.getColor("List.dropCellBackground");
			fg = UIManager.getColor("List.dropCellForeground");

			selected = true;
		}

		if (selected) {
			logger.debug(list.getSelectionBackground().toString());
			logger.debug(list.getSelectionForeground().toString());
			label.setBackground(
					bg == null ? list.getSelectionBackground() : bg);
			label.setForeground(
					fg == null ? list.getSelectionForeground() : fg);
		} else {
			label.setBackground(list.getBackground());
			label.setForeground(list.getForeground());
		}

		if (value instanceof Icon) {
			label.setIcon((Icon) value);
			label.setText("");
		} else {
			label.setIcon(null);
			label.setText((value == null) ? "" : value.toString());
		}

		label.setEnabled(list.isEnabled());
		label.setFont(list.getFont());

		Border border = null;
		if (cellHasFocus) {
			if (selected) {
				border = UIManager
						.getBorder("List.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = UIManager.getBorder("List.focusCellHighlightBorder");
			}
		} else {
			border = UIManager.getBorder("List.cellNoFocusBorder");
		}
		label.setBorder(border);

		return label;
	}

}
