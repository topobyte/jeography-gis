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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometrySourceTGS implements GeometrySource
{

	private final String filename;
	private final int n;

	/**
	 * Create a new source for a geometry that has been read from a tagged
	 * geometry set.
	 * 
	 * @param filename
	 *            the name of the file.
	 * @param n
	 *            the index of the geometry within the file.
	 */
	public GeometrySourceTGS(String filename, int n)
	{
		this.filename = filename;
		this.n = n;
	}

	@Override
	public String getInfo()
	{
		return filename + " (" + n + ")";
	}

}
