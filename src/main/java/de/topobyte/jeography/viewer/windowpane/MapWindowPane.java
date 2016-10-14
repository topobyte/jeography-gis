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

package de.topobyte.jeography.viewer.windowpane;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.viewer.windowpane.patterns.Formatters;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MapWindowPane extends JPanel
{

	private static final long serialVersionUID = -3817433094278662941L;

	final MapWindow mapWindow;

	JLabel labelCenterLon;
	JLabel labelCenterLat;

	/**
	 * Create a new Dialog.
	 * 
	 * @param parent
	 *            the parent frame.
	 * @param title
	 *            the title of the dialog.
	 * @param mapWindow
	 *            the MapWindow instance to monitor.
	 */
	public MapWindowPane(final MapWindow mapWindow)
	{
		this.mapWindow = mapWindow;

		labelCenterLon = new JLabel();
		labelCenterLat = new JLabel();

		mapWindow.addChangeListener(new MapWindowChangeListener() {

			@Override
			public void changed()
			{
				updateCenterPosition();
			}
		});
		updateCenterPosition();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel("center position:");
		JLabel labelLon = new JLabel("lon:");
		JLabel labelLat = new JLabel("lat:");

		List<JButton> buttons = new ArrayList<>();
		JButton buttonOsmWeb = new PatternUrlButton("Openstreetmap Mapnik",
				mapWindow,
				"http://www.openstreetmap.org/?lat=%f&lon=%f&zoom=%d&layers=M");
		JButton buttonPotlatch1 = new PatternUrlButton("Potlatch 1", mapWindow,
				"http://www.openstreetmap.org/edit?editor=potlatch&lat=%f&lon=%f&zoom=%d&layers=M");
		JButton buttonPotlatch2 = new PatternUrlButton("Potlatch 2", mapWindow,
				"http://www.openstreetmap.org/edit?editor=potlatch2&lat=%f&lon=%f&zoom=%d&layers=M");

		JButton buttonCopyFpLatLon = new CoordinateFormatterClipboardButton(
				Formatters.FORMATTER_FP_LAT_LON, mapWindow);
		JButton buttonCopyFpLonLat = new CoordinateFormatterClipboardButton(
				Formatters.FORMATTER_FP_LON_LAT, mapWindow);
		JButton buttonCopyDegMinSec = new CoordinateFormatterClipboardButton(
				Formatters.FORMATTER_DEGREES, mapWindow);
		JButton buttonCopyXml = new CoordinateFormatterClipboardButton(
				Formatters.FORMATTER_XML, mapWindow);

		buttons.add(buttonCopyFpLatLon);
		buttons.add(buttonCopyFpLonLat);
		buttons.add(buttonCopyDegMinSec);
		buttons.add(buttonCopyXml);
		buttons.add(buttonOsmWeb);
		buttons.add(buttonPotlatch1);
		buttons.add(buttonPotlatch2);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.PAGE_START;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(label, c);

		c.gridwidth = 1;
		c.gridy = 1;
		c.gridx = 0;
		add(labelLon, c);
		c.gridx = 1;
		add(labelCenterLon, c);

		c.gridy = 2;
		c.gridx = 0;
		add(labelLat, c);
		c.gridx = 1;
		add(labelCenterLat, c);

		c.gridx = 0;
		c.gridwidth = 2;
		for (JButton button : buttons) {
			c.gridy += 1;
			add(button, c);
		}

		c.weighty = 1.0;
		add(new JPanel(), c);
	}

	void updateCenterPosition()
	{
		double centerLon = mapWindow.getCenterLon();
		double centerLat = mapWindow.getCenterLat();

		String lon = String.format("%.6f", centerLon);
		String lat = String.format("%.6f", centerLat);

		labelCenterLon.setText(lon);
		labelCenterLat.setText(lat);
	}

}
