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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <T>
 *            the type of objects.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageSourceUrlPattern<T> implements ImageSource<T, BufferedImage>
{

	static final Logger logger = LoggerFactory
			.getLogger(ImageSourceUrlPattern.class);

	private UrlResoluter<T> resolver;

	private String userAgent;

	private final int nTries;

	/**
	 * An ImageSource implementation based that works with PathResoluter
	 * 
	 * @param resolver
	 *            the url generator
	 * @param nTries
	 *            the number of times to retry failing images before giving up
	 */
	public ImageSourceUrlPattern(UrlResoluter<T> resolver, int nTries)
	{
		this.resolver = resolver;
		this.nTries = nTries;
	}

	/**
	 * Set the PathResoluter used to resolve image URLs.
	 * 
	 * @param resolver
	 *            the resolver to use
	 */
	public void setPathResoluter(UrlResoluter<T> resolver)
	{
		this.resolver = resolver;
	}

	/**
	 * Set the user-agent to use during HTTP-requests.
	 * 
	 * @param userAgent
	 *            the user agent to use.
	 */
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	@Override
	public BufferedImage load(T thing)
	{
		BufferedImageAndBytes imageAndBytes = loadImage(thing);
		if (imageAndBytes == null) {
			return null;
		}
		return imageAndBytes.image;
	}

	/**
	 * Load the image and return it as a BufferedImage together with the raw
	 * bytes.
	 * 
	 * @param thing
	 *            the thing to load the image for.
	 * @return the image and its bytes.
	 */
	public BufferedImageAndBytes loadImage(T thing)
	{
		String iurl = resolver.getUrl(thing);
		for (int k = 0; k < nTries; k++) {
			if (k > 0) {
				logger.debug("retry #" + k);
			}
			InputStream cis = null;
			InputStream bis = null;
			try {
				URL url = new URL(iurl);
				URLConnection connection = url.openConnection();
				if (userAgent != null) {
					connection.setRequestProperty("User-Agent", userAgent);
				}
				cis = connection.getInputStream();
				bis = new BufferedInputStream(cis);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(40000);
				byte[] buffer = new byte[1024];
				while (true) {
					int b = bis.read(buffer);
					if (b < 0) {
						break;
					}
					baos.write(buffer, 0, b);
				}
				byte[] bytes = baos.toByteArray();

				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				BufferedImage image = ImageIO.read(bais);
				return new BufferedImageAndBytes(image, bytes);
			} catch (MalformedURLException e) {
				continue;
			} catch (IOException e) {
				continue;
			} finally {
				try {
					if (cis != null) {
						cis.close();
					}
				} catch (IOException e) {
					System.out.println("unable to close InputStream: "
							+ e.getMessage());
				}
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (IOException e) {
					System.out.println("unable to close InputStream: "
							+ e.getMessage());
				}
			}
		}

		return null;
	}

}
