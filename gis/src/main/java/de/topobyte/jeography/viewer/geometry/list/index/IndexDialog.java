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

package de.topobyte.jeography.viewer.geometry.list.index;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.locationtech.jts.geom.Geometry;

import de.topobyte.jeography.viewer.geometry.list.panels.GeometryIndexPanel;
import de.topobyte.jsi.GenericSpatialIndex;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class IndexDialog extends JPanel
{

	private static final long serialVersionUID = -6910935875383454803L;

	/**
	 * Create a dialog to present the denoted index.
	 * 
	 * @param index
	 *            the index to present.
	 */
	public IndexDialog(GenericSpatialIndex<Geometry> index)
	{

		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		GeometryIndexPanel indexPanel = new GeometryIndexPanel(index, true,
				false);

		indexPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

		c.weightx = 1.0;
		c.weighty = 1.0;
		add(indexPanel, c);
	}

	/**
	 * Display the dialog.
	 * 
	 * @param source
	 *            the component to get the containing frame from.
	 */
	public void showDialog(JComponent source)
	{

		JFrame frame = Components.getContainingFrame(source);
		JDialog dialog = new JDialog(frame, "Geometry Index");
		dialog.setContentPane(this);
		dialog.pack();
		dialog.setVisible(true);
	}

}
