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

package de.topobyte.jeography.viewer.geometry.list;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Collection;
import java.util.TooManyListenersException;

import javax.swing.JButton;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryDestinationTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TrashButton extends JButton implements DropTargetListener
{

	final static Logger logger = LoggerFactory.getLogger(TrashButton.class);

	private static final long serialVersionUID = -3240046437158106308L;

	/**
	 * Create a new TrashButton with the denoted text.
	 * 
	 * @param text
	 *            the text to display.
	 */
	public TrashButton(String text)
	{
		super(text);

		setTransferHandler(new GeometryDestinationTransferHandler() {

			private static final long serialVersionUID = -3894879679025530271L;

			@Override
			public void handle(Collection<Geometry> geometries,
					TransferSupport ts)
			{
				logger.debug("handle");
			}

			@Override
			public void reorder(TransferSupport ts)
			{
				logger.debug("reorder");
			}

			@Override
			public boolean canImport(TransferSupport ts)
			{
				setCanImport(false);
				if (ts.getDropAction() == MOVE) {
					setCanImport(super.canImport(ts));
				}
				return isCanImport();
			}

		});
		try {
			getDropTarget().addDropTargetListener(this);
		} catch (TooManyListenersException e) {
			logger.debug(
					"unalbe to add drop target listener: " + e.getMessage());
		}
	}

	private boolean canImport = false;

	void setCanImport(boolean state)
	{
		canImport = state;
	}

	boolean isCanImport()
	{
		return canImport;
	}

	private boolean drop = false;

	private void setDrop(boolean drop)
	{
		this.drop = drop && isCanImport();
		if (this.drop) {
			getModel().setPressed(true);
			getModel().setArmed(true);
		} else {
			getModel().setPressed(false);
			getModel().setArmed(false);
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		setDrop(true);
		repaint();
	}

	@Override
	public void dragExit(DropTargetEvent dte)
	{
		setDrop(false);
		repaint();
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		// do nothing here
	}

	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		setDrop(false);
		repaint();
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
		setDrop(true);
		repaint();
	}

}
