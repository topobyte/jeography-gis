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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TileConfigEditorPanelUrlDisk extends AbstractTileConfigEditorPanel
{

	private static final long serialVersionUID = 4562262009270282742L;

	private JTextField editName;
	private JTextField editUrl;
	private JTextField editPath;
	private JTextField editUserAgent;

	private final TileConfigUrlDisk originalTileConfig;

	/**
	 * Constructor
	 * 
	 * @param tileConfig
	 *            the tile configuration to edit.
	 */
	public TileConfigEditorPanelUrlDisk(TileConfigUrlDisk tileConfig)
	{
		super(new GridBagLayout());

		this.originalTileConfig = tileConfig;

		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		JLabel labelName = new JLabel("Name:");
		editName = new JTextField(tileConfig.getName());

		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0.0;
		add(labelName, c);
		c.gridx = 1;
		c.weightx = 1.0;
		add(editName, c);

		JLabel labelUrl = new JLabel("Url:");
		editUrl = new JTextField(tileConfig.getUrl());
		JLabel labelPath = new JLabel("Path:");
		editPath = new JTextField(tileConfig.getPath());
		JLabel labelUserAgent = new JLabel("User Agent:");
		editUserAgent = new JTextField(tileConfig.getUserAgent());

		for (JComponent comp : new JComponent[] { editName, editUrl, editPath,
				editUserAgent }) {
			comp.addFocusListener(this);
		}

		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0.0;
		add(labelUrl, c);
		c.gridx = 1;
		c.weightx = 1.0;
		add(editUrl, c);

		c.gridy = 2;
		c.gridx = 0;
		c.weightx = 0.0;
		add(labelPath, c);
		c.gridx = 1;
		c.weightx = 1.0;
		add(editPath, c);

		c.gridy = 3;
		c.gridx = 0;
		c.weightx = 0.0;
		add(labelUserAgent, c);
		c.gridx = 1;
		c.weightx = 1.0;
		add(editUserAgent, c);

	}

	@Override
	public TileConfig generateTileConfig()
	{
		TileConfigUrlDisk tileConfig = new TileConfigUrlDisk(
				originalTileConfig.getId(), editName.getText(),
				editUrl.getText(), editPath.getText());
		tileConfig.setUserAgent(editUserAgent.getText());
		return tileConfig;
	}

}
