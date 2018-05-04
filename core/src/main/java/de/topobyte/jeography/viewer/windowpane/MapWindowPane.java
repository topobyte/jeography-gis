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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.viewer.statusbar.StatusBarCallback;
import de.topobyte.jeography.viewer.statusbar.StatusBarInfoEmitter;
import de.topobyte.jeography.viewer.util.ScrollablePanel;
import de.topobyte.jeography.viewer.windowpane.patterns.Formatters;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MapWindowPane extends JPanel implements StatusBarInfoEmitter
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

		JLabel label = new JLabel("center position:");
		JLabel labelLon = new JLabel("lon:");
		JLabel labelLat = new JLabel("lat:");

		List<JButton> buttons = new ArrayList<>();
		JButton buttonOsmWeb = new PatternUrlButton("Openstreetmap Mapnik",
				mapWindow,
				"http://www.openstreetmap.org/?lat=%f&lon=%f&zoom=%d&layers=M");
		JButton buttonOsmShortLink = new ShortLinkButton(mapWindow);
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
		buttons.add(buttonOsmShortLink);
		buttons.add(buttonPotlatch1);
		buttons.add(buttonPotlatch2);

		for (JButton button : buttons) {
			button.setHorizontalAlignment(SwingConstants.LEFT);
		}

		for (JButton button : buttons) {
			if (button instanceof CoordinateFormatterClipboardButton) {
				final CoordinateFormatterClipboardButton cfcb = (CoordinateFormatterClipboardButton) button;
				button.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent evt)
					{
						triggerStatusBarInfoAvailable(cfcb.getClipboardText());
					}

					@Override
					public void mouseExited(MouseEvent evt)
					{
						triggerStatusBarNoInfo();
					}

				});
			} else if (button instanceof UrlButton) {
				final UrlButton urlButton = (UrlButton) button;
				button.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent evt)
					{
						triggerStatusBarInfoAvailable(urlButton.getUrl());
					}

					@Override
					public void mouseExited(MouseEvent evt)
					{
						triggerStatusBarNoInfo();
					}

				});
			}
		}

		GridBagConstraintsEditor ce = new GridBagConstraintsEditor();
		GridBagConstraints c = ce.getConstraints();

		ce.weight(1.0, 0.0).anchor(GridBagConstraints.PAGE_START);

		ce.gridPos(0, 0).gridWidth(2).fill(GridBagConstraints.HORIZONTAL);
		add(label, c);

		ce.gridPos(0, 1).gridWidth(1);
		add(labelLon, c);
		ce.gridX(1);
		add(labelCenterLon, c);

		ce.gridPos(0, 2);
		add(labelLat, c);
		ce.gridX(1);
		add(labelCenterLat, c);

		ScrollablePanel buttonPane = new ScrollablePanel(new GridBagLayout());
		buttonPane.setTracksViewportWidth(true);
		JScrollPane jsp = new JScrollPane(buttonPane);

		ce.gridWidth(2).weightY(1.0).fill(GridBagConstraints.BOTH);
		ce.gridPos(0, GridBagConstraints.RELATIVE);
		add(jsp, c);

		GridBagConstraintsEditor de = new GridBagConstraintsEditor();
		GridBagConstraints d = de.getConstraints();
		de.gridPos(0, GridBagConstraints.RELATIVE).weightX(1.0);
		de.fill(GridBagConstraints.HORIZONTAL)
				.anchor(GridBagConstraints.PAGE_START);
		for (JButton button : buttons) {
			buttonPane.add(button, d);
		}
		de.weightY(1.0).fill(GridBagConstraints.BOTH);
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(0, 0));
		buttonPane.add(filler, d);
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

	private List<StatusBarCallback> statusBarCallbacks = new ArrayList<>();

	@Override
	public void addStatusBarCallback(StatusBarCallback callback)
	{
		statusBarCallbacks.add(callback);
	}

	@Override
	public void removeStatusBarCallback(StatusBarCallback callback)
	{
		statusBarCallbacks.remove(callback);
	}

	private void triggerStatusBarInfoAvailable(String info)
	{
		for (StatusBarCallback callback : statusBarCallbacks) {
			callback.infoAvailable(info);
		}
	}

	private void triggerStatusBarNoInfo()
	{
		for (StatusBarCallback callback : statusBarCallbacks) {
			callback.noInfoAvailable();
		}
	}

}
