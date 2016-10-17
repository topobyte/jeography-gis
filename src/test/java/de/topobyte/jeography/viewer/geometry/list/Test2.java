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

package de.topobyte.jeography.viewer.geometry.list;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.topobyte.jeography.viewer.geometry.list.index.IndexList;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationListFactory;
import de.topobyte.jeography.viewer.geometry.list.operation.Operation;
import de.topobyte.jeography.viewer.geometry.select.GeometrySelectionOperation;
import de.topobyte.swing.util.FrameHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Test2
{

	final static Logger logger = LoggerFactory.getLogger(Test2.class);

	/**
	 * Simple test to show this list.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{

		// file list
		final GeometryFileList gl = new GeometryFileList();
		JFrame frame = FrameHelper.showFrameWithComponent("files", gl, 400, 600,
				0, 0, false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e)
			{
				gl.getListModel()
						.add(new File(
								"/tmp/osm/germany/osm/kreise/Kronach(62383).jsg"),
								0);
				gl.getListModel()
						.add(new File(
								"/tmp/osm/germany/osm/kreise/Kulmbach(62479).jsg"),
								1);
				gl.getListModel().add(new File("/tmp/temp.jsg"), 2);
				gl.getListModel().add(new File("/tmp/test3.jsg"), 3);
			}
		});
		frame.setVisible(true);

		// second
		final OperationList unionList = OperationListFactory
				.createOperationList(Operation.UNION);
		FrameHelper.showFrameWithComponent("union", unionList, 400, 600, 400, 0,
				true);

		Geometry geometry = new GeometryFactory()
				.createPoint(new Coordinate(1.0, 2.0));
		unionList.getList().getModel().add(geometry, 0);
		unionList.getList().getModel().add(geometry, 0);
		unionList.getList().getModel().add(geometry, 0);

		// third
		final OperationList intersectionList = OperationListFactory
				.createOperationList(Operation.INTERSECTION);
		FrameHelper.showFrameWithComponent("intersection", intersectionList,
				400, 600, 800, 0, true);

		IndexList indexList = new IndexList();
		FrameHelper.showFrameWithComponent("index", indexList, 400, 600, 1000,
				0, true);

		GeometrySelectionOperation gso = new GeometrySelectionOperation();
		FrameHelper.showFrameWithComponent("selection", gso, 300, 200, 0, 100,
				true);
	}

}
