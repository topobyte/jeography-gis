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

package de.topobyte.jeography.viewer.config.edit.tileconfig.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import de.topobyte.jeography.viewer.config.edit.selectable.Selectable;
import de.topobyte.jeography.viewer.config.edit.tileconfig.TileConfigEditorList;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RemoveTileConfigAction extends TileConfigAction
{

	private static final long serialVersionUID = -1990250784319927470L;

	/**
	 * Create a new action
	 * 
	 * @param editorList
	 *            the editor list this action is about.
	 */
	public RemoveTileConfigAction(TileConfigEditorList editorList)
	{
		super(editorList);
		setName("Remove");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JComponent c = editorList.getSelectionChain().getCurrent();
		Selectable current = (Selectable) editorList.getSelectionChain()
				.getCurrent();
		if (current == null) {
			return;
		}
		editorList.remove((Selectable) c);
		editorList.revalidate();
	}

}
