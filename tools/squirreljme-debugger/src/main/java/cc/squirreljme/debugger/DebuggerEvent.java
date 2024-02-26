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
 * Debugger event.
 *
 * @since 2024/01/20
 */
public abstract class DebuggerEvent
{
	/** The suspend policy. */
	protected final JDWPSuspendPolicy suspend;
	
	/** The thread this event is in. */
	protected final InfoThread thread;
	
	/**
	 * Initializes the base event.
	 *
	 * @param __thread The thread this is in.
	 * @param __suspend The suspend policy.
	 * @since 2024/01/26
	 */
	public DebuggerEvent(InfoThread __thread, JDWPSuspendPolicy __suspend)
	{
		this.thread = __thread;
		this.suspend = __suspend;
	}
}
