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

package de.topobyte.jeography.viewer.selection.pane;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.jeography.viewer.selection.polygonal.PolySelectionAdapter;
import de.topobyte.jeography.viewer.selection.polygonal.Selection;
import de.topobyte.jeography.viewer.selection.polygonal.SelectionChangeListener;
import de.topobyte.jeography.viewer.selection.polygonal.SelectionTree;

/**
 * A component to inspect a polygonal selection.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PolyPane extends JPanel implements SelectionChangeListener
{

	private static final long serialVersionUID = -1845206202334930327L;

	// private final JeographyGIS gis;
	// private final PolySelectionAdapter polySelectionAdapter;

	/**
	 * Create a new instance of a PolyPane.
	 * 
	 * @param gis
	 *            the JeographyGIS instance in use.
	 * @param polySelectionAdapter
	 *            the polygonal selection adapter that backs this view.
	 */
	public PolyPane(JeographyGIS gis, PolySelectionAdapter polySelectionAdapter)
	{
		// this.gis = gis;
		// this.polySelectionAdapter = polySelectionAdapter;

		Selection selection = polySelectionAdapter.getSelection();
		selection.addSelectionChangeListener(this);

		SelectionTree selectionTree = new SelectionTree(selection);
		JScrollPane jspTree = new JScrollPane();
		jspTree.setViewportView(selectionTree);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(jspTree, c);

		jspTree.setPreferredSize(new Dimension(100, 50));
	}

	@Override
	public void pixelValuesChanged()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void geographicValuesChanged()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void selectionChanged()
	{
		// TODO Auto-generated method stub
	}

}
