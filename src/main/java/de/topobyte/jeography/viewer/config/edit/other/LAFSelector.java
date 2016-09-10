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

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import de.topobyte.jeography.viewer.config.Configuration;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class LAFSelector extends JComboBox
{

	private static final long serialVersionUID = 6856865390726849784L;

	private static LookAndFeelInfo[] lafs;

	public LAFSelector(Configuration configuration)
	{
		super(buildValues());

		setRenderer(new Renderer());
		setEditable(false);

		String lookAndFeel = configuration.getLookAndFeel();
		setSelectedIndex(-1);
		for (int i = 0; i < getModel().getSize(); i++) {
			LookAndFeelInfo info = (LookAndFeelInfo) getModel().getElementAt(i);
			if (info == null && lookAndFeel == null) {
				setSelectedIndex(i);
				break;
			}
			if (info != null && info.getClassName().equals(lookAndFeel)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	private static LookAndFeelInfo[] buildValues()
	{
		LookAndFeelInfo[] lafsA = UIManager.getInstalledLookAndFeels();
		lafs = new LookAndFeelInfo[lafsA.length + 1];
		lafs[0] = null;
		for (int i = 0; i < lafsA.length; i++) {
			lafs[i + 1] = lafsA[i];
		}
		return lafs;
	}

	private class Renderer extends BasicComboBoxRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);

			if (value != null) {
				LookAndFeelInfo item = (LookAndFeelInfo) value;
				setText(item.getName());
			} else {
				setText("default");
			}

			return this;
		}
	}

	public String getSelectedLookAndFeel()
	{
		int index = getSelectedIndex();
		if (index < 0) {
			return null;
		}
		LookAndFeelInfo laf = lafs[index];
		if (laf == null) {
			return null;
		}
		return laf.getClassName();
	}

}
