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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.jeography.swing.widgets.ButtonWithDropdown;
import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.selection.action.ApiAction;
import de.topobyte.jeography.viewer.selection.action.ClipboardAction;
import de.topobyte.jeography.viewer.selection.action.DismissAction;
import de.topobyte.jeography.viewer.selection.action.DownloadAction;
import de.topobyte.jeography.viewer.selection.action.ExportImageAction;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelectionFormatter;
import de.topobyte.jeography.viewer.selection.rectangular.Latitude;
import de.topobyte.jeography.viewer.selection.rectangular.Longitude;
import de.topobyte.jeography.viewer.selection.rectangular.Selection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionChangeListener;
import de.topobyte.jeography.viewer.statusbar.MouseOverClipboardAdapter;
import de.topobyte.jeography.viewer.statusbar.StatusBarCallback;
import de.topobyte.jeography.viewer.statusbar.StatusBarInfoEmitter;
import de.topobyte.jeography.viewer.statusbar.StatusBarInfoReceiver;
import de.topobyte.swing.util.ImageLoader;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RectPane extends JPanel implements SelectionChangeListener,
		StatusBarInfoEmitter, StatusBarInfoReceiver
{

	private static final long serialVersionUID = 7041037824730396550L;

	private JLabel labelLon, labelLat;
	private Icon image1, image2;
	private JTextField lon1, lon2, lat1, lat2;
	private JTextField[] textfields;

	private final SelectionAdapter selectionAdapter;

	private static final String placeHolder = "none";

	private List<JButton> buttons = new ArrayList<>();
	private boolean[] states = new boolean[] { true, false, false, true, true,
			true, false, true };

	private BboxDragGeometryPanel dragPanel;

	private boolean haveSelection = false;

	/**
	 * Create a new Panel for a rectangular selection.
	 * 
	 * @param gis
	 *            the JeographyGIS instance this is connected to.
	 * 
	 * @param selectionAdapter
	 *            the adapter that manages the selection.
	 */
	public RectPane(JeographyGIS gis, SelectionAdapter selectionAdapter)
	{
		this.selectionAdapter = selectionAdapter;
		selectionAdapter.addSelectionChangeListener(this);

		labelLon = new JLabel("Lon:");
		labelLat = new JLabel("Lat:");
		image1 = ImageLoader.load("res/images/square_top_left.png");
		image2 = ImageLoader.load("res/images/square_bottom_right.png");
		lon1 = new JTextField();
		lon2 = new JTextField();
		lat1 = new JTextField();
		lat2 = new JTextField();
		textfields = new JTextField[] { lon1, lat1, lon2, lat2 };

		for (JTextField field : textfields) {
			field.setEditable(false);
			field.setHorizontalAlignment(SwingConstants.RIGHT);
			field.setText(placeHolder);
		}

		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraintsEditor e = new GridBagConstraintsEditor(c);

		setLayout(new GridBagLayout());
		JPanel pTextfields = new JPanel(new GridBagLayout());
		JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.LEADING));

		e.fill(GridBagConstraints.BOTH).weightX(1.0).gridY(0);
		add(pTextfields, c);
		e.weightY(1.0).gridY(1);
		add(pButtons, c);
		// e.gridY(2);
		// add(new JPanel(), c);

		/*
		 * layout of the textfields and describing row / column
		 */

		e.fill(GridBagConstraints.BOTH).weightX(1.0).gridX(2).gridY(0);

		// first row
		e.weightX(1.0).gridY(0).gridX(1);
		pTextfields.add(labelLon, c);
		e.gridX(2);
		pTextfields.add(labelLat, c);

		// second row
		e.weightX(0.0).gridY(1).gridX(0);
		pTextfields.add(new JLabel(image1), c);
		e.weightX(1.0).gridX(1);
		pTextfields.add(lon1, c);
		e.gridX(2);
		pTextfields.add(lat1, c);

		// third row
		e.weightX(0.0).gridY(2).gridX(0);
		pTextfields.add(new JLabel(image2), c);
		e.weightX(1.0).gridX(1);
		pTextfields.add(lon2, c);
		e.gridX(2);
		pTextfields.add(lat2, c);

		List<GeographicSelectionFormatter> formatters = GeographicSelectionFormatters.FORMATTERS;

		/*
		 * buttons
		 */
		ExportImageAction eia = new ExportImageAction(gis, selectionAdapter);
		DownloadAction da = new DownloadAction(gis, selectionAdapter);
		ApiAction aa = new ApiAction(gis, selectionAdapter);
		ClipboardAction ca = new ClipboardAction(gis, selectionAdapter,
				formatters.get(0));
		DismissAction dma = new DismissAction(gis, selectionAdapter);

		ClipboardPopupMenu popupClipboard = new ClipboardPopupMenu(this,
				selectionAdapter, formatters);
		ButtonWithDropdown buttonClipboard = new ButtonWithDropdown(ca,
				ca.getIcon(), popupClipboard);

		buttonClipboard.addMouseListener(new MouseOverClipboardAdapter(
				buttonClipboard, this, ca::getClipboardText));

		JButton buttonApi = new JButton(aa);
		buttonApi.addMouseListener(new MouseOverClipboardAdapter(buttonApi,
				this, aa::getClipboardText));

		buttons.add(new JButton(da));
		buttons.add(
				new JButton(ImageLoader.load("res/images/16/edit-delete.png")));
		buttons.add(new JButton(
				ImageLoader.load("res/images/16/edit-delete-advanced.png")));
		buttons.add(new JButton(eia));
		buttons.add(buttonApi);
		buttons.add(buttonClipboard);
		buttons.add(new JButton(
				ImageLoader.load("res/images/16/stock_bookmark.png")));
		buttons.add(new JButton(dma));

		for (JButton button : buttons) {
			pButtons.add(button);
			button.setText(null);
			button.setMargin(new Insets(0, 0, 0, 0));
		}

		dragPanel = new BboxDragGeometryPanel(null);
		pButtons.add(dragPanel);
		Icon iconPolygon = ImageLoader.load("res/images/16/polygon.png");
		dragPanel.add(new JLabel(iconPolygon));

		updateButtonStates(true);
	}

	@Override
	public void pixelValuesChanged()
	{
		updateButtonStates(false);
	}

	@Override
	public void geographicValuesChanged()
	{
		int digits = 6;

		GeographicSelection selection = selectionAdapter
				.getGeographicSelection();
		if (selection == null) {
			lon1.setText(placeHolder);
			lat1.setText(placeHolder);
			lon2.setText(placeHolder);
			lat2.setText(placeHolder);
			dragPanel.setBoundingBox(null);
		} else {
			NumberFormat format = NumberFormat.getInstance();
			format.setMinimumFractionDigits(digits);
			format.setMaximumFractionDigits(digits);
			lon1.setText(format.format(selection.getX1().value()));
			lat1.setText(format.format(selection.getY1().value()));
			lon2.setText(format.format(selection.getX2().value()));
			lat2.setText(format.format(selection.getY2().value()));
			dragPanel.setBoundingBox(selection.toBoundingBox());
		}
	}

	private void updateButtonStates(boolean force)
	{
		Selection<Longitude, Latitude> selection = selectionAdapter
				.getGeographicSelection();
		boolean haveSelection = selection != null;

		if (haveSelection == this.haveSelection && !force) {
			return;
		}
		this.haveSelection = haveSelection;
		if (haveSelection) {
			updateStateHaveSelection();
		} else {
			updateStateNoSelection();
		}
	}

	private void updateStateHaveSelection()
	{
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(states[i]);
		}
		// TODO: enable drag box here
	}

	private void updateStateNoSelection()
	{
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(false);
		}
		// TODO: disable drag box here
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

	@Override
	public void triggerStatusBarInfoAvailable(String info)
	{
		for (StatusBarCallback callback : statusBarCallbacks) {
			callback.infoAvailable(info);
		}
	}

	@Override
	public void triggerStatusBarNoInfo()
	{
		for (StatusBarCallback callback : statusBarCallbacks) {
			callback.noInfoAvailable();
		}
	}

}
