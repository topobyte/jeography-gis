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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.util.xml.XElement;
import de.topobyte.jeography.viewer.core.Viewer;

public class OperationListLayout implements MultipleCDockableLayout
{

	private Viewer viewer;

	public OperationListLayout(Viewer viewer)
	{
		this.viewer = viewer;
	}

	public Viewer getViewer()
	{
		return viewer;
	}

	@Override
	public void readStream(DataInputStream in) throws IOException
	{
		// nothing to do
	}

	@Override
	public void readXML(XElement element)
	{
		// nothing to do
	}

	@Override
	public void writeStream(DataOutputStream out) throws IOException
	{
		// nothing to do
	}

	@Override
	public void writeXML(XElement element)
	{
		// nothing to do
	}

}
