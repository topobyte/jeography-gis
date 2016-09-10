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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryListTransferhandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryList extends JPanel
{

	final static Logger logger = LoggerFactory.getLogger(GeometryList.class);

	private static final long serialVersionUID = -3418352956823109052L;

	private GeomList geomList;

	/**
	 * Public constructor.
	 */
	public GeometryList()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		geomList = new GeomList();
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(geomList);
		// list.setFont(new Font("Arial", 0, 20));
		geomList.setDropMode(DropMode.INSERT);

		TransferHandler transferhandler = new GeometryListTransferhandler(
				geomList);
		geomList.setTransferHandler(transferhandler);
		geomList.setDragEnabled(true);

		PreviewMouseAdapter previewMouseAdapter = new PreviewMouseAdapter(
				geomList);
		geomList.addMouseListener(previewMouseAdapter);

		JPanel buttons = new JPanel();
		BoxLayout boxLayout = new BoxLayout(buttons, BoxLayout.LINE_AXIS);
		buttons.setLayout(boxLayout);

		// JComponent trash = new TrashButton("trash");
		JComponent trash = new TrashLabel("trash");
		buttons.add(trash);

		c.fill = GridBagConstraints.BOTH;

		c.gridy = 0;
		c.weighty = 0.0;
		add(buttons, c);

		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(jsp, c);
	}

	/**
	 * @return the GeomList used.
	 */
	public GeomList getList()
	{
		return geomList;
	}

}
