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

package de.topobyte.jeography.viewer.util;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ScrollablePanel extends JPanel implements Scrollable
{

	private static final long serialVersionUID = -1831323546379143291L;

	private boolean tracksViewportWidth = false;
	private boolean tracksViewportHeight = false;

	public ScrollablePanel()
	{
		super();
	}

	public ScrollablePanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
	}

	public ScrollablePanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
	}

	public ScrollablePanel(LayoutManager layout)
	{
		super(layout);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 1;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return tracksViewportWidth;
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return tracksViewportHeight;
	}

	public boolean isTracksViewportWidth()
	{
		return tracksViewportWidth;
	}

	public void setTracksViewportWidth(boolean tracksViewportWidth)
	{
		this.tracksViewportWidth = tracksViewportWidth;
	}

	public boolean isTracksViewportHeight()
	{
		return tracksViewportHeight;
	}

	public void setTracksViewportHeight(boolean tracksViewportHeight)
	{
		this.tracksViewportHeight = tracksViewportHeight;
	}

}
