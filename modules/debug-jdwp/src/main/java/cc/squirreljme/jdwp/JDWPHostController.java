// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.CallStackStepping;
import cc.squirreljme.jdwp.event.JDWPHostEventFilter;
import cc.squirreljme.jdwp.event.FieldOnly;
import cc.squirreljme.jdwp.host.JDWPCommandHandler;
import cc.squirreljme.jdwp.host.JDWPHostBinding;
import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTrip;
import cc.squirreljme.jdwp.host.views.JDWPView;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import cc.squirreljme.jdwp.host.views.JDWPViewKind;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import cc.squirreljme.jdwp.host.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class acts as the main controller interface for JDWP and acts as a kind
 * of polling system to interface with something.
 *
 * @since 2021/03/08
 */
public final class JDWPHostController
	implements Closeable, Runnable
{
	/** Should debugging be enabled? */
	static final boolean _DEBUG =
		Boolean.getBoolean("cc.squirreljme.jdwp.debug");
	
	/** The communication link. */
	protected final JDWPCommLink commLink;
	
	/** Debugger state. */
	protected final JDWPHostState state;
		
	/** The event manager. */
	protected final JDWPHostEventManager eventManager =
		new JDWPHostEventManager();
	
	/** The binding, which is called to perform any actions. */
	private final Reference<JDWPHostBinding> _bind;
		
	/** The ID lock. */
	private final Object _nextIdMonitor =
		new Object();
	
	/** Value cache. */
	private final Deque<JDWPValue> _freeValues =
		new LinkedList<>();
	
	/** The global trips that are available. */
	private final JDWPTrip[] _trips =
		new JDWPTrip[JDWPGlobalTrip.values().length];
	
	/** Weak self reference, so there are not 1000 of these. */
	private final Reference<JDWPHostController> _weakThis =
		new WeakReference<>(this);
	
	/** Held packets. */
	private final Queue<JDWPPacket> _heldPackets =
		new LinkedList<>();
	
	/** Are events to the debugger being held? */
	volatile boolean _holdEvents;
	
	/** Is this closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the controller which manages the communication of JDWP.
	 * 
	 * @param __bind The binding to use.
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPHostController(JDWPHostBinding __bind, InputStream __in,
		OutputStream __out)
		throws NullPointerException
	{
		if (__bind == null || __in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this._bind = new WeakReference<>(__bind);
		this.state = new JDWPHostState(new WeakReference<>(__bind));
		this.commLink = new JDWPCommLink(__in, __out);
		
		// Set sizes that we use
		this.commLink.setIdSizes(new JDWPIdSizes(
			4, 4, 4, 4, 4));
		
		// Setup Communication Link thread
		Thread thread = new Thread(this, "JDWPController");
		thread.start();
	}
	
	/**
	 * Returns all threads.
	 *
	 * @param __filterVisible Filter visible threads?
	 * @return All threads.
	 * @since 2021/04/10
	 */
	public final Object[] allThreads(boolean __filterVisible)
	{
		// Current state
		JDWPHostState state = this.getState();
		
		// Get groups
		JDWPViewThreadGroup groupView = state.view(
			JDWPViewThreadGroup.class, JDWPViewKind.THREAD_GROUP);
		JDWPViewThread threadView = state.view(
			JDWPViewThread.class, JDWPViewKind.THREAD);
		
		// All available threads
		ArrayList<Object> allThreads = new ArrayList<>(); 
		
		// Start from the root thread group and get all the threads
		// under them, since this is a machine to thread linkage
		for (Object group : this.bind().debuggerThreadGroups())
		{
			// Register thread group
			state.items.put(group);
			state.items.put(groupView.instance(group));
			
			// Obtain all threads from this group
			List<Object> threads = new ArrayList<>();
			for (Object thread : groupView.threads(group))
				if (!__filterVisible ||
					JDWPHostUtils.isVisibleThread(threadView, thread))
					threads.add(thread);
			
			// Register each thread
			for (Object thread : threads)
			{
				state.items.put(thread);
				
				// We could be at a point where the thread is initialized but
				// the instance of that thread is not yet known
				Object threadInstance = threadView.instance(thread);
				if (threadInstance != null)
					state.items.put(threadInstance);
			}
			
			// Store into the list
			allThreads.ensureCapacity(
				allThreads.size() + threads.size());
			allThreads.addAll(threads);
		}
		
		return allThreads.toArray(new Object[allThreads.size()]);
	}
	
	/**
	 * Returns all the known types.
	 * 
	 * @param __cached Do we use the type cache?
	 * @return All the available types.
	 * @since 2021/04/14
	 */
	public List<Object> allTypes(boolean __cached)
	{
		List<Object> allTypes = new LinkedList<>();
		
		// Using all the known cached types
		if (__cached)
		{
			JDWPViewType viewType = this.viewType();
			for (Object obj : this.getState().items.values())
				if (viewType.isValid(obj))
					allTypes.add(obj);
		}
		
		// Get a fresh perspective on all the loaded types
		else
		{
			for (Object group : this.allThreadGroups())
				allTypes.addAll(this.allTypes(group));
		}
		
		return allTypes;
	}
	
	/**
	 * Returns all the types within the given group.
	 * 
	 * @param __group The group to search.
	 * @return All the types within the group.
	 * @since 2021/04/25
	 */
	public List<Object> allTypes(Object __group)
		throws NullPointerException
	{
		if (__group == null)
			throw new NullPointerException("NARG");
			
		Object[] types = this.viewThreadGroup().allTypes(__group);
		
		// Register all types so that the debugger knows about their existence
		JDWPHostLinker<Object> items = this.getState().items;
		for (Object type : types)
			items.put(type);
		
		return Arrays.asList(types);
	}
	
	/**
	 * Returns all thread groups.
	 * 
	 * @return All thread groups.
	 * @since 2021/04/10
	 */
	public final Object[] allThreadGroups()
	{
		// Get all thread groups
		Object[] groups = this.bind().debuggerThreadGroups();
		
		// Register each one
		JDWPHostState state = this.getState();
		for (Object group : groups)
			state.items.put(group);
		
		return groups;
	}
	
	/**
	 * Returns the binding.
	 * 
	 * @return The binding.
	 * @throws JDWPException If the binding has been garbage collected.
	 * @since 2021/05/07
	 */
	public JDWPHostBinding bind()
		throws JDWPException
	{
		/* {@squirreljme.error AG0h The JDWP Binding has been garbage
		collected.} */
		JDWPHostBinding rv = this._bind.get();
		if (rv == null)
			throw new JDWPException("AG0h");
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/08
	 */
	@Override
	public void close()
	{
		// Only close once
		synchronized (this)
		{
			if (this._closed)
				return;
			this._closed = true;
		}
		
		// Close the communication link
		try
		{
			this.commLink.close();
		}
		
		// Close and remove all held packets.
		finally
		{
			synchronized (this)
			{
				Queue<JDWPPacket> heldPackets = this._heldPackets;
				while (!heldPackets.isEmpty())
					heldPackets.poll().close();
			}
		}
	}
	
	/**
	 * Returns the command handler for packets.
	 *
	 * @param __commandSet The command set to get.
	 * @param __command The command used.
	 * @return The handler for commands.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	public JDWPCommandHandler commandHandler(JDWPCommandSet __commandSet,
		int __command)
		throws NullPointerException
	{
		if (__commandSet == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Creates an event packet.
	 * 
	 * @param __policy The suspension policy used.
	 * @param __kind The kind of event to give.
	 * @param __responseId The response identifier.
	 * @return The event packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public JDWPPacket event(JDWPSuspendPolicy __policy, JDWPEventKind __kind,
		int __responseId)
		throws NullPointerException
	{
		if (__policy == null || __kind == null)
			throw new NullPointerException("NARG");
		
		JDWPPacket rv = this.commLink.request(64, 100);
		
		// Write the single event header
		rv.writeByte(__policy.id);
		rv.writeInt(1);
		rv.writeByte(__kind.id);
		rv.writeInt(__responseId);
		
		return rv;
	}
	
	/**
	 * Returns the debugger state.
	 *
	 * @return The debugger state.
	 * @since 2024/01/23
	 */
	public JDWPHostState getState()
	{
		return this.state;
	}
	
	/**
	 * Returns the location of the given thread.
	 * 
	 * @param __thread The thread to get from.
	 * @return The current thread location.
	 * @since 2021/04/25
	 */
	public JDWPHostLocation locationOf(Object __thread)
	{
		JDWPViewType viewType = this.viewType();
		JDWPViewThread viewThread = this.viewThread();
		JDWPViewFrame viewFrame = this.viewFrame();
		
		// Get the current frames and see if 
		Object[] frames = viewThread.frames(__thread);
		if (frames == null || frames.length == 0)
			return JDWPHostLocation.BLANK;
		
		// Get frame details
		Object topFrame = frames[0];
		Object type = viewFrame.atClass(topFrame);
		int methodDx = viewFrame.atMethodIndex(topFrame);
		
		// Make sure the types are added!
		JDWPHostLinker<Object> items = this.getState().items;
		if (topFrame != null)
			items.put(topFrame);
		if (type != null)
			items.put(type);
		
		// Build information
		return new JDWPHostLocation(type,
			methodDx,
			viewFrame.atCodeIndex(topFrame),
			viewType.methodName(type, methodDx),
			viewType.methodSignature(type, methodDx));
	}
	
	/**
	 * Polls for state changes within JDWP and processes any events as
	 * needed.
	 * 
	 * @return If JDWP debugging will continue.
	 * @throws JDWPException If there is an issue with the connection.
	 * @since 2021/03/10
	 */
	public boolean poll()
		throws JDWPException
	{
		// Read in any packets and process them as they come
		for (JDWPCommLink commLink = this.commLink;;)
		{
			// Drain any held packets
			for (;;)
			{
				// Is there a packet?
				JDWPPacket packet;
				synchronized (this)
				{
					// If we are closed, do nothing
					if (this._closed)
						return false;
					
					// If we are still holding events, do not drain any
					if (this._holdEvents)
						break;
					
					// Remove the next packet, if there is any
					packet = this._heldPackets.poll();
				}
				
				// If there is no packet stop processing
				if (packet == null)
					break;
				
				// Send this packet and close when done
				try (JDWPPacket ignored = packet)
				{
					commLink.send(packet);
				}
			}
			
			// Normal packet holding
			try (JDWPPacket packet = commLink.poll())
			{
				// No data?
				if (packet == null)
					break;
				
				// Debug
				if (JDWPHostController._DEBUG)
					Debugging.debugNote("JDWP: <- %s", packet);
				
				// Ignore any reply packet we received
				if (packet.isReply())
					continue;
				
				// Resultant packet, returned as a result
				JDWPPacket result;
				
				// Get the command and if it is unknown, ignore it
				JDWPCommandHandler command = this.commandHandler(
					packet.commandSet(), packet.command());
				if (command == null)
					result = this.reply(packet.id(),
						JDWPErrorType.NOT_IMPLEMENTED);
				
				// Execute the command normally
				else
				{
					try
					{
						// Execute the command asynchronously
						result = command.execute(this, packet);
						
						// If a result is missing, assume nothing needed
						if (result == null)
							result = this.reply(packet.id(),
								JDWPErrorType.NO_ERROR);
					}
					
					// There was an error executing this command
					catch (JDWPCommandException e)
					{
						// Print a trace of it
						e.printStackTrace();
						
						// Use this result
						result = this.reply(
							packet.id(), e.type);
					}
				}
				
				// Send the result to the debugger, close when done
				try (JDWPPacket ignored = result)
				{
					commLink.send(result);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Creates a reply packet.
	 * 
	 * @param __id The identifier.
	 * @param __error The error code.
	 * @return The packet used.
	 * @since 2021/03/12
	 */
	public final JDWPPacket reply(int __id, JDWPErrorType __error)
	{
		return this.commLink.reply(__id, __error);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public void run()
	{
		// This runs in a thread, so if this thread ever stops for any reason
		// we terminate the connection
		try (JDWPHostController ignored = this)
		{
			JDWPCommLink commLink = this.commLink;
			while (!commLink.isShutdown())
				this.poll();
		}
	}
	
	/**
	 * Signals a JDWP event, this will find any request and perform suspension
	 * as requested by the debugger.
	 * 
	 * @param __thread The thread signaling this, if not known this will be
	 * {@code null}.
	 * @param __kind The kind of event to signal.
	 * @param __args Arguments to the signal packet.
	 * @throws NullPointerException On null arguments.
	 * @return If an event was found and sent for it.
	 * @since 2021/03/16
	 */
	public boolean signal(Object __thread, JDWPEventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		// Make sure the thread is known
		if (__thread != null)
			this.getState().items.put(__thread);
		
		// Is this a special unconditional event
		boolean unconditional = false;
		if (__kind == JDWPEventKind.UNCONDITIONAL_BREAKPOINT)
		{
			unconditional = true;
			__kind = JDWPEventKind.BREAKPOINT;
		}
		
		// Go through all compatible events for this thread
		boolean hit = false;
		for (JDWPHostEventRequest request : this.eventManager.find(
			this, __thread, unconditional, __kind, __args))
		{
			// Suspend all threads?
			if (request.suspendPolicy == JDWPSuspendPolicy.ALL)
				for (Object thread : this.allThreads(false))
					this.viewThread().suspension(thread).suspend();
			
			// Suspend only a single thread?
			else if (request.suspendPolicy == JDWPSuspendPolicy.EVENT_THREAD)
			{
				if (__thread != null)
					this.viewThread().suspension(__thread).suspend();
			}
			
			// Event was hit
			hit = true;
			
			// Send response to the VM of the event that just occurred
			try (JDWPPacket packet = this.event(request.suspendPolicy,
				__kind, request.id))
			{
				if (JDWPHostController._DEBUG)
					Debugging.debugNote("JDWP: Event #%d %s",
						request.id, __kind);
				
				// Write the signal event data
				__kind.write(this, __thread, packet, __args);
				
				// Are we holding events? Save this for later if so
				synchronized (this)
				{
					if (this._holdEvents)
					{
						this._heldPackets.add(this.commLink
							.getPacket().copyOf(packet));
						continue;
					}
				}
				
				// Send it away!
				this.commLink.send(packet);
			}
		}
		
		return hit;
	}
	
	/**
	 * Returns a trip for the given type of global trip.
	 * 
	 * @param <T> The type of trip.
	 * @param __cl The type of trip.
	 * @param __t The type of trip.
	 * @return The trip for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public final <T extends JDWPTrip> T trip(Class<T> __cl, JDWPGlobalTrip __t)
		throws NullPointerException
	{
		if (__cl == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Was a trip already created? Use that if so
		JDWPTrip[] trips = this._trips;
		JDWPTrip trip = trips[__t.ordinal()];
		if (trip != null)
			return __cl.cast(trip);
		
		// Otherwise set up a new trip
		Reference<JDWPHostController> ref = this._weakThis;
		switch (__t)
		{
			case CLASS_STATUS:
				trip = new __TripClassStatus__(ref);
				break;
			
			case FIELD:
				trip = new __TripField__(ref);
				break;
			
			case VM_STATE:
				trip = new __TripVmState__(ref);
				break;
			
			case THREAD:
				trip = new __TripThreadAlive__(ref);
				break;
			
			default:
				throw Debugging.oops(__t);
		}
		
		// Cache and use it
		this._trips[__t.ordinal()] = trip;
		return  __cl.cast(trip);
	}
	
	/**
	 * Injects the given event for trips to occur at later point.
	 * 
	 * @param __request The request being tripped at a later point.
	 * @since 2021/04/17
	 */
	public void tripRequest(JDWPHostEventRequest __request)
		throws NullPointerException
	{
		if (__request == null)
			throw new NullPointerException("NARG");
		
		Reference<JDWPHostController> ref = this._weakThis;
		
		// Depends on the event
		JDWPHostEventFilter filter = __request.filter;
		switch (__request.eventKind)
		{
				// Breakpoint at a random position
			case BREAKPOINT:
				{
					// If there is no location, this is a pointless breakpoint
					JDWPHostLocation location = filter.location;
					if (location == null)
						return;
					
					// Initializes the breakpoint
					this.viewType().methodBreakpoint(location.type,
						location.methodDx, (int)location.codeDx,
						new __TripBreakpoint__(ref));
				}
				break;
				
			case FIELD_ACCESS:
			case FIELD_MODIFICATION:
				{
					// We need to know the field we are watching
					FieldOnly fieldOnly = filter.fieldOnly;
					if (fieldOnly == null)
						return;
					
					// Indicate we want to watch this field
					this.viewType().fieldWatch(
						fieldOnly.type, fieldOnly.fieldDx,
						__request.eventKind ==
							JDWPEventKind.FIELD_MODIFICATION);
				}
				break;
				
				// Single stepping
			case SINGLE_STEP:
				{
					// An example single step
					// EventRequest[id=483,kind=SINGLE_STEP,suspend=ALL,left=1,
					// filter=EventFilter{callStackStepping=CallStackStepping[
					// thread=Thread-6: callback#0,depth=0,size=1],
					// exception=null, excludeClass=ClassPattern(kotlin.
					// KotlinNullPointerException), fieldOnly=null,
					// includeClass=null, location=null, thisInstance=null,
					// thread=null, type=null}]
					
					// If there is no stepping what are we going to do?
					CallStackStepping stepping =
						filter.callStackStepping;
					if (stepping == null || stepping.thread == null)
						return;
					
					// Thread was also specified but does not match?
					if (filter.thread != null &&
						filter.thread != stepping.thread)
						return;
					
					// Tell the thread to enter stepping mode
					JDWPHostStepTracker stepTracker = this.viewThread()
						.stepTracker(stepping.thread);
					stepTracker.steppingSet(this, stepping.thread,
						stepping.size, stepping.depth);
				}
				break;
			
				// Preparing/loading a class, since this could be a class that
				// we already know about, we need to go through all of the
				// classes to find the right one
			case CLASS_PREPARE:
				{
					// If this filter does not have a type match then it is
					// very likely a very generic one
					if (filter == null || !filter.hasTypeMatch())
						return;
					
					// Go through all of our known classes and report ones
					// that we already know about. Note use the cached types
					// so we do not have to ask the VM about it.
					JDWPViewType viewType = this.viewType();
					for (Object type : this.allTypes(true))
						if (filter.meetsType(viewType, type))
							this.signal(null, __request.eventKind,
								type, JDWPClassStatus.INITIALIZED);
				}
				break;
		}
	}
	
	/**
	 * Returns a value to store data in.
	 * 
	 * @return A value to store data in.
	 * @since 2021/03/19
	 */
	public final JDWPValue value()
	{
		Deque<JDWPValue> freeValues = this._freeValues;
		synchronized (this._freeValues)
		{
			// Use an existing free value for recycling?
			JDWPValue rv = freeValues.poll();
			if (rv != null)
				return rv.__resetToOpen();
			
			// Otherwise make a new one
			//noinspection resource
			return new JDWPValue(freeValues).__resetToOpen();
		}
	}
	
	/**
	 * Returns the view of the given type.
	 * 
	 * @param <V> The type to view.
	 * @param __type The type to view.
	 * @param __kind The kind of viewer to use.
	 * @return The view for the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/07/12
	 */
	public final <V extends JDWPView> V view(Class<V> __type,
		JDWPViewKind __kind)
	{
		return this.getState().<V>view(__type, __kind);
	}
	
	/**
	 * Returns the frame viewer.
	 * 
	 * @return The frame viewer.
	 * @since 2021/04/11
	 */
	public final JDWPViewFrame viewFrame()
	{
		return this.getState().view(JDWPViewFrame.class, JDWPViewKind.FRAME);
	}
	
	/**
	 * Returns the object viewer.
	 * 
	 * @return The object viewer.
	 * @since 2021/04/10
	 */
	public final JDWPViewObject viewObject()
	{
		return this.getState().view(JDWPViewObject.class, JDWPViewKind.OBJECT);
	}
	
	/**
	 * Returns the thread viewer.
	 * 
	 * @return The thread viewer.
	 * @since 2021/04/10
	 */
	public final JDWPViewThread viewThread()
	{
		return this.getState().view(JDWPViewThread.class, JDWPViewKind.THREAD);
	}
	
	/**
	 * Returns the viewer for thread groups.
	 * 
	 * @return The viewer for thread groups.
	 * @since 2021/04/10
	 */
	public final JDWPViewThreadGroup viewThreadGroup()
	{
		return this.getState().view(JDWPViewThreadGroup.class,
			JDWPViewKind.THREAD_GROUP);
	}
	
	/**
	 * Returns the viewer for types.
	 * 
	 * @return The viewer for types.
	 * @since 2021/04/11
	 */
	public final JDWPViewType viewType()
	{
		return this.getState().view(JDWPViewType.class,
			JDWPViewKind.TYPE);
	}
}
