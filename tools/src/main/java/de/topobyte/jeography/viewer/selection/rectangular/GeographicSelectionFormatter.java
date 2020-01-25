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

package de.topobyte.jeography.viewer.selection.rectangular;

import java.util.Locale;

public class GeographicSelectionFormatter
{

	private String name;
	private String pattern;

	public GeographicSelectionFormatter(String name, String pattern)
	{
		this.name = name;
		this.pattern = pattern;
	}

	public String getName()
	{
		return name;
	}

	public String format(GeographicSelection selection)
	{
		return String.format(Locale.US, pattern, selection.getX1().value(),
				selection.getY1().value(), selection.getX2().value(),
				selection.getY2().value());
	}

}
