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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestParsers
{

	public static void main(String[] args)
	{
		testLonLat();
		testOsm();
		testOsm2();
	}

	private static void testLonLat()
	{
		PatternRecognizer recognizer = new PatternRecognizerLonLat();

		String[] strings = { "33.810398,-117.921046", };

		for (String string : strings) {
			Location location = recognizer.parse(string);
			System.out.println(location);
		}
	}

	private static void testOsm()
	{
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
			System.out.println(location);
		}
	}

	private static void testOsm2()
	{
		PatternRecognizer recognizer = new PatternRecognizerOsm2();

		String[] strings = {
				"http://www.openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16",
				"http://openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16",
				"https://www.openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16",
				"https://openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16",
				"www.openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16",
				"openstreetmap.org/?lat=33.81211&lon=-117.91914&zoom=16" };

		for (String string : strings) {
			Location location = recognizer.parse(string);
			System.out.println(location);
		}
	}
}
