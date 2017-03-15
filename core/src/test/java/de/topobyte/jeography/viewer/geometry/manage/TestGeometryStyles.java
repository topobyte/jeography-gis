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

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestGeometryStyles
{

	/**
	 * Simple test to show this list.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeometryStyles styles = new GeometryStyles();
		frame.setContentPane(styles);
		frame.setSize(new Dimension(400, 600));
		frame.setVisible(true);
	}

}
