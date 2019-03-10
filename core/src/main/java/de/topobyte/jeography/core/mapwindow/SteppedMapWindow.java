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

package de.topobyte.jeography.core.mapwindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.BBox;
import de.topobyte.geomath.WGS84;
import de.topobyte.interactiveview.ZoomChangedListener;
import de.topobyte.jeography.core.OverlayPoint;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SteppedMapWindow implements TileMapWindow
{

	final static Logger logger = LoggerFactory
			.getLogger(SteppedMapWindow.class);

	private static final int DEFAULT_ZOOM_MIN = 1;
	private static final int DEFAULT_ZOOM_MAX = 18;

	private int zoomMin = DEFAULT_ZOOM_MIN;
	private int zoomMax = DEFAULT_ZOOM_MAX;

	int tileWidth = 256, tileHeight = 256;

	double lon;
	double lat;
	int width, height, zoom, tx, ty;
	// number of pixels the first tile is offset
	// _____________________________________
	// |yoff|
	// |____|___________________________
	// | |
	// |____|
	// |xoff| // xoff and yoff non-negative
	// | |
	// | |
	// | |
	// | |
	// | |
	int xoff, yoff;

	/**
	 * @param width
	 *            the window's width.
	 * @param height
	 *            the window's height.
	 * @param zoom
	 *            the zoom level.
	 * @param tx
	 *            the tile x coordinate.
	 * @param ty
	 *            the tile y coordinate.
	 * @param xoff
	 *            position on the tile.
	 * @param yoff
	 *            position on the tile.
	 */
	public SteppedMapWindow(int width, int height, int zoom, int tx, int ty,
			int xoff, int yoff)
	{
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.tx = tx;
		this.ty = ty;
		this.xoff = xoff;
		this.yoff = yoff;
		geoFromTiles();
	}

	/**
	 * @param width
	 *            the window's width.
	 * @param height
	 *            the window's height.
	 * @param zoom
	 *            the zoom level.
	 * @param lon
	 *            the center longitude.
	 * @param lat
	 *            the center latitude.
	 */
	public SteppedMapWindow(int width, int height, int zoom, double lon,
			double lat)
	{
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.lon = lon;
		this.lat = lat;
		tilesFromGeo();
	}

	/**
	 * @param bbox
	 *            a bounding box to cover.
	 * @param zoom
	 *            the zoomlevel to use.
	 */
	public SteppedMapWindow(BBox bbox, int zoom)
	{
		this.zoom = zoom;
		// compute tx, ty, xoff, yoff, width, height
		double tileX1 = WGS84.lon2merc(bbox.getLon1(), 1 << zoom);
		double tileY1 = WGS84.lat2merc(bbox.getLat1(), 1 << zoom);
		double tileX2 = WGS84.lon2merc(bbox.getLon2(), 1 << zoom);
		double tileY2 = WGS84.lat2merc(bbox.getLat2(), 1 << zoom);
		logger.debug(
				String.format("%f,%f %f,%f", tileX1, tileY1, tileX2, tileY2));
		tx = (int) tileX1;
		ty = (int) tileY1;
		xoff = (int) Math.round((tileX1 - tx) * tileWidth);
		yoff = (int) Math.round((tileY1 - ty) * tileHeight);
		while (xoff >= tileWidth) {
			xoff -= tileWidth;
			tx += 1;
		}
		while (yoff >= tileHeight) {
			yoff -= tileHeight;
			ty += 1;
		}
		width = (int) Math.ceil((tileX2 - tileX1) * tileWidth);
		height = (int) Math.ceil((tileY2 - tileY1) * tileHeight);
		logger.debug(String.format("%d,%d", width, height));

		geoFromTiles();
	}

	private boolean checkAndCorrectLongitudeBounds()
	{
		// TODO: using loops here is simple, yet a bit of a overhead
		boolean boundariesCrossed = false;
		while (lon > 180) {
			boundariesCrossed = true;
			lon -= 360;
		}
		while (lon < -180) {
			boundariesCrossed = true;
			lon += 360;
		}
		return boundariesCrossed;
	}

	private void geoFromTiles()
	{
		// compute lon/lat
		lon = getCenterLon();
		lat = getCenterLat();
		if (checkAndCorrectLongitudeBounds()) {
			tilesFromGeo();
		}
	}

	private void tilesFromGeo()
	{
		// compute tx, ty, xoff, yoff
		double tileX = WGS84.lon2merc(lon, 1 << zoom);
		double tileY = WGS84.lat2merc(lat, 1 << zoom);
		tileX -= width / 2.0 / tileWidth;
		tileY -= height / 2.0 / tileHeight;
		// NOTE: floor is important here. A simple cast to int
		// won't do it in the case of negative values
		tx = (int) Math.floor(tileX);
		ty = (int) Math.floor(tileY);
		xoff = (int) Math.round((tileX - tx) * tileWidth);
		yoff = (int) Math.round((tileY - ty) * tileHeight);
		while (xoff >= tileWidth) {
			xoff -= tileWidth;
			tx += 1;
		}
		while (yoff >= tileHeight) {
			yoff -= tileHeight;
			ty += 1;
		}
		logger.debug(String.format("%d,%d %d,%d", tx, ty, xoff, yoff));
	}

	/**
	 * Get the window's center's tile coordinate.
	 * 
	 * @return the center's tile coordinate.
	 */
	@Override
	public double getCenterX()
	{
		double x = tx * tileWidth;
		x += xoff;
		x += width / 2.0;
		return x / tileWidth;
	}

	/**
	 * Get the window's center's tile coordinate.
	 * 
	 * @return the center's tile coordinate.
	 */
	@Override
	public double getCenterY()
	{
		double y = ty * tileHeight;
		y += yoff;
		y += height / 2.0;
		return y / tileHeight;
	}

	/**
	 * @return the MapWindow's center's longitude
	 */
	@Override
	public double getCenterLon()
	{
		return WGS84.merc2lon(getCenterX(), 1 << zoom);
	}

	/**
	 * @return the MapWindow's center's latitude
	 */
	@Override
	public double getCenterLat()
	{
		return WGS84.merc2lat(getCenterY(), 1 << zoom);
	}

	/**
	 * Get the tile coordinate at the given x in view space.
	 * 
	 * @param px
	 *            the position in view space.
	 * @return the position in tile space.
	 */
	@Override
	public double getPositionX(int px)
	{
		double x = tx * tileWidth;
		x += xoff;
		x += px;
		return x / tileWidth;
	}

	/**
	 * Get the tile coordinate at the given y in view space.
	 * 
	 * @param py
	 *            the position in view space.
	 * @return the position in tile space.
	 */
	@Override
	public double getPositionY(int py)
	{
		double y = ty * tileHeight;
		y += yoff;
		y += py;
		return y / tileHeight;
	}

	/**
	 * Get the longitude of the given x in view space.
	 * 
	 * @param x
	 *            the position in view space.
	 * @return the longitude of this position.
	 */
	@Override
	public double getPositionLon(int x)
	{
		return WGS84.merc2lon(getPositionX(x), 1 << zoom);
	}

	/**
	 * Get the latitude of the given y in view space.
	 * 
	 * @param y
	 *            the position in view space.
	 * @return the latitude of this position.
	 */
	@Override
	public double getPositionLat(int y)
	{
		return WGS84.merc2lat(getPositionY(y), 1 << zoom);
	}

	/**
	 * Get the covered area as a bounding box.
	 * 
	 * @return the bounding box.
	 */
	@Override
	public BBox getBoundingBox()
	{
		double lon1 = getPositionLon(0);
		double lon2 = getPositionLon(width);
		double lat1 = getPositionLat(0);
		double lat2 = getPositionLat(height);
		return new BBox(lon1, lat1, lon2, lat2);
	}

	/**
	 * @return the number of tiles on the x-axis
	 */
	@Override
	public int getNumTilesX()
	{
		int n = 1;
		int coverFirst = tileWidth - xoff;
		int left = width - coverFirst;
		if (left >= 0) {
			left += tileWidth - 1;
			n = 1 + left / tileWidth;
		}
		return n;
	}

	/**
	 * @return the number of tiles on the y-axis
	 */
	@Override
	public int getNumTilesY()
	{
		int n = 1;
		int coverFirst = tileHeight - yoff;
		int top = height - coverFirst;
		if (top >= 0) {
			top += tileHeight - 1;
			n = 1 + top / tileHeight;
		}
		return n;
	}

	/**
	 * @return the current zoom level.
	 */
	@Override
	public double getZoom()
	{
		return zoom;
	}

	@Override
	public int getZoomLevel()
	{
		return zoom;
	}

	/**
	 * @return the current width.
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return the current height.
	 */
	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public Iterator<TileOnWindow> iterator()
	{
		return new MapWindowIterator(this);
	}

	/**
	 * Adjust the size of the window.
	 * 
	 * @param newWidth
	 *            the new width value.
	 * @param newHeight
	 *            the new height value.
	 */
	@Override
	public void resize(int newWidth, int newHeight)
	{
		width = newWidth;
		height = newHeight;
		tilesFromGeo();
		fireChangeListeners();
	}

	/**
	 * Reposition the map window with the given values. Positive values move the
	 * window up and left. You may supply negative values here.
	 * 
	 * @param dx
	 *            the amount of pixels to move to the left.
	 * @param dy
	 *            the amount of pixels to move to the top.
	 */
	@Override
	public void move(int dx, int dy)
	{
		// to move down right, supply negative values here.
		xoff += dx;
		yoff += dy;

		// check for overflow within tilesize
		while (xoff < 0) {
			xoff += tileWidth;
			tx -= 1;
		}
		while (xoff > tileWidth) {
			xoff -= tileWidth;
			tx += 1;
		}
		while (yoff < 0) {
			yoff += tileHeight;
			ty -= 1;
		}
		while (yoff > tileHeight) {
			yoff -= tileHeight;
			ty += 1;
		}
		geoFromTiles();
		fireChangeListeners();
	}

	/**
	 * Set the maximum allowed zoomlevel.
	 * 
	 * @param zoomMax
	 *            the maximum zoomlevel to allow
	 */
	@Override
	public void setMaxZoom(int zoomMax)
	{
		this.zoomMax = zoomMax;
	}

	/**
	 * Set the minimum allowed zoomlevel.
	 * 
	 * @param zoomMin
	 *            the minimum zoomlevel to allow
	 */
	@Override
	public void setMinZoom(int zoomMin)
	{
		this.zoomMin = zoomMin;
	}

	/**
	 * @return the maximal zoom level allowed.
	 */
	@Override
	public int getMaxZoom()
	{
		return zoomMax;
	}

	/**
	 * @return the minimal zoom level allowed.
	 */
	@Override
	public int getMinZoom()
	{
		return zoomMin;
	}

	/**
	 * zoom in to center if possible.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	@Override
	public boolean zoomIn()
	{
		int oldZoom = zoom;
		if (zoom < zoomMax) {
			zoom += 1;
		}
		tilesFromGeo();
		fireChangeListeners();
		fireZoomListeners();
		return zoom != oldZoom;
	}

	/**
	 * zoom out from center if possible.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	@Override
	public boolean zoomOut()
	{
		int oldZoom = zoom;
		if (zoom > zoomMin) {
			zoom -= 1;
		}
		tilesFromGeo();
		fireChangeListeners();
		fireZoomListeners();
		return zoom != oldZoom;
	}

	/**
	 * zoom in to the denoted zoomlevel if possible.
	 * 
	 * @param zoomlevel
	 *            the new zoomlevel to set.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	@Override
	public boolean zoom(int zoomlevel)
	{
		if (zoomlevel > zoomMax || zoomlevel < zoomMin) {
			return false;
		}
		if (zoomlevel == zoom) {
			return false;
		}
		zoom = zoomlevel;
		tilesFromGeo();
		fireChangeListeners();
		fireZoomListeners();
		return true;
	}

	@Override
	public void zoomInToPosition(int x, int y)
	{
		this.lon = getPositionLon(x);
		this.lat = getPositionLat(y);
		checkAndCorrectLongitudeBounds();
		zoomIn();
	}

	@Override
	public void zoomOutToPosition(int x, int y)
	{
		this.lon = getPositionLon(x);
		this.lat = getPositionLat(y);
		checkAndCorrectLongitudeBounds();
		zoomOut();
	}

	Set<MapWindowChangeListener> listenersChangeGeneral = new HashSet<>();
	Set<ZoomChangedListener> listenersChangeZoom = new HashSet<>();
	Set<MapWindowTileSizeListener> listenersChangeTileSize = new HashSet<>();
	Set<MapWindowWorldScaleListener> listenersChangeWorldScale = new HashSet<>();

	/**
	 * Add the given listener to the set of general change listeners. The
	 * listener will be notified on all events.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	@Override
	public void addChangeListener(MapWindowChangeListener listener)
	{
		listenersChangeGeneral.add(listener);
	}

	/**
	 * Add the given listener to the set of zoom change listeners. The listener
	 * will be notified on zoom events only.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	@Override
	public void addZoomListener(ZoomChangedListener listener)
	{
		listenersChangeZoom.add(listener);
	}

	/**
	 * Add the given listener to the set of tile size change listeners. The
	 * listener will be notified on tile size events only.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	@Override
	public void addTileSizeListener(MapWindowTileSizeListener listener)
	{
		listenersChangeTileSize.add(listener);
	}

	@Override
	public void addWorldScaleListener(MapWindowWorldScaleListener listener)
	{
		listenersChangeWorldScale.add(listener);
	}

	private void fireChangeListeners()
	{
		for (MapWindowChangeListener listener : listenersChangeGeneral) {
			listener.changed();
		}
	}

	private void fireZoomListeners()
	{
		for (ZoomChangedListener listener : listenersChangeZoom) {
			listener.zoomChanged();
		}
	}

	private void fireTileSizeChangeListeners()
	{
		for (MapWindowTileSizeListener listener : listenersChangeTileSize) {
			listener.tileSizeChanged();
		}
	}

	private void fireWorldScaleChangeListeners()
	{
		for (MapWindowWorldScaleListener listener : listenersChangeWorldScale) {
			listener.worldScaleChanged();
		}
	}

	/**
	 * Calculate the current x position of the given longitude.
	 * 
	 * @param ilon
	 *            the longitude.
	 * @return the longitude's position on this window.
	 */
	@Override
	public double longitudeToX(double ilon)
	{
		double tileX = WGS84.lon2merc(ilon, 1 << zoom);
		double pos = tileX - tx - xoff / (double) tileWidth;
		return pos * tileWidth;
	}

	/**
	 * Calculate the current y position of the given latitude.
	 * 
	 * @param ilat
	 *            the latitude.
	 * @return the latitude's position on this window.
	 */
	@Override
	public double latitudeToY(double ilat)
	{
		double tileY = WGS84.lat2merc(ilat, 1 << zoom);
		double pos = tileY - ty - yoff / (double) tileHeight;
		return pos * tileHeight;
	}

	/**
	 * Calculate the current x position of the given mercator x.
	 * 
	 * @param mx
	 *            the mercator value in the window's zoom level dimension.
	 * @return the longitude's position on this window.
	 */
	@Override
	public double mercatorToX(double mx)
	{
		if (tileWidth == Tile.SIZE) {
			int bx = tx * Tile.SIZE + xoff;
			double pos = mx - bx;
			return pos;
		} else {
			double ratio = (tileWidth / (double) Tile.SIZE);
			double bx = tx * (Tile.SIZE) + xoff / ratio;
			double pos = mx - bx;
			return pos * ratio;
		}
	}

	/**
	 * Calculate the current y position of the given mercator y.
	 * 
	 * @param my
	 *            the mercator value in the window's zoom level dimension.
	 * @return the latitude's position on this window.
	 */
	@Override
	public double mercatorToY(double my)
	{
		if (tileHeight == Tile.SIZE) {
			int by = ty * Tile.SIZE + yoff;
			double pos = my - by;
			return pos;
		} else {
			double ratio = (tileHeight / (double) Tile.SIZE);
			double by = ty * (Tile.SIZE) + yoff / ratio;
			double pos = my - by;
			return pos * ratio;
		}
	}

	/**
	 * Center the map on this position.
	 * 
	 * @param longitude
	 *            the longitude.
	 * @param latitude
	 *            the latitude.
	 */
	@Override
	public void gotoLonLat(double longitude, double latitude)
	{
		lon = longitude;
		lat = latitude;
		checkAndCorrectLongitudeBounds();
		tilesFromGeo();
		fireChangeListeners();
	}

	/**
	 * Move the viewport to show all points.
	 * 
	 * @param points
	 *            the points to show
	 */
	@Override
	public void gotoPoints(Collection<OverlayPoint> points)
	{
		OverlayPoint mean = OverlayPoint.mean(points);
		gotoLonLat(mean.getLongitude(), mean.getLatitude());
		OverlayPoint minimum = OverlayPoint.minimum(points);
		OverlayPoint maximum = OverlayPoint.maximum(points);
		zoom = zoomMax;
		tilesFromGeo();
		while (!containsPoint(minimum) || !containsPoint(maximum)) {
			if (!zoomOut()) {
				break;
			}
		}
		fireChangeListeners();
	}

	/**
	 * @param lon1
	 *            minimum longitude.
	 * @param lon2
	 *            maximum longitude.
	 * @param lat1
	 *            minimum latitude.
	 * @param lat2
	 *            maximum latitude.
	 */
	@Override
	public void gotoLonLat(double lon1, double lon2, double lat1, double lat2)
	{
		List<OverlayPoint> points = new ArrayList<>();
		points.add(new OverlayPoint(lon1, lat1));
		points.add(new OverlayPoint(lon2, lat2));
		gotoPoints(points);
	}

	/**
	 * Test whether this point is within this window.
	 * 
	 * @param point
	 *            the point to test for.
	 * @return whether this point is currently within this window.
	 */
	@Override
	public boolean containsPoint(OverlayPoint point)
	{
		double tileX = WGS84.lon2merc(point.getLongitude(), 1 << zoom);
		double tileY = WGS84.lat2merc(point.getLatitude(), 1 << zoom);
		double minX = tx + xoff / (double) tileWidth;
		double maxX = tx + (xoff + width) / (double) tileWidth;
		double minY = ty + yoff / (double) tileHeight;
		double maxY = ty + (yoff + height) / (double) tileHeight;
		return tileX >= minX && tileX <= maxX && tileY >= minY && tileY <= maxY;
	}

	@Override
	public double getX(double x)
	{
		return longitudeToX(x);
	}

	@Override
	public double getY(double y)
	{
		return latitudeToY(y);
	}

	/**
	 * Get the position of the given window relative to this window.
	 * 
	 * @param mapWindow
	 *            the window to inspect.
	 * @return the x offset relative to this window's top left corner.
	 */
	public int getPositionX(SteppedMapWindow mapWindow)
	{
		int thisX = tx * tileWidth + xoff;
		int otherX = mapWindow.tx * tileWidth + mapWindow.xoff;
		// logger.debug("this.x: " + thisX);
		// logger.debug("other.x: " + otherX);
		return otherX - thisX;
	}

	/**
	 * Get the position of the given window relative to this window.
	 * 
	 * @param mapWindow
	 *            the window to inspect.
	 * @return the y offset relative to this window's top left corner.
	 */
	public int getPositionY(SteppedMapWindow mapWindow)
	{
		int thisY = ty * tileHeight + yoff;
		int otherY = mapWindow.ty * tileHeight + mapWindow.yoff;
		// logger.debug("this.y: " + thisY);
		// logger.debug("other.y: " + otherY);
		return otherY - thisY;
	}

	@Override
	public boolean setTileSize(int tileSize)
	{
		if (this.tileWidth == tileSize && this.tileHeight == tileSize) {
			return false;
		}
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		tilesFromGeo();
		fireChangeListeners();
		fireTileSizeChangeListeners();
		fireWorldScaleChangeListeners();
		return true;
	}

	@Override
	public int getTileWidth()
	{
		return tileWidth;
	}

	@Override
	public int getTileHeight()
	{
		return tileHeight;
	}

	@Override
	public int minimumCacheSize()
	{
		int tilesX = (width + tileWidth - 1) / tileWidth;
		int tilesY = (height + tileHeight - 1) / tileHeight;
		int cacheSize = (tilesX + 1) * (tilesY + 1);
		return cacheSize;
	}

	@Override
	public double getWorldsizePixels()
	{
		return (1 << zoom) * tileWidth;
	}

	@Override
	public boolean setWorldScale(int worldScale)
	{
		return setTileSize(worldScale);
	}

	@Override
	public int getWorldScale()
	{
		return getTileWidth();
	}

}
