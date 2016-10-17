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

package de.topobyte.jeography.viewer.config.edit.other;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import bibliothek.gui.dock.common.theme.ThemeMap;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.swing.util.ElementWrapper;
import de.topobyte.swing.util.combobox.ListComboBoxModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DockingFramesThemeSelector
		extends JComboBox<ElementWrapper<DockingFramesThemeSelector.Entry>>
{

	private static final long serialVersionUID = 1L;
	private List<Entry> entries;

	public DockingFramesThemeSelector(Configuration configuration)
	{
		entries = new ArrayList<>();
		entries.add(new Entry(ThemeMap.KEY_BASIC_THEME, "Basic"));
		entries.add(new Entry(ThemeMap.KEY_BUBBLE_THEME, "Bubble"));
		entries.add(new Entry(ThemeMap.KEY_ECLIPSE_THEME, "Eclipse"));
		entries.add(new Entry(ThemeMap.KEY_FLAT_THEME, "Flat"));
		entries.add(new Entry(ThemeMap.KEY_SMOOTH_THEME, "Smooth"));

		ListComboBoxModel<Entry> model = new ListComboBoxModel<Entry>(entries) {

			@Override
			public String toString(Entry element)
			{
				return element.value;
			}
		};

		setModel(model);

		int index = 0;
		String theme = configuration.getDockingFramesTheme();
		if (theme != null) {
			for (int i = 0; i < entries.size(); i++) {
				if (theme.equals(entries.get(i).key)) {
					index = i;
					break;
				}
			}
		}

		setSelectedIndex(index);
	}

	public String getSelectedTheme()
	{
		int index = getSelectedIndex();
		Entry entry = entries.get(index);
		return entry.key;
	}

	static class Entry
	{

		private String key;
		private String value;

		public Entry(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

	}

}