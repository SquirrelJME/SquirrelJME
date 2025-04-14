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

/**
 * Modifier for events.
 *
 * @since 2024/01/26
 */
public interface EventModifier
{
	/**
	 * Writes the modifier to the output.
	 *
	 * @param __debuggerState The debugger state.
	 * @param __packet The packet to write to.
	 * @since 2024/01/26
	 */
	void write(DebuggerState __debuggerState, JDWPPacket __packet);
}
