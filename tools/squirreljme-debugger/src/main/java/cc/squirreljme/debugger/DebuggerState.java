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
import cc.squirreljme.jdwp.CommandSetEventRequest;
import cc.squirreljme.jdwp.CommandSetThreadReference;
import cc.squirreljme.jdwp.CommandSetVirtualMachine;
import cc.squirreljme.jdwp.ErrorType;
import cc.squirreljme.jdwp.EventKind;
import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.SuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.multiphasicapps.classfile.ClassName;

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
	
	/** Sent tally. */
	protected final TallyTracker sentTally =
		new TallyTracker();
	
	/** The current capabilities of the remote virtual machine. */
	protected final CapabilityStatus capabilities =
		new CapabilityStatus();
	
	/** Stored information manager. */
	protected final StoredInfoManager storedInfo =
		new StoredInfoManager();
	
	/** Handler for all replies. */
	private final Map<Integer, ReplyHandler> _replies =
		new LinkedHashMap<>();
	
	/** Handlers for events. */
	private final Map<Integer, EventHandler> _eventHandlers =
		new LinkedHashMap<>();
	
	/** Has the virtual machine been started? */
	private volatile boolean _hasStarted;
	
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
	 * Returns the event handler for the given request.
	 *
	 * @param __requestId The request to get.
	 * @return The handler for the given event, assuming it exists.
	 * @since 2024/01/20
	 */
	public EventHandler eventHandler(int __requestId)
	{
		synchronized (this)
		{
			return this._eventHandlers.get(__requestId);
		}
	}
	
	/**
	 * Requests that an event be listened for.
	 *
	 * @param __kind The kind of event to request.
	 * @param __suspend The suspension policy to use for the given event.
	 * @param __handler The handler for the event, this is optional.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public void eventSet(EventKind __kind, SuspendPolicy __suspend,
		EventHandler __handler)
		throws NullPointerException
	{
		if (__kind == null || __suspend == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.EVENT_REQUEST,
			CommandSetEventRequest.SET))
		{
			// Fill in information
			out.writeByte(__kind.debuggerId());
			out.writeByte(__suspend.debuggerId());
			
			// Currently just no modifiers
			out.writeInt(0);
			
			// Send it, wait for the response for it
			if (__handler == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					int eventId = __reply.readInt();
					
					// Store handler for later
					Map<Integer, EventHandler> handlers = this._eventHandlers;
					synchronized (this)
					{
						handlers.put(eventId, __handler);
					}
				});
		}
	}
	
	/**
	 * Has this virtual machine been started?
	 *
	 * @return If it has been started or not.
	 * @since 2024/01/21
	 */
	public boolean hasStarted()
	{
		synchronized (this)
		{
			return this._hasStarted;
		}
	}
	
	/**
	 * Looks up the given class.
	 *
	 * @param __className The name of the class to lookup.
	 * @param __found The action to perform when the class is found.
	 * @param __notFound The action to perform when the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public void lookupClass(ClassName __className,
		Consumer<InfoClass> __found, Consumer<Throwable> __notFound)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			CommandSetVirtualMachine.CLASSES_BY_SIGNATURE))
		{
			this.sendThenWait(packet, 3000, (__ignored, __reply) ->
				{
					if (__reply.hasError())
					{
						__notFound.accept(new Throwable(
							__reply.error().toString()));
						return;
					}
					
					throw Debugging.todo();
				});
		}
	}
	
	/**
	 * Creates a reply packet.
	 *
	 * @param __id The packet ID that is being responded to.
	 * @param __error The error to use for the packet.
	 * @return The resultant reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public JDWPPacket reply(JDWPPacket __id, ErrorType __error)
		throws NullPointerException
	{
		if (__id == null || __error == null)
			throw new NullPointerException("NARG");
		
		return this.reply(__id.id(), __error);
	}
	
	/**
	 * Creates a reply packet.
	 *
	 * @param __id The packet ID that is being responded to.
	 * @param __error The error to use for the packet.
	 * @return The resultant reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public JDWPPacket reply(int __id, ErrorType __error)
		throws NullPointerException
	{
		if (__error == null)
			throw new NullPointerException("NARG");
		
		return this.commLink.reply(__id, __error);
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
		
		// Perform default trackers
		this.__defaultInit();
		
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
		
		// Tally up
		this.sentTally.increment();
	}
	
	/**
	 * Sends the request to the remote end and blocks until a reply is given
	 * to the packet.
	 *
	 * @param __packet The packet to send.
	 * @param __timeoutMs How long to wait until this times out.
	 * @param __reply The method to call when this is handled.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public void sendThenWait(JDWPPacket __packet, long __timeoutMs,
		ReplyHandler __reply)
		throws NullPointerException
	{
		if (__packet == null || __reply == null)
			throw new NullPointerException("NARG");
		if (__timeoutMs <= 0)
			throw new IllegalArgumentException("NEGV");
		
		throw Debugging.todo();
	}
	
	/**
	 * Sets that the virtual machine has started.
	 *
	 * @since 2024/01/21
	 */
	public void setStarted()
	{
		synchronized (this)
		{
			this._hasStarted = true;
		}
	}
	
	/**
	 * Resumes a single thread.
	 *
	 * @param __thread The thread to resume.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public void threadResume(InfoThread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.THREAD_REFERENCE,
			CommandSetThreadReference.RESUME))
		{
			// Write the ID of the thread
			out.writeId(__thread.id);
			
			// Send it
			this.send(out);
		}
	}
	
	/**
	 * Resumes all threads.
	 *
	 * @since 2024/01/21
	 */
	public void threadResumeAll()
	{
		// The all version uses the VM command set as there is no
		// base thread to use
		try (JDWPPacket out = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			CommandSetVirtualMachine.RESUME))
		{
			// Send it
			this.send(out);
		}
	}
	
	/**
	 * Default initialization.
	 *
	 * @since 2024/01/20
	 */
	private void __defaultInit()
	{
		// Version information
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			CommandSetVirtualMachine.VERSION))
		{
			this.send(packet, (__state, __reply) -> {
				Debugging.debugNote("Description: %s",
					__reply.readString());
				Debugging.debugNote("JDWP Major: %d",
					__reply.readInt());
				Debugging.debugNote("JDWP Minor: %d",
					__reply.readInt());
				Debugging.debugNote("VM Version: %s",
					__reply.readString());
				Debugging.debugNote("VM Name: %s",
					__reply.readString());
			});
		}
		
		// Get the capabilities of the remote VM, so we know what we can and
		// cannot do
		this.capabilities.update(this);
		
		// Thread events, with no particular handler
		this.eventSet(EventKind.THREAD_START, SuspendPolicy.NONE,
			(__state, __reply) -> {});
		this.eventSet(EventKind.THREAD_DEATH, SuspendPolicy.NONE,
			(__state, __reply) -> {});
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
			// Debug
			Debugging.debugNote("Process event: %s", __packet);
			
			// Handle this as an event
			EventProcessor.handle(this, __packet);
			
			// Do not do any more processing
			return;
		}
		
		Debugging.debugNote("Handle non-event?");
	}
}
