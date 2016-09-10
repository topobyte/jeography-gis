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

package de.topobyte.jeography.viewer.selection.polygonal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import de.topobyte.jeography.core.mapwindow.MapWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Selection
{

	final static Logger logger = LoggerFactory.getLogger(Selection.class);

	private LineEditor currentEditor = null;
	private List<LineEditor> editors = new ArrayList<>();
	private final MapWindow window;

	/**
	 * Create a new Selection for the given window.
	 * 
	 * @param window
	 *            the window to use for the selection. The window is necessary
	 *            to transform between pixel and geographic values
	 */
	public Selection(MapWindow window)
	{
		this.window = window;
		currentEditor = new LineEditor(window);
		editors.add(currentEditor);
	}

	/**
	 * Add the point (x, y) to the current line. If the last line is closed or
	 * there is no current line, start a new one.
	 * 
	 * @param x
	 *            the x coordinate of the new point.
	 * @param y
	 *            the y coordinate of the new point.
	 */
	public void add(int x, int y)
	{
		logger.debug("add: " + x + "," + y);
		if (getCurrentEditor().isClosed()) {
			logger.debug("new editor...");
			newEditor();
		}
		getCurrentEditor().add(x, y);
		triggerChange();
	}

	/**
	 * Remove the last point from the current line. If the current line is
	 * empty, the next removal will proceed at a random instance of the other
	 * lines.
	 */
	public void remove()
	{
		getCurrentEditor().removeLast();
		if (getCurrentEditor().isEmpty()) {
			removeEditor();
		}
		triggerChange();
	}

	/**
	 * If possible (enough points), close the current line to a polygon.
	 */
	public void close()
	{
		if (getCurrentEditor().canClose()) {
			getCurrentEditor().removeLast();
			getCurrentEditor().close();
		}
		triggerChange();
	}

	private LineEditor getCurrentEditor()
	{
		return currentEditor;
	}

	private void newEditor()
	{
		currentEditor = new LineEditor(window);
		editors.add(currentEditor);
	}

	private void removeEditor()
	{
		editors.remove(currentEditor);
		if (editors.size() > 0) {
			currentEditor = editors.get(editors.size() - 1);
		} else {
			newEditor();
		}
	}

	/**
	 * Retrieve the geometries represented by this selection.
	 * 
	 * @return the selection in form of a GeometryCollection
	 */
	public GeometryCollection getGeometries()
	{
		List<Geometry> list = getGeometriesAsList();
		Geometry[] array = list.toArray(new Geometry[0]);
		return new GeometryFactory().createGeometryCollection(array);
	}

	/**
	 * Retrieve the geometries represented by this selection.
	 * 
	 * @return the selection in form of a list of geometries.
	 */
	public List<Geometry> getGeometriesAsList()
	{
		ArrayList<Geometry> list = new ArrayList<>();
		for (LineEditor editor : editors) {
			Geometry geometry = editor.getGeometryPixels();
			if (geometry != null) {
				list.add(geometry);
			}
		}
		return list;
	}

	/**
	 * Retrieve the geometries represented by this selection.
	 * 
	 * @return the selection in form of a list of geometries.
	 */
	public List<Geometry> getGeometriesAsListDegrees()
	{
		ArrayList<Geometry> list = new ArrayList<>();
		for (LineEditor editor : editors) {
			Geometry geometry = editor.getGeometryDegrees();
			if (geometry != null) {
				list.add(geometry);
			}
		}
		return list;
	}

	/**
	 * Rebuild the pixel representations of the lines from the degree instances.
	 */
	public void fromDegrees()
	{
		for (LineEditor editor : editors) {
			editor.fromDegrees();
		}
		triggerPixelValuesChanged();
	}

	/**
	 * Rebuild the geographic representations of the lines from the pixel
	 * instances.
	 */
	public void fromPixels()
	{
		for (LineEditor editor : editors) {
			editor.fromPixels();
		}
		triggerGeographicValuesChanged();
	}

	private List<SelectionChangeListener> listeners = new ArrayList<>();

	/**
	 * Add a listener that get informed on changes of the selections.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addSelectionChangeListener(SelectionChangeListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Remove a listener from the list of selection listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeSelectionChangeListener(SelectionChangeListener listener)
	{
		listeners.remove(listener);
	}

	private void triggerPixelValuesChanged()
	{
		for (SelectionChangeListener l : listeners) {
			l.pixelValuesChanged();
		}
	}

	private void triggerGeographicValuesChanged()
	{
		for (SelectionChangeListener l : listeners) {
			l.geographicValuesChanged();
		}
	}

	private void triggerChange()
	{
		for (SelectionChangeListener l : listeners) {
			l.selectionChanged();
		}
	}

	/**
	 * Add the denoted LineString to the list of edited geometries.
	 * 
	 * @param string
	 *            the LineString to add.
	 */
	public void add(LineString string)
	{
		if (currentEditor.isEmpty()) {
			editors.remove(editors.size() - 1);
		}
		LineEditor editor = new LineEditor(window);
		editors.add(editor);
		currentEditor = editor;
		editor.setLineDegrees(string);
		triggerChange();
	}

	/**
	 * Remove the element at the denoted index position from the list of
	 * geometries.
	 * 
	 * @param index
	 *            the index of the geometry to remove.
	 */
	public void remove(int index)
	{
		LineEditor removed = editors.remove(index);
		if (currentEditor == removed) {
			newEditor();
		}
		triggerChange();
	}

	/**
	 * Set the currently edited geometry to the one referenced by the denoted
	 * index.
	 * 
	 * @param index
	 *            the index of the geometry to edit.
	 */
	public void setEditedGeometry(int index)
	{
		LineEditor editor = editors.get(index);
		currentEditor = editor;
	}

}
