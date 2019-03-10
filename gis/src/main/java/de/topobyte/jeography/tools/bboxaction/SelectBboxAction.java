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

package de.topobyte.jeography.tools.bboxaction;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.topobyte.adt.geo.BBox;
import de.topobyte.jeography.tools.bboxaction.bboxchooser.BboxChooser;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class SelectBboxAction extends AbstractAction implements Action
{

	private static final long serialVersionUID = 5673802417623778337L;

	private BBox bbox;
	private final Component component;

	public SelectBboxAction(BBox bbox, Component component)
	{
		this.bbox = bbox;
		this.component = component;
		putValue(Action.NAME, "select");
	}

	public void setBbox(BBox bbox)
	{
		this.bbox = bbox;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		BboxChooser bc = new BboxChooser(bbox);
		int ret = bc.showDialog(component);
		if (ret == BboxChooser.APPROVE_OPTION) {
			BBox selectedBbox = bc.getBbox();
			bboxSelected(selectedBbox);
		}
	}

	public abstract void bboxSelected(BBox bbox);

}
