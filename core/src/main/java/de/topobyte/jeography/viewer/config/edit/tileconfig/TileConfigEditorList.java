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

package de.topobyte.jeography.viewer.config.edit.tileconfig;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.TileConfigDisk;
import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;
import de.topobyte.jeography.viewer.config.edit.selectable.Selectable;
import de.topobyte.jeography.viewer.config.edit.selectable.SelectionChain;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileConfigEditorList extends JPanel implements Scrollable
{

	private static final long serialVersionUID = -8258220504458574287L;

	private GridBagLayout layout;
	private int nConfigs = 0;
	private SelectionChain chain;

	private List<TileConfigEditorPanel> panels = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param configs
	 *            the tile configurations to edit
	 */
	public TileConfigEditorList(List<TileConfig> configs)
	{
		layout = new GridBagLayout();
		setLayout(layout);

		chain = new SelectionChain();

		for (TileConfig tileConfig : configs) {
			TileConfigEditorPanel editorPanel = null;
			if (tileConfig instanceof TileConfigUrlDisk) {
				editorPanel = new TileConfigEditorPanelUrlDisk(
						(TileConfigUrlDisk) tileConfig);
			} else if (tileConfig instanceof TileConfigDisk) {
				editorPanel = new TileConfigEditorPanelDisk(
						(TileConfigDisk) tileConfig);
			}
			panels.add(editorPanel);
			add((JComponent) editorPanel);
			chain.add(editorPanel);
		}
		setConstraints();
	}

	/*
	 * Scrollable implementation
	 */

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 1;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	/*
	 * end of Scrollable implementation
	 */

	/**
	 * @return the chain of selectable components in this list.
	 */
	public SelectionChain getSelectionChain()
	{
		return chain;
	}

	/**
	 * Add a tile-config to be displayed.
	 * 
	 * @param tileConfig
	 *            the new TileConfig
	 */
	public void add(TileConfigUrlDisk tileConfig)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;

		final TileConfigEditorPanelUrlDisk editorPanel = new TileConfigEditorPanelUrlDisk(
				tileConfig);
		c.gridy = nConfigs;
		add(editorPanel, c);
		panels.add(editorPanel);
		nConfigs += 1;
		chain.add(editorPanel);
		setConstraints();
	}

	/**
	 * Remove the denoted component from the list.
	 * 
	 * @param selected
	 *            the component to remove from the list.
	 */
	public void remove(Selectable selected)
	{
		remove((Component) selected);
		panels.remove(selected);
		nConfigs -= 1;
		setConstraints();
		chain.remove(selected);
	}

	private void setConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;

		c.gridy = 0;
		for (TileConfigEditorPanel panel : panels) {
			layout.setConstraints((JComponent) panel, c);
			c.gridy = c.gridy + 1;
		}
	}

	/**
	 * Set the values of the denoted configuration instance according to the
	 * settings in this GUI.
	 * 
	 * @param tileConfigs
	 *            the list of tileConfigs to populate
	 */
	public void setValues(List<TileConfig> tileConfigs)
	{
		for (TileConfigEditorPanel panel : panels) {
			TileConfig tileConfig = panel.generateTileConfig();
			tileConfigs.add(tileConfig);
		}
	}

}
