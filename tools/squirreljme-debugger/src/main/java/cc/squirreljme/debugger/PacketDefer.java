// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommLink;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Packet deferral handler.
 *
 * @since 2024/01/26
 */
public class PacketDefer
{
	/** Deferred packets. */
	private final Deque<JDWPPacket> _defer =
		new LinkedList<>();
	
	/**
	 * Defers the given packet.
	 *
	 * @param __link The used link.
	 * @param __packet The packet to defer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public void defer(JDWPCommLink __link, JDWPPacket __packet)
		throws NullPointerException
	{
		if (__link == null || __packet == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		JDWPPacket copy;
		Deque<JDWPPacket> defer = this._defer;
		synchronized (this)
		{
			// Make a copy of it for later
			copy = __link.getPacket().copyOf(__packet)
				.resetReadPosition();
			
			// Store it
			defer.addLast(copy);
		}
		
		// Debug
		if (JDWPCommLink.DEBUG)
			Debugging.debugNote("Deferring %s (%08x) -> (%08x)...",
				__packet, System.identityHashCode(__packet),
				System.identityHashCode(copy));
	}
	
	/**
	 * Removes all the deferred packets.
	 *
	 * @return The removed packets.
	 * @since 2024/01/26
	 */
	public JDWPPacket[] removeAll()
	{
		Deque<JDWPPacket> defer = this._defer;
		JDWPPacket[] result;
		synchronized (this)
		{
			if (defer.isEmpty())
				result = null;
			else
				result = defer.toArray(
					new JDWPPacket[defer.size()]);
			
			// Clear it out
			defer.clear();
		}
		
		// Return all the deferred packets
		return result;
	}
}
