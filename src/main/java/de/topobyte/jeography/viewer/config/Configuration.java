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

package de.topobyte.jeography.viewer.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A configuration for JeographyGIS.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Configuration
{

	private int width = 1000, height = 600;
	private boolean isOnline = true;
	private boolean showCrosshair = false;
	private boolean showGrid = false;
	private boolean showTileNumbers = false;
	private boolean showOverlay = false;
	private boolean showGeometryManager = false;
	private boolean showSelectionRectDialog = false;
	private boolean showSelectionPolyDialog = false;
	private boolean showMapWindowDialog = false;
	private double lon = 13.414;
	private double lat = 52.49;
	private int zoom = 9;
	private String lookAndFeel = null;

	private List<TileConfig> tileConfigs = new ArrayList<>();
	private List<TileConfig> tileConfigsOverlay = new ArrayList<>();

	private Path pathDatabase = null;

	/**
	 * @param tileConfigs
	 *            the list of configurations to store.
	 */
	public void setTileConfigs(List<TileConfig> tileConfigs)
	{
		this.tileConfigs = tileConfigs;
	}

	/**
	 * @return the list of configurations stored.
	 */
	public List<TileConfig> getTileConfigs()
	{
		return tileConfigs;
	}

	/**
	 * @param tileConfigsOverlay
	 *            the list of configurations to store.
	 */
	public void setTileConfigsOverlay(List<TileConfig> tileConfigsOverlay)
	{
		this.tileConfigsOverlay = tileConfigsOverlay;
	}

	/**
	 * @return the list of configurations stored.
	 */
	public List<TileConfig> getTileConfigsOverlay()
	{
		return tileConfigsOverlay;
	}

	/**
	 * Create a default osm-based configuration.
	 * 
	 * @return the created configuration.
	 */
	public static Configuration createDefaultConfiguration()
	{
		Configuration configuration = new Configuration();

		configuration.tileConfigs.add(new TileConfigUrlDisk(1, "Mapnik",
				"http://tile.openstreetmap.org/%d/%d/%d.png",
				"/tmp/mapImagesMapnik"));
		configuration.tileConfigs.add(new TileConfigUrlDisk(2, "Mapnik (de)",
				"http://osm.rrze.fau.de/osmde/%d/%d/%d.png",
				"/tmp/mapImagesMapnikDe"));
		configuration.tileConfigs.add(new TileConfigUrlDisk(3, "Cyclemap",
				"http://a.tile.opencyclemap.org/cycle/%d/%d/%d.png",
				"/tmp/mapImagesCyclemap"));
		configuration.tileConfigs.add(new TileConfigUrlDisk(4, "OpenTopoMap",
				"http://opentopomap.org/%d/%d/%d.png",
				"/tmp/mapImagesOpenTopoMap"));

		String userAgent = "Jeography GIS";

		for (TileConfig tileConfig : configuration.tileConfigs) {
			if (tileConfig instanceof TileConfigUrl) {
				((TileConfigUrl) tileConfig).setUserAgent(userAgent);
			} else if (tileConfig instanceof TileConfigUrlDisk) {
				((TileConfigUrlDisk) tileConfig).setUserAgent(userAgent);
			}
		}

		configuration.pathDatabase = Paths.get("/tmp/places.sqlite");

		return configuration;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the isOnline
	 */
	public boolean isOnline()
	{
		return isOnline;
	}

	/**
	 * @param isOnline
	 *            the isOnline to set
	 */
	public void setOnline(boolean isOnline)
	{
		this.isOnline = isOnline;
	}

	/**
	 * @return the showCrosshair
	 */
	public boolean isShowCrosshair()
	{
		return showCrosshair;
	}

	/**
	 * @param showCrosshair
	 *            the showCrosshair to set
	 */
	public void setShowCrosshair(boolean showCrosshair)
	{
		this.showCrosshair = showCrosshair;
	}

	/**
	 * @return the showGrid
	 */
	public boolean isShowGrid()
	{
		return showGrid;
	}

	/**
	 * @param showGrid
	 *            the showGrid to set
	 */
	public void setShowGrid(boolean showGrid)
	{
		this.showGrid = showGrid;
	}

	/**
	 * @return the showTileNumbers
	 */
	public boolean isShowTileNumbers()
	{
		return showTileNumbers;
	}

	/**
	 * @param showTileNumbers
	 *            the showTileNumbers to set
	 */
	public void setShowTileNumbers(boolean showTileNumbers)
	{
		this.showTileNumbers = showTileNumbers;
	}

	/**
	 * @return the showOverlay
	 */
	public boolean isShowOverlay()
	{
		return showOverlay;
	}

	/**
	 * @param showOverlay
	 *            the showOverlay to set
	 */
	public void setShowOverlay(boolean showOverlay)
	{
		this.showOverlay = showOverlay;
	}

	/**
	 * @return the showGeometryManager
	 */
	public boolean isShowGeometryManager()
	{
		return showGeometryManager;
	}

	/**
	 * @param showGeometryManager
	 *            the showGeometryManager to set
	 */
	public void setShowGeometryManager(boolean showGeometryManager)
	{
		this.showGeometryManager = showGeometryManager;
	}

	/**
	 * @return the showSelectionRectDialog
	 */
	public boolean isShowSelectionRectDialog()
	{
		return showSelectionRectDialog;
	}

	/**
	 * @return the showSelectionPolyDialog
	 */
	public boolean isShowSelectionPolyDialog()
	{
		return showSelectionPolyDialog;
	}

	/**
	 * @param showSelectionRectDialog
	 *            the showSelectionRectDialog to set
	 */
	public void setShowSelectionRectDialog(boolean showSelectionRectDialog)
	{
		this.showSelectionRectDialog = showSelectionRectDialog;
	}

	/**
	 * @param showSelectionPolyDialog
	 *            the showSelectionPolyDialog to set
	 */
	public void setShowSelectionPolyDialog(boolean showSelectionPolyDialog)
	{
		this.showSelectionPolyDialog = showSelectionPolyDialog;
	}

	/**
	 * @return the showMapWindowDialog
	 */
	public boolean isShowMapWindowDialog()
	{
		return showMapWindowDialog;
	}

	/**
	 * @param showMapWindowDialog
	 *            the showMapWindowDialog to set
	 */
	public void setShowMapWindowDialog(boolean showMapWindowDialog)
	{
		this.showMapWindowDialog = showMapWindowDialog;
	}

	/**
	 * @return the lon
	 */
	public double getLon()
	{
		return lon;
	}

	/**
	 * @param lon
	 *            the lon to set
	 */
	public void setLon(double lon)
	{
		this.lon = lon;
	}

	/**
	 * @return the lat
	 */
	public double getLat()
	{
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat)
	{
		this.lat = lat;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom()
	{
		return zoom;
	}

	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(int zoom)
	{
		this.zoom = zoom;
	}

	public String getLookAndFeel()
	{
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel)
	{
		this.lookAndFeel = lookAndFeel;
	}

	public Path getPathDatabase()
	{
		return pathDatabase;
	}

	public void setPathDatabase(Path pathDatabase)
	{
		this.pathDatabase = pathDatabase;
	}

}
