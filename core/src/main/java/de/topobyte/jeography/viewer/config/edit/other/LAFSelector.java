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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.swing.util.ElementWrapper;
import de.topobyte.swing.util.combobox.ListComboBoxModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class LAFSelector extends JComboBox<ElementWrapper<LAFSelector.Entry>>
{

	private static final long serialVersionUID = 6856865390726849784L;

	private static List<Entry> entries = null;

	static {
		LookAndFeelInfo[] lafsA = UIManager.getInstalledLookAndFeels();
		entries = new ArrayList<>();
		entries.add(new Entry(null));
		for (int i = 0; i < lafsA.length; i++) {
			entries.add(new Entry(lafsA[i]));
		}
	}

	public LAFSelector(Configuration configuration)
	{
		ListComboBoxModel<Entry> model = new ListComboBoxModel<Entry>(entries) {

			@Override
			public String toString(Entry element)
			{
				if (element.laf == null) {
					return "Default";
				}
				return element.laf.getName();
			}
		};

		setModel(model);

		setEditable(false);

		int index = 0;
		String lookAndFeel = configuration.getLookAndFeel();
		if (lookAndFeel != null) {
			for (int i = 0; i < entries.size(); i++) {
				Entry entry = entries.get(i);
				if (entry.laf == null) {
					continue;
				}
				if (lookAndFeel.equals(entry.laf.getClassName())) {
					index = i;
					break;
				}
			}
		}

		setSelectedIndex(index);
	}

	public String getSelectedLookAndFeel()
	{
		int index = getSelectedIndex();
		if (index < 0) {
			return null;
		}
		Entry entry = entries.get(index);
		if (entry.laf == null) {
			return null;
		}
		return entry.laf.getClassName();
	}

	static class Entry
	{

		private LookAndFeelInfo laf;

		public Entry(LookAndFeelInfo laf)
		{
			this.laf = laf;
		}

	}

}
