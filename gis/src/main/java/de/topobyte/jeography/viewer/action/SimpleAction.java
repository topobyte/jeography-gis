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

import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.swing.util.EmptyIcon;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class SimpleAction
		extends de.topobyte.swing.util.action.SimpleAction
{

	private static final long serialVersionUID = 1727617884915345905L;

	final static Logger logger = LoggerFactory.getLogger(SimpleAction.class);

	public SimpleAction()
	{
		// nothing to be done here
	}

	public SimpleAction(String name, String description)
	{
		super(name, description);
	}

	/**
	 * Set this action's icon from the denoted filename.
	 * 
	 * @param filename
	 *            the icon to use.
	 */
	protected void setIconFromResource(String filename)
	{
		if (filename != null) {
			logger.debug("loading icon: " + filename);
			setIcon(filename);
		} else {
			setIcon(new EmptyIcon(24));
		}
	}

	public Icon getIcon()
	{
		return icon;
	}

}
