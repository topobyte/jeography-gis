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

package de.topobyte.jeography.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.swing.util.BufferedImageIcon;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageLoader
{

	static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);

	/**
	 * Load an image from the given filename. The file-resource will be resolved
	 * by using the classloader. ImageIO will be tried first, then SVG via
	 * batik.
	 * 
	 * @param filename
	 *            the resource to load.
	 * @return the Icon loaded or null.
	 */
	public static Icon load(String filename)
	{
		if (filename == null) {
			return null;
		}

		// first try ImageIO
		BufferedImage bi = null;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(filename);
			bi = ImageIO.read(is);
			is.close();
		} catch (Exception e) {
			logger.debug("unable to load icon: '" + filename
					+ "' exception message: " + e.getMessage());
			e.printStackTrace();
		}
		if (bi != null) {
			return new BufferedImageIcon(bi);
		}

		// second, try reading an SVG image via batik
		try {
			PNGTranscoder t = new PNGTranscoder();
			TranscoderInput input = new TranscoderInput(Thread.currentThread()
					.getContextClassLoader().getResourceAsStream(filename));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(baos);
			t.transcode(input, output);
			baos.flush();
			baos.close();
			byte[] byteArray = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			bi = ImageIO.read(bais);
		} catch (Exception e) {
			logger.debug("unable to load svg icon: '" + filename
					+ "' exception message: " + e.getMessage());
		}
		if (bi != null) {
			return new BufferedImageIcon(bi);
		}

		// unable to load image
		return null;
	}

}
