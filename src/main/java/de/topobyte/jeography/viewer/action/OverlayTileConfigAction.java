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

import de.topobyte.jeography.core.TileConfigListener;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OverlayTileConfigAction extends ViewerAction implements
		TileConfigListener
{

	private static final long serialVersionUID = 9111846066502896410L;

	static final Logger logger = LoggerFactory
			.getLogger(OverlayTileConfigAction.class);

	private boolean enabled = true;
	private TileConfig config;

	/**
	 * Create a new action for choosing tileConfig.
	 * 
	 * @param viewer
	 *            the viewer to use for.
	 * @param config
	 *            the config to select.
	 */
	public OverlayTileConfigAction(Viewer viewer, TileConfig config)
	{
		super(viewer, null);
		this.config = config;
		viewer.addOverlayTileConfigListener(this);
		enabled = viewer.getOverlayTileConfig().getId() != config.getId();
	}

	@Override
	public Object getValue(String key)
	{
		if (key == Action.SMALL_ICON) {
			return null;
		} else if (key.equals(Action.NAME)) {
			return config.getName();
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return config.getName();
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getViewer().setOverlayTileConfig(config);
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	public void changed()
	{
		firePropertyChange("enabled", null, null);
	}

	@Override
	public void tileConfigChanged()
	{
		boolean nowEnabled = getViewer().getOverlayTileConfig().getId() != config
				.getId();
		boolean emit = (enabled != nowEnabled);
		enabled = nowEnabled;
		if (emit) {
			changed();
		}
	}

}
