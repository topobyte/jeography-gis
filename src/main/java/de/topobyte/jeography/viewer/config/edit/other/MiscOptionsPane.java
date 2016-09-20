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

package de.topobyte.jeography.viewer.config.edit.other;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.nio.file.Paths;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.config.Configuration;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class MiscOptionsPane extends JPanel implements Scrollable
{

	private static final long serialVersionUID = -3628373331256579144L;

	final static Logger logger = LoggerFactory.getLogger(MiscOptionsPane.class);

	private LAFSelector lafSelector;
	private BooleanOption showGrid;
	private BooleanOption showTileNumbers;
	private BooleanOption showCrosshair;
	private BooleanOption showOverlay;
	private BooleanOption isOnline;
	private TextOption databaseFile;
	private IntegerOption width;
	private IntegerOption height;
	private DoubleOption lon;
	private DoubleOption lat;
	private IntegerOption zoom;

	// -grid <yes/no> show grid?
	// -tile_numbers <yes/no> show tile numbers?
	// -crosshair <yes/no> show crosshair?
	// -overlay <yes/no> show overlay?
	// -network <yes/no> start online / offline?

	// -db <file> file to use for database queries
	// -height <pixels> Window height
	// -width <pixels> Window width

	// -zoom <int> zoom level to use
	// -lat <double> latitude to show
	// -lon <double> longitude to show

	/**
	 * Constructor
	 * 
	 * @param configuration
	 *            the configuration to edit.
	 */
	public MiscOptionsPane(Configuration configuration)
	{
		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());

		lafSelector = new LAFSelector(configuration);
		showGrid = new BooleanOption("show grid", configuration.isShowGrid());
		showTileNumbers = new BooleanOption("show tile numbers",
				configuration.isShowTileNumbers());
		showCrosshair = new BooleanOption("show crosshair",
				configuration.isShowCrosshair());
		showOverlay = new BooleanOption("show overlay",
				configuration.isShowOverlay());
		isOnline = new BooleanOption("online", configuration.isOnline());
		showGrid.setAlignmentX(0.0f);
		showTileNumbers.setAlignmentX(0.0f);
		showCrosshair.setAlignmentX(0.0f);
		showOverlay.setAlignmentX(0.0f);
		isOnline.setAlignmentX(0.0f);
		databaseFile = new TextOption("database", configuration
				.getPathDatabase().toString());
		width = new IntegerOption("width", configuration.getWidth());
		height = new IntegerOption("height", configuration.getHeight());
		lon = new DoubleOption("lon", configuration.getLon());
		lat = new DoubleOption("lat", configuration.getLat());
		zoom = new IntegerOption("zoom", configuration.getZoom());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridy = 0;
		c.gridwidth = 2;

		add(lafSelector, c);
		c.gridy++;
		add(showGrid, c);
		c.gridy++;
		add(showTileNumbers, c);
		c.gridy++;
		add(showCrosshair, c);
		c.gridy++;
		add(showOverlay, c);
		c.gridy++;
		add(isOnline, c);
		c.gridy++;

		addAsRow(databaseFile, c, true);
		addAsRow(width, c, true);
		addAsRow(height, c, true);
		addAsRow(lon, c, true);
		addAsRow(lat, c, true);
		addAsRow(zoom, c, true);

		// SwingHelper.setBorder(this, Color.GREEN);
	}

	private void addAsRow(TwoComponentOption option, GridBagConstraints c,
			boolean incrementY)
	{
		addAsRow(option.getFirstComponent(), option.getSecondComponent(), c);
		if (incrementY) {
			c.gridy++;
		}
	}

	private void addAsRow(JComponent a, JComponent b, GridBagConstraints c)
	{
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 0;
		add(a, c);
		c.weightx = 0.0;
		c.gridx = 1;
		add(b, c);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 1;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	/**
	 * Set the values of the denoted configuration instance according to the
	 * settings in this GUI.
	 * 
	 * @param configuration
	 *            the configuration whose values to set.
	 */
	public void setValues(Configuration configuration)
	{
		configuration.setOnline(isOnline.getCheckBox().isSelected());
		configuration
				.setShowCrosshair(showCrosshair.getCheckBox().isSelected());
		configuration.setShowGrid(showGrid.getCheckBox().isSelected());
		configuration.setShowOverlay(showOverlay.getCheckBox().isSelected());
		configuration.setShowTileNumbers(showTileNumbers.getCheckBox()
				.isSelected());
		configuration.setWidth(width.getValue());
		configuration.setHeight(height.getValue());
		configuration.setZoom(zoom.getValue());
		configuration.setLon(lon.getValue());
		configuration.setLat(lat.getValue());
		configuration.setPathDatabase(Paths.get(databaseFile.getValue()));
		configuration.setLookAndFeel(lafSelector.getSelectedLookAndFeel());
	}

}
