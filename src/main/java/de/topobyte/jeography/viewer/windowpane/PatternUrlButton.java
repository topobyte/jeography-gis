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

package de.topobyte.jeography.viewer.windowpane;

import java.util.Locale;

import de.topobyte.jeography.core.mapwindow.MapWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PatternUrlButton extends UrlButton
{

	private static final long serialVersionUID = -7062450067319743273L;

	private MapWindow mapWindow;
	private String pattern;

	public PatternUrlButton(String title, MapWindow mapWindow, String pattern)
	{
		super(title);
		this.mapWindow = mapWindow;
		this.pattern = pattern;
	}

	@Override
	public String getUrl()
	{
		int zoom = (int) Math.round(mapWindow.getZoom());
		return String.format(Locale.US, pattern, mapWindow.getCenterLat(),
				mapWindow.getCenterLon(), zoom);
	}

}
