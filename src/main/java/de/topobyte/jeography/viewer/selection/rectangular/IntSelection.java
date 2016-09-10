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

package de.topobyte.jeography.viewer.selection.rectangular;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class IntSelection extends Selection<Integer, Integer>
{

	/**
	 * Create new integer selection.
	 * 
	 * @param x1
	 *            the first x value.
	 * @param x2
	 *            the second x value.
	 * @param y1
	 *            the first y value.
	 * @param y2
	 *            the second y value.
	 */
	public IntSelection(int x1, int x2, int y1, int y2)
	{
		super(x1, x2, y1, y2);
	}

	/**
	 * @return the width of the selection.
	 */
	public int getWidth()
	{
		return getX2() - getX1();
	}

	/**
	 * @return the height of the selection.
	 */
	public int getHeight()
	{
		return getY2() - getY1();
	}

}
