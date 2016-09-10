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

package de.topobyte.jeography.viewer.geometry.list.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Lineal;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.geom.Puntal;

/**
 * A panel that can represent a geometry for example within a list rendering
 * component.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryPanel extends JPanel
{

	private static final long serialVersionUID = -8093536930193936865L;

	private Geometry geometry;

	private JLabel label = new JLabel();
	private JLabel label1 = new JLabel();
	private JLabel label2 = new JLabel();

	/**
	 * Public constructor that sets up the panel's components but does not
	 * initialization.
	 */
	public GeometryPanel()
	{
		init();
	}

	/**
	 * Public constructor that initializes the panel to display information
	 * about the denoted geometry.
	 * 
	 * @param geometry
	 *            the geometry to display information about.
	 */
	public GeometryPanel(Geometry geometry)
	{
		this();
		setup(geometry);
	}

	private void init()
	{
		this.setOpaque(true);

		this.removeAll();

		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);

		Box box = new Box(BoxLayout.Y_AXIS);

		label.setIcon(new Icon() {

			@Override
			public int getIconHeight()
			{
				return 30;
			}

			@Override
			public int getIconWidth()
			{
				return 30;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y)
			{
				// do nothing
			}

		});
		this.add(label);
		box.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(box);

		// label.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		// label1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		// label2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		// box.setBorder(BorderFactory.createLineBorder(Color.red, 1));
		// panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		box.add(Box.createRigidArea(new Dimension(0, 2)));
		box.add(label1);
		box.add(Box.createRigidArea(new Dimension(0, 2)));
		box.add(label2);
		box.add(Box.createRigidArea(new Dimension(0, 2)));
	}

	/**
	 * Setup the panel to display information about the denoted geometry.
	 * 
	 * @param geometry
	 *            the geometry to display information about.
	 */
	public void setup(Geometry geometry)
	{
		this.geometry = geometry;
		label1.setText(getLabel1Text());
		label2.setText(getLabel2Text());
	}

	/**
	 * Setup the panel to display information about the denoted geometry and
	 * reinitialize the panel.
	 * 
	 * @param geometry
	 *            the geometry to display information about.
	 */
	public void setupAndInit(Geometry geometry)
	{
		setup(geometry);
		init();
	}

	/**
	 * @return the currently displayed geometry.
	 */
	protected Geometry getGeometry()
	{
		return geometry;
	}

	private String getLabel1Text()
	{
		return geometry.getGeometryType();
	}

	private String getLabel2Text()
	{
		if (geometry instanceof Puntal) {
			Point p = (Point) geometry;
			Coordinate coord = p.getCoordinate();
			return String.format("%6f,%6f", coord.x, coord.y);
		} else if (geometry instanceof Lineal) {
			return String.format("%d points", geometry.getNumPoints());
		} else if (geometry instanceof Polygonal) {
			return String.format("%d points", geometry.getNumPoints());
		}
		return String.format("%d points", geometry.getNumPoints());
	}

}
