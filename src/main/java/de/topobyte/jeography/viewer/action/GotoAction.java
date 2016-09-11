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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GotoAction extends GISAction
{

	private static final long serialVersionUID = 1829920578913695079L;

	final static Logger logger = LoggerFactory.getLogger(GotoAction.class);

	/**
	 * Create an action for showing the go-to dialog
	 * 
	 * @param gis
	 *            the JeographyGIS instance this action is for.
	 */
	public GotoAction(JeographyGIS gis)
	{
		super(gis, null);
		setName("go to");
		setDescription("go to coordinates");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO: implement this
	}

}
