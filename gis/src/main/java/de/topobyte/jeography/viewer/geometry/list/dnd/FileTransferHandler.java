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

package de.topobyte.jeography.viewer.geometry.list.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.swing.util.dnd.DestinationTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class FileTransferHandler extends TransferHandler
		implements DestinationTransferHandler
{

	final static Logger logger = LoggerFactory
			.getLogger(FileTransferHandler.class);

	private static final long serialVersionUID = -1777333169478016689L;

	/**
	 * This method will get called for each list of files to handle.
	 * 
	 * @param handleFiles
	 *            the list of files to handle.
	 * @param ts
	 *            the TransferSupport this is about.
	 */
	public abstract void handleFiles(List<File> handleFiles,
			TransferSupport ts);

	@Override
	public boolean canImport(TransferSupport ts)
	{
		DataFlavor[] flavors = ts.getDataFlavors();
		boolean ok = false;
		for (int i = 0; i < flavors.length; i++) {
			final DataFlavor curFlavor = flavors[i];
			if (curFlavor
					.equals(java.awt.datatransfer.DataFlavor.javaFileListFlavor)
					|| curFlavor.isRepresentationClassReader()) {
				ok = true;
				break;
			}
		}
		return ok;
	}

	@Override
	public boolean importData(TransferSupport ts)
	{
		Transferable tr = ts.getTransferable();
		boolean handleable = false;
		List<File> handleFiles = new ArrayList<>();

		if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			try {
				List<?> flist = (List<?>) tr
						.getTransferData(DataFlavor.javaFileListFlavor);
				File[] files = flist.toArray(new File[0]);
				for (File f : files) {
					handleFiles.add(f);
				}
				handleable = true;
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isRepresentationClassReader()) {
					try {
						Reader reader = flavors[i].getReaderForText(tr);
						BufferedReader br = new BufferedReader(reader);
						handleFiles = createFileArray(br);
					} catch (UnsupportedFlavorException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					handleable = true;
					break;
				}
			}

		}

		if (handleable) {
			handleFiles(handleFiles, ts);
		}

		return handleable;
	}

	private static String ZERO_CHAR_STRING = "" + (char) 0;

	static List<File> createFileArray(BufferedReader bReader)
	{
		List<File> list = new java.util.ArrayList<>();
		try {
			java.lang.String line = null;
			while ((line = bReader.readLine()) != null) {
				try {
					// kde seems to append a 0 char to the end of the reader
					if (ZERO_CHAR_STRING.equals(line))
						continue;

					File file = new File(new java.net.URI(line));
					list.add(file);
				} catch (Exception ex) {
					logger.debug("Error with " + line + ": " + ex.getMessage());
				}
			}
		} catch (IOException ex) {
			logger.debug("FileDrop: IOException");
		}
		return list;
	}

}
