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

import java.util.List;

import org.locationtech.jts.geom.Geometry;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GenericOperationList extends OperationList
{

	private static final long serialVersionUID = -5343020769122435682L;

	private final OperationEvaluator evaluator;

	/**
	 * Create a Operation list that computes its result via the denoted
	 * evaluator instance.
	 * 
	 * @param evaluator
	 *            the evaluator used to generate the resulting geometry.
	 */
	public GenericOperationList(OperationEvaluator evaluator)
	{
		this.evaluator = evaluator;
	}

	@Override
	protected Geometry operationResult(List<Geometry> geometries)
	{
		return evaluator.operationResult(geometries);
	}

}
