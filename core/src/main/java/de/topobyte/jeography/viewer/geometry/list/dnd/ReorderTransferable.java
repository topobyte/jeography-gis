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

package de.topobyte.jeography.viewer.geometry.list.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ReorderTransferable implements Transferable
{

	final static Logger logger = LoggerFactory
			.getLogger(ReorderTransferable.class);

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		logger.debug("getTransferDataFlavors()");
		List<DataFlavor> flavorList = new ArrayList<>();
		return flavorList.toArray(new DataFlavor[0]);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
	{
		logger.debug("getTransferData()");
		return null;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		logger.debug("isDataFlavorSupported()");
		return false;
	}

}
