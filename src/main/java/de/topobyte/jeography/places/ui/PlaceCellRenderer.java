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

package de.topobyte.jeography.places.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.jeography.places.model.Place;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PlaceCellRenderer extends DefaultListCellRenderer
{

	private static final long serialVersionUID = -1356221737689189480L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

		Place place = (Place) value;

		JLabel labelName = new JLabel(toString(place));
		JLabel labelMeta = new JLabel(String.format("%.3f %.3f",
				place.getLon(), place.getLat()));

		labelMeta.setFont(labelMeta.getFont().deriveFont(Font.PLAIN));

		GridBagConstraintsEditor c = new GridBagConstraintsEditor();
		c.anchor(GridBagConstraints.LINE_START);
		c.fill(GridBagConstraints.BOTH);
		c.weightX(1);

		c.gridX(0);
		c.gridY(1);
		panel.add(labelName, c.getConstraints());
		c.gridY(2);
		panel.add(labelMeta, c.getConstraints());

		panel.setOpaque(false);
		if (isSelected) {
			panel.setOpaque(true);
			panel.setBackground(list.getSelectionBackground());
		}

		return panel;
	}

	public String toString(Place element)
	{
		StringBuilder buffer = new StringBuilder();
		Set<String> names = new HashSet<>();
		names.add(element.getName());
		buffer.append(element.getName());

		int n = 0;
		for (String name : element.getAltNames().values()) {
			if (!names.contains(name)) {
				if (n++ == 0) {
					buffer.append(" (");
				} else {
					buffer.append(", ");
				}
				names.add(name);
				buffer.append(name);
			}
		}
		if (n > 0) {
			buffer.append(")");
		}

		return buffer.toString();
	}

}
