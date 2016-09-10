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

package de.topobyte.jeography.viewer.action;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class FullscreenAction extends GISAction
{

	private static final long serialVersionUID = -2996666797609599345L;

	final static Logger logger = LoggerFactory
			.getLogger(FullscreenAction.class);

	/**
	 * Create the action
	 * 
	 * @param gis
	 *            the JeographyGIS instance to work on.
	 */
	public FullscreenAction(JeographyGIS gis)
	{
		super(gis, null, "fullscreen");
		setDescription("toggle fullscreen");
	}

	private Point lastNonFullscreenLocation = null;
	private Dimension lastNonFullscreenSize = null;

	@Override
	public void actionPerformed(ActionEvent e)
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		if (!gd.isFullScreenSupported()) {
			return;
		}

		JFrame frame = Components.getContainingFrame(getGIS());
		boolean currentlyFullscreen = (gd.getFullScreenWindow() == frame);

		logger.debug("currently fullscreen?: " + currentlyFullscreen);

		if (!currentlyFullscreen) {
			lastNonFullscreenSize = frame.getSize();
			lastNonFullscreenLocation = frame.getLocationOnScreen();
		}
		List<Window> windows = new ArrayList<>();
		for (Window window : frame.getOwnedWindows()) {
			if (window.isVisible()) {
				windows.add(window);
			}
		}

		frame.dispose();
		frame.setUndecorated(!currentlyFullscreen);
		if (currentlyFullscreen) {
			gd.setFullScreenWindow(null);
		} else {
			gd.setFullScreenWindow(frame);
		}
		if (currentlyFullscreen) {
			frame.setSize(lastNonFullscreenSize);
			frame.setLocation(lastNonFullscreenLocation);
		}
		frame.setVisible(true);

		for (Window window : windows) {
			window.setVisible(true);
		}

		frame.requestFocus();
	}

}
