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

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A renderer for the geometry list that displays a panel with detailed
 * information for each entry.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryListCellRendererPanel implements ListCellRenderer<Geometry>
{

	GeometryPanel panel = new GeometryPanel();

	/**
	 * Public constructor for the rendering component.
	 */
	public GeometryListCellRendererPanel()
	{
		panel.setOpaque(true);
		panel.setBackground(new Color(0xff0000));
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Geometry> list, Geometry value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		panel.setupAndInit(value);

		panel.setComponentOrientation(list.getComponentOrientation());

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

		boolean even = index % 2 == 0;

		if (selected) {
			panel.setBackground(
					bg == null ? list.getSelectionBackground() : bg);
			panel.setForeground(
					fg == null ? list.getSelectionForeground() : fg);
		} else {
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
		}

		if (even) {
			Color color = panel.getBackground();
			panel.setBackground(changeColor(color));
		}

		// if (value instanceof Icon) {
		// label1.setIcon((Icon) value);
		// label1.setText("");
		// } else {
		// label1.setIcon(null);
		// label1.setText((value == null) ? "" : value.toString());
		// }

		panel.setEnabled(list.isEnabled());
		panel.setFont(list.getFont());

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
		panel.setBorder(border);

		return panel;
	}

	private Color changeColor(Color color)
	{
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		int value = 10;
		int sum = r + g + b;
		int alteration = sum > 384 ? -value : value;

		r += alteration;
		g += alteration;
		b += alteration;

		return new Color(r, g, b);
	}

}
