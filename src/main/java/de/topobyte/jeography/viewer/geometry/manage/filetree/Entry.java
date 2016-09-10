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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreePath;

/**
 * The base class for entries within the file tree.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Entry
{

	Node parent;

	/**
	 * Create a new Entry as a child of the denoted parent node.
	 * 
	 * @param parent
	 *            the parent node.
	 */
	Entry(Node parent)
	{
		this.parent = parent;
	}

	/**
	 * Retrieve the TreePath.
	 * 
	 * @return the path within the tree.
	 */
	public TreePath getTreePath()
	{
		List<Entry> path = getPath();
		Entry[] entries = path.toArray(new Entry[0]);
		TreePath treePath = new TreePath(entries);
		return treePath;
	}

	/**
	 * Get the node this entry is a child of.
	 * 
	 * @return the parent node.
	 */
	public Node getParent()
	{
		return parent;
	}

	/**
	 * Get the path within the tree to this Entry as a list of entries.
	 * 
	 * @return a list of entries.
	 */
	public List<Entry> getPath()
	{
		List<Entry> path = parent == null ? new ArrayList<Entry>() : parent
				.getPath();
		path.add(Entry.this);
		return path;
	}

	/**
	 * Get the unique namespace that describes the path to this Entry as a
	 * string.
	 * 
	 * @return the namespace representing this entry's path.
	 */
	public String getNamespace()
	{
		List<Entry> path = getPath();
		StringBuilder strb = new StringBuilder();
		Iterator<Entry> iterator = path.iterator();
		// skip root
		iterator.next();
		// build string
		while (iterator.hasNext()) {
			Entry entry = iterator.next();
			strb.append(entry.toString());
			if (iterator.hasNext()) {
				strb.append("/");
			}
		}
		return strb.toString();
	}

}
