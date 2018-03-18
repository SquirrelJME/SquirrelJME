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

/**
 * This is a displayable which utilizes Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplayable
	extends LcdDisplayable
{
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
	public void setTitle(String __t)
	{
		this._title = __t;
		
		// If this is bound to a display then update the title
	}
}

