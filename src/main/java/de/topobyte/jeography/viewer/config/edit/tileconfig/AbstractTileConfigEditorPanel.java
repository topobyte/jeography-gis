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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.UIManager;

import de.topobyte.jeography.viewer.config.edit.selectable.SelectionEvent;
import de.topobyte.jeography.viewer.config.edit.selectable.SelectionListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class AbstractTileConfigEditorPanel extends JPanel implements
		TileConfigEditorPanel, FocusListener
{

	private static final long serialVersionUID = 5194579240225144159L;

	/**
	 * @param manager
	 *            the LayoutManager to use.
	 */
	public AbstractTileConfigEditorPanel(LayoutManager manager)
	{
		super(manager);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (isSelected()) {
			g2d.getColor();
			Color color = UIManager.getColor("List.selectionBackground");
			g2d.setColor(color);
			// g2d.setStroke(new BasicStroke(3.0f));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	private boolean selected = false;

	@Override
	public boolean isSelected()
	{
		return selected;
	}

	@Override
	public void setSelected(boolean state)
	{
		if (selected == state) {
			return;
		}
		selected = state;
		triggerSelectionChange(selected);
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		if (selected) {
			return;
		}
		selected = true;
		triggerSelectionChange(selected);
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		// do nothing
	}

	private List<SelectionListener> listeners = new ArrayList<>();

	@Override
	public void addSelectionListener(SelectionListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeSelectionListener(SelectionListener listener)
	{
		listeners.remove(listener);
	}

	private void triggerSelectionChange(boolean state)
	{
		SelectionEvent event = new SelectionEvent(state);
		for (SelectionListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}

}
