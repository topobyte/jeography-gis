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

import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;
import de.topobyte.jeography.viewer.config.edit.tileconfig.TileConfigEditorList;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class AddTileConfigAction extends TileConfigAction
{

	private static final long serialVersionUID = 8063537304808331253L;

	/**
	 * Create a new action
	 * 
	 * @param editorList
	 *            the editor list this action is about.
	 */
	public AddTileConfigAction(TileConfigEditorList editorList)
	{
		super(editorList);
		setName("Add");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		TileConfigUrlDisk config = new TileConfigUrlDisk(-1, "foo", "url",
				"path");
		editorList.add(config);
		editorList.revalidate();
	}

}
