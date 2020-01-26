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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.locationtech.jts.geom.Geometry;

import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.viewer.geometry.action.ExportAction;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometrySourceNull;
import de.topobyte.jeography.viewer.selection.polyaction.EditAction;
import de.topobyte.jeography.viewer.selection.polyaction.RemoveAction;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;

/**
 * A component that displays the geometries of a polygonal selection as a tree.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionTree extends JTree
{

	private static final long serialVersionUID = -6771963494871701426L;

	private SelectionTreeModel model;

	/**
	 * Create a new SelectionTree for the denoted polygonal selection.
	 * 
	 * @param selection
	 *            the selection to display in a tree.
	 */
	public SelectionTree(Selection selection)
	{
		setRootVisible(false);

		model = new SelectionTreeModel(selection);
		setModel(model);

		SelectionTreeMouseAdapter mouseAdapter = new SelectionTreeMouseAdapter();
		addMouseListener(mouseAdapter);

		SelectionTreeTransferHandler transferHandler = new SelectionTreeTransferHandler();
		setTransferHandler(transferHandler);
		setDragEnabled(true);
	}

	@Override
	public SelectionTreeModel getModel()
	{
		return model;
	}

	private class SelectionTreeMouseAdapter extends MouseAdapter
	{

		public SelectionTreeMouseAdapter()
		{
			// nothing to do
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			TreePath treePath = SelectionTree.this
					.getPathForLocation(event.getX(), event.getY());
			if (treePath == null) {
				return;
			}

			if (event.getButton() == MouseEvent.BUTTON1) {
				if (event.getClickCount() == 2) {
					showPreview(treePath);
				}
			}
			if (event.getButton() == MouseEvent.BUTTON3) {
				showContextMenu(event.getX(), event.getY());
			}
		}

		private void showPreview(TreePath treePath)
		{
			GeometryNode node = (GeometryNode) treePath.getLastPathComponent();
			Geometry geometry = node.getGeometry();

			GeometryPreview preview = new GeometryPreview();
			GeoObject taggedGeometry = new GeoObject(geometry);
			Set<GeometryContainer> tgs = new HashSet<>();
			GeometryContainer gc = new GeometryContainer(0, taggedGeometry,
					new GeometrySourceNull());
			tgs.add(gc);
			preview.showViewerWithFile(SelectionTree.this, tgs, "preview");
		}

		private void showContextMenu(int x, int y)
		{
			TreePath path = SelectionTree.this.getPathForLocation(x, y);
			Object object = path.getLastPathComponent();
			GeometryNode geometryNode = (GeometryNode) object;
			Geometry geometry = geometryNode.getGeometry();

			EditAction editAction = new EditAction(getModel(), path);
			RemoveAction removeAction = new RemoveAction(getModel(), path);
			ExportAction exportAction = new ExportAction(geometry);

			JPopupMenu menu = new JPopupMenu();
			JMenuItem itemEdit = new JMenuItem(editAction);
			JMenuItem itemRemove = new JMenuItem(removeAction);
			JMenuItem itemExport = new JMenuItem(exportAction);
			menu.add(itemEdit);
			menu.add(itemRemove);
			menu.add(itemExport);
			menu.show(SelectionTree.this, x, y);
			menu.setVisible(true);
		}
	}

	class SelectionTreeTransferHandler extends GeometrySourceTransferHandler
	{

		private static final long serialVersionUID = -1932321626207988555L;

		@Override
		public Collection<Geometry> getGeometries()
		{
			List<Geometry> list = new ArrayList<>();
			TreePath[] paths = getSelectionPaths();
			for (TreePath path : paths) {
				list.add(getModel().getGeometry(path));
			}
			return list;
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return COPY;
		}

	}

}
