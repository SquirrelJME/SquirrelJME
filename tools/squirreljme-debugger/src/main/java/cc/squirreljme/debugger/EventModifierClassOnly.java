// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Only refer to the given class.
 *
 * @since 2024/07/27
 */
public class EventModifierClassOnly
	implements EventModifier
{
	/** The reference ID to catch. */
	protected final JDWPId id;
	
	/**
	 * Initializes the class only exception.
	 *
	 * @param __id The class identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/27
	 */
	public EventModifierClassOnly(JDWPId __id)
		throws NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/27
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
	{
		__packet.writeId(this.id);
	}
}
