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

package de.topobyte.jeography.viewer.selection.polyaction;

import java.awt.event.ActionEvent;

import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.action.SimpleAction;
import de.topobyte.jeography.viewer.selection.polygonal.SelectionTreeModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class EditAction extends SimpleAction
{

	final static Logger logger = LoggerFactory.getLogger(EditAction.class);

	private static final long serialVersionUID = 5060790684819680647L;

	private final SelectionTreeModel model;
	private final TreePath path;

	/**
	 * Create a new EditAction.
	 * 
	 * @param model
	 *            the model to edit within.
	 * 
	 * @param path
	 *            the path within the tree to the entry to edit.
	 */
	public EditAction(SelectionTreeModel model, TreePath path)
	{
		super("edit", "edit geometry");
		this.model = model;
		this.path = path;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int index = model.getIndexForPath(path);
		model.getSelection().setEditedGeometry(index);
	}

}
