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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.geometry.io.PolygonLoader;
import de.topobyte.jeography.viewer.geometry.list.dnd.FileTransferHandler;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;
import de.topobyte.swing.util.dnd.DestinationSourceTransferHandler;
import de.topobyte.swing.util.list.ArrayListModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryFileList extends JPanel
{

	final static Logger logger = LoggerFactory
			.getLogger(GeometryFileList.class);

	private static final long serialVersionUID = -3418352956823109052L;

	private JList<File> list;
	GeometryFileListModel listModel;

	/**
	 * Public constructor.
	 */
	public GeometryFileList()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		listModel = new GeometryFileListModel();
		list = new JList<>(getListModel());
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);
		list.setDropMode(DropMode.INSERT);

		TransferHandler transferhandler = new TheTransferHandler();
		list.setTransferHandler(transferhandler);
		list.setDragEnabled(true);

		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(jsp, c);

		// new DropTarget(list, this);
		// new FileDrop(System.out, list, this);
	}

	GeometryFileListModel getListModel()
	{
		return listModel;
	}

	JList<File> getList()
	{
		return list;
	}

	class GeometryFileListModel extends ArrayListModel<File>
	{

		private static final long serialVersionUID = -7995883895872819241L;

		public Geometry getGeometry(int index)
		{
			File file = getElementAt(index);
			try {
				Geometry geometry = PolygonLoader
						.readPolygon(file.getAbsolutePath());
				return geometry;
			} catch (IOException e) {
				logger.debug("unable to load geometry: " + file);
			}
			return null;
		}
	}

	class TheTransferHandler extends DestinationSourceTransferHandler
	{

		private static final long serialVersionUID = -1833778393371284859L;

		public TheTransferHandler()
		{
			TheGeometrySourceHandler source = new TheGeometrySourceHandler();
			setSourceHandler(source);
			TheFileDestinationHandler destination = new TheFileDestinationHandler();
			setDestinationHandler(destination);
		}

		class TheGeometrySourceHandler extends GeometrySourceTransferHandler
		{

			private static final long serialVersionUID = -5370940808027275582L;

			@Override
			public int getSourceActions(JComponent c)
			{
				return COPY;
			}

			@Override
			public Collection<Geometry> getGeometries()
			{
				List<Geometry> geometries = new ArrayList<>();
				for (int i : getList().getSelectedIndices()) {
					Geometry geometry = listModel.getGeometry(i);
					if (geometry != null) {
						geometries.add(geometry);
					}
				}
				return geometries;
			}

		}

		class TheFileDestinationHandler extends FileTransferHandler
		{

			private static final long serialVersionUID = -5464544714541387347L;

			@Override
			public void handleFiles(List<File> handleFiles, TransferSupport ts)
			{
				JList.DropLocation dropLocation = (JList.DropLocation) ts
						.getDropLocation();
				int index = dropLocation.getIndex();
				for (File file : handleFiles) {
					logger.debug("handle to index: " + index + ": " + file);
				}
				listModel.addAll(handleFiles, index);
			}

		}
	}

}
