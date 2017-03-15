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

package de.topobyte.jeography.viewer.geometry.list.measure;

import javax.swing.JFrame;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestMeasureList
{

	/**
	 * Test the component.
	 * 
	 * Display a JFrame containing just the MeasureList
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MeasureList measureList = new MeasureList();
		frame.setContentPane(measureList);

		frame.setVisible(true);
		frame.setSize(250, 500);
	}

}
