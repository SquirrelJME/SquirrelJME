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
import cc.squirreljme.runtime.lcdui.server.LcdCallbackManager;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayables;

/**
 * The manager for Swing displayables.
 *
 * @since 2018/03/18
 */
public class SwingDisplayables
	extends LcdDisplayables
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	protected LcdDisplayable internalCreateDisplayable(int __handle,
		SystemTask __task, DisplayableType __type, LcdCallbackManager __cb)
		throws NullPointerException
	{
		if (__task == null || __type == null)
			throw new NullPointerException("NARG");
		
		return new SwingDisplayable(__handle, __task, __type, __cb);
	}
}

