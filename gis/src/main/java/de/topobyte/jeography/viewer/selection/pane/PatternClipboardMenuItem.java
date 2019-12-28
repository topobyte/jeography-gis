// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.selection.pane;

import de.topobyte.jeography.swing.widgets.ClipboardMenuItem;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelectionFormatter;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

public class PatternClipboardMenuItem extends ClipboardMenuItem
{

	private static final long serialVersionUID = 1L;

	private SelectionAdapter selectionAdapter;
	private GeographicSelectionFormatter formatter;

	public PatternClipboardMenuItem(SelectionAdapter selectionAdapter,
			GeographicSelectionFormatter formatter)
	{
		super(formatter.getName());
		this.selectionAdapter = selectionAdapter;
		this.formatter = formatter;
	}

	@Override
	public String getClipboardText()
	{
		return formatter.format(selectionAdapter.getGeographicSelection());
	}

}
