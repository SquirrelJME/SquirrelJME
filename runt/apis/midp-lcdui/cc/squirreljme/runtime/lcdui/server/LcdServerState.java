// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.task.SystemTask;

/**
 * This contains the state of the LCD server for each individual task which
 * is running.
 *
 * @since 2018/03/23
 */
public final class LcdServerState
{
	/** The task this is running for. */
	protected final SystemTask task;
	
	/**
	 * Initializes the state for the server.
	 *
	 * @param __task The task running this.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public LcdServerState(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
	}
}

