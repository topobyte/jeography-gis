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

package de.topobyte.jeography.core;

import java.util.Collection;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class OverlayPoint
{

	private double lon;
	private double lat;

	/**
	 * Create a point.
	 * 
	 * @param lon
	 *            this point's longitude.
	 * @param lat
	 *            this.point's latitude.
	 */
	public OverlayPoint(double lon, double lat)
	{
		this.lon = lon;
		this.lat = lat;
	}

	/**
	 * Get this point's longitude.
	 * 
	 * @return the longitude.
	 */
	public double getLongitude()
	{
		return lon;
	}

	/**
	 * Get this point's latitude.
	 * 
	 * @return the latitude.
	 */
	public double getLatitude()
	{
		return lat;
	}

	/**
	 * Calculate a new point that is the mean of all input points.
	 * 
	 * @param points
	 *            a collection of points.
	 * @return the mean of all points as a point.
	 */
	public static OverlayPoint mean(Collection<OverlayPoint> points)
	{
		double lon = 0.0;
		double lat = 0.0;

		for (OverlayPoint p : points) {
			lon += p.getLongitude();
			lat += p.getLatitude();
		}

		lon /= points.size();
		lat /= points.size();

		return new OverlayPoint(lon, lat);
	}

	/**
	 * Calculate a new point that has as its coordinate the minimum in both
	 * dimensions.
	 * 
	 * @param points
	 *            a collection of points.
	 * @return a point representing the minimum.
	 */
	public static OverlayPoint minimum(Collection<OverlayPoint> points)
	{
		double lon = Double.POSITIVE_INFINITY;
		double lat = Double.POSITIVE_INFINITY;
		for (OverlayPoint p : points) {
			if (p.getLongitude() < lon)
				lon = p.getLongitude();
			if (p.getLatitude() < lat)
				lat = p.getLatitude();
		}
		return new OverlayPoint(lon, lat);
	}

	/**
	 * Calculate a new point that has as its coordinate the maximum in both
	 * dimensions.
	 * 
	 * @param points
	 *            a collection of points.
	 * @return a point representing the maximum.
	 */
	public static OverlayPoint maximum(Collection<OverlayPoint> points)
	{
		double lon = Double.NEGATIVE_INFINITY;
		double lat = Double.NEGATIVE_INFINITY;
		for (OverlayPoint p : points) {
			if (p.getLongitude() > lon)
				lon = p.getLongitude();
			if (p.getLatitude() > lat)
				lat = p.getLatitude();
		}
		return new OverlayPoint(lon, lat);
	}

	@Override
	public String toString()
	{
		return lon + "," + lat;
	}

}
