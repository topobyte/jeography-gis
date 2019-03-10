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

package de.topobyte.jeography.viewer.config.edit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.edit.other.MiscOptionsPane;
import de.topobyte.jeography.viewer.config.edit.tileconfig.TileConfigEditorListPanel;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class ConfigurationEditor extends JPanel
		implements ActionListener
{

	private static final long serialVersionUID = 3497678160259202506L;

	private MiscOptionsPane miscOptions;
	private TileConfigEditorListPanel editorListTiles;
	private TileConfigEditorListPanel editorListOverlays;

	/**
	 * Create a new editor for the denoted configuration.
	 * 
	 * @param configuration
	 *            the configuration instance to edit.
	 */
	public ConfigurationEditor(Configuration configuration)
	{
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JTabbedPane tabbed = new JTabbedPane(SwingConstants.LEFT);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		add(tabbed, c);

		editorListTiles = new TileConfigEditorListPanel(
				configuration.getTileConfigs());
		editorListOverlays = new TileConfigEditorListPanel(
				configuration.getTileConfigsOverlay());

		miscOptions = new MiscOptionsPane(configuration);
		JScrollPane jspMisc = new JScrollPane();
		jspMisc.setViewportView(miscOptions);

		tabbed.add("General", jspMisc);
		tabbed.add("Tiles", editorListTiles);
		tabbed.add("Overlays", editorListOverlays);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(1, 2));

		JButton buttonCancel = new JButton("Cancel");
		JButton buttonOk = new JButton("Ok");

		buttonGrid.add(buttonCancel);
		buttonGrid.add(buttonOk);

		buttons.add(Box.createHorizontalGlue());
		buttons.add(buttonGrid);

		c.gridy = 1;
		c.weighty = 0.0;
		add(buttons, c);

		buttonCancel.setActionCommand("cancel");
		buttonOk.setActionCommand("ok");
		buttonCancel.addActionListener(this);
		buttonOk.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("ok")) {
			ok();
		} else if (e.getActionCommand().equals("cancel")) {
			cancel();
		}
	}

	/**
	 * Called when the OK button has been clicked
	 */
	public abstract void ok();

	/**
	 * Called when the cancel button has been clicked
	 */
	public abstract void cancel();

	/**
	 * @return the resulting configuration.
	 */
	public Configuration getConfiguration()
	{
		Configuration configuration = new Configuration();
		miscOptions.setValues(configuration);
		editorListTiles.setValues(configuration.getTileConfigs());
		editorListOverlays.setValues(configuration.getTileConfigsOverlay());
		return configuration;
	}

}
