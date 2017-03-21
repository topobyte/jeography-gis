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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.tiles.CachePathProvider;

/**
 * @param <T>
 *            the type of objects.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageSourcePathPattern<T> implements ImageSource<T, BufferedImage>
{

	static final Logger logger = LoggerFactory
			.getLogger(ImageSourcePathPattern.class);

	private CachePathProvider<T> resolver;

	/**
	 * An ImageSource implementation based that works with PathResoluter
	 * 
	 * @param resolver
	 *            the path generator
	 */
	public ImageSourcePathPattern(CachePathProvider<T> resolver)
	{
		this.resolver = resolver;
	}

	/**
	 * Set the PathResoluter used to resolve image URLs.
	 * 
	 * @param resolver
	 *            the resolver to use
	 */
	public void setPathResoluter(CachePathProvider<T> resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public BufferedImage load(T thing)
	{
		BufferedImage image = loadImage(thing);
		if (image == null) {
			return null;
		}
		return image;
	}

	/**
	 * Load the image and return it as a BufferedImage together with the raw
	 * bytes.
	 * 
	 * @param thing
	 *            the thing to load the image for.
	 * @return the image and its bytes.
	 */
	public BufferedImage loadImage(T thing)
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
					logger.warn("unable to close FileInputStream: "
							+ e.getMessage());
				}
			}
		}

		return null;
	}

}
