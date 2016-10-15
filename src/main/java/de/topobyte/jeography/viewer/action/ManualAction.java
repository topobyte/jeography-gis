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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ManualAction extends GISAction
{

	private static final long serialVersionUID = 3220722775351218109L;

	final static Logger logger = LoggerFactory.getLogger(ManualAction.class);

	/**
	 * Create an action for displaying a manual dialog
	 * 
	 * @param gis
	 *            the JeographyGIS instance this action is for.
	 */
	public ManualAction(JeographyGIS gis)
	{
		super(gis, null);
		setName("manual");
		setDescription("show the manual dialog");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFrame frame = getMainFrame();
		ManualDialog manualDialog = new ManualDialog(frame);
		manualDialog.setSize(400, 300);
		manualDialog.setLocationRelativeTo(frame);
		manualDialog.setVisible(true);
	}

	private class ManualDialog extends JDialog
	{

		private static final long serialVersionUID = -6673051400374126614L;

		public ManualDialog(Window owner)
		{
			super(owner, "Manual");
			JScrollPane jsp = new JScrollPane();
			setContentPane(jsp);

			JEditorPane pane = new JEditorPane();
			jsp.setViewportView(pane);
			pane.setEditable(false);

			HTMLEditorKit kit = new HTMLEditorKit();
			pane.setEditorKit(kit);

			String filename = "res/help/manual.html";
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource(filename);
			try {
				logger.debug("url: " + url);
				pane.setPage(url);
			} catch (IOException e) {
				logger.debug("unable to set page: " + e.getMessage());
			}
		}
	}

}
