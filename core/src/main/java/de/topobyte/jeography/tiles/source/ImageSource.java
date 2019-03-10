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

package de.topobyte.jeography.tiles.source;

/**
 * @param <T>
 *            the type of things the data is connected to.
 * 
 * @param <D>
 *            the type of objects this image source creates.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface ImageSource<T, D>
{

	/**
	 * Create the image.
	 * 
	 * @param thing
	 *            the thing to load data for.
	 * 
	 * @return the image or null on error.
	 */
	public D load(T thing);

}
