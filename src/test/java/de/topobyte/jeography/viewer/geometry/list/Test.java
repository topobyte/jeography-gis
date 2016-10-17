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

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.manage.GeometryManager;
import de.topobyte.jeography.viewer.geometry.manage.GeometryTree;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Test
{

	final static Logger logger = LoggerFactory.getLogger(Test.class);

	/**
	 * Simple test to show this list.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final GeometryFileList gl = new GeometryFileList();
		frame.setContentPane(gl);
		frame.setSize(new Dimension(400, 600));

		frame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e)
			{
				gl.getListModel().add(
						new File("/tmp/osm/germany/kreise/Kronach(62383).jsg"),
						0);
				gl.getListModel().add(
						new File("/tmp/osm/germany/kreise/Kulmbach(62479).jsg"),
						1);
			}
		});
		frame.setVisible(true);

		// second

		JFrame frame2 = new JFrame();
		frame2.setSize(new Dimension(400, 600));
		frame2.setLocation(400, 0);
		final GeometryList geometryList = new GeometryList();
		frame2.setContentPane(geometryList);
		frame2.setVisible(true);

		// third

		JFrame frame3 = new JFrame();
		frame3.setSize(new Dimension(400, 600));
		frame3.setLocation(800, 0);
		final GeometryList geometryList2 = new GeometryList();
		frame3.setContentPane(geometryList2);
		frame3.setVisible(true);

		// geometryTree

		JFrame frame4 = new JFrame();
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeometryManager geometryManager = new GeometryManager();
		GeometryTree gt = new GeometryTree(geometryManager);
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(gt);
		frame4.setContentPane(jsp);
		frame4.setSize(new Dimension(400, 600));
		frame4.setVisible(true);

		gt.getModel().addDirectory(gt.getModel().getRoot(), "foo");
		gt.getModel().addFile(
				new File("/tmp/osm/geometryOperations/bundeslander/62422.jsg"),
				gt.getModel().getRoot());
		gt.getModel().addDirectory(gt.getModel().getRoot(), "bar");
		gt.getModel().addDirectory(gt.getModel().getRoot(), "carasdfsdf");

	}

}
