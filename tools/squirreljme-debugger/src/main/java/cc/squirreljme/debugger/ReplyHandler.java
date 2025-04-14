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
 * Interface for handling replies to requests and otherwise.
 *
 * @since 2024/01/19
 */
public interface ReplyHandler
{
	/** Ignored reply, does not matter. */
	ReplyHandler IGNORED =
		(__ignored1, __ignored2) -> {};
	
	/**
	 * Handles the given packet.
	 *
	 * @param __debuggerState The debugger state.
	 * @param __packet The packet to handle the reply for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	void handlePacket(DebuggerState __debuggerState, JDWPPacket __packet)
		throws NullPointerException;
}
