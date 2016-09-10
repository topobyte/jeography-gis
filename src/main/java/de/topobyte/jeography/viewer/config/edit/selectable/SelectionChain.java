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

package de.topobyte.jeography.viewer.config.edit.selectable;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SelectionChain
{

	private Selectable current = null;

	private List<SelectionChainListener> listeners = new ArrayList<>();

	/**
	 * Remove a component from this chain of selectable components.
	 * 
	 * @param component
	 *            the component to remove.
	 */
	public void remove(final Selectable component)
	{
		// not so much to do here, just reset current to null, because we must
		// not
		// use any dangling references to components that have been remove
		// anymore.
		if (current == component) {
			current = null;
			SelectionChainEvent event = new SelectionChainEvent(this);
			fireSelectionChainEvent(event);
		}
		// removal of listeners not necessary since the components are probably
		// not in use
		// anymore.
	}

	/**
	 * Add a component to this chain of selectable components.
	 * 
	 * @param selectable
	 *            the component to add.
	 */
	public void add(final Selectable selectable)
	{
		final JComponent component = (JComponent) selectable;

		component.setOpaque(true);
		component.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				component.requestFocusInWindow();
			}
		});

		selectable.addSelectionListener(new SelectionListener() {

			@Override
			public void selectionChanged(SelectionEvent event)
			{
				if (getCurrent() != selectable) {
					SelectionChain.this.setCurrent(selectable);
					component.repaint();
				}
			}
		});

		component.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e)
			{
				// do nothing
			}

			@Override
			public void focusGained(FocusEvent e)
			{
				component.repaint();
				SelectionChain.this.setCurrent(selectable);
			}
		});
	}

	/**
	 * Set the currently selected component within this chain
	 * 
	 * @param component
	 *            the selected component.
	 */
	protected void setCurrent(Selectable component)
	{
		JComponent before = null, now = null;

		if (current != null) {
			current.setSelected(false);
			before = (JComponent) current;
		}
		now = (JComponent) component;
		component.setSelected(true);
		current = component;

		if (before != null) {
			before.repaint();
		}
		now.repaint();

		SelectionChainEvent event = new SelectionChainEvent(this);
		fireSelectionChainEvent(event);
	}

	/**
	 * Add a listener to receive notifications about selection events
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addSelectionChainListener(SelectionChainListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Add a listener to receive notifications about selection events
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeSelectionChainListener(SelectionChainListener listener)
	{
		listeners.remove(listener);
	}

	private void fireSelectionChainEvent(SelectionChainEvent event)
	{
		for (SelectionChainListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}

	/**
	 * @return the currently selected component.
	 */
	public JComponent getCurrent()
	{
		return (JComponent) current;
	}

}
