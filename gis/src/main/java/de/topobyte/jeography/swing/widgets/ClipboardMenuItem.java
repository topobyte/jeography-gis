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

package de.topobyte.jeography.swing.widgets;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class ClipboardMenuItem extends JMenuItem
		implements ActionListener
{

	private static final long serialVersionUID = -6148481295854649697L;

	public ClipboardMenuItem(String title)
	{
		super(title);
		addActionListener(this);
	}

	public abstract String getClipboardText();

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Clipboard clipboard = getToolkit().getSystemClipboard();

		Transferable transferable = new Transferable() {

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				if (flavor.equals(DataFlavor.stringFlavor)) {
					return true;
				}
				return false;
			}

			@Override
			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] { DataFlavor.stringFlavor };
			}

			@Override
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException
			{
				if (flavor.equals(DataFlavor.stringFlavor)) {
					return getClipboardText();
				}
				throw new UnsupportedFlavorException(flavor);
			}
		};

		ClipboardOwner owner = null;

		clipboard.setContents(transferable, owner);
	}

}
