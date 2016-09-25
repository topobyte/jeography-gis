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

package de.topobyte.jeography.viewer.gotolocation;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestParsers
{

	private Location noZoom = new Location(-117.921046, 33.810398);
	private Location withZoom = new Location(-117.921046, 33.810398, 15);

	private void assertEquals(Location expected, Location actual, double delta)
	{
		Assert.assertEquals(expected.hasZoom(), actual.hasZoom());
		if (actual.hasZoom()) {
			Assert.assertEquals(expected.getZoom(), actual.getZoom());
		}
		Assert.assertEquals(expected.getLon(), actual.getLon(), delta);
		Assert.assertEquals(expected.getLat(), actual.getLat(), delta);
	}

	@Test
	public void testLonLat()
	{
		double delta = 0.000001;
		PatternRecognizer recognizer = new PatternRecognizerLonLat();

		String[] strings = { "33.810398,-117.921046", };

		for (String string : strings) {
			Location location = recognizer.parse(string);
			assertEquals(noZoom, location, delta);
		}
	}

	@Test
	public void testOsm()
	{
		double delta = 0.000001;
		PatternRecognizer recognizer = new PatternRecognizerOsm();

		String[] strings = {
				"http://www.openstreetmap.org/#map=15/33.810398/-117.921046",
				"http://openstreetmap.org/#map=15/33.810398/-117.921046",
				"https://www.openstreetmap.org/#map=15/33.810398/-117.921046",
				"https://openstreetmap.org/#map=15/33.810398/-117.921046",
				"www.openstreetmap.org/#map=15/33.810398/-117.921046",
				"openstreetmap.org/#map=15/33.810398/-117.921046" };

		for (String string : strings) {
			Location location = recognizer.parse(string);
			assertEquals(withZoom, location, delta);
		}
	}

	@Test
	public void testOsm2()
	{
		double delta = 0.000001;
		PatternRecognizer recognizer = new PatternRecognizerOsm2();

		String[] strings = {
				"http://www.openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"http://openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"https://www.openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"https://openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"www.openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15",
				"openstreetmap.org/?lat=33.810398&lon=-117.921046&zoom=15" };

		for (String string : strings) {
			Location location = recognizer.parse(string);
			assertEquals(withZoom, location, delta);
		}
	}

}
