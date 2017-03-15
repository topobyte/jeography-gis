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

package de.topobyte.jeography.viewer.geometry.manage;

import java.util.ArrayList;
import java.util.List;

import de.topobyte.chromaticity.ColorCode;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryStyle
{

	private String name;
	private ColorCode colorFill, colorStroke;
	private boolean fill, stroke;
	private boolean drawNodes;
	private double lineWidth;

	/**
	 * A default style
	 */
	public GeometryStyle()
	{
		this("default", "0x66ff0000", "0xaa000000", true, true, false, 1.0);
	}

	/**
	 * Create a GeometryStyle
	 * 
	 * @param name
	 *            the name of the style
	 * @param colFill
	 *            color for fillings
	 * @param colStroke
	 *            color for lines
	 * @param fill
	 *            whether to paint fillings
	 * @param stroke
	 *            whether to paint lines
	 * @param drawNodes
	 *            whether to draw nodes
	 * @param lineWidth
	 *            the width of lines.
	 */
	public GeometryStyle(String name, String colFill, String colStroke,
			boolean fill, boolean stroke, boolean drawNodes, double lineWidth)
	{
		this.name = name;
		this.fill = fill;
		this.stroke = stroke;
		this.drawNodes = drawNodes;
		colorFill = fromString(colFill);
		colorStroke = fromString(colStroke);
		this.lineWidth = lineWidth;
	}

	/**
	 * Set many values.
	 * 
	 * @param colorFill
	 *            the fill color.
	 * @param colorStroke
	 *            the stroke color.
	 * @param fill
	 *            whether to fill.
	 * @param stroke
	 *            whether to stroke.
	 * @param drawNodes
	 *            whether to draw nodes.
	 * @param lineWidth
	 *            the width of the stroke line.
	 */
	public void setMany(ColorCode colorFill, ColorCode colorStroke,
			boolean fill, boolean stroke, boolean drawNodes, double lineWidth)
	{
		this.fill = fill;
		this.stroke = stroke;
		this.drawNodes = drawNodes;
		this.colorFill = colorFill;
		this.colorStroke = colorStroke;
		this.lineWidth = lineWidth;
		triggerChange();
	}

	private ColorCode fromString(String colorHex)
	{
		if (colorHex.startsWith("0x")) {
			String hex = colorHex.substring(2);
			long l = Long.parseLong(hex, 16);
			int i = (int) l;
			return new ColorCode(i, true);
		}
		return new ColorCode(0x00000000, true);
	}

	/**
	 * @return the name of this rule.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the color to use for fillings.
	 */
	public ColorCode getColorFill()
	{
		return colorFill;
	}

	/**
	 * @return the color used for lines.
	 */
	public ColorCode getColorStroke()
	{
		return colorStroke;
	}

	/**
	 * @return whether to draw fillings.
	 */
	public boolean isFill()
	{
		return fill;
	}

	/**
	 * @return whether to paint lines
	 */
	public boolean isStroke()
	{
		return stroke;
	}

	/**
	 * @return whether to paint nodes
	 */
	public boolean isDrawNodes()
	{
		return drawNodes;
	}

	/**
	 * @return the line width
	 */
	public double getLineWidth()
	{
		return lineWidth;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
		triggerNameChange();
	}

	/**
	 * @param colorFill
	 *            the colorFill to set
	 */
	public void setColorFill(ColorCode colorFill)
	{
		this.colorFill = colorFill;
		triggerChange();
	}

	/**
	 * @param colorStroke
	 *            the colorStroke to set
	 */
	public void setColorStroke(ColorCode colorStroke)
	{
		this.colorStroke = colorStroke;
		triggerChange();
	}

	/**
	 * @param fill
	 *            the fill to set
	 */
	public void setFill(boolean fill)
	{
		this.fill = fill;
		triggerChange();
	}

	/**
	 * @param drawNodes
	 *            whether to draw nodes.
	 */
	public void setDrawNodes(boolean drawNodes)
	{
		this.drawNodes = drawNodes;
	}

	/**
	 * @param stroke
	 *            the stroke to set
	 */
	public void setStroke(boolean stroke)
	{
		this.stroke = stroke;
		triggerChange();
	}

	/**
	 * @param lineWidth
	 *            the lineWidth to set
	 */
	public void setLineWidth(double lineWidth)
	{
		this.lineWidth = lineWidth;
		triggerChange();
	}

	/**
	 * @param listeners
	 *            the listeners to set
	 */
	public void setListeners(List<GeometryStyleChangeListener> listeners)
	{
		this.listeners = listeners;
	}

	private List<GeometryStyleChangeListener> listeners = new ArrayList<>();

	/**
	 * @param l
	 *            the listener to add
	 */
	public void addChangeListener(GeometryStyleChangeListener l)
	{
		listeners.add(l);
	}

	/**
	 * @param l
	 *            the listener to remove
	 */
	public void removeChangeListener(GeometryStyleChangeListener l)
	{
		listeners.remove(l);
	}

	private void triggerChange()
	{
		for (GeometryStyleChangeListener listener : listeners) {
			listener.changedAttributes();
		}
	}

	private void triggerNameChange()
	{
		for (GeometryStyleChangeListener listener : listeners) {
			listener.changedName();
		}
	}

}
