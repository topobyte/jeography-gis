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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.topobyte.jeography.tiles.BufferedImageAndBytes;
import de.topobyte.jeography.tiles.FileCache;
import de.topobyte.jeography.tiles.PathResoluter;

/**
 * @param <T>
 *            the type of things that map to cached images.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageProviderDisk<T> extends ImageProvider<T, BufferedImage>
{

	private PathResoluter<T> resolver;
	private FileCache<T> cache;

	public ImageProviderDisk(PathResoluter<T> resolver)
	{
		super(1);
		this.resolver = resolver;
		cache = new FileCache<>(resolver);
	}

	@Override
	public BufferedImage load(T thing)
	{
		String cacheFile = resolver.getCacheFile(thing);

		File file = new File(cacheFile);
		if (!file.exists()) {
			return null;
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			BufferedImage image = ImageIO.read(fis);
			return image;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					System.out.println("unable to close FileInputStream: "
							+ e.getMessage());
				}
			}
		}

		return null;
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
		// TODO: dispatch into separate Thread
		cache.push(thing, biab);
	}

}
