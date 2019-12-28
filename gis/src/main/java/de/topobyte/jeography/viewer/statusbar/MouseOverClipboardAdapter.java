// Copyright 2019 Sebastian Kuerten
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

package de.topobyte.jeography.viewer.statusbar;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

public class MouseOverClipboardAdapter extends MouseAdapter
{

	private Component component;
	private StatusBarInfoReceiver receiver;
	private Supplier<String> supplier;

	public MouseOverClipboardAdapter(Component component,
			StatusBarInfoReceiver receiver, Supplier<String> supplier)
	{
		this.component = component;
		this.receiver = receiver;
		this.supplier = supplier;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (component.isEnabled()) {
			receiver.triggerStatusBarInfoAvailable(supplier.get());
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (component.isEnabled()) {
			receiver.triggerStatusBarNoInfo();
		}
	}

}
