// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.tools;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.tools.bboxaction.bboxchooser.BboxChooser;

public class TestBboxChooser
{

	public static void main(String[] args)
	{
		BBox bbox = new BBox(13.263244, 52.571342, 13.515930, 52.450988);
		BboxChooser bc = new BboxChooser(bbox);
		int ret = bc.showDialog(null);
		if (ret == BboxChooser.APPROVE_OPTION) {
			BBox selectedBbox = bc.getBbox();
			System.out.println("Selected: " + selectedBbox);
		} else {
			System.out.println("Dismissed");
		}
		System.exit(0);
	}

}
