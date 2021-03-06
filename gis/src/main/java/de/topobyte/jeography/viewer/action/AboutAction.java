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

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.JeographyGIS;
import de.topobyte.jeography.viewer.dialogs.AboutDialog;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class AboutAction extends GISAction
{

	private static final long serialVersionUID = 3220722775351218109L;

	final static Logger logger = LoggerFactory.getLogger(AboutAction.class);

	/**
	 * Create an action for displaying an about dialog
	 * 
	 * @param gis
	 *            the JeographyGIS instance this action is for.
	 */
	public AboutAction(JeographyGIS gis)
	{
		super(gis, null);
		setName("about");
		setDescription("show the about dialog");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFrame frame = getMainFrame();
		AboutDialog aboutDialog = new AboutDialog(frame);
		aboutDialog.setSize(400, 300);
		aboutDialog.setLocationRelativeTo(frame);
		aboutDialog.setVisible(true);
	}

}
