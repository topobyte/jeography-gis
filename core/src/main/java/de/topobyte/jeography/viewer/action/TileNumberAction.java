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

package de.topobyte.jeography.viewer.action;

import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.swing.util.action.SimpleBooleanAction;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileNumberAction extends SimpleBooleanAction
{

	private static final long serialVersionUID = -7954791594958331416L;

	private Viewer viewer;

	/**
	 * Create a new action for toggling the visibility of tile numbers.
	 * 
	 * @param viewer
	 *            the viewer connected.
	 */
	public TileNumberAction(Viewer viewer)
	{
		super("tile numbers", "toggle visibility of the tile numbers");
		this.viewer = viewer;
		setIcon("res/images/font.png");
	}

	@Override
	public boolean getState()
	{
		return viewer.isDrawTileNumbers();
	}

	@Override
	public void toggleState()
	{
		viewer.setDrawTileNumbers(!viewer.isDrawTileNumbers());
	}

}
