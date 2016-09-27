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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.topobyte.interactiveview.ZoomChangedListener;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestZoomControl
{

	public static void main(String[] args)
	{

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			System.out.println("error while setting LAF");
		}

		JFrame frame = new JFrame("Zoom Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.setContentPane(panel);
		final ZoomControl zc = new ZoomControl();
		zc.setBorder(BorderFactory.createLineBorder(Color.RED));
		panel.add(zc);

		zc.addZoomChangedListener(new ZoomChangedListener() {

			@Override
			public void zoomChanged()
			{
				System.out.println(zc.getValue());
			}
		});

		frame.setSize(400, 400);
		frame.setVisible(true);
	}

}
