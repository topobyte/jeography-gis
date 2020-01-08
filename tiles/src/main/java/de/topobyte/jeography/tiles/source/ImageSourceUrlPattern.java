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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.tiles.BufferedImageAndBytes;
import de.topobyte.jeography.tiles.UrlProvider;

/**
 * @param <T>
 *            the type of objects.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageSourceUrlPattern<T>
		implements ImageSource<T, BufferedImageAndBytes>
{

	static final Logger logger = LoggerFactory
			.getLogger(ImageSourceUrlPattern.class);

	private UrlProvider<T> resolver;

	private String userAgent;

	private final int nTries;

	private boolean online = true;

	private CloseableHttpClient client;

	/**
	 * An ImageSource implementation based that works with UrlResoluter
	 * 
	 * @param resolver
	 *            the url generator
	 * @param nTries
	 *            the number of times to retry failing images before giving up
	 */
	public ImageSourceUrlPattern(UrlProvider<T> resolver, int nTries)
	{
		this.resolver = resolver;
		this.nTries = nTries;

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(10);
		cm.setDefaultMaxPerRoute(10);

		client = HttpClients.custom().setConnectionManager(cm).build();
	}

	/**
	 * Set the UrlResoluter used to resolve image URLs.
	 * 
	 * @param resolver
	 *            the resolver to use
	 */
	public void setPathResoluter(UrlProvider<T> resolver)
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

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	@Override
	public BufferedImageAndBytes load(T thing)
	{
		return loadImage(thing);
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
			if (!online) {
				break;
			}
			if (k > 0) {
				logger.debug("retry #" + k);
			}
			try {
				URL url = new URL(iurl);
				HttpGet get = new HttpGet(url.toURI());
				HttpClientContext context = HttpClientContext.create();

				if (userAgent != null) {
					get.addHeader("User-Agent", userAgent);
				}
				try (CloseableHttpResponse response = client.execute(get,
						context)) {
					if (response.getStatusLine()
							.getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity entity = response.getEntity();
						byte[] bytes = EntityUtils.toByteArray(entity);
						ByteArrayInputStream bais = new ByteArrayInputStream(
								bytes);
						BufferedImage image = ImageIO.read(bais);
						return new BufferedImageAndBytes(image, bytes);
					}
				}
			} catch (URISyntaxException | IOException e) {
				logger.warn("Exception: " + e.getMessage());
				continue;
			}
		}

		return null;
	}

}
