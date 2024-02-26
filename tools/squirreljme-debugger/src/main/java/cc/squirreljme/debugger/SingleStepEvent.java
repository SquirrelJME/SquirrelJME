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
 * Single step event.
 *
 * @since 2024/01/26
 */
public class SingleStepEvent
	extends DebuggerEvent
{
	/** The location of the event. */
	public final FrameLocation location;
	
	/**
	 * Initializes the single step event.
	 *
	 * @param __thread The thread that was stepped.
	 * @param __suspend The suspend state of the thread.
	 * @param __location The location where the stepping occurred.
	 * @since 2024/01/26
	 */
	public SingleStepEvent(InfoThread __thread, JDWPSuspendPolicy __suspend,
		FrameLocation __location)
	{
		super(__thread, __suspend);
		
		this.location = __location;
	}
}
