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
 * Used to limit the number of times an event occurs.
 *
 * @since 2024/01/26
 */
public class EventModifierCount
	implements EventModifier
{
	/** The count. */
	protected final int count;
	
	/**
	 * Initializes the count.
	 *
	 * @param __count The count.
	 * @since 2024/01/26
	 */
	public EventModifierCount(int __count)
	{
		this.count = __count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
	{
		__packet.writeByte(
			JDWPEventModifierKind.LIMIT_OCCURRENCES.debuggerId());
		__packet.writeInt(this.count);
	}
}
