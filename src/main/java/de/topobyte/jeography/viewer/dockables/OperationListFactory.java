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
import de.topobyte.jeography.viewer.geometry.list.operation.Operation;

public class OperationListFactory implements
		MultipleCDockableFactory<OperationListDockable, OperationListLayout>
{

	private JeographyGIS gis;
	private Operation operation;

	public OperationListFactory(JeographyGIS gis, Operation operation)
	{
		this.gis = gis;
		this.operation = operation;
	}

	@Override
	public OperationListLayout create()
	{
		return new OperationListLayout(gis.getViewer());
	}

	@Override
	public OperationListDockable read(OperationListLayout layout)
	{
		OperationListDockable dockable = new OperationListDockable(this,
				layout.getViewer(), operation);
		return dockable;
	}

	@Override
	public OperationListLayout write(OperationListDockable dockable)
	{
		OperationListLayout layout = create();
		return layout;
	}

	@Override
	public boolean match(OperationListDockable dockable,
			OperationListLayout layout)
	{
		return false;
	}

}