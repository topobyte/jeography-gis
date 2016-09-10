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

package de.topobyte.jeography.viewer.geometry.manage.filetree;

import java.io.File;

/**
 * A class representing a leaf within the file tree.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Leaf extends Entry
{

	private static int idFactory = 1;

	private int id;
	private File file;

	/**
	 * Create a new Leaf that is a child of the denoted parent with the denoted
	 * file.
	 * 
	 * @param parent
	 *            the parent node this leaf is a child of.
	 * @param file
	 *            the file represented.
	 */
	public Leaf(Node parent, File file)
	{
		super(parent);
		this.file = file;
		id = idFactory++;
	}

	/**
	 * @return the unique identifier of this leaf.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the file represented by this leaf.
	 */
	public File getFile()
	{
		return file;
	}

	@Override
	public String toString()
	{
		return file.getName();
	}

}
