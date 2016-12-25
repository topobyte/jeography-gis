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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.sax.TransformerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import de.topobyte.jeography.viewer.geometry.manage.filetree.Entry;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Leaf;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
// public class GeometryTreeModel implements TreeTableModel {

public class GeometryTreeModel implements TreeModel
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometryTreeModel.class);

	private Node root = new Node(null, "geometries");

	/**
	 * Standard constructor
	 */
	public GeometryTreeModel()
	{
		// root.childs.add(new Node(root, "foo"));
		// root.childs.add(new Node(root, "bar"));
		// root.childs.add(new Node(root, "cow"));
	}

	/**
	 * Create this folder namespace.
	 * 
	 * @param namespace
	 *            a namespace with parts seperated by dots.
	 * @return the node representing the last part in the tree given by the
	 *         namespace path.
	 */
	public Node createNamespace(String namespace)
	{
		String[] parts = namespace.split("/");
		Node node = root;
		for (String part : parts) {
			if (!node.hasChildNode(part)) {
				node = addChildNode(node, part);
			} else {
				node = node.getChildNode(part);
			}
		}
		return node;
	}

	/**
	 * Get an entry by it's namespace.
	 * 
	 * @param namespace
	 *            the namespace to use for searching
	 * @return the found entry, or null
	 */
	public Entry getByNamespace(String namespace)
	{
		if (namespace.length() == 0) {
			return root;
		}
		String[] parts = namespace.split("/");
		Node node = root;
		for (String part : parts) {
			Entry e = node.getChildEntry(part);
			if (e == null) {
				return null;
			}
			if (e instanceof Node) {
				node = (Node) e;
			} else {
				return e;
			}
		}
		return node;
	}

	private Node addChildNode(Node parent, String childname)
	{
		Node node = parent.addChildNode(childname);
		triggerTreeModelListenersInsertion(parent, node);
		return node;
	}

	/**
	 * Add all files in this directory to the given namespace.
	 * 
	 * @param directory
	 *            the directory containing the files.
	 * @param namespace
	 *            the namespace to add the files in.
	 */
	public void addContained(String directory, String namespace)
	{
		Node node = createNamespace(namespace);
		addContained(directory, node);
	}

	/**
	 * Add all files in this directory to the given node.
	 * 
	 * @param directory
	 *            the directory containing the files.
	 * @param node
	 *            the node to add the files in
	 */
	private void addContained(String directory, Node node)
	{
		File dir = new File(directory);
		addContained(dir, node);
	}

	/**
	 * Add all files in this directory to the given node.
	 * 
	 * @param dir
	 *            the directory containing the files.
	 * @param node
	 *            the node to add the files in.
	 */
	private void addContained(File dir, Node node)
	{
		if (!dir.exists() || !dir.isDirectory() || !dir.canRead())
			return;

		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dirfile, String name)
			{
				if (name.endsWith(".ser")) {
					return true;
				}
				if (name.endsWith(".jsg")) {
					return true;
				}
				if (name.endsWith(".tgs")) {
					return true;
				}
				if (name.endsWith(".smx")) {
					return true;
				}
				if (name.endsWith(".wkt")) {
					return true;
				}
				if (name.endsWith(".wkb")) {
					return true;
				}
				return false;
			}
		});
		List<File> filelist = new ArrayList<>();
		for (File f : files) {
			filelist.add(f);
		}
		addFiles(filelist, node);
	}

	@Override
	public Node getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		if (parent instanceof Node) {
			Node node = (Node) parent;
			return node.getChild(index);
		}
		return "error";
	}

	@Override
	public int getChildCount(Object parent)
	{
		if (parent instanceof Node) {
			Node node = (Node) parent;
			return node.getTotalNumberOfChilds();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		if (node instanceof Node) {
			return false;
		}
		if (node instanceof Leaf) {
			return true;
		}
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		String value = (String) newValue;
		Entry entry = (Entry) path.getLastPathComponent();
		if (entry instanceof Node) {
			Node node = (Node) entry;
			node.setName(value);
			triggerTreeModelListenersChanged(node);
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		logger.debug("getIndexOfChild " + parent + " " + child);
		int val = -1;
		if (parent == null || child == null) {
			val = -1;
		} else if (parent instanceof Node) {
			Node p = (Node) parent;
			val = p.getIndexOfChild(child);
		}
		logger.debug("index is " + val);
		return val;
	}

	Set<TreeModelListener> listeners = new HashSet<>();

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		logger.debug("adding tree model listener");
		listeners.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		logger.debug("removing tree model listener");
		listeners.remove(l);
	}

	private void triggerTreeModelListenersChanged(Entry entry)
	{
		for (TreeModelListener tml : listeners) {
			TreeModelEvent e = new TreeModelEvent(this, entry.getTreePath(),
					null, null);
			tml.treeNodesChanged(e);
		}
	}

	private void triggerTreeModelListenersInsertion(Node parent, Node nodeAdded)
	{
		TreePath path = parent.getTreePath();
		logger.debug("path: " + path);
		List<Object> childrenList = new ArrayList<>();
		List<Integer> childrenIndicesList = new ArrayList<>();

		int index = parent.getIndexOfChild(nodeAdded);
		childrenList.add(nodeAdded);
		childrenIndicesList.add(index);

		logger.debug("new indices: " + childrenIndicesList.toString());

		Object[] children = childrenList.toArray();
		int[] childIndices = new int[childrenIndicesList.size()];
		for (int i = 0; i < childIndices.length; i++) {
			childIndices[i] = childrenIndicesList.get(i);
		}

		for (TreeModelListener tml : listeners) {
			TreeModelEvent e = new TreeModelEvent(this, path, childIndices,
					children);
			tml.treeNodesInserted(e);
		}
	}

	private void triggerTreeModelListenersInsertion(Node parent,
			List<Leaf> entriesAdded)
	{
		TreePath path = parent.getTreePath();
		logger.debug("path: " + path);
		List<Object> childrenList = new ArrayList<>();
		List<Integer> childrenIndicesList = new ArrayList<>();

		for (Leaf e : entriesAdded) {
			int index = parent.getIndexOfChild(e);
			childrenList.add(e);
			childrenIndicesList.add(index);
		}
		logger.debug("new indices: " + childrenIndicesList.toString());

		Object[] children = childrenList.toArray();
		int[] childIndices = new int[childrenIndicesList.size()];
		for (int i = 0; i < childIndices.length; i++) {
			childIndices[i] = childrenIndicesList.get(i);
		}

		for (TreeModelListener tml : listeners) {
			TreeModelEvent e = new TreeModelEvent(this, path, childIndices,
					children);
			tml.treeNodesInserted(e);
		}
	}

	// /*
	// * here come the additional TreeTableModel methods
	// */
	//
	// @Override
	// public Class<?> getColumnClass(int n) {
	// return String.class;
	// }
	//
	// @Override
	// public int getColumnCount() {
	// return 2;
	// }
	//
	// @Override
	// public String getColumnName(int n) {
	// switch (n) {
	// case 0:
	// return "name";
	// case 1:
	// return "description";
	// }
	// return null;
	// }
	//
	// @Override
	// public int getHierarchicalColumn() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public Object getValueAt(Object node, int col) {
	// if (node instanceof Node) {
	// Node n = (Node) node;
	// return n.toString();
	// }
	// if (node instanceof Entry) {
	// Entry e = (Entry) node;
	// return e.toString();
	// }
	// return "none";
	// }
	//
	// @Override
	// public boolean isCellEditable(Object node, int col) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public void setValueAt(Object value, Object node, int col) {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * Add the directory 'name' to the denoted node.
	 * 
	 * @param node
	 *            the node to add a child for.
	 * @param name
	 *            the new node's name.
	 */
	public void addDirectory(Node node, String name)
	{
		String[] splitted = name.split("/");
		Node parent = node;
		for (String s : splitted) {
			Entry entry = parent.getChildEntry(s);
			if (entry == null) {
				Node child = parent.addChildNode(s);
				triggerTreeModelListenersInsertion(parent, child);
				parent = child;
			} else {
				if (entry instanceof Node) {
					parent = (Node) entry;
				}
			}
		}
	}

	// private void addDirectory

	/**
	 * Add the list of files to the given path.
	 * 
	 * @param files
	 *            the files to add.
	 * @param path
	 *            the path, where to add.
	 */
	public void addFiles(List<File> files, TreePath path)
	{
		Object last = root;
		if (path != null) {
			last = path.getLastPathComponent();
		}
		if (last instanceof Node) {
			logger.debug("drop on folder");
			addFiles(files, (Node) last);
		} else if (last instanceof Leaf) {
			logger.debug("drop on entry");
			Entry e = (Entry) last;
			List<Entry> p = e.getPath();
			if (p.size() > 1) {
				Entry entry = p.get(p.size() - 2);
				if (entry instanceof Node) {
					addFiles(files, (Node) entry);
				}
			}
		}
	}

	/**
	 * Add the denoted file to the denoted parent node.
	 * 
	 * @param file
	 *            the file to add.
	 * @param parent
	 *            the node to add the file in.
	 */
	public void addFile(File file, Node parent)
	{
		ArrayList<File> list = new ArrayList<>();
		list.add(file);
		addFiles(list, parent);
	}

	/**
	 * Add the denoted list of files to the denoted parent node.
	 * 
	 * @param handleFiles
	 *            the files to add.
	 * @param parent
	 *            the node to add the files in.
	 */
	public void addFiles(List<File> handleFiles, Node parent)
	{
		List<Leaf> entriesAdded = new ArrayList<>();
		for (File file : handleFiles) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				List<File> subfiles = new ArrayList<>();
				for (File f : files) {
					subfiles.add(f);
				}
				addFiles(subfiles, parent);
			}
			if (!accept(file)) {
				continue;
			}
			Leaf e = new Leaf(parent, file);
			parent.getLeafs().add(e);
			entriesAdded.add(e);
			logger.debug("adding: " + file.toString());
		}
		logger.debug(parent.getLeafs().toString());
		triggerTreeModelListenersInsertion(parent, entriesAdded);
	}

	private boolean accept(File file)
	{
		String name = file.getName();
		logger.debug("checking acceptance of file: " + name);
		if (name.endsWith(".ser")) {
			return true;
		}
		if (name.endsWith(".jsg")) {
			return true;
		}
		if (name.endsWith(".tgs")) {
			return true;
		}
		if (name.endsWith(".smx")) {
			return true;
		}
		if (name.endsWith(".wkb")) {
			return true;
		}
		if (name.endsWith(".wkt")) {
			return true;
		}
		return false;
	}

	/**
	 * Serialize the model to a XML output.
	 * 
	 * @param handler
	 *            the handler to output to.
	 * @throws SAXException
	 *             if a xml error occurs.
	 */
	public void serialize(TransformerHandler handler) throws SAXException
	{
		AttributesImpl atts = new AttributesImpl();
		handler.startElement("", "", "tree", atts);

		serialize(handler, root, atts, true);

		handler.endElement("", "", "tree");
	}

	private void serialize(TransformerHandler handler, Entry entry,
			AttributesImpl atts, boolean ignore) throws SAXException
	{
		atts.clear();
		if (entry instanceof Leaf) {
			if (!ignore) {
				Leaf leaf = (Leaf) entry;
				File file = leaf.getFile();
				String path = file.getAbsolutePath();
				atts.addAttribute("", "", "path", "CDATA", path);
				handler.startElement("", "", "entry", atts);
				handler.endElement("", "", "entry");
			}
		} else if (entry instanceof Node) {
			Node node = (Node) entry;
			if (!ignore) {
				atts.addAttribute("", "", "name", "CDATA", node.getName());
				handler.startElement("", "", "folder", atts);
			}
			for (int i = 0; i < node.getTotalNumberOfChilds(); i++) {
				Entry child = node.getChild(i);
				serialize(handler, child, atts, false);
			}
			if (!ignore) {
				handler.endElement("", "", "folder");
			}
		}
	}

	/**
	 * Read a GeometryTreeModel from the given Inputstream and SAXParser
	 * 
	 * @param sax
	 *            the parser to use.
	 * @param inputStream
	 *            the input stream to read from.
	 * @return the new GeometryTreeModel instance.
	 * @throws SAXException
	 *             if an error occurred while reading.
	 * @throws IOException
	 *             if an error occurred while reading.
	 */
	public static GeometryTreeModel deserialize(SAXParser sax,
			FileInputStream inputStream) throws SAXException, IOException
	{
		final GeometryTreeModel model = new GeometryTreeModel();

		DefaultHandler handler = new DefaultHandler() {

			int nTree = 0;
			boolean accept = false;

			Node root = model.getRoot();
			Node currentNode = root;

			@Override
			public void startElement(String uri, String localName, String qName,
					Attributes atts)
			{
				if (qName.equals("tree")) {
					nTree += 1;
					accept = nTree == 1;
				} else {
					if (!accept) {
						return;
					}
					if (qName.equals("folder")) {
						String name = atts.getValue("name");
						Node node = new Node(currentNode, name);
						currentNode.getChilds().add(node);
						currentNode = node;
					} else if (qName.equals("entry")) {
						String path = atts.getValue("path");
						Leaf leaf = new Leaf(currentNode, new File(path));
						currentNode.getLeafs().add(leaf);
					}
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName)
			{
				if (qName.equals("tree")) {
					// nothing
				} else if (qName.equals("folder")) {
					currentNode = currentNode.getParent();
				} else if (qName.equals("entry")) {
					// nothing
				}
			}
		};
		sax.parse(inputStream, handler);
		return model;
	}

	/**
	 * Remove the denoted element from the model.
	 * 
	 * @param path
	 *            the path describing the element to remove.
	 */
	public void removePath(TreePath path)
	{
		if (!pathValid(path)) {
			return;
		}
		Entry entry = (Entry) path.getLastPathComponent();
		TreePath parentPath = path.getParentPath();
		Node parent = (Node) parentPath.getLastPathComponent();
		int removedIndex = parent.remove(entry);

		int[] childIndices = new int[] { removedIndex };

		for (TreeModelListener tml : listeners) {
			TreeModelEvent e = new TreeModelEvent(this, parentPath,
					childIndices, null);
			tml.treeNodesRemoved(e);
		}
	}

	private boolean pathValid(TreePath path)
	{
		if (path.getPathCount() == 0) {
			logger.debug("path invalid: length 0");
			return false;
		}
		if (root != path.getPathComponent(0)) {
			logger.debug("path invalid: root not valid");
			return false;
		}
		Node node = root;
		for (int i = 1; i < path.getPathCount(); i++) {
			Object component = path.getPathComponent(i);

			if (node.getIndexOfChild(component) == -1) {
				logger.debug(
						"path invalid: an entry in path could not be found");
				return false;
			}

			if (i < path.getPathCount() - 1) {
				node = (Node) component;
			}
		}
		logger.debug("path valid");
		return true;
	}

}
