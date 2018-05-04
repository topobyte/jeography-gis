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

package de.topobyte.jeography.viewer.zoom;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.topobyte.interactiveview.ZoomChangedListener;
import de.topobyte.swing.util.ImageLoader;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ZoomControl extends JPanel
{

	private static final long serialVersionUID = 7298321263941033127L;

	private JButton minus, plus;
	private JLabel label;

	private double[] values = new double[] { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5,
			1.75, 2 };

	private int value = 3;

	public ZoomControl()
	{
		setLayout(new FlowLayout());
		label = new JLabel(getText());
		Icon in = ImageLoader.load("res/images/16/zoom-in.png");
		Icon out = ImageLoader.load("res/images/16/zoom-out.png");
		minus = new JButton(out);
		plus = new JButton(in);
		add(minus);
		add(label);
		add(plus);

		plus.setMargin(new Insets(0, 0, 0, 0));
		minus.setMargin(new Insets(0, 0, 0, 0));

		minus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				decreaseZoom();
			}
		});

		plus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				increaseZoom();
			}
		});
	}

	protected void increaseZoom()
	{
		if (value < values.length - 1) {
			value = value + 1;
			updateTextAndStates();
			fireZoomChangedListeners();
		}
	}

	protected void decreaseZoom()
	{
		if (value > 0) {
			value = value - 1;
			updateTextAndStates();
			fireZoomChangedListeners();
		}
	}

	private void updateTextAndStates()
	{
		label.setText(getText());
		minus.setEnabled(value != 0);
		plus.setEnabled(value != values.length - 1);
	}

	private String getText()
	{
		return String.format("%.0f%%", values[value] * 100);
	}

	private List<ZoomChangedListener> listeners = new ArrayList<>();

	public void addZoomChangedListener(ZoomChangedListener listener)
	{
		listeners.add(listener);
	}

	public void removeZoomChangedListener(ZoomChangedListener listener)
	{
		listeners.remove(listener);
	}

	private void fireZoomChangedListeners()
	{
		for (ZoomChangedListener listener : listeners) {
			listener.zoomChanged();
		}
	}

	public double getValue()
	{
		return values[value];
	}

}
