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

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class NetworkStateAction extends ViewerAction
{

	private static final long serialVersionUID = -7959344011070063393L;

	static final Logger logger = LoggerFactory
			.getLogger(NetworkStateAction.class);

	private static final String FILE_ONLINE = "res/images/network-idle.png";
	private static final String FILE_OFFLINE = "res/images/network-offline.png";

	private String text;

	/**
	 * @param viewer
	 *            the viewer to monitor with this action.
	 */
	public NetworkStateAction(Viewer viewer)
	{
		super(viewer, null);
		configure();
	}

	private void configure()
	{
		setIconFromResource(getViewer().getNetworkState() ? FILE_ONLINE
				: FILE_OFFLINE);
		text = getViewer().getNetworkState() ? "disable network"
				: "enable network";
	}

	@Override
	public Object getValue(String key)
	{
		// System.out.println(key);
		if (key.equals(Action.SMALL_ICON)) {
			return icon;
		} else if (key.equals(Action.NAME)) {
			return text;
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return "toggle whether network is used to retrieve tiles";
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getViewer().setNetworkState(!getViewer().getNetworkState());
		configure();
		firePropertyChange(Action.SMALL_ICON, null, null);
		firePropertyChange(Action.NAME, null, null);
	}

}
