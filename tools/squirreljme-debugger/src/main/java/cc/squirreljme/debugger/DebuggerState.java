// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.CommLink;
import cc.squirreljme.jdwp.EventKind;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.SuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Stores the debugger state.
 *
 * @since 2024/01/19
 */
public class DebuggerState
	implements Runnable
{
	/** The communication link used. */
	protected final CommLink commLink;
	
	/** Disconnected tally. */
	protected final TallyTracker disconnectedTally =
		new TallyTracker();
	
	/** Received packets. */
	protected final TallyTracker receiveTally =
		new TallyTracker();
	
	/**
	 * Initializes the debugger state.
	 *
	 * @param __commLink The communication link.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public DebuggerState(CommLink __commLink)
		throws NullPointerException
	{
		if (__commLink == null)
			throw new NullPointerException("NARG");
		
		this.commLink = __commLink;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void run()
	{
		CommLink link = this.commLink;
		TallyTracker receiveTally = this.receiveTally;
		
		// Infinite read loop, read in packets accordingly
		for (;;)
			try (JDWPPacket packet = link.poll())
			{
				// Only when interrupted or terminated does this stop
				if (packet == null)
					break;
				
				// Tally up!
				receiveTally.increment();
				
				// Debug
				Debugging.debugNote("Read: %s", packet);
				
				// Handle packet
				if (packet.isReply())
					this.__processReply(packet);
				else
					this.__processRequest(packet);
			}
			catch (JDWPException __e)
			{
				__e.printStackTrace(System.err);
				
				// Stop if shutdown
				if (link.isShutdown())
					break;
			}
		
		// Disconnected so indicate that
		this.disconnectedTally.increment();
	}
	
	/**
	 * Processes the given request packet.
	 *
	 * @param __packet The packet to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	private void __processReply(JDWPPacket __packet)
		throws NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
	}
	
	/**
	 * Processes the given request packet.
	 *
	 * @param __packet The packet to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	private void __processRequest(JDWPPacket __packet)
		throws NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// If this is not the composite event kind, then just ignore
		if (__packet.commandSetId() != 64 && __packet.command() != 100)
			return;
		
		// If the packet is blank, ignore it
		if (__packet.length() == 0)
			return;
		
		// Read the suspension policy
		SuspendPolicy suspend = SuspendPolicy.of(__packet.readByte());
		
		// Process all events
		int numEvents = __packet.readInt();
		for (int seq = 0; seq < numEvents; seq++)
		{
			// Is this event known?
			int rawKind = __packet.readByte();
			EventKind kind = EventKind.of(rawKind);
			if (kind == null)
			{
				Debugging.debugNote("Unknown event kind: %d", rawKind);
				return;
			}
			
			// Depends on the event kind
			switch (kind)
			{
					// Unhandled currently
				default:
					Debugging.debugNote("Unhandled event kind: %d (%s)",
						rawKind, kind);
					return;
			}
		}
	}
}
