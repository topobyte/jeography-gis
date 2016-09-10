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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.edit.selectable.Selectable;
import de.topobyte.jeography.viewer.config.edit.selectable.SelectionChainEvent;
import de.topobyte.jeography.viewer.config.edit.selectable.SelectionChainListener;
import de.topobyte.jeography.viewer.config.edit.tileconfig.action.AddTileConfigAction;
import de.topobyte.jeography.viewer.config.edit.tileconfig.action.DuplicateTileConfigAction;
import de.topobyte.jeography.viewer.config.edit.tileconfig.action.RemoveTileConfigAction;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileConfigEditorListPanel extends JPanel implements
		SelectionChainListener
{

	private static final long serialVersionUID = 6047260382147500642L;

	private JButton buttonAdd;
	private JButton buttonDuplicate;
	private JButton buttonRemove;

	private TileConfigEditorList list;

	/**
	 * Constructor
	 * 
	 * @param configs
	 *            the tile configurations edited.
	 */
	public TileConfigEditorListPanel(List<TileConfig> configs)
	{
		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setAlignmentX(LEFT_ALIGNMENT);

		// JPanel buttons = new JPanel(new GridBagLayout());
		// buttons.setOpaque(true);
		// buttons.setBackground(Color.RED);

		list = new TileConfigEditorList(configs);
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;

		c.gridy = 0;
		c.weighty = 0.0;
		add(buttonBox, c);

		c.gridy = 1;
		c.weighty = 1.0;
		add(jsp, c);

		buttonAdd = new JButton(new AddTileConfigAction(list));
		buttonDuplicate = new JButton(new DuplicateTileConfigAction(list));
		buttonRemove = new JButton(new RemoveTileConfigAction(list));

		// c.fill = GridBagConstraints.NONE;
		// c.weightx = 0.0;
		// c.gridy = 0;
		//
		// c.gridx = 0;
		// buttons.add(buttonAdd, c);
		// c.gridx = 1;
		// buttons.add(buttonDuplicate, c);
		// c.gridx = 2;
		// buttons.add(buttonRemove, c);
		buttonBox.add(buttonAdd);
		buttonBox.add(buttonDuplicate);
		buttonBox.add(buttonRemove);

		setActive(false);

		list.getSelectionChain().addSelectionChainListener(this);
	}

	private void setActive(boolean active)
	{
		buttonAdd.setEnabled(true);
		buttonDuplicate.setEnabled(active);
		buttonRemove.setEnabled(active);
	}

	@Override
	public void selectionChanged(SelectionChainEvent event)
	{
		Selectable current = (Selectable) event.getSelectionChain()
				.getCurrent();
		boolean active = current != null;
		setActive(active);
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
		list.setValues(tileConfigs);
	}

}
