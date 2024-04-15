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
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPIdSizeUnknownException;
import cc.squirreljme.jdwp.JDWPIdSizes;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
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
	
	/** Waiting packets. */
	protected final TallyTracker waitingTally =
		new TallyTracker();
	
	/** Latency. */
	protected final TallyTracker latency =
		new TallyTracker();
	
	/**
	 * Tally for tracking when the VM is dead.
	 * 
	 * @since 2024/01/27
	 */
	protected final TallyTracker vmDeadTally =
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
	
	/** Handlers for events. */
	protected final EventHandlers eventHandlers =
		new EventHandlers();
	
	/** Deferred packets. */
	protected final PacketDefer defer =
		new PacketDefer();
	
	/** Awaiting replies. */
	protected final AwaitingReplies replies =
		new AwaitingReplies();
	
	/** Called when the virtual machine is ready to be debugger. */
	protected final Consumer<DebuggerState> ready;
	
	/** Preferences for the debugger. */
	protected final Preferences preferences;
	
	/** Has the virtual machine been started? */
	private volatile boolean _hasStarted;
	
	/** Interpreter for byte code locations. */
	volatile FrameLocationInterpret _locationInterpret =
		FrameLocationInterpret.ADDRESS;
	
	/**
	 * Initializes the debugger state.
	 *
	 * @param __commLink The communication link.
	 * @param __preferences Debugger preferences.
	 * @param __ready The callback to execute when the debugger sequence is
	 * ready.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public DebuggerState(JDWPCommLink __commLink, Preferences __preferences,
		Consumer<DebuggerState> __ready)
		throws NullPointerException
	{
		if (__commLink == null || __preferences == null)
			throw new NullPointerException("NARG");
		
		this.commLink = __commLink;
		this.preferences = __preferences;
		this.ready = __ready;
	}
	
	/**
	 * Queries and returns all the virtual machine threads.
	 *
	 * @param __callback The callback to execute when the threads are obtained.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public void allThreads(Consumer<InfoThread[]> __callback)
		throws NullPointerException
	{
		if (__callback == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.ALL_THREADS))
		{
			// Send it out
			this.send(out, (__state, __reply) -> {
					int numThreads = __reply.readInt();
					
					StoredInfo<InfoThread> stored =
						__state.storedInfo.getThreads();
					
					// Read in all threads
					InfoThread[] threads = new InfoThread[numThreads];
					for (int i = 0; i < numThreads; i++)
					{
						JDWPId thread = __reply.readId(JDWPIdKind.THREAD_ID);
						threads[i] = stored.get(__state, thread);
					}
					
					// Sort all the threads if we can
					Arrays.sort(threads);
					
					// Send to the callback all the updated threads
					__callback.accept(threads);
				}, ReplyHandler.IGNORED);
		}
	}
	
	/**
	 * Requests that an event be listened for.
	 *
	 * @param __kind The kind of event to request.
	 * @param __suspend The suspension policy to use for the given event.
	 * @param __handler The handler for the event, this is optional.
	 * @param __postResponse Handler to call when the event ID is known.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public void eventSet(JDWPEventKind __kind, JDWPSuspendPolicy __suspend,
		EventModifier[] __modifiers, EventHandler<?> __handler,
		IntConsumer __postResponse)
		throws NullPointerException
	{
		if (__kind == null || __suspend == null || __handler == null)
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
			this.send(out, (__ignored, __reply) -> {
				int eventId = __reply.readInt();
				
				// Debug
				if (JDWPCommLink.DEBUG)
					Debugging.debugNote("Awaiting Event %d -> %s",
						eventId, __handler);
				
				// Store handler for later
				this.eventHandlers.put(eventId, __handler);
				
				// Inform of event?
				if (__postResponse != null)
					__postResponse.accept(eventId);
			}, ReplyHandler.IGNORED);
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
			this.send(packet,
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
				}, ReplyHandler.IGNORED);
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
		
		StoredInfoManager storedInfo = this.storedInfo;
		InfoClass inClass = storedInfo.getClasses().get(this, classId);
		
		// Initialize location
		return new FrameLocation(
			__inThread,
			inClass,
			inClass.getMethod(methodId),
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
		PacketDefer defer = this.defer;
		
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
					JDWPPacket[] deferred = defer.removeAll();
					
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
					
					// Call the ready handler if one is set
					if (this.ready != null)
						this.ready.accept(this);
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
						try (HexDumpOutputStream dump =
								 new HexDumpOutputStream(System.err))
						{
							dump.write(packet.toByteArray());
						}
						catch (IOException ignored)
						{
						}
				}
				
				// Only when interrupted or terminated does this stop
				if (packet == null)
				{
					if (JDWPCommLink.DEBUG)
						Debugging.debugNote(
							"JDWP Interrupted/Terminated.");
					
					break;
				}
				
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
		this.send(__packet, null, null);
	}
	
	/**
	 * Sends the given packet with an optional reply handler for when a
	 * response is received.
	 *
	 * @param __packet The packet to send.
	 * @param __pass The reply handler to use for this packet.
	 * @param __fail Called when the packet has failed.
	 * @throws IllegalArgumentException If a reply handler is specified and
	 * this is a reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public void send(JDWPPacket __packet, ReplyHandler __pass,
		ReplyHandler __fail)
		throws IllegalArgumentException, NullPointerException
	{
		this.send(__packet, __pass, __fail, null);
	}
	
	/**
	 * Sends the given packet with an optional reply handler for when a
	 * response is received.
	 *
	 * @param __packet The packet to send.
	 * @param __pass The reply handler to use for this packet.
	 * @param __fail Called when the packet has failed.
	 * @param __always This handler is always called.
	 * @throws IllegalArgumentException If a reply handler is specified and
	 * this is a reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/27
	 */
	public void send(JDWPPacket __packet, ReplyHandler __pass,
		ReplyHandler __fail, ReplyHandler __always)
		throws IllegalArgumentException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// Wanting to handle a reply to this packet 
		if (__pass != null)
		{
			// It makes no sense to handle a reply to a reply
			if (__packet.isReply())
				throw new IllegalArgumentException(
					"Cannot handle a reply to a reply.");
			
			// Store handler for replies before we send as the pipe could be
			// really fast!
			this.replies.await(__packet.id(), __pass, __fail, __always);
			
			// Mark as waiting
			this.waitingTally.increment();
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
	 * Helper for sending known values that always calls sync at the end.
	 *
	 * @param __out The out packet.
	 * @param __value The known value.
	 * @param __sync The sync to call.
	 * @param __pass Called on successful packets.
	 * @param __fail Called on failed packets.
	 * @since 2024/01/27
	 */
	public void sendKnown(JDWPPacket __out, KnownValue<?> __value,
		KnownValueCallback<?> __sync, ReplyHandler __pass,
		ReplyHandler __fail)
	{
		this.send(__out, __pass, __fail, (__state, __reply) -> {
				if (__sync != null)
					__sync.sync(__state, (KnownValue)__value);
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
			this.send(out, (__ignored, __reply) -> {
				// Inform that it was done
				if (__done != null)
					__done.run();
			}, ReplyHandler.IGNORED);
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
			this.send(out, (__ignored, __reply) -> {
				if (__done != null)
					__done.run();
			}, ReplyHandler.IGNORED);
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
				new EventModifierCount(__count),
				new EventModifierSingleStep(__thread,
					__depth, __size)
			};
		
		// An example single step
		// EventRequest[id=483,kind=SINGLE_STEP,suspend=ALL,left=1,
		// filter=EventFilter{callStackStepping=CallStackStepping[
		// thread=Thread-6: callback#0,depth=0,size=1],
		// exception=null, excludeClass=ClassPattern(kotlin.
		// KotlinNullPointerException), fieldOnly=null,
		// includeClass=null, location=null, thisInstance=null,
		// thread=null, type=null}]
		// Compared to:
		// EventRequest[id=125,kind=SINGLE_STEP,suspend=ALL,left=1,
		// filter=EventFilter{callStackStepping=CallStackStepping[
		// thread=Thread-1: main,depth=OUT,size=MIN], exception=null, 
		// excludeClass=null, fieldOnly=null, includeClass=null, 
		// location=null, thisInstance=null, thread=null, type=null}]
		this.eventSet(JDWPEventKind.SINGLE_STEP,
			JDWPSuspendPolicy.EVENT_THREAD,
			modifiers,
			__handler,
			(__id) -> {
				// The thread is stopped, so we must resume here
				this.threadResume(__thread, null);
			});
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
			this.send(out, (__ignored, __reply) -> {
				if (__done != null)
					__done.run();
			}, ReplyHandler.IGNORED);
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
			this.send(out, (__ignored, __reply) -> {
				if (__done != null)
					__done.run();
			}, ReplyHandler.IGNORED);
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
				}, ReplyHandler.IGNORED);
		}
		
		// Version information
		try (JDWPPacket packet = this.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.VERSION))
		{
			this.send(packet, this::__remoteVmInfo, ReplyHandler.IGNORED);
		}
		
		// Get the capabilities of the remote VM, so we know what we can and
		// cannot do
		this.capabilities.update(this);
		
		// Thread events, with no particular handler
		this.eventSet(JDWPEventKind.THREAD_START, JDWPSuspendPolicy.NONE,
			null, (__state, __event) -> {
			}, null);
		this.eventSet(JDWPEventKind.THREAD_DEATH, JDWPSuspendPolicy.NONE,
			null, (__state, __reply) -> {}, null);
		
		// Trick the SquirrelJME hosted environment to handle
		// DebugShelf.breakpoint() calls
		this.eventSet(JDWPEventKind.EXCEPTION, JDWPSuspendPolicy.ALL,
			new EventModifier[]{new EventModifierClassMatch(
				"cc.squirreljme.emulator.__PseudoBreakpoint__")},
			(__state, __reply) -> {}, null);
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
		PacketDefer defer = this.defer;
				
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
			defer.defer(link, __packet);
		}
		
		// General exception
		catch (JDWPException __e)
		{
			__e.printStackTrace();
			
			throw __e;
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
		AwaitingReply handler = this.replies.remove(__packet.id());
		
		// If there is no handler, just ignore
		if (handler == null)
		{
			if (JDWPCommLink.DEBUG)
				Debugging.debugNote("No handler for reply %d...",
					__packet.id());
			
			return;
		}
		
		// Set latency
		int latency = (int)Math.min(Integer.MAX_VALUE,
			(System.nanoTime() - handler.nanoTime) / 1_000_000L);
		this.latency.set(latency);
		
		// Debug
		if (JDWPCommLink.DEBUG)
			Debugging.debugNote("Packet %d (%08x): Latency %d ms",
				handler.id, handler.id, latency);
		
		// Decrement waiting count
		this.waitingTally.decrement();
		
		// Always ensure that the always-callback gets executed regardless
		// of any exceptions or otherwise that may occur
		try
		{
			// Call the pass/fail handler accordingly
			if (__packet.hasError())
			{
				if (handler.fail != null)
					handler.fail.handlePacket(this, __packet);
			}
			else
			{
				if (handler.pass != null)
					handler.pass.handlePacket(this, __packet);
			}
		}
		finally
		{
			// Call the always-handler?
			if (handler.always != null)
				handler.always.handlePacket(this, __packet);
		}
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
			if (JDWPCommLink.DEBUG)
				Debugging.debugNote("Process event: %s", __packet);
			
			// Handle this as an event
			EventProcessor.handle(this, __packet);
			
			// Do not do any more processing
			return;
		}
		
		// Debug
		if (JDWPCommLink.DEBUG)
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
