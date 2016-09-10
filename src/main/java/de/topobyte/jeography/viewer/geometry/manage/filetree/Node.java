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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class representing inner nodes within the file tree.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Node extends Entry
{

	final static Logger logger = LoggerFactory.getLogger(Node.class);

	private String name;
	List<Node> childs;
	List<Leaf> entries;

	/**
	 * Create a new Node that is a child of the denoted parent with the denoted
	 * name.
	 * 
	 * @param parent
	 *            the parent node this node is a child of.
	 * @param name
	 *            the name of this node.
	 */
	public Node(Node parent, String name)
	{
		super(parent);
		this.name = name;
		childs = new ArrayList<>();
		entries = new ArrayList<>();
		//
		// for (int i = 1; i <= 3; i++) {
		// entries.add(new Leaf(Node.this, "foobar " + i));
		// }
	}

	/**
	 * @param name
	 *            the name of this node.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name of this node.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Add a child node to this node with the denoted name.
	 * 
	 * @param childname
	 *            the name of the child.
	 * @return the newly created node.
	 */
	public Node addChildNode(String childname)
	{
		Node node = new Node(this, childname);
		this.childs.add(node);
		return node;
	}

	/**
	 * Get the index of the denoted child entry. Indices for leafs are biased by
	 * the number of child nodes.
	 * 
	 * @param child
	 *            the object to lookup.
	 * @return the index of the child or -1 if not contained or not a Node or
	 *         Leaf instance.
	 */
	public int getIndexOfChild(Object child)
	{
		if (child instanceof Node) {
			return getIndexOfChild((Node) child);
		} else if (child instanceof Leaf) {
			return getIndexOfChild((Leaf) child);
		}
		return -1;
	}

	private int getIndexOfChild(Node child)
	{
		for (int i = 0; i < childs.size(); i++) {
			Node ni = childs.get(i);
			if (ni == child) {
				return i;
			}
		}
		return -1;
	}

	private int getIndexOfChild(Leaf child)
	{
		for (int i = 0; i < entries.size(); i++) {
			Leaf ei = entries.get(i);
			if (ei == child) {
				return i + childs.size();
			}
		}
		return -1;
	}

	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * @return the number of children (nodes + leafs) within this node.
	 */
	public int getTotalNumberOfChilds()
	{
		int value = childs.size() + entries.size();
		logger.debug("getTotalNumberOfChilds(" + Node.this + ") -> " + value);
		return value;
	}

	/**
	 * Get the n'th child of this node.
	 * 
	 * @param n
	 *            the index of the child.
	 * @return the n'th child entry.
	 */
	public Entry getChild(int n)
	{
		if (n < childs.size()) {
			return childs.get(n);
		}
		return entries.get(n - childs.size());
	}

	/**
	 * Find out whether this node has a child with the denoted name.
	 * 
	 * @param childname
	 *            the name of the child to find
	 * @return the index of a child with the denoted name.
	 */
	public boolean hasChildNode(String childname)
	{
		for (Node node : childs) {
			if (node.name.equals(childname)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the node with the denoted name.
	 * 
	 * @param childname
	 *            the name of the node to find.
	 * @return the child node or null
	 */
	public Node getChildNode(String childname)
	{
		for (Node node : childs) {
			if (node.name.equals(childname)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Find the entry with the denoted name.
	 * 
	 * @param childname
	 *            the name of the entry to find.
	 * @return the child node or null
	 */
	public Entry getChildEntry(String childname)
	{
		for (Node node : childs) {
			if (node.name.equals(childname)) {
				return node;
			}
		}
		for (Leaf leaf : entries) {
			if (leaf.toString().equals(childname)) {
				return leaf;
			}
		}
		return null;
	}

	/**
	 * Get the list of child leafs.
	 * 
	 * @return a list of leafs.
	 */
	public List<Leaf> getLeafs()
	{
		return entries;
	}

	/**
	 * Get the list of child nodes.
	 * 
	 * @return a list of nodes.
	 */
	public List<Node> getChilds()
	{
		return childs;
	}

	/**
	 * Remove the denoted entry from the tree.
	 * 
	 * @param entry
	 *            the entry to remove.
	 * @return the index of the removed index or -1 on failure.
	 */
	public int remove(Entry entry)
	{
		if (entry instanceof Leaf) {
			Leaf leaf = (Leaf) entry;
			int index = getIndexOfChild(leaf);
			entries.remove(index - childs.size());
			return index;
		} else if (entry instanceof Node) {
			Node node = (Node) entry;
			int index = getIndexOfChild(node);
			childs.remove(index);
			return index;
		}
		return -1;
	}

}
