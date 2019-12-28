// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

public class ButtonWithDropdown extends JButton
{

	private static final long serialVersionUID = 1L;

	private JPopupMenu menu;

	private BasicArrowButton dropdownButton;

	public ButtonWithDropdown(Icon icon, JPopupMenu menu)
	{
		this.menu = menu;
		init(icon);
	}

	public ButtonWithDropdown(Action action, Icon icon, JPopupMenu menu)
	{
		this.menu = menu;
		init(icon);
		setAction(action);
		setIcon(null);
		setText(null);
	}

	private void init(Icon icon)
	{
		setLayout(new BorderLayout());
		add(new JLabel(icon), BorderLayout.CENTER);

		dropdownButton = new BasicArrowButton(SwingConstants.SOUTH, null,
				Color.GRAY, Color.BLACK, Color.WHITE);
		dropdownButton.setMargin(new Insets(0, 0, 0, 0));
		add(dropdownButton, BorderLayout.EAST);

		dropdownButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e)
			{
				menu.show(ButtonWithDropdown.this, 0, getHeight());
			}

		});
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		dropdownButton.setEnabled(b);
	}

}
