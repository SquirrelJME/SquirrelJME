// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Class based match for events.
 *
 * @since 2024/01/31
 */
public class EventModifierClassMatch
	implements EventModifier
{
	/** The class pattern. */
	protected final String pattern;
	
	/**
	 * Initializes the class match pattern.
	 *
	 * @param __pattern The pattern to match against.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/31
	 */
	public EventModifierClassMatch(String __pattern)
		throws NullPointerException
	{
		if (__pattern == null)
			throw new NullPointerException("NARG");
		
		this.pattern = __pattern;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/31
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
	{
		__packet.writeString(this.pattern);
	}
}
