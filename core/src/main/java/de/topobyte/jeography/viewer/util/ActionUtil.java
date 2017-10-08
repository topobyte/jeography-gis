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

package de.topobyte.jeography.viewer.util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import de.topobyte.jeography.viewer.action.MoveAction;
import de.topobyte.jeography.viewer.action.ZoomAction;
import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ActionUtil
{

	public static void setupZoomActions(Viewer viewer, InputMap inputMap,
			ActionMap actionMap)
	{
		ZoomAction zoomIn = new ZoomAction(viewer, true);
		ZoomAction zoomOut = new ZoomAction(viewer, false);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "zoom in");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
				"zoom out");

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "zoom in");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoom out");

		actionMap.put("zoom in", zoomIn);
		actionMap.put("zoom out", zoomOut);
	}

	public static void setupMovementActions(Viewer viewer, InputMap inputMap,
			ActionMap actionMap)
	{
		int little = 8;
		int much = 256;
		MoveAction moveLittleUp = new MoveAction(viewer, "up",
				"move the viewers viewport up", null, 0, -little);
		MoveAction moveLittleDown = new MoveAction(viewer, "down",
				"move the viewers viewport down", null, 0, little);
		MoveAction moveLittleLeft = new MoveAction(viewer, "left",
				"move the viewers viewport left", null, -little, 0);
		MoveAction moveLittleRight = new MoveAction(viewer, "right",
				"move the viewers viewport right", null, little, 0);
		MoveAction moveMuchUp = new MoveAction(viewer, "up",
				"move the viewers viewport up", null, 0, -much);
		MoveAction moveMuchDown = new MoveAction(viewer, "down",
				"move the viewers viewport down", null, 0, much);
		MoveAction moveMuchLeft = new MoveAction(viewer, "left",
				"move the viewers viewport left", null, -much, 0);
		MoveAction moveMuchRight = new MoveAction(viewer, "right",
				"move the viewers viewport right", null, much, 0);

		actionMap.put("up", moveLittleUp);
		actionMap.put("down", moveLittleDown);
		actionMap.put("left", moveLittleLeft);
		actionMap.put("right", moveLittleRight);

		actionMap.put("up much", moveMuchUp);
		actionMap.put("down much", moveMuchDown);
		actionMap.put("left much", moveMuchLeft);
		actionMap.put("right much", moveMuchRight);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "up");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "down");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "left");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "right");

		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK),
				"up much");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK),
				"down much");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK),
				"left much");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
				InputEvent.SHIFT_MASK), "right much");

		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.SHIFT_MASK),
				"up much");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.SHIFT_MASK),
				"down much");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.SHIFT_MASK),
				"left much");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.SHIFT_MASK),
				"right much");
	}

}
