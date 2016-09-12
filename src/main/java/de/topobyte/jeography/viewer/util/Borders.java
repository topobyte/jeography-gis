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

package de.topobyte.jeography.viewer.util;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Borders
{

	private static final Color green = new Color(0xaa00ff00, true);
	private static final Color red = new Color(0xaaff0000, true);

	public static Border validityBorder(boolean valid)
	{
		Border b1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border b2 = BorderFactory.createLineBorder(valid ? green : red);
		Border b3 = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		return BorderFactory.createCompoundBorder(b3,
				BorderFactory.createCompoundBorder(b2, b1));
	}

}
