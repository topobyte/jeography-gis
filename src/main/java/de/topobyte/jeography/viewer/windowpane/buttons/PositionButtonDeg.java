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

package de.topobyte.jeography.viewer.windowpane.buttons;

import de.topobyte.jeography.core.mapwindow.MapWindow;
import de.topobyte.jeography.viewer.windowpane.ClipboardButton;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class PositionButtonDeg extends ClipboardButton
{

	private static final long serialVersionUID = 9182954207805942607L;

	private MapWindow mapWindow;

	public PositionButtonDeg(String title, MapWindow mapWindow)
	{
		super(title);
		this.mapWindow = mapWindow;
	}

	@Override
	public String getClipboardText()
	{
		String text = degMinSec(mapWindow.getCenterLat(),
				mapWindow.getCenterLon());
		return text;
	}

	private String degMinSec(double centerLat, double centerLon)
	{
		String text = degMinSecLat(centerLat) + "," + degMinSecLon(centerLon);
		return text;
	}

	private String degMinSecLat(double lat)
	{
		String letter = lat >= 0 ? "N" : "S";
		double abs = Math.abs(lat);
		return degs(abs) + "/" + mins(abs) + "/" + secs(abs) + "/" + letter;
	}

	private String degMinSecLon(double lon)
	{
		String letter = lon >= 0 ? "E" : "W";
		double abs = Math.abs(lon);
		return degs(abs) + "/" + mins(abs) + "/" + secs(abs) + "/" + letter;
	}

	private int degs(double d)
	{
		return (int) Math.floor(d);
	}

	private int mins(double d)
	{
		return ((int) Math.round((d - degs(d)) * 3600)) / 60;
	}

	private int secs(double d)
	{
		return ((int) Math.round((d - degs(d)) * 3600)) % 60;
	}

}
