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

package de.topobyte.jeography.places.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.places.Dao;
import de.topobyte.jeography.places.model.Place;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.swing.util.DocumentAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SearchUI extends JPanel
		implements ActionListener, ListSelectionListener
{

	private static final long serialVersionUID = 1623220323023117627L;

	final static Logger logger = LoggerFactory.getLogger(SearchUI.class);

	ActivatableJList<Place> listResults;
	PlaceResultListModel resultModel;

	private JPanel statusBar;
	private JLabel statusLabel;

	private SearchWorker worker;

	private String query = null;

	public SearchUI(IConnection connection) throws QueryException
	{
		final JTextField input = new JTextField("");

		resultModel = new PlaceResultListModel();

		JScrollPane jspPlaces = new JScrollPane();
		listResults = new ActivatableJList<>(resultModel);
		jspPlaces.setViewportView(listResults);

		jspPlaces.setPreferredSize(new Dimension(500, 400));

		listResults.addActionListener(this);

		listResults.setCellRenderer(new PlaceCellRenderer());

		input.getDocument().addDocumentListener(new DocumentAdapter() {

			@Override
			public void update(DocumentEvent e)
			{
				try {
					SearchUI.this.update(input.getText());
				} catch (QueryException ex) {
					ex.printStackTrace();
				}
			}

		});

		listResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		createStatusBar();

		// Layout

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(input, c);
		c.weighty = 1.0;
		add(jspPlaces, c);
		c.weighty = 0.0;
		add(statusBar, c);

		Dao dao = new Dao(connection);
		worker = new SearchWorker(this, dao);
		Thread thread = new Thread(worker);
		thread.start();

		update(input.getText());
	}

	private synchronized void setQuery(String query)
	{
		this.query = query;
	}

	synchronized String getQuery()
	{
		return query;
	}

	protected void update(String text) throws QueryException
	{
		setQuery(text);
		worker.kickOff();
	}

	protected void updateWithResults(String text, List<Place> results)
	{
		resultModel.update(results);
		String patternNumResults = resultModel.hasMaxResults() ? ">= %d" : "%d";
		String numResults = String.format(patternNumResults,
				resultModel.getSize());
		String statusText = String.format("query '%s', results: %s", text,
				numResults);
		statusLabel.setText(statusText);
	}

	private void createStatusBar()
	{
		statusBar = new JPanel(new GridBagLayout());
		statusLabel = new JLabel();

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		statusBar.add(statusLabel, c);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == listResults) {
			int sel = listResults.getSelectedIndex();
			Object object = resultModel.getObject(sel);

			Place place = (Place) object;
			firePlaceActivation(place);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting()) {
			return;
		}
	}

	private List<PlaceActivationListener> placeListeners = new ArrayList<>();

	/**
	 * Add a listener to be informed on activation of a place.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addPlaceActivationListener(PlaceActivationListener listener)
	{
		placeListeners.add(listener);
	}

	/**
	 * Remove a listener from the list of listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removePlaceActivationListener(PlaceActivationListener listener)
	{
		placeListeners.remove(listener);
	}

	private void firePlaceActivation(Place place)
	{
		for (PlaceActivationListener listener : placeListeners) {
			listener.placeActivated(place);
		}
	}

}