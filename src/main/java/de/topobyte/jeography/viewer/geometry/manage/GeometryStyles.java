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

package de.topobyte.jeography.viewer.geometry.manage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryStyles extends JPanel
{

	final static Logger logger = LoggerFactory.getLogger(GeometryStyles.class);

	private static final long serialVersionUID = -7132631362696225788L;

	JList<GeometryStyle> list;
	GeometryStylesModel model;

	private ListCellRenderer<GeometryStyle> renderer;

	/**
	 * Create a new GeometryStyles panel
	 */
	public GeometryStyles()
	{
		model = new GeometryStylesModel();

		renderer = new GeometryStylesRenderer();
		list = new JList<>(model);
		list.setCellRenderer(renderer);

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(jsp, c);

		model.add(new GeometryStyle("Blue", "0x330000ff", "0xff0000ff", true,
				true, false, 1.0));
		model.add(new GeometryStyle("Red", "0xaaff0000", "0xff0000ff", true,
				true, false, 3.0));
		// for (int i = 0; i < 10; i++) {
		// model.add(new GeometryStyle("Green " + i, "0x3300ff00", "0x66ffff00",
		// true, true,
		// 2.0));
		// }
		model.add(new GeometryStyle("Green", "0x3300ff00", "0x66ffff00", true,
				true, false, 2.0));

		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event)
			{
				if (event.getClickCount() == 2) {
					int index = list.locationToIndex(event.getPoint());
					GeometryStyle style = model.getElementAt(index);
					edit(index, style);
				}
			}
		});
	}

	void edit(int index, GeometryStyle style)
	{
		EditGeometryStyleDialog dialog = new EditGeometryStyleDialog(model,
				style);
		boolean accept = dialog.showDialog() == JOptionPane.OK_OPTION;
		if (!accept) {
			return;
		}
		logger.debug("disposed: " + index);

		// TODO: only if (changed)
		style.setMany(dialog.getColorFill(), dialog.getColorOutline(), true,
				true, dialog.getDrawNodes(), dialog.getLineWidth());
	}

	/**
	 * @return the model in use.
	 */
	public GeometryStylesModel getModel()
	{
		return model;
	}

	/**
	 * Simple test to show this list.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeometryStyles styles = new GeometryStyles();
		frame.setContentPane(styles);
		frame.setSize(new Dimension(400, 600));
		frame.setVisible(true);
	}
}

class GeometryStylesRenderer implements ListCellRenderer<GeometryStyle>
{

	private JPanel panel;
	private JPanel p1;
	private JPanel p2;
	private GeometryStylePanel gsp;
	private JLabel label;

	public GeometryStylesRenderer()
	{
		panel = new JPanel(new GridBagLayout());
		p1 = new JPanel();
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p2.setBackground(new Color(0x00000000, true));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.insets.right = 4;
		panel.add(p1, c);
		c.insets.right = 0;

		c.weightx = 1.0;
		panel.add(p2, c);

		label = new JLabel();
		p2.add(label);

		gsp = new GeometryStylePanel();
		p1.add(gsp);
		p1.setBorder(BorderFactory.createLineBorder(new Color(0x000000), 1));
		gsp.setPreferredSize(new Dimension(40, 40));
		p1.setBackground(new Color(0xffffffff, true));

		Border empty = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Border line = BorderFactory.createLineBorder(
				new Color(0x33000000, true), 2);
		Border b1 = BorderFactory.createCompoundBorder(empty, line);
		Border b2 = BorderFactory.createCompoundBorder(b1, empty);
		panel.setBorder(b2);
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends GeometryStyle> list, GeometryStyle value,
			int index, boolean isSelected, boolean cellHasFocus)
	{

		GeometryStyle style = value;
		label.setText(style.getName());
		gsp.setStyle(style);

		panel.setBackground(new Color(0x00000000, true));
		if (isSelected) {
			panel.setBackground(new Color(0x66ff0000, true));
		}
		if (cellHasFocus && isSelected) {
			panel.setBackground(new Color(0x99ff0000, true));
		} else if (cellHasFocus && !isSelected) {
			panel.setBackground(new Color(0x99ffff00, true));
		}

		return panel;
	}

}
