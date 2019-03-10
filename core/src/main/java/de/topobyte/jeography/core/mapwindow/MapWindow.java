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

import java.util.Collection;

import de.topobyte.adt.geo.BBox;
import de.topobyte.interactiveview.ZoomChangedListener;
import de.topobyte.jeography.core.OverlayPoint;
import de.topobyte.jgs.transform.CoordinateTransformer;

/**
 * A MapWindow displays a portion of an image of the world. That underlying
 * image of the world has the size 2^zoom * worldscale.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface MapWindow extends CoordinateTransformer
{

	/**
	 * Set the scale factor of the map that determines how big the image of the
	 * world is.
	 * 
	 * @param worldScale
	 *            the factor by which to multiply the worldsize.
	 * @return whether the scale actually changed
	 */
	public boolean setWorldScale(int worldScale);

	/**
	 * @return the scale factor the world image is scaled up with.
	 */
	public int getWorldScale();

	/**
	 * @return the size of the whole world's image in pixels
	 */
	public double getWorldsizePixels();

	/**
	 * @return the MapWindow's center's longitude
	 */
	public double getCenterLon();

	/**
	 * @return the MapWindow's center's latitude
	 */
	public double getCenterLat();

	/**
	 * @return the current zoom level.
	 */
	public double getZoom();

	/**
	 * Get the longitude of the given x in view space.
	 * 
	 * @param x
	 *            the position in view space.
	 * @return the longitude of this position.
	 */
	public double getPositionLon(int x);

	/**
	 * Get the latitude of the given y in view space.
	 * 
	 * @param y
	 *            the position in view space.
	 * @return the latitude of this position.
	 */
	public double getPositionLat(int y);

	/**
	 * Get the covered area as a bounding box.
	 * 
	 * @return the bounding box.
	 */
	public BBox getBoundingBox();

	/**
	 * @return the current width.
	 */
	public int getWidth();

	/**
	 * @return the current height.
	 */
	public int getHeight();

	/**
	 * Adjust the size of the window.
	 * 
	 * @param newWidth
	 *            the new width value.
	 * @param newHeight
	 *            the new height value.
	 */
	public void resize(int newWidth, int newHeight);

	/**
	 * Reposition the map window with the given values. Positive values move the
	 * window up and left. You may supply negative values here.
	 * 
	 * @param dx
	 *            the amount of pixels to move to the left.
	 * @param dy
	 *            the amount of pixels to move to the top.
	 */
	public void move(int dx, int dy);

	/**
	 * Set the maximum allowed zoomlevel.
	 * 
	 * @param zoomMax
	 *            the maximum zoomlevel to allow
	 */
	public void setMaxZoom(int zoomMax);

	/**
	 * Set the minimum allowed zoomlevel.
	 * 
	 * @param zoomMin
	 *            the minimum zoomlevel to allow
	 */
	public void setMinZoom(int zoomMin);

	/**
	 * @return the maximal zoom level allowed.
	 */
	public int getMaxZoom();

	/**
	 * @return the minimal zoom level allowed.
	 */
	public int getMinZoom();

	/**
	 * zoom in to center if possible.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	public boolean zoomIn();

	/**
	 * zoom out from center if possible.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	public boolean zoomOut();

	/**
	 * zoom in to the denoted zoomlevel if possible.
	 * 
	 * @param zoomlevel
	 *            the new zoomlevel to set.
	 * 
	 * @return whether this operation changed the zoom level.
	 */
	public boolean zoom(int zoomlevel);

	public void zoomInToPosition(int x, int y);

	public void zoomOutToPosition(int x, int y);

	/**
	 * Add the given listener to the set of general change listeners. The
	 * listener will be notified on all events.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addChangeListener(MapWindowChangeListener listener);

	/**
	 * Add the given listener to the set of zoom change listeners. The listener
	 * will be notified on zoom events only.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addZoomListener(ZoomChangedListener listener);

	/**
	 * Add the given listener to the set of world scale change listeners. The
	 * listener will be notified on world scale events only.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addWorldScaleListener(MapWindowWorldScaleListener listener);

	/**
	 * Calculate the current x position of the given longitude.
	 * 
	 * @param ilon
	 *            the longitude.
	 * @return the longitude's position on this window.
	 */
	public double longitudeToX(double ilon);

	/**
	 * Calculate the current y position of the given latitude.
	 * 
	 * @param ilat
	 *            the latitude.
	 * @return the latitude's position on this window.
	 */
	public double latitudeToY(double ilat);

	/**
	 * Calculate the current x position of the given mercator x.
	 * 
	 * @param mx
	 *            the mercator value in the window's zoom level dimension.
	 * @return the longitude's position on this window.
	 */
	public double mercatorToX(double mx);

	/**
	 * Calculate the current y position of the given mercator y.
	 * 
	 * @param my
	 *            the mercator value in the window's zoom level dimension.
	 * @return the latitude's position on this window.
	 */
	public double mercatorToY(double my);

	/**
	 * Center the map on this position.
	 * 
	 * @param longitude
	 *            the longitude.
	 * @param latitude
	 *            the latitude.
	 */
	public void gotoLonLat(double longitude, double latitude);

	/**
	 * Move the viewport to show all points.
	 * 
	 * @param points
	 *            the points to show
	 */
	public void gotoPoints(Collection<OverlayPoint> points);

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
	public void gotoLonLat(double lon1, double lon2, double lat1, double lat2);

	/**
	 * Test whether this point is within this window.
	 * 
	 * @param point
	 *            the point to test for.
	 * @return whether this point is currently within this window.
	 */
	public boolean containsPoint(OverlayPoint point);

}
