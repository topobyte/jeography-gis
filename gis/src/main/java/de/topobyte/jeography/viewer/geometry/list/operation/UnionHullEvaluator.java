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

import de.topobyte.jts.utils.JtsHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class UnionHullEvaluator implements OperationEvaluator
{

	@Override
	public Geometry operationResult(List<Geometry> geometries)
	{
		Geometry union = JtsHelper.collection(geometries).union();
		Geometry hull = union.convexHull();
		return hull;
	}

}
