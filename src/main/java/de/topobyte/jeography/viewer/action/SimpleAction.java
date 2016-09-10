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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.util.ImageLoader;
import de.topobyte.jeography.viewer.util.EmptyIcon;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class SimpleAction extends AbstractAction
{

	private static final long serialVersionUID = 1727617884915345905L;

	final static Logger logger2 = LoggerFactory.getLogger(SimpleAction.class);

	/**
	 * The name of this action.
	 */
	protected String name = null;

	/**
	 * The short description of this action.
	 */
	protected String description = null;

	/**
	 * The icon for this action.
	 */
	protected Icon icon = null;

	/**
	 * Create a default SimpleAction that provides only null values
	 */
	public SimpleAction()
	{
		// nothing to be done here
	}

	/**
	 * Create a SimpleAction that provides name and description
	 * 
	 * @param name
	 *            the name of the action.
	 * @param description
	 *            the short description of the action.
	 */
	public SimpleAction(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	@Override
	public Object getValue(String key)
	{
		if (key.equals("SmallIcon")) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return name;
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return description;
		}
		return null;
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
			logger2.debug("loading icon: " + filename);
			icon = ImageLoader.load(filename);
		} else {
			icon = new EmptyIcon(24);
		}
	}

	/**
	 * Set the name of this action.
	 * 
	 * @param name
	 *            the new name.
	 */
	protected void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Set the description of this action.
	 * 
	 * @param description
	 *            the new description.
	 */
	protected void setDescription(String description)
	{
		this.description = description;
	}

}
