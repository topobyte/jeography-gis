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

package de.topobyte.jeography.tiles;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class FileCache<T>
{

	final static Logger logger = LoggerFactory.getLogger(FileCache.class);

	private PathResoluter<T> resolver;

	public FileCache(PathResoluter<T> resolver)
	{
		this.resolver = resolver;
	}

	/**
	 * Put this thing's loaded image into the disk-cache.
	 * 
	 * @param thing
	 *            the thing.
	 * @param biab
	 *            the thing's data to cache.
	 */
	public void push(T thing, BufferedImageAndBytes biab)
	{
		String cacheFileName = resolver.getCacheFile(thing);
		logger.debug("Writing to cache: " + cacheFileName);
		try {
			FileOutputStream fos = new FileOutputStream(cacheFileName);
			fos.write(biab.bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
