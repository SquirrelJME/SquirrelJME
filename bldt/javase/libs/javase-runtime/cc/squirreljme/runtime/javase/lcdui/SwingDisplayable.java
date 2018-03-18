// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
import javax.swing.JPanel;

/**
 * This is a displayable which utilizes Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplayable
	extends LcdDisplayable
{
	/** The panel which makes up this displayable. */
	final JPanel _panel =
		new JPanel();
	
	/** The title to use. */
	private volatile String _title;
	
	/**
	 * Initializes the swing displayable.
	 *
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @since 2018/03/18
	 */
	public SwingDisplayable(int __handle, SystemTask __task,
		DisplayableType __type)
	{
		super(__handle, __task, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		JPanel panel = this._panel;
		panel.repaint(Math.max(0, __x), Math.max(0, __y),
			Math.min(panel.getWidth(), __w), Math.min(panel.getHeight(), __h));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final void setTitle(String __t)
	{
		this._title = __t;
		
		// If this is bound to a display then update the title
		SwingDisplay display = (SwingDisplay)this.getCurrent();
		if (display != null)
			display._frame.setTitle(__t);
	}
}

