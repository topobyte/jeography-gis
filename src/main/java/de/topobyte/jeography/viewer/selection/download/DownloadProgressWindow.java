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

package de.topobyte.jeography.viewer.selection.download;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.config.TileConfigUrlDisk;
import de.topobyte.jeography.viewer.geometry.manage.EventJDialog;
import de.topobyte.jeography.viewer.selection.rectangular.GeographicSelection;
import de.topobyte.swing.util.ButtonPane;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class DownloadProgressWindow extends EventJDialog implements
		DownloadProgressListener
{

	private static final long serialVersionUID = -830093656649774630L;

	private final TileDownloader tileDownloader;
	private JProgressBar progressBar;

	private JLabel labelTotalN;

	private JLabel labelCompleteN;

	private JLabel labelFailedN;

	/**
	 * Test the dialog
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		Configuration configuration = Configuration
				.createDefaultConfiguration();
		GeographicSelection selection = new GeographicSelection(12.969360,
				13.840027, 52.703019, 52.305120);
		List<Integer> levels = new ArrayList<>();
		levels.add(1);
		levels.add(2);
		levels.add(3);
		levels.add(4);
		levels.add(5);
		levels.add(6);
		levels.add(7);
		levels.add(8);

		List<TileConfig> tileConfigs = configuration.getTileConfigs();
		List<TileConfigUrlDisk> httpConfigs = new ArrayList<>();
		for (TileConfig config : tileConfigs) {
			if (config instanceof TileConfigUrlDisk) {
				httpConfigs.add((TileConfigUrlDisk) config);
			}
		}

		TileDownloader downloader = new TileDownloader(httpConfigs.get(0),
				null, selection.toBoundingBox(), levels);

		DownloadProgressWindow dialog = new DownloadProgressWindow(null,
				downloader);

		dialog.setSize(500, 400);
		dialog.pack();
		dialog.setVisible(true);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event)
			{
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent event)
			{
				System.exit(0);
			}
		});
	}

	/**
	 * Create a new instance.
	 * 
	 * @param parent
	 *            the parent frame for this dialog
	 * 
	 * @param tileDownloader
	 *            the tileDownloader to monitor.
	 */
	public DownloadProgressWindow(JFrame parent,
			final TileDownloader tileDownloader)
	{
		super(parent, "download progress");
		this.tileDownloader = tileDownloader;

		tileDownloader.addProgressListener(this);

		JPanel panel = new JPanel(new GridBagLayout());
		setContentPane(panel);

		GridBagConstraints c = new GridBagConstraints();

		JLabel labelTotal = new JLabel("tiles total: ");
		JLabel labelComplete = new JLabel("tiles completed:");
		JLabel labelFailed = new JLabel("tiles failed:");
		labelTotalN = new JLabel(""
				+ tileDownloader.getNumberOfTilesToDownload());
		labelCompleteN = new JLabel("0");
		labelFailedN = new JLabel("0");

		progressBar = new JProgressBar(0,
				tileDownloader.getNumberOfTilesToDownload());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JButton buttonPauseResume = new JButton("pause");
		JButton buttonCancel = new JButton("cancel");

		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy = 0;
		panel.add(labelTotal, c);
		c.gridy = 1;
		panel.add(labelComplete, c);
		c.gridy = 2;
		panel.add(labelFailed, c);

		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridy = 0;
		panel.add(labelTotalN, c);
		c.gridy = 1;
		panel.add(labelCompleteN, c);
		c.gridy = 2;
		panel.add(labelFailedN, c);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridx = 0;
		c.weightx = 1.0;
		panel.add(progressBar, c);

		List<JButton> buttons = new ArrayList<>();
		buttons.add(buttonPauseResume);
		buttons.add(buttonCancel);
		ButtonPane buttonPane = new ButtonPane(buttons);

		c.gridy = 4;
		c.gridwidth = 2;
		c.gridx = 0;
		panel.add(buttonPane, c);

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				tileDownloader.cancel();
			}
		});

		buttonPauseResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				tileDownloader.pauseResume();
			}
		});
	}

	@Override
	public void progress()
	{
		int n = tileDownloader.getTilesCompleted()
				+ tileDownloader.getTilesGivenUp();
		progressBar.setValue(n);

		labelTotalN.setText("" + tileDownloader.getNumberOfTilesToDownload());
		labelCompleteN.setText("" + tileDownloader.getTilesCompleted());
		labelFailedN.setText("" + tileDownloader.getTilesGivenUp());
	}

}
