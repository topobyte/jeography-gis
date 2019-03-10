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

package de.topobyte.jeography.viewer.dockables;

import bibliothek.gui.dock.common.MultipleCDockableFactory;
import de.topobyte.jeography.viewer.JeographyGIS;

public class GeometryListFactory implements
		MultipleCDockableFactory<GeometryListDockable, GeometryListLayout>
{

	private JeographyGIS gis;

	public GeometryListFactory(JeographyGIS gis)
	{
		this.gis = gis;
	}

	@Override
	public GeometryListLayout create()
	{
		return new GeometryListLayout(gis.getViewer());
	}

	@Override
	public GeometryListDockable read(GeometryListLayout layout)
	{
		GeometryListDockable dockable = new GeometryListDockable(this,
				layout.getViewer());
		return dockable;
	}

	@Override
	public GeometryListLayout write(GeometryListDockable dockable)
	{
		GeometryListLayout layout = create();
		return layout;
	}

	@Override
	public boolean match(GeometryListDockable dockable,
			GeometryListLayout layout)
	{
		return false;
	}

}