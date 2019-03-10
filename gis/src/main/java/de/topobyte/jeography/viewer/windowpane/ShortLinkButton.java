// Copyright 2018 Sebastian Kuerten
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

import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.util.OsmShortLinks;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShortLinkButton extends UrlButton
{

	private static final long serialVersionUID = 1L;

	private MapWindow mapWindow;

	public ShortLinkButton(MapWindow mapWindow)
	{
		super("Browser: ShortLink");
		this.mapWindow = mapWindow;
	}

	@Override
	public String getUrl()
	{
		int zoom = (int) Math.round(mapWindow.getZoom());
		String code = OsmShortLinks.encode(mapWindow.getCenterLon(),
				mapWindow.getCenterLat(), zoom);
		return String.format("https://osm.org/go/%s", code);
	}

}
