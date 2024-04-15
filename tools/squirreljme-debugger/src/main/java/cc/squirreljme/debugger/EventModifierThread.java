// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPEventModifierKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Not Described.
 *
 * @since 2024/01/26
 */
public class EventModifierThread
	implements EventModifier
{
	/** The thread to set. */
	protected final InfoThread thread;
	
	/**
	 * Initializes the thread modifier.
	 *
	 * @param __thread The thread to adjust.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public EventModifierThread(InfoThread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
	{
		// Single step in this thread
		__packet.writeByte(JDWPEventModifierKind.THREAD_ONLY.debuggerId());
		__packet.writeId(this.thread.id);
	}
}
