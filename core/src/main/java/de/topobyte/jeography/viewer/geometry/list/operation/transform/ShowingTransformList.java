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

package de.topobyte.jeography.viewer.geometry.list.operation.transform;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingOperationList;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowingTransformList extends ShowingOperationList
{

	private static final long serialVersionUID = 704986111160705317L;

	TransformEvaluator evaluator;
	private Controls controls;

	/**
	 * Create a list for geometries that may be translated with control items.
	 * The result of the operation will be displayed.
	 * 
	 * @param viewer
	 *            the viewer to display the result on.
	 */
	public ShowingTransformList(Viewer viewer)
	{
		super(new TransformEvaluator(), viewer);
		evaluator = (TransformEvaluator) getEvaluator();

		controls = new Controls();
		setOperationControls(controls);

		update();
	}

	void update()
	{
		double tx = (Double) controls.spinnerTranslateX.getModel().getValue();
		double ty = (Double) controls.spinnerTranslateY.getModel().getValue();
		double scx = (Double) controls.spinnerScaleX.getModel().getValue();
		double scy = (Double) controls.spinnerScaleY.getModel().getValue();
		double shx = (Double) controls.spinnerShearX.getModel().getValue();
		double shy = (Double) controls.spinnerShearY.getModel().getValue();
		double theta = (Double) controls.spinnerRotate.getModel().getValue();
		evaluator.setTx(tx);
		evaluator.setTy(ty);
		evaluator.setScx(scx);
		evaluator.setScy(scy);
		evaluator.setShx(shx);
		evaluator.setShy(shy);
		evaluator.setTheta(theta);
		calculateResult();
	}

	private class Controls extends JPanel
	{

		private static final long serialVersionUID = 8396868872466746725L;

		JSpinner spinnerTranslateX;
		JSpinner spinnerTranslateY;

		JSpinner spinnerScaleX;
		JSpinner spinnerScaleY;

		JSpinner spinnerShearX;
		JSpinner spinnerShearY;

		JSpinner spinnerRotate;

		public Controls()
		{
			SpinnerNumberModel modelTranslateX = new SpinnerNumberModel(
					evaluator.getTx(), null, null, 0.0001);
			SpinnerNumberModel modelTranslateY = new SpinnerNumberModel(
					evaluator.getTy(), null, null, 0.0001);
			SpinnerNumberModel modelScaleX = new SpinnerNumberModel(
					evaluator.getScx(), null, null, 0.0001);
			SpinnerNumberModel modelScaleY = new SpinnerNumberModel(
					evaluator.getScy(), null, null, 0.0001);
			SpinnerNumberModel modelShearX = new SpinnerNumberModel(
					evaluator.getShx(), null, null, 0.0001);
			SpinnerNumberModel modelShearY = new SpinnerNumberModel(
					evaluator.getShy(), null, null, 0.0001);
			SpinnerNumberModel modelRotate = new SpinnerNumberModel(
					evaluator.getTheta(), null, null, 0.0001);

			spinnerTranslateX = new JSpinner(modelTranslateX);
			spinnerTranslateY = new JSpinner(modelTranslateY);

			spinnerScaleX = new JSpinner(modelScaleX);
			spinnerScaleY = new JSpinner(modelScaleY);

			spinnerShearX = new JSpinner(modelShearX);
			spinnerShearY = new JSpinner(modelShearY);

			spinnerRotate = new JSpinner(modelRotate);

			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(6);
			String pattern = format.toPattern();
			System.out.println(String.format("Pattern: %s", pattern));

			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.gridy = 0;
			add(spinnerTranslateX, c);
			add(spinnerTranslateY, c);
			c.gridy = 1;
			add(spinnerScaleX, c);
			add(spinnerScaleY, c);
			c.gridy = 2;
			add(spinnerShearX, c);
			add(spinnerShearY, c);
			c.gridy = 3;
			c.gridwidth = 2;
			add(spinnerRotate, c);

			JSpinner[] spinners = new JSpinner[] { spinnerTranslateX,
					spinnerTranslateY, spinnerScaleX, spinnerScaleY,
					spinnerShearX, spinnerShearY, spinnerRotate };

			for (JSpinner spinner : spinners) {
				spinner.setEditor(new JSpinner.NumberEditor(spinner, pattern));

				spinner.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e)
					{
						ShowingTransformList.this.update();
					}
				});
			}

		}
	}

}
