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

package de.topobyte.jeography.viewer.selection.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.action.GISAction;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ClipboardAction extends GISAction
{

	private static final long serialVersionUID = 8870407480951831801L;

	final static Logger logger = LoggerFactory.getLogger(ClipboardAction.class);

	private final SelectionAdapter selectionAdapter;

	/**
	 * Create this action with the given SelectionAdapter as a source for the
	 * download area.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is about.
	 * 
	 * @param selectionAdapter
	 *            the adapter to get the selection from.
	 */
	public ClipboardAction(JeographyGIS gis, SelectionAdapter selectionAdapter)
	{
		super(gis, "res/images/16/edit-copy.png");
		this.name = "copy to clipboard";
		this.description = "copy the selection's coordinates to clipboard";

		this.selectionAdapter = selectionAdapter;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		final GeographicSelection selection = selectionAdapter
				.getGeographicSelection();

		if (selection == null) {
			return;
		}

		logger.debug("geo: " + selection.toString());
		logger.debug("box: " + selection.toBoundingBox());

		Clipboard clipboard = getGIS().getMainPanel().getToolkit()
				.getSystemClipboard();
		logger.debug(clipboard.toString());

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
					String text = selection.toString();
					return text;
				}
				throw new UnsupportedFlavorException(flavor);
			}
		};

		ClipboardOwner owner = null;

		clipboard.setContents(transferable, owner);
	}

	private String textForXml(GeographicSelection selection, int digits)
	{
		String text = selection.toString();
		String pattern = "lon1=\"%%.%df\" lon2=\"%%.%df\" lat1=\"%%.%df\" lat2=\"%%.%df\"";
		String realPattern = String.format(pattern, digits, digits, digits,
				digits);
		System.out.println(realPattern);
		text = String.format(realPattern, selection.getX1().value(),
				selection.getX2().value(), selection.getY1().value(),
				selection.getY2().value());
		return text;
	}

}
