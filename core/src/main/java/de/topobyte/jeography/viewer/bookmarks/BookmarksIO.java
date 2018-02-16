// Copyright 2017 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.bookmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import de.topobyte.melon.io.StreamUtil;

public class BookmarksIO
{

	public static List<Bookmark> read(Path path) throws IOException
	{
		InputStream input = StreamUtil.bufferedInputStream(path);
		List<Bookmark> result = read(input);
		input.close();
		return result;
	}

	public static List<Bookmark> read(InputStream is) throws IOException
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			throw new IOException(e);
		}

		BookmarksSaxHandler saxHandler = BookmarksSaxHandler.createInstance();
		try {
			parser.parse(is, saxHandler);
		} catch (SAXException e) {
			throw new IOException(e);
		}

		return saxHandler.getBookmarks();
	}

	public static void write(Path path, List<Bookmark> bookmarks)
			throws IOException
	{
		OutputStream output = StreamUtil.bufferedOutputStream(path);
		write(output, bookmarks);
		output.close();
	}

	public static void write(OutputStream os, List<Bookmark> bookmarks)
			throws IOException
	{
		OutputStreamWriter writer = new OutputStreamWriter(os);
		PrintWriter pw = new PrintWriter(writer);
		pw.println("<bookmarks>");
		for (Bookmark bookmark : bookmarks) {
			pw.println(String.format(
					"    <bookmark lat=\"%.6f\" lon=\"%.6f\" name=\"%s\"",
					bookmark.getCoordinate().y, bookmark.getCoordinate().x,
					bookmark.getName()));
		}
		pw.println("</bookmarks>");
		pw.close();
	}

}
