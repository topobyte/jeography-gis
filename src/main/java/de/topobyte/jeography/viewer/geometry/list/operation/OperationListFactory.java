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
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OperationListFactory
{

	/**
	 * Create a operation list.
	 * 
	 * @param operation
	 *            the operation to provide.
	 * @return the list.
	 */
	public static OperationList createOperationList(Operation operation)
	{
		switch (operation) {
		case COLLECTION:
			return new GenericOperationList(new CollectionEvaluator());
		case DIFFERENCE:
			return new GenericOperationList(new DifferenceEvaluator());
		case INTERSECTION:
			return new GenericOperationList(new IntersectionEvaluator());
		case UNION:
			return new GenericOperationList(new UnionEvaluator());
		case HULL:
			return new GenericOperationList(new UnionHullEvaluator());
		default:
			return null;
		}
	}

}
