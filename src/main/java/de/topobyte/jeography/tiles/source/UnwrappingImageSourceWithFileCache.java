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

import java.awt.image.BufferedImage;

import de.topobyte.jeography.tiles.BufferedImageAndBytes;
import de.topobyte.jeography.tiles.FileCache;
import de.topobyte.jeography.tiles.PathResoluter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class UnwrappingImageSourceWithFileCache<T> implements
		ImageSource<T, BufferedImage>
{

	public ImageSource<T, BufferedImageAndBytes> source;
	private FileCache<T> cache;

	public UnwrappingImageSourceWithFileCache(
			ImageSource<T, BufferedImageAndBytes> source,
			PathResoluter<T> resolver)
	{
		this.source = source;
		cache = new FileCache<>(resolver);
	}

	@Override
	public BufferedImage load(T thing)
	{
		BufferedImageAndBytes result = source.load(thing);
		if (result != null) {
			cache.push(thing, result);
			return result.getImage();
		}
		return null;
	}

}
