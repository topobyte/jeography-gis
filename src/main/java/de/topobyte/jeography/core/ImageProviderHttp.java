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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <T>
 *            the type of things that map to cached images.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageProviderHttp<T> extends
		ImageProvider<T, BufferedImageAndBytes>
{

	static final Logger logger = LoggerFactory.getLogger(ImageProvider.class);

	ImageSourceUrlPattern<T> imageSource;

	ImageProviderHttp(UrlResoluter<T> resolver, int nThreads, int nTries)
	{
		super(nThreads);
		imageSource = new ImageSourceUrlPattern<>(resolver, nTries);
	}

	/**
	 * Set the user-agent to use during HTTP-requests.
	 * 
	 * @param userAgent
	 *            the user agent to use.
	 */
	public void setUserAgent(String userAgent)
	{
		imageSource.setUserAgent(userAgent);
	}

	@Override
	public BufferedImageAndBytes load(T thing)
	{
		return imageSource.loadImage(thing);
	}

}
