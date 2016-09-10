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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.executables.JeographyGIS;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class GISAction extends SimpleAction
{

	private static final long serialVersionUID = -225117643828809550L;

	static final Logger logger = LoggerFactory.getLogger(GISAction.class);

	private JeographyGIS gis;
	private boolean enabled = true;

	/**
	 * Create a new GISAction.
	 * 
	 * @param gis
	 *            the JeographyGIS instance for this action.
	 * @param filename
	 *            the filename of an image or null
	 */
	public GISAction(JeographyGIS gis, String filename)
	{
		this.gis = gis;
		setIconFromResource(filename);
	}

	/**
	 * Create a new GISAction
	 * 
	 * @param gis
	 *            the JeographyGIS instance for this action.
	 * @param filename
	 *            the filename of an image or null.
	 * @param title
	 *            the title for this action.
	 */
	public GISAction(JeographyGIS gis, String filename, String title)
	{
		this(gis, filename);
		setName(title);
	}

	/**
	 * @return the viewer this action is about.
	 */
	public JeographyGIS getGIS()
	{
		return gis;
	}

	@Override
	public void putValue(String key, Object value)
	{
		// System.out.println("putValue: " + key + " : " + value);
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public void setEnabled(boolean b)
	{
		enabled = b;
	}

}
