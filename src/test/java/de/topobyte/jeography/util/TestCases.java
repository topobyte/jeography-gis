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

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
class TestCases
{

	public static TestCase[] TESTS = {
			// A manually added test:
			new TestCase(-0.2884, 51.5113, 9, "euuwjm--"),

			// Automatically generated tests:
			new TestCase(83.148403, -46.703445, 18, "j9tJBdplP--"),
			new TestCase(-68.741795, -68.938810, 3, "IftX--"),
			new TestCase(-60.041376, -20.665947, 18, "NiZh5~CmB--"),
			new TestCase(173.815029, -85.845266, 3, "qpvW--"),
			new TestCase(-81.016572, -66.798513, 9, "JCh9Xz--"),
			new TestCase(73.862908, -89.750398, 18, "iiCRbHYiQ--"),
			new TestCase(167.215270, -71.191676, 15, "q2zt1da0--"),
			new TestCase(-77.074277, -87.313125, 16, "IIdbBhCs"),
			new TestCase(176.660203, -2.298088, 11, "v~LNRRB-"),
			new TestCase(173.140260, 12.606175, 7, "64tW_"),
			new TestCase(122.000526, 4.805898, 16, "4jzxE9A0"),
			new TestCase(174.482691, 8.302936, 8, "6uVSX4-"),
			new TestCase(172.286762, 40.024281, 13, "78jUWra"),
			new TestCase(-156.861505, -25.350237, 1, "E0V"),
			new TestCase(-178.385183, -77.130303, 9, "AQYYc_--"),
			new TestCase(113.737525, 19.162444, 11, "40W4AnG-"),
			new TestCase(169.756916, 65.834258, 10, "_9HOK2"),
			new TestCase(-163.820051, -33.951871, 8, "EN~FFm-"),
			new TestCase(127.615380, -88.258770, 3, "ooyf--"),
			new TestCase(-89.368035, 80.673253, 10, "dQRvWg"),
			new TestCase(-103.955265, 50.956648, 4, "WmgX"),
			new TestCase(96.487647, 21.331397, 14, "4XSTtkCc-"),
			new TestCase(127.364679, 51.487641, 2, "8smH-"),
			new TestCase(-66.132251, -3.813865, 16, "N0Trhb7m"),
			new TestCase(166.344811, 40.373106, 5, "72k0z-"),
			new TestCase(-76.625316, 28.215241, 13, "ZMKECPH"),
			new TestCase(-13.648323, 63.508944, 2, "e2wx-"),
			new TestCase(76.650141, 12.258706, 10, "yymUV5"),
			new TestCase(-52.881814, -81.492144, 2, "Itgp-"),
			new TestCase(-178.872340, -69.729384, 12, "AVDxsjD--"),
			new TestCase(169.902044, 88.041087, 15, "~9GnFtrC--"),
			new TestCase(78.890583, 25.027199, 16, "zoVDwoYq"),
			new TestCase(-68.703053, 62.690490, 12, "cesMYP7--"),
			new TestCase(-112.024568, -54.970202, 17, "DwFzKdWx2-"),
			new TestCase(-148.774372, 63.056073, 8, "U2lOdC-"),
			new TestCase(86.446794, 38.395987, 2, "z7Zr-"),
			new TestCase(150.773113, -40.102195, 11, "uJ5fPy3-"),
			new TestCase(97.284347, 8.359600, 2, "4Gdc-"),
			new TestCase(-163.983377, 26.410302, 8, "RJtoX8-"),
			new TestCase(-105.249246, -1.551069, 5, "H3NYb-"),
			new TestCase(-48.110132, -4.025356, 13, "N_awlrZ"),
			new TestCase(-102.254781, -40.340920, 17, "Gj5kghMj5-"),
			new TestCase(72.374018, 44.163515, 10, "z156fz"),
			new TestCase(-6.628714, 12.582649, 15, "a4t0kyUv--"),
			new TestCase(148.128610, -59.869964, 15, "rMZzZqqm--"),
			new TestCase(33.822330, -79.438429, 15, "gtUAsxnJ--"),
			new TestCase(-72.875425, -12.797194, 16, "NPFY8mz~"),
			new TestCase(-43.251074, 32.550485, 13, "bFYa7Tg"),
			new TestCase(61.833084, 43.275092, 16, "zdvqxXfs"),
			new TestCase(121.754037, -89.438677, 12, "oijScJl--"),
			new TestCase(-67.690895, 7.241293, 18, "YO6y8h~rn--"),
			new TestCase(-172.778471, -15.608185, 1, "FGN"),
			new TestCase(68.046305, 23.452070, 10, "zgE5Tm"),
			new TestCase(169.185938, -32.620316, 1, "u4F"),
			new TestCase(-66.638755, 45.063295, 8, "cgCDta-"),
			new TestCase(-32.149525, 76.920451, 17, "fNMcG3aLx-"),
			new TestCase(155.895528, -34.000420, 10, "uP3mmv"),
			new TestCase(-172.036445, 6.780955, 7, "QGPMk"),
			new TestCase(66.218879, -21.734994, 2, "nKsD-"),
			new TestCase(-161.596227, -43.812786, 5, "EKNHK-"),
			new TestCase(-102.889910, -29.928658, 12, "Gzm2dzp--"),
			new TestCase(-109.779775, -32.394792, 13, "GwP8_x5"),
			new TestCase(-84.762942, -56.039714, 6, "JQqa0--"),
			new TestCase(-152.274983, -66.443220, 18, "BgvKKlivK--"),
			new TestCase(-149.015316, 70.600265, 3, "VjgU--"),
			new TestCase(158.799896, 42.476091, 8, "71C84Y-"),
			new TestCase(-153.579398, -58.181571, 10, "Blmwgs"),
			new TestCase(134.811158, 77.501399, 13, "9v6n45a"),
			new TestCase(27.321314, 18.764543, 18, "w05s998Wh--"),
			new TestCase(-93.988018, -24.748075, 18, "G~JY2UAAr--"),
			new TestCase(-101.609838, 73.767913, 4, "Xmre"),
			new TestCase(90.056961, 26.980578, 8, "5BQR3T-"),
			new TestCase(-76.364353, -78.481370, 11, "IYKeCXD-"),
			new TestCase(152.108449, 62.915243, 12, "_eEdzG~--"),
			new TestCase(81.989210, -35.304476, 11, "mtlw5oj-"),
			new TestCase(-136.253616, -49.274718, 9, "B_tWz_--"),
			new TestCase(-146.638716, 37.912764, 4, "Rzve"),
			new TestCase(-140.288963, 15.149974, 11, "Q7FK51z-"),
			new TestCase(62.008157, -8.987800, 12, "naUHlU6--"),
			new TestCase(83.050317, 10.568640, 15, "yt8AvfbS--"),
			new TestCase(-105.780991, -63.084414, 2, "DjSw-"),
			new TestCase(-103.670304, -39.940920, 7, "Gj0lK"),
			new TestCase(-167.253611, -54.871604, 7, "BYNXE"),
			new TestCase(109.221707, -62.832856, 12, "pLbMjxo--"),
			new TestCase(-130.250557, -72.485741, 5, "CUp8Q-"),
			new TestCase(88.362263, -37.136162, 4, "mu2m"),
			new TestCase(-7.219372, 59.326816, 18, "e5i2oqVQq--"),
			new TestCase(36.683487, 67.352539, 7, "091Sb"),
			new TestCase(-45.222072, -89.704687, 14, "Iqq2b8IZ-"),
			new TestCase(171.430582, -62.652988, 13, "rpb8Ou~"),
			new TestCase(1.210265, 34.445971, 17, "xQD38TYZL-"),
			new TestCase(164.227467, 8.574573, 17, "6nClgd5Ij-"),
			new TestCase(-157.047856, -27.128703, 10, "E0E2TG"),
			new TestCase(109.099173, -38.584473, 18, "sOODdfkmv--"),
			new TestCase(-136.217036, 0.964141, 15, "QqsZXSpI--"),
			new TestCase(40.152852, -52.620487, 16, "h7GE45ed"),
			new TestCase(70.256260, -84.161619, 13, "ikK6c_U"),
			new TestCase(-148.928870, 60.437222, 5, "UzlXk-"),
			new TestCase(-113.230512, -37.168479, 11, "GO8ujxF-"),
			new TestCase(-41.932083, -34.127193, 6, "OF0dv--"), };

}
