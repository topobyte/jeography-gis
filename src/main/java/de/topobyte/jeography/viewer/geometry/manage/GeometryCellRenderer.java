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
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.manage.filetree.Entry;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Leaf;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
// public class GeometryCellRenderer extends JLabel implements TreeCellRenderer
// {

public class GeometryCellRenderer extends JPanel implements TreeCellRenderer
{

	static final Logger logger = LoggerFactory
			.getLogger(GeometryCellRenderer.class);

	private static final long serialVersionUID = -2785907065519986997L;

	private DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();

	private JLabel label = new JLabel();

	private Icon iconDirectory = null;
	private Icon iconPolygon = null;
	private Icon iconPolygons = null;

	/**
	 * Create the GeometryCellRenderer
	 */
	public GeometryCellRenderer()
	{
		try {
			BufferedImage imageDirectory = ImageIO
					.read(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("res/images/directory.png"));
			iconDirectory = new ImageIcon(imageDirectory);
			BufferedImage imagePolygon = ImageIO
					.read(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("res/images/polygon.png"));
			iconPolygon = new ImageIcon(imagePolygon);
			BufferedImage imagePolygons = ImageIO
					.read(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("res/images/polygonn.png"));
			iconPolygons = new ImageIcon(imagePolygons);
		} catch (IOException e) {
			// do nothing
		}

		// setLayout(new GridBagLayout());
		setLayout(new BoxLayout(this, 0));

		label.setBorder(BorderFactory.createEmptyBorder());
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		// setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// setBorder(BorderFactory.createEmptyBorder(-5, -5, -5, -5));
		// label.setBorder(BorderFactory.createEmptyBorder(-5, -5, -5, -5));
		Insets insets = getBorder().getBorderInsets(label);
		logger.debug(insets.toString());

		// GridBagConstraints c = new GridBagConstraints();
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.weightx = 1.0;
		// add(label, c);
		add(label);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object e,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{

		//
		// Component component = super.getTreeCellRendererComponent(tree, value,
		// selected,
		// expanded, leaf, row,
		// hasFocus);

		logger.debug("" + e.getClass() + " " + row);

		Entry entry = (Entry) e;

		if (entry instanceof Node) {
			if (expanded) {
				// label.setIcon(dtcr.getDefaultOpenIcon());
				label.setIcon(iconDirectory);
			} else {
				// label.setIcon(dtcr.getDefaultClosedIcon());
				label.setIcon(iconDirectory);
			}
		} else if (entry instanceof Leaf) {
			// label.setIcon(null);
			String name = entry.toString();
			if (name.endsWith(".tgs")) {
				label.setIcon(iconPolygons);
			} else {
				label.setIcon(iconPolygon);
			}
		}

		// setPreferredSize(new Dimension(250, 40));
		add(label);
		label.setText(entry.toString());

		boolean isDropCell = false;
		Color fg = dtcr.getForeground();

		JTree.DropLocation dropLocation = tree.getDropLocation();
		if (dropLocation != null && dropLocation.getChildIndex() == -1
				&& tree.getRowForPath(dropLocation.getPath()) == row) {

			Color col = UIManager.getColor("Tree.dropCellForeground");
			if (col != null) {
				fg = col;
			} else {
				fg = dtcr.getTextSelectionColor();
			}

			isDropCell = true;
		}

		setForeground(fg);
		setBackground(dtcr.getBackground());
		label.setForeground(fg);
		label.setBackground(dtcr.getBackground());

		if (isDropCell) {
			// setBackground(new Color(0xFFFFFF00, true));
		} else {
			// setBackground(new Color(0xFFFFFF00, true));
			if (selected && hasFocus) {
				// label.setForeground(new Color(0xFFFFFF00, true));
				setBackground(new Color(0x99FF0000, true));
			} else if (selected && !hasFocus) {
				setBackground(new Color(0x66FF0000, true));
			} else if (hasFocus) {
				setBackground(new Color(0x99FFFF00, true));
			} else {
				// setBackground(new Color(0xFF0000FF, true));
				setBackground(dtcr.getBackground());
			}
		}

		// int width = tree.getWidth();
		//

		return this;
		// return component;
	}

}
