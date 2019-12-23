// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.dialogs;

import java.awt.Window;
import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AboutDialog extends JDialog
{

	private static final long serialVersionUID = -6673051400374126614L;

	final static Logger logger = LoggerFactory.getLogger(AboutDialog.class);

	public AboutDialog(Window owner)
	{
		super(owner, "About");
		JScrollPane jsp = new JScrollPane();
		setContentPane(jsp);

		JEditorPane pane = new JEditorPane();
		jsp.setViewportView(pane);
		pane.setEditable(false);

		HTMLEditorKit kit = new HTMLEditorKit();
		pane.setEditorKit(kit);

		String filename = "res/help/about.html";
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
