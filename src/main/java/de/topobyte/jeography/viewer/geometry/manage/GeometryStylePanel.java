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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.chromaticity.AwtColors;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryStylePanel extends JPanel
{

	private static final long serialVersionUID = 8369761276647597078L;

	GeometryStyle style;

	/**
	 * Set the style to display.
	 * 
	 * @param style
	 *            the style to display.
	 */
	public void setStyle(GeometryStyle style)
	{
		this.style = style;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		GraphicsUtil.useAntialiasing(g2d, true);

		int offsetX = 5;
		int offsetY = 5;
		int height = getHeight() - offsetX * 2;
		int width = getWidth() - offsetY * 2;

		int checkSize = 5;
		Color checkColor1 = new Color(0xff808080, true);
		Color checkColor2 = new Color(0xffc0c0c0, true);
		g2d.setColor(checkColor1);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(checkColor2);
		for (int y = 0; y < getHeight(); y += checkSize) {
			for (int x = (y % 2) == 0 ? 0 : checkSize; x < getWidth(); x += checkSize * 2) {
				g2d.fillRect(x, y, checkSize, checkSize);
			}
		}

		g2d.setColor(AwtColors.convert(style.getColorFill()));
		g2d.fillOval(offsetX, offsetY, width, height);
		g2d.setColor(AwtColors.convert(style.getColorStroke()));
		g2d.setStroke(new BasicStroke((float) style.getLineWidth()));
		g2d.drawOval(offsetX, offsetY, width, height);
	}

}
