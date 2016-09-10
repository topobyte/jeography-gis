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

package de.topobyte.jeography.viewer.geometry.manage;

/**
 * A listener interface for the GeometryRuleModel.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public interface GeometryRuleModelListener
{

	/**
	 * Triggered when a rule has been added to the model.
	 * 
	 * @param rule
	 *            the rule added.
	 */
	public void ruleAdded(GeometryRule rule);

	/**
	 * Triggered when a rule has been removed from the model.
	 * 
	 * @param rule
	 *            the rule removed.
	 */
	public void ruleRemoved(GeometryRule rule);

	/**
	 * Triggered when a rule has been changed somehow.
	 * 
	 * @param rule
	 *            the rule that changed.
	 */
	public void ruleChanged(GeometryRule rule);

	/**
	 * Triggered when rules have been reordered.
	 */
	public void rulesReordered();

	/**
	 * Triggered when rules have changed complexly.
	 */
	public void rulesChanged();

}
