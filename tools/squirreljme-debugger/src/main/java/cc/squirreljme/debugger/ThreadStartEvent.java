// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPSuspendPolicy;

/**
 * Indicates that a thread has been started.
 *
 * @since 2024/01/28
 */
public class ThreadStartEvent
	extends DebuggerEvent
{
	/**
	 * Initializes the event.
	 *
	 * @param __thread The thread that was started.
	 * @param __suspend The suspend state of the thread.
	 * @since 2024/01/28
	 */
	public ThreadStartEvent(InfoThread __thread, JDWPSuspendPolicy __suspend)
	{
		super(__thread, __suspend);
	}
}
