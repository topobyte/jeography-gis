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

import org.buildobjects.process.ProcBuilder;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CreateTestCases
{

	public static void main(String[] args)
	{
		String commandPattern = "print ShortLink.encode(%f, %f, %d)";
		String outputPattern = "new TestCase(%f, %f, %d, \"%s\"),";

		Random random = new Random(0);
		for (int i = 0; i < 100; i++) {
			double lon = random.nextDouble() * 360 - 180;
			double lat = random.nextDouble() * 180 - 90;
			int zoom = random.nextInt(18) + 1;
			String command = String.format(commandPattern, lon, lat, zoom);
			String output = ProcBuilder.run("ruby", "-r", "/tmp/short_link.rb",
					"-e", command);
			System.out.println(
					String.format(outputPattern, lon, lat, zoom, output));
		}
	}

}
