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

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.topobyte.swing.util.ImageLoader;

public class TestButtonWithDropdown
{

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Test button with dropdown");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.setContentPane(panel);

		Icon icon = ImageLoader.load("res/images/16/stock_bookmark.png");

		final JPopupMenu menu = new JPopupMenu();
		menu.add("foo");
		menu.add("bar");

		ButtonWithDropdown button = new ButtonWithDropdown(icon, menu);
		button.setMargin(new Insets(0, 0, 0, 0));
		panel.add(button);

		frame.setSize(500, 400);
		frame.setVisible(true);
	}

}