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

package de.topobyte.jeography.viewer.geometry.list.operation;

/**
 * An enumeration of the available operations.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public enum Operation {

	/**
	 * union operation
	 */
	UNION,
	/**
	 * intersection operation
	 */
	INTERSECTION,
	/**
	 * difference operation
	 */
	DIFFERENCE,
	/**
	 * collection creation
	 */
	COLLECTION,
	/**
	 * hull
	 */
	HULL

}
