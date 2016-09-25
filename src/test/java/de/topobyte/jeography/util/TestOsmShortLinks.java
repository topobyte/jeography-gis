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

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.topobyte.jeography.viewer.gotolocation.Location;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestOsmShortLinks
{

	@Test
	public void encode()
	{
		for (TestCase test : TestCases.TESTS) {
			String result = OsmShortLinks.encode(test.lon, test.lat, test.zoom);
			Assert.assertEquals(test.result, result);
		}
	}

	@Test
	public void decode()
	{
		Random random = new Random(0);
		for (int zoom = 1; zoom <= 18; zoom++) {
			double digits = Math.max(0, (zoom) / 3d);
			double delta = 5 * Math.pow(0.1, digits);

			for (int i = 0; i < 1000; i++) {
				double lon = random.nextDouble() * 360 - 180;
				double lat = random.nextDouble() * 180 - 90;
				String result = OsmShortLinks.encode(lon, lat, zoom);
				TestCase test = new TestCase(lon, lat, zoom, result);

				Location location = OsmShortLinks.decode(test.result);
				Assert.assertEquals(test.lon, location.getLon(), delta);
				Assert.assertEquals(test.lat, location.getLat(), delta);
				Assert.assertEquals(test.zoom, location.getZoom());
			}
		}
	}

}
