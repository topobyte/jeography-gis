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
 * @param <T>
 *            the type of x values.
 * @param <U>
 *            the type of y values.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Selection<T extends Comparable<T>, U extends Comparable<U>>
{

	private T x1, x2;
	private U y1, y2;

	/**
	 * Create a selection.
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
	public Selection(T x1, T x2, U y1, U y2)
	{
		if (x1.compareTo(x2) < 0) {
			this.x1 = x1;
			this.x2 = x2;
		} else {
			this.x1 = x2;
			this.x2 = x1;
		}

		if (y1.compareTo(y2) < 0) {
			this.y1 = y1;
			this.y2 = y2;
		} else {
			this.y1 = y2;
			this.y2 = y1;
		}

	}

	/**
	 * @return the first x value.
	 */
	public T getX1()
	{
		return x1;
	}

	/**
	 * @return the second x value.
	 */
	public T getX2()
	{
		return x2;
	}

	/**
	 * @return the first y value.
	 */
	public U getY1()
	{
		return y1;
	}

	/**
	 * @return the second y value.
	 */
	public U getY2()
	{
		return y2;
	}

	/**
	 * @param x1
	 *            the first x value.
	 */
	public void setX1(T x1)
	{
		this.x1 = x1;
	}

	/**
	 * @param x2
	 *            the second x value.
	 */
	public void setX2(T x2)
	{
		this.x2 = x2;
	}

	/**
	 * @param y1
	 *            the first y value.
	 */
	public void setY1(U y1)
	{
		this.y1 = y1;
	}

	/**
	 * @param y2
	 *            the second y value.
	 */
	public void setY2(U y2)
	{
		this.y2 = y2;
	}

	@Override
	public String toString()
	{
		StringBuilder strb = new StringBuilder();
		strb.append(x1);
		strb.append(",");
		strb.append(y1);
		strb.append(":");
		strb.append(x2);
		strb.append(",");
		strb.append(y2);
		return strb.toString();
	}

}
