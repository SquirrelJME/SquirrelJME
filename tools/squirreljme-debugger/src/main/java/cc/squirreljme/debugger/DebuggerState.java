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
import cc.squirreljme.jdwp.JDWPCommandSetEventRequest;
import cc.squirreljme.jdwp.JDWPCommandSetThreadReference;
import cc.squirreljme.jdwp.JDWPCommandSetVirtualMachine;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPEventModifierKind;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPIdSizeUnknownException;
import cc.squirreljme.jdwp.JDWPIdSizes;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;
import cc.squirreljme.jdwp.JDWPSuspend;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
	protected final JDWPCommLink commLink;
	
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
	
	/** The frame context. */
	protected final ContextThreadFrame context =
		new ContextThreadFrame();
	
	/** Handler for all replies. */
	private final Map<Integer, ReplyHandler> _replies =
		new LinkedHashMap<>();
	
	/** Handlers for events. */
	private final Map<Integer, EventHandler<?>> _eventHandlers =
		new LinkedHashMap<>();
	
	/** Deferred packets. */
	private final Deque<JDWPPacket> _defer =
		new LinkedList<>();
	
	/** Has the virtual machine been started? */
	private volatile boolean _hasStarted;
	
	/** Interpreter for byte code locations. */
	volatile FrameLocationInterpret _locationInterpret =
		FrameLocationInterpret.ADDRESS;
	
	/**
	 * Initializes the debugger state.
	 *
	 * @param __commLink The communication link.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public DebuggerState(JDWPCommLink __commLink)
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
	public void eventSet(JDWPEventKind __kind, JDWPSuspendPolicy __suspend,
		EventModifier[] __modifiers, EventHandler<?> __handler)
		throws NullPointerException
	{
		if (__kind == null || __suspend == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.EVENT_REQUEST,
			JDWPCommandSetEventRequest.SET))
		{
			// Fill in information
			out.writeByte(__kind.debuggerId());
			out.writeByte(__suspend.debuggerId());
			
			// No modifiers specified?
			if (__modifiers == null || __modifiers.length == 0)
				out.writeInt(0);
			
			// Write otherwise
			else
			{
				out.writeInt(__modifiers.length);
				for (EventModifier modifier : __modifiers)
					modifier.write(this, out);
			}
			
			// Send it, wait for the response for it
			if (__handler == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					int eventId = __reply.readInt();
					
					// Store handler for later
					Map<Integer, EventHandler<?>> handlers =
						this._eventHandlers;
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
		Consumer<InfoClass[]> __found, Consumer<Throwable> __notFound)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.CLASSES_BY_SIGNATURE))
		{
			// Request the class
			packet.writeString(__className.field().toString());
			
			// Send it off and wait for a response before doing something
			this.sendThenWait(packet, Utils.TIMEOUT,
				(__state, __reply) ->
				{
					// Timed out?
					if (__reply == null)
					{
						__notFound.accept(new Throwable("Timed out."));
						return;
					}
					
					// Was there an error?
					if (__reply.hasError())
					{
						__notFound.accept(new Throwable(
							__reply.error().toString()));
						return;
					}
					
					// Were there any resultant classes?
					int count = __reply.readInt();
					if (count == 0)
					{
						__notFound.accept(new Throwable("No classes found."));
						return;
					}
					
					// Get references for each class
					StoredInfo<InfoClass> classStorage =
						__state.storedInfo.getClasses();
					
					// Read all class IDs
					InfoClass[] foundClasses = new InfoClass[count];
					for (int i = 0; i < count; i++)
					{
						// Ignore tag
						__reply.readByte();
						
						// Get referenced class
						foundClasses[i] = classStorage.get(__state,
							__reply.readId(JDWPIdKind.REFERENCE_TYPE_ID));
						
						// Ignore status
						__reply.readInt();
					}
					
					// Call the handler with all the found classes
					__found.accept(foundClasses);
				});
		}
	}
	
	/**
	 * Reads the location information.
	 *
	 * @param __inThread The thread this is in.
	 * @param __packet The packet to read the location information from.
	 * @return The resultant location.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public FrameLocation readLocation(InfoThread __inThread,
		JDWPPacket __packet)
		throws NullPointerException
	{
		if (__inThread == null || __packet == null)
			throw new NullPointerException("NARG");
		
		// Ignore tag
		__packet.readByte();
		
		// Get the class and method we are in
		JDWPId classId = __packet.readId(JDWPIdKind.REFERENCE_TYPE_ID);
		JDWPId methodId = __packet.readId(JDWPIdKind.METHOD_ID);
		
		// Read location index
		long index = __packet.readLong();
		
		// Initialize location
		StoredInfoManager storedInfo = this.storedInfo;
		return new FrameLocation(
			__inThread,
			storedInfo.getClasses().get(this, classId),
			storedInfo.getMethods().get(this, methodId),
			index);
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
	public JDWPPacket reply(JDWPPacket __id, JDWPErrorType __error)
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
	public JDWPPacket reply(int __id, JDWPErrorType __error)
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
		JDWPCommLink link = this.commLink;
		TallyTracker receiveTally = this.receiveTally;
		Deque<JDWPPacket> defer = this._defer;
		
		// Perform default trackers
		this.__defaultInit();
		
		// Infinite read loop, read in packets accordingly
		boolean sizesKnown = false;
		for (;;)
		{
			// Debug
			if (JDWPCommLink.DEBUG)
				Debugging.debugNote("Polling JDWPPacket...");
			
			// Do we know sizes yet?
			if (!sizesKnown)
			{
				// Process any deferred packets if they are known
				sizesKnown = link.areSizesKnown();
				if (sizesKnown)
				{
					// Debug
					if (JDWPCommLink.DEBUG)
						Debugging.debugNote(
							"Processing deferred packets...");
					
					// These packets are from a time when sizes are not known,
					// so we need to place in all the sizes
					JDWPIdSizes sizes = link.idSizes();
					
					// Read in everything so we do not keep a lock
					JDWPPacket[] deferred;
					synchronized (this)
					{
						if (defer.isEmpty())
							deferred = null;
						else
							deferred = defer.toArray(
								new JDWPPacket[defer.size()]);
						
						// Clear it out
						defer.clear();
					}
					
					// Process all the packets
					if (deferred != null)
						for (JDWPPacket copy : deferred)
							try (JDWPPacket packet = copy)
							{
								// Set sizes
								packet.setIdSizes(sizes);
								
								// Process it
								this.__process(packet.resetReadPosition());
							}
				}
			}
			
			// Poll next packet
			try (JDWPPacket packet = link.poll())
			{
				// Debug
				if (JDWPCommLink.DEBUG)
				{
					Debugging.debugNote("DEBUGGER <- %s", packet);
					
					if (packet != null)
						try (HexDumpOutputStream dump = new HexDumpOutputStream(
							System.err))
						{
							dump.write(packet.toByteArray());
						}
						catch (IOException ignored)
						{
						}
				}
				
				// Only when interrupted or terminated does this stop
				if (packet == null)
					break;
				
				// Tally up!
				receiveTally.increment();
				
				// Perform processing on it
				this.__process(packet);
			}
			catch (JDWPException __e)
			{
				// Print error to the output
				__e.printStackTrace(System.err);
				
				// Stop if shutdown
				if (link.isShutdown())
					break;
			}
		}
		
		// Debug
		if (JDWPCommLink.DEBUG)
			Debugging.debugNote("JDWP Loop End...");
		
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
		
		// Debug
		if (JDWPCommLink.DEBUG)
			Debugging.debugNote("DEBUGGER -> %s (%x)",
				__packet, System.identityHashCode(__packet));
		
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
		
		// The response holder
		JDWPPacket[] reply = new JDWPPacket[1];
		
		// Send the packet, use a custom handler to set the value
		this.send(__packet, (__state, __back) -> {
			synchronized (reply)
			{
				// Set the packet, need a copy because it gets implicit closed
				reply[0] = __state.commLink.getPacket().copyOf(__back);
				
				// Signal
				reply.notifyAll();
			}
		});
		
		// Wait on the signal
		JDWPPacket given;
		synchronized (reply)
		{
			// Get the value
			given = reply[0];
			
			// If not found yet, wait on it
			if (given == null)
				try
				{
					reply.wait(__timeoutMs);
				}
				catch (InterruptedException ignored)
				{
				}
			
			// Read again
			given = reply[0];
		}
		
		// Call reply handler, it is always called even when null as that
		// means there was a timeout
		try
		{
			__reply.handlePacket(this, given);
		}
		
		// Need to close the packet since it was dangling open
		finally
		{
			if (given != null)
				given.close();
		}
	}
	
	/**
	 * Sends the request to the remote end and blocks until a reply is given
	 * to the packet.
	 *
	 * @param __packet The packet to send.
	 * @param __timeoutMs How long to wait until this times out.
	 * @param __successHandler The handler to call on success.
	 * @param __failHandler The handler to call on failure.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public void sendThenWait(JDWPPacket __packet, long __timeoutMs,
		ReplyHandler __successHandler, ReplyHandler __failHandler)
		throws NullPointerException
	{
		this.sendThenWait(__packet, __timeoutMs, (__state, __result) -> {
			if (__result == null || __result.hasError())
				__failHandler.handlePacket(__state, __result);
			else
				__successHandler.handlePacket(__state, __result);
		});
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
	 * @param __done The method to call when the action has been performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public void threadResume(InfoThread __thread, Runnable __done)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.RESUME))
		{
			// Write the ID of the thread
			out.writeId(__thread.id);
			
			// Send it
			if (__done == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					__done.run();
				});
		}
	}
	
	/**
	 * Resumes all threads.
	 *
	 * @param __done The method to call when the action has been performed.
	 * @since 2024/01/21
	 */
	public void threadResumeAll(Runnable __done)
	{
		// The all version uses the VM command set as there is no
		// base thread to use
		try (JDWPPacket out = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.RESUME))
		{
			// Send it
			if (__done == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					__done.run();
				});
		}
	}
	
	/**
	 * Steps in a thread.
	 *
	 * @param __thread The thread to step in.
	 * @param __count The steps to make.
	 * @param __depth The step depth.
	 * @param __size The step size.
	 * @param __handler The handler to use for the event update.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public void threadStep(InfoThread __thread, int __count,
		JDWPStepDepth __depth, JDWPStepSize __size,
		EventHandler<SingleStepEvent> __handler)
		throws NullPointerException
	{
		if (__thread == null || __depth == null || __size == null)
			throw new NullPointerException("NARG");
		
		// Modifiers to use
		EventModifier[] modifiers = {
				new EventModifierSingleStep(__thread,
					__depth, __size)
			};
		
		// Just normal event set
		for (int i = 0; i < __count; i++)
			this.eventSet(JDWPEventKind.SINGLE_STEP,
				JDWPSuspendPolicy.EVENT_THREAD,
				modifiers,
				__handler);
	}
	
	/**
	 * Suspends the specified thread.
	 *
	 * @param __thread The thread to suspend.
	 * @param __done The method to call when the action has been performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public void threadSuspend(InfoThread __thread, Runnable __done)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.SUSPEND))
		{
			// Write the ID of the thread
			out.writeId(__thread.id);
			
			// Send it
			if (__done == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					__done.run();
				});
		}
	}
	
	/**
	 * Suspends all threads.
	 *
	 * @param __done The method to call when the action has been performed.
	 * @since 2024/01/25
	 */
	public void threadSuspendAll(Runnable __done)
	{
		// The all version uses the VM command set as there is no
		// base thread to use
		try (JDWPPacket out = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.SUSPEND))
		{
			// Send it
			if (__done == null)
				this.send(out);
			else
				this.send(out, (__ignored, __reply) -> {
					__done.run();
				});
		}
	}
	
	/**
	 * Default initialization.
	 *
	 * @since 2024/01/20
	 */
	private void __defaultInit()
	{
		// We need to know the sizes of variable length entries, otherwise
		// we cannot read them at all
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.ID_SIZES))
		{
			this.send(packet,
				(__state, __reply) -> {
					Debugging.debugNote("Read ID Sizes...");
					
					// Read all of these in
					int[] sizes = new int[JDWPIdKind.NUM_KINDS];
					for (int i = 0; i < sizes.length; i++)
						sizes[i] = __reply.readInt();
					
					// Initialize sizes
					JDWPIdSizes result = new JDWPIdSizes(sizes);
					this.commLink.setIdSizes(result);
					
					// Debug
					Debugging.debugNote("Sizes read: %s",
						result);
				});
		}
		
		// Version information
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.VERSION))
		{
			this.send(packet, this::__remoteVmInfo);
		}
		
		// Get the capabilities of the remote VM, so we know what we can and
		// cannot do
		this.capabilities.update(this);
		
		// Thread events, with no particular handler
		this.eventSet(JDWPEventKind.THREAD_START, JDWPSuspendPolicy.NONE,
			null, (__state, __reply) -> {});
		this.eventSet(JDWPEventKind.THREAD_DEATH, JDWPSuspendPolicy.NONE,
			null, (__state, __reply) -> {});
	}
	
	/**
	 * Processes the given packet, or defers it.
	 *
	 * @param __packet The packet to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	private void __process(JDWPPacket __packet)
		throws NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		JDWPCommLink link = this.commLink;
		Deque<JDWPPacket> defer = this._defer;
				
		// Try to process the packet
		try
		{
			// Handle packet
			if (__packet.isReply())
				this.__processReply(__packet);
			else
				this.__processRequest(__packet);
		}
		
		// The sizes are not known, we still want this packet, but we
		// need to handle it at some point
		catch (JDWPIdSizeUnknownException ignored)
		{
			// Store for later
			synchronized (this)
			{
				JDWPPacket copy = link.getPacket().copyOf(__packet)
					.resetReadPosition();
				
				// Debug
				Debugging.debugNote("Deferring %s (%08x) -> (%08x)...",
					__packet, System.identityHashCode(__packet),
					System.identityHashCode(copy));
				
				defer.addLast(copy);
			}
		}
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
	
	/**
	 * Initializes the remote virtual machine info.
	 *
	 * @param __state The state used.
	 * @param __reply The reply packet.
	 * @since 2024/01/26
	 */
	private void __remoteVmInfo(DebuggerState __state, JDWPPacket __reply)
	{
		// Read all the info
		String desc = __reply.readString();
		int jdwpMajor = __reply.readInt();
		int jdwpMinor = __reply.readInt();
		String vmVersion = __reply.readString();
		String vmName = __reply.readString();
		
		// SquirrelJME uses indexes for byte code instructions rather than
		// their actual address
		if (desc.contains("SquirrelJME") || vmName.contains("SquirrelJME"))
			this._locationInterpret = FrameLocationInterpret.INDEX;
		
		// Note it
		Debugging.debugNote("Description: %s",
			desc);
		Debugging.debugNote("JDWP Major: %d",
			jdwpMajor);
		Debugging.debugNote("JDWP Minor: %d",
			jdwpMinor);
		Debugging.debugNote("VM Version: %s",
			vmVersion);
		Debugging.debugNote("VM Name: %s",
			vmName);
	}
}
