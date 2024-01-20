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
import cc.squirreljme.jdwp.ErrorType;
import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;

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
	
	/** The current capabilities of the remote virtual machine. */
	protected final CapabilityStatus capabilities =
		new CapabilityStatus();
	
	/** Stored information manager. */
	protected final StoredInfoManager storedInfo =
		new StoredInfoManager();
	
	/** Handler for all replies. */
	private final Map<Integer, ReplyHandler> _replies =
		new SortedTreeMap<>();
	
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
	 * Creates a packet for a request.
	 *
	 * @param __commandSet The command set to use.
	 * @param __command The command to use.
	 * @return The newly created packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public JDWPPacket request(JDWPCommandSet __commandSet,
		JDWPCommand __command)
		throws NullPointerException
	{
		return this.commLink.request(__commandSet, __command);
	}
	
	/**
	 * Creates a packet for a request.
	 *
	 * @param __commandSet The command set to use.
	 * @param __command The command to use.
	 * @return The newly created packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public JDWPPacket request(int __commandSet, int __command)
	{
		return this.commLink.request(__commandSet, __command);
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
		
		// Update remote capabilities
		this.capabilities.update(this);
		
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
	 * Sends the given packet.
	 *
	 * @param __packet The packet to send.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public void send(JDWPPacket __packet)
		throws NullPointerException
	{
		this.send(__packet, null);
	}
	
	/**
	 * Sends the given packet with an optional reply handler for when a
	 * response is received.
	 *
	 * @param __packet The packet to send.
	 * @param __reply The reply handler to use for this packet.
	 * @throws IllegalArgumentException If a reply handler is specified and
	 * this is a reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public void send(JDWPPacket __packet, ReplyHandler __reply)
		throws IllegalArgumentException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// Wanting to handle a reply to this packet 
		if (__reply != null)
		{
			// It makes no sense to handle a reply to a reply
			if (__packet.isReply())
				throw new IllegalArgumentException(
					"Cannot handle a reply to a reply.");
			
			// Store handler for replies before we send as the pipe could be
			// really fast!
			Map<Integer, ReplyHandler> replies = this._replies;
			synchronized (this)
			{
				replies.put(__packet.id(), __reply);
			}
		}
		
		// Send over the link
		this.commLink.send(__packet);
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
		
		// See if there is a handler for this reply
		ReplyHandler handler;
		Map<Integer, ReplyHandler> replies = this._replies;
		synchronized (this)
		{
			// Only handle once!
			handler = replies.remove(__packet.id());
		}
		
		// If there is no handler, just ignore
		if (handler == null)
		{
			Debugging.debugNote("No handler for reply %d...",
				__packet.id());
			
			return;
		}
		
		// Call the handler accordingly
		handler.handlePacket(this, __packet);
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
		
		// If this is an event, handle it specifically
		if (__packet.commandSetId() == 64 && __packet.command() == 100)
		{
			// Handle this as an event
			EventProcessor.handle(this, __packet);
			
			// Do not do any more processing
			return;
		}
		
		Debugging.debugNote("Handle non-event?");
	}
}
