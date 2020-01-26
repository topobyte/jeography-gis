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

package de.topobyte.jeography.viewer.bookmarks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.locationtech.jts.geom.Coordinate;

import de.topobyte.awt.util.GridBagConstraintsEditor;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class BookmarksRenderer extends JPanel
		implements ListCellRenderer<Bookmark>
{

	private static final long serialVersionUID = 6406286243991965045L;

	private JLabel labelName = new JLabel();
	private JLabel labelCoord = new JLabel();

	public BookmarksRenderer()
	{
		Font f = labelCoord.getFont();
		labelCoord.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraintsEditor editor = new GridBagConstraintsEditor(c);

		editor.fill(GridBagConstraints.HORIZONTAL).weightX(1.0);

		editor.gridX(0).gridY(0).weightX(1.0).weightY(1.0);
		add(labelName, c);
		editor.gridX(0).gridY(1).weightX(1.0).weightY(1.0);
		add(labelCoord, c);
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Bookmark> list, Bookmark value, int index,
			boolean isSelected, boolean cellHasFocus)
	{

		Bookmark bookmark = value;

		labelName.setText(bookmark.getName());
		Coordinate c = bookmark.getCoordinate();
		labelCoord.setText(String.format("%.3f,%.3f", c.x, c.y));

		Color background;
		Color foreground;

		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert()
				&& dropLocation.getIndex() == index) {
			background = Color.BLUE;
			foreground = Color.WHITE;
		} else if (isSelected) {
			background = UIManager.getColor("List.selectionBackground");
			foreground = UIManager.getColor("List.selectionForeground");
		} else {
			background = Color.WHITE;
			foreground = Color.BLACK;
		}

		setBackground(background);
		setForeground(foreground);

		labelName.setForeground(foreground);
		labelCoord.setForeground(foreground);

		return this;
	}

}
