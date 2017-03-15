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

package de.topobyte.jeography.viewer.selection.polygonal;

import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.swing.util.tree.TreeModelListenerList;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionTreeModel implements TreeModel, SelectionChangeListener
{

	private Logger logger = LoggerFactory.getLogger(SelectionTreeModel.class);

	private final Selection selection;

	/**
	 * Create a new SelectionTreeModel for the denoted selection.
	 * 
	 * @param selection
	 *            the selection to build a model for.
	 */
	public SelectionTreeModel(Selection selection)
	{
		this.selection = selection;

		selection.addSelectionChangeListener(this);
	}

	@Override
	public Object getRoot()
	{
		return this;
	}

	/**
	 * @return the selection object.
	 */
	public Selection getSelection()
	{
		return selection;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		if (parent == this) {
			List<Geometry> list = selection.getGeometriesAsListDegrees();
			Geometry geometry = list.get(index);
			return new GeometryNode(index, geometry);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent)
	{
		if (parent == this) {
			List<Geometry> list = selection.getGeometriesAsList();
			return list.size();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		if (node instanceof SelectionTreeModel) {
			return false;
		}
		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	TreeModelListenerList listenerList = new TreeModelListenerList();

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		listenerList.addTreeModelListener(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		listenerList.addTreeModelListener(l);
	}

	private void triggerModelChanged()
	{
		TreePath path = new TreePath(this);
		TreeModelEvent event = new TreeModelEvent(this, path);
		listenerList.triggerTreeStructureChanged(event);
	}

	@Override
	public void pixelValuesChanged()
	{
		triggerModelChanged();
	}

	@Override
	public void geographicValuesChanged()
	{
		triggerModelChanged();
	}

	@Override
	public void selectionChanged()
	{
		triggerModelChanged();
	}

	/**
	 * Remove the element pointed to with the denoted path.
	 * 
	 * @param path
	 *            the element to remove.
	 */
	public void remove(TreePath path)
	{
		logger.debug("remove. path: " + path);
		GeometryNode node = (GeometryNode) path.getLastPathComponent();
		int index = node.getIndex();
		selection.remove(index);
	}

	/**
	 * Retrieve the index of the denoted path within the tree.
	 * 
	 * @param path
	 *            the path to retrieve the index for.
	 * @return the index within the tree.
	 */
	public int getIndexForPath(TreePath path)
	{
		logger.debug("remove. path: " + path);
		GeometryNode node = (GeometryNode) path.getLastPathComponent();
		return node.getIndex();
	}

	/**
	 * Retrieve the geometry at the denoted path within the tree.
	 * 
	 * @param path
	 *            the path to retrieve the geometry for.
	 * @return the geometry.
	 */
	public Geometry getGeometry(TreePath path)
	{
		GeometryNode node = (GeometryNode) path.getLastPathComponent();
		return node.getGeometry();
	}

}

class GeometryNode
{

	private final int index;
	private final Geometry geometry;

	public GeometryNode(int index, Geometry geometry)
	{
		this.index = index;
		this.geometry = geometry;
	}

	@Override
	public String toString()
	{
		String type = geometry.getGeometryType();
		return "geometry (" + type + ")";
	}

	public int getIndex()
	{
		return index;
	}

	public Geometry getGeometry()
	{
		return geometry;
	}

}
