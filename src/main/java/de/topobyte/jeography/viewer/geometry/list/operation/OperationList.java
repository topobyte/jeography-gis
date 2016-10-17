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

package de.topobyte.jeography.viewer.geometry.list.operation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.topobyte.jeography.viewer.geometry.action.ExportAction;
import de.topobyte.jeography.viewer.geometry.action.InspectCollectionAction;
import de.topobyte.jeography.viewer.geometry.list.DragEnabledGeometryPanel;
import de.topobyte.jeography.viewer.geometry.list.GeomList;
import de.topobyte.jeography.viewer.geometry.list.PreviewMouseAdapter;
import de.topobyte.jeography.viewer.geometry.list.TrashLabel;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometryListTransferhandler;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class OperationList extends JPanel implements ListDataListener
{

	final static Logger logger = LoggerFactory.getLogger(OperationList.class);

	private static final long serialVersionUID = -3418352956823109052L;

	private Geometry result;

	private DragEnabledGeometryPanel resultPanel;
	private GeomList list;

	private JButton resultButton;

	private JPanel controlsContainer;
	private JComponent controls = null;

	/**
	 * Public constructor.
	 */
	public OperationList()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		controlsContainer = new JPanel();

		result = getEmptyGeometry();

		// resultPanel = new GeometryPanel(result);
		resultPanel = new DragEnabledGeometryPanel(result);

		resultPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

		resultPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						showPreview(getResult());
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					showContext(e.getX(), e.getY());
				}
			}
		});

		list = new GeomList();
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);
		// list.setFont(new Font("Arial", 0, 20));
		list.setDropMode(DropMode.INSERT);

		TransferHandler transferhandler = new GeometryListTransferhandler(list);
		list.setTransferHandler(transferhandler);
		list.setDragEnabled(true);

		PreviewMouseAdapter previewMouseAdapter = new PreviewMouseAdapter(list);
		list.addMouseListener(previewMouseAdapter);

		JPanel buttons = new JPanel();
		BoxLayout boxLayout = new BoxLayout(buttons, BoxLayout.LINE_AXIS);
		buttons.setLayout(boxLayout);

		// JComponent trash = new TrashButton("trash");
		JComponent trash = new TrashLabel("trash");
		buttons.add(trash);

		resultButton = new JButton("calculate result");
		buttons.add(resultButton);
		resultButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				calculateResult();
			}

		});

		c.fill = GridBagConstraints.BOTH;

		c.gridy = 0;
		c.weighty = 0.0;
		add(buttons, c);

		c.gridy++;
		add(resultPanel, c);

		c.gridy++;
		// add(controlsContainer, c);

		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(jsp, c);

		list.getModel().addListDataListener(this);
	}

	Geometry getResult()
	{
		return result;
	}

	private Geometry getEmptyGeometry()
	{
		Geometry geom = new GeometryFactory().createGeometryCollection(null);
		return geom;
	}

	/**
	 * @return the GeomList used.
	 */
	public GeomList getList()
	{
		return list;
	}

	@Override
	public void contentsChanged(ListDataEvent e)
	{
		updateHappened();
	}

	@Override
	public void intervalAdded(ListDataEvent e)
	{
		updateHappened();
	}

	@Override
	public void intervalRemoved(ListDataEvent e)
	{
		updateHappened();
	}

	private void updateHappened()
	{
		if (getList().getModel().getSize() == 0) {
			resultButton.setEnabled(false);
		} else {
			resultButton.setEnabled(true);
		}
	}

	/**
	 * Calculate the result of the operation. This method is used to receive the
	 * result of the operation performed by a subclass.
	 * 
	 * @param geometries
	 *            the geometries to invoke the operation with.
	 * @return the resulting geometry.
	 */
	protected abstract Geometry operationResult(List<Geometry> geometries);

	/**
	 * The operation to be performed to create the result
	 */
	protected void calculateResult()
	{
		List<Geometry> geometries = new ArrayList<>();
		for (int i = 0; i < getList().getModel().getSize(); i++) {
			Geometry geometry = getList().getModel().getElementAt(i);
			geometries.add(geometry);
		}

		result = operationResult(geometries);

		resultUpdated();
	}

	/**
	 * Called whenever the result has been calculated.
	 */
	protected void resultUpdated()
	{
		resultPanel.setup(result);
	}

	void showPreview(Geometry geometry)
	{
		new GeometryPreview().showViewerWithFile(this, geometry, "Preview");
	}

	void showContext(int x, int y)
	{
		JPopupMenu popup = new JPopupMenu();

		ExportAction exportAction = new ExportAction(result);
		JMenuItem itemExport = new JMenuItem(exportAction);
		popup.add(itemExport);

		if (result instanceof GeometryCollection) {
			JFrame frame = Components.getContainingFrame(this);
			InspectCollectionAction inspectAction = new InspectCollectionAction(
					frame, result);
			JMenuItem itemInspect = new JMenuItem(inspectAction);
			popup.add(itemInspect);
		}

		popup.show(resultPanel, x, y);
	}

	/**
	 * Set the controls to display in the upper region of the list panel.
	 * 
	 * @param component
	 *            the component to display for controlling objects of the
	 *            operation.
	 */
	public void setOperationControls(JComponent component)
	{
		this.controls = component;

		GridBagConstraints cx = new GridBagConstraints();

		cx.fill = GridBagConstraints.BOTH;
		cx.weightx = 1.0;
		cx.weighty = 0.0;
		cx.gridy = 2;
		controlsContainer.add(controls, cx);

		add(controlsContainer, cx);

		controlsContainer.setBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		controlsContainer.removeAll();

		controlsContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		controlsContainer.add(controls, c);
	}

}
