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

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.Operations;

public class OperationListDockable extends DefaultMultipleCDockable
{

	public OperationListDockable(OperationListFactory factory, Viewer viewer,
			Operations operation)
	{
		super(factory);
		setTitleText(operation.name());
		DockableHelper.setDefaultOptions(this);

		OperationList list = de.topobyte.jeography.viewer.geometry.list.operation.OperationListFactory
				.createOperationList(operation);
		add(list);
	}

}
