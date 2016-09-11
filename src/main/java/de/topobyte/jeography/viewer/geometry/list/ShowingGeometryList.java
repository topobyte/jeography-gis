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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.viewer.core.PaintListener;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.ImageManagerGeometry;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryListTransferhandler;
import de.topobyte.jeography.viewer.geometry.manage.EditGeometryStyleDialog;
import de.topobyte.jeography.viewer.geometry.manage.GeometryContainer;
import de.topobyte.jeography.viewer.geometry.manage.GeometrySourceNull;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStyle;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStylePanel;
import de.topobyte.jeography.viewer.geometry.manage.GeometryStylesModel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowingGeometryList extends JPanel implements PaintListener,
		ListDataListener
{

	final static Logger logger = LoggerFactory
			.getLogger(ShowingGeometryList.class);

	private static final long serialVersionUID = -3418352956823109052L;

	private Viewer viewer;

	private GeometryStyle style;

	private GeometryStylePanel gsp;
	private GeomList geomList;

	private ImageManagerGeometry manager;

	/**
	 * Public constructor.
	 * 
	 * @param viewer
	 *            the viewer to use.
	 */
	public ShowingGeometryList(Viewer viewer)
	{
		this.viewer = viewer;

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

		manager = new ImageManagerGeometry(viewer.getMapWindow());
		manager.addLoadListener(viewer);

		JPanel buttons = new JPanel();
		BoxLayout boxLayout = new BoxLayout(buttons, BoxLayout.LINE_AXIS);
		buttons.setLayout(boxLayout);

		style = new GeometryStyle();

		gsp = new GeometryStylePanel();
		gsp.setStyle(new GeometryStyle());
		gsp.setPreferredSize(new Dimension(40, 40));
		gsp.setMaximumSize(new Dimension(40, 40));

		gsp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					editStyle();
				}
			}
		});

		// JComponent trash = new TrashButton("trash");
		JComponent trash = new TrashLabel("trash");
		buttons.add(trash);
		buttons.add(gsp);

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

		viewer.addPaintListener(this);

		geomList.getModel().addListDataListener(this);
	}

	/**
	 * @return the GeomList used.
	 */
	public GeomList getList()
	{
		return geomList;
	}

	@Override
	public void onPaint(TileMapWindow mapWindow, Graphics g)
	{
		for (TileOnWindow tow : mapWindow) {
			BufferedImage imageOverlay = manager.get(tow);
			if (imageOverlay != null)
				g.drawImage(imageOverlay, tow.getDX(), tow.getDY(), null);
		}
	}

	private void update()
	{
		int id = 1;
		List<GeometryContainer> geometries = new ArrayList<>();
		for (int i = 0; i < geomList.getModel().getSize(); i++) {
			Geometry geometry = geomList.getModel().getElementAt(i);
			GeoObject tg = new GeoObject(geometry);
			GeometryContainer gc = new GeometryContainer(id++, tg,
					new GeometrySourceNull());
			geometries.add(gc);
		}
		manager.setGeometries(style, geometries);

		viewer.repaint();
	}

	@Override
	public void contentsChanged(ListDataEvent e)
	{
		update();
	}

	@Override
	public void intervalAdded(ListDataEvent e)
	{
		update();
	}

	@Override
	public void intervalRemoved(ListDataEvent e)
	{
		update();
	}

	void editStyle()
	{
		GeometryStylesModel model = new GeometryStylesModel();
		EditGeometryStyleDialog dialog = new EditGeometryStyleDialog(model,
				style);
		boolean accept = dialog.showDialog() == JOptionPane.OK_OPTION;
		if (!accept) {
			return;
		}

		style.setMany(dialog.getColorFill(), dialog.getColorOutline(), true,
				true, dialog.getDrawNodes(), dialog.getLineWidth());
		gsp.setStyle(style);
		gsp.repaint();

		update();
	}

}
