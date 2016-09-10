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

package de.topobyte.jeography.viewer.geometry.select;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.topobyte.jeography.viewer.geometry.list.TrashLabel;
import de.topobyte.jeography.viewer.geometry.list.panels.ContextEnabledGeometryPanel;
import de.topobyte.jeography.viewer.geometry.list.panels.GeometryIndexPanel;
import de.topobyte.jsi.GenericRTree;
import de.topobyte.jsi.GenericSpatialIndex;
import de.topobyte.jsijts.JsiAndJts;
import de.topobyte.swing.util.FrameHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometrySelectionOperation extends JPanel
{

	private static final long serialVersionUID = -1635794074628327505L;

	private GeometryIndexPanel indexPanel;
	private ContextEnabledGeometryPanel inputGeometryPanel;
	private ContextEnabledGeometryPanel outputGeometryPanel;

	/**
	 * Test the component by displaying it in a frame.
	 * 
	 * @param args
	 *            none.
	 */
	public static void main(String[] args)
	{
		GeometrySelectionOperation gso = new GeometrySelectionOperation();
		JFrame frame = FrameHelper.showFrameWithComponent("test", gso, 200,
				400, 0, 0, true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Create an instance of the selection operation component.
	 */
	public GeometrySelectionOperation()
	{
		GenericRTree<Geometry> index = new GenericRTree<>();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		indexPanel = new GeometryIndexPanel(index, true, true);

		// DragEnabledGeometryIndexPanel indexPanel = new
		// DragEnabledGeometryIndexPanel(index);
		indexPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

		GeometryFactory factory = new GeometryFactory();
		GeometryCollection input = factory
				.createGeometryCollection(new Geometry[0]);
		GeometryCollection output = factory
				.createGeometryCollection(new Geometry[0]);

		inputGeometryPanel = new ContextEnabledGeometryPanel(input, true, true);
		inputGeometryPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

		outputGeometryPanel = new ContextEnabledGeometryPanel(output, true,
				false);
		outputGeometryPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

		c.gridx = 0;
		c.gridy = -1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1.0;
		c.weighty = 0.0;

		JPanel buttons = new JPanel();
		BoxLayout boxLayout = new BoxLayout(buttons, BoxLayout.LINE_AXIS);
		buttons.setLayout(boxLayout);

		JComponent trash = new TrashLabel("trash");
		buttons.add(trash);

		JButton button = new JButton("calculate");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				calculateResult();
			}
		});
		buttons.add(button);

		c.gridy++;
		add(buttons, c);

		c.gridy++;
		add(new JLabel("index"), c);
		c.gridy++;
		add(indexPanel, c);

		c.gridy++;
		add(new JLabel("input"), c);
		c.gridy++;
		add(inputGeometryPanel, c);

		c.gridy++;
		add(new JLabel("output"), c);
		c.gridy++;
		add(outputGeometryPanel, c);

		c.gridy++;
		c.weighty = 1.0;
		add(new JPanel(), c);
	}

	void calculateResult()
	{
		GenericSpatialIndex<Geometry> index = indexPanel.getData();
		Geometry input = inputGeometryPanel.getData();

		Set<Geometry> intersects = index.intersects(JsiAndJts
				.toRectangle(input));

		Collection<Geometry> results = new ArrayList<>();
		for (Geometry intersecting : intersects) {
			if (intersecting.intersects(input)) {
				results.add(intersecting);
			}
		}

		GeometryFactory factory = new GeometryFactory();
		GeometryCollection result = factory.createGeometryCollection(results
				.toArray(new Geometry[0]));

		outputGeometryPanel.setup(result);

		System.out.println(index.size());
		System.out.println(input);
		System.out.println(result);
	}

}
