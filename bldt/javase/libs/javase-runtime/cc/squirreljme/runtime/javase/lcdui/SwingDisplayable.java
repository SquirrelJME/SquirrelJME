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
 * This is a displayable which utilizes the swing interface to display things.
 *
 * @since 2018/03/17
 */
public class SwingDisplayable
	extends LcdDisplayable
{
	/**
	 * Initializes the base displayable.
	 *
	 * @param __lock The locking object used.
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @since 2018/03/17
	 */
	public SwingDisplayable(Object __lock, int __handle, SystemTask __task,
		DisplayableType __type)
	{
		super(__lock, __handle, __task, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	protected void internalSetTitle(String __t)
	{
		// The title is only set if this is currently being displayed
		synchronized (this.lock)
		{
		}
	}
}

