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

import java.util.Collection;

import javax.swing.TransferHandler.TransferSupport;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface GeometryDestination
{

	/**
	 * Reorder action occurred due to drag'n'drop.
	 * 
	 * @param ts
	 *            the TransferSupport instance involved.
	 */
	public void reorder(TransferSupport ts);

	/**
	 * Handle to insert the denoted geometries here.
	 * 
	 * @param geometries
	 *            the geometries to handle.
	 * @param ts
	 *            the TransferSupport instance involved.
	 */
	public void handle(Collection<Geometry> geometries, TransferSupport ts);

}
