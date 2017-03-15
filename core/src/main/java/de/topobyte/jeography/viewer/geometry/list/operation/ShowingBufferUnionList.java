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

package de.topobyte.jeography.viewer.geometry.list.operation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.topobyte.jeography.viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowingBufferUnionList extends ShowingOperationList
{

	private static final long serialVersionUID = 704986111160705317L;

	private UnionBufferEvaluator evaluator;
	private Controls controls;

	/**
	 * Create a new list.
	 * 
	 * @param viewer
	 *            the viewer to show results on.
	 */
	public ShowingBufferUnionList(Viewer viewer)
	{
		super(new UnionBufferEvaluator(), viewer);
		evaluator = (UnionBufferEvaluator) getEvaluator();

		controls = new Controls();
		setOperationControls(controls);

		update();
	}

	void update()
	{
		Object value = controls.spinner.getModel().getValue();
		double tolerance = (Double) value;
		evaluator.setBufferTolerance(tolerance);
		calculateResult();
	}

	private class Controls extends JPanel
	{

		private static final long serialVersionUID = -1398948947755157469L;

		JSpinner spinner;

		public Controls()
		{
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.1,
					0.0, null, 0.001);
			spinner = new JSpinner(spinnerNumberModel);

			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			add(spinner, c);

			spinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e)
				{
					ShowingBufferUnionList.this.update();
				}
			});
		}
	}

}
