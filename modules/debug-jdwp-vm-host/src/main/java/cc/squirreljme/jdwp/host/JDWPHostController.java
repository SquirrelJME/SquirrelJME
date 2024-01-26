// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPClassStatus;
import cc.squirreljme.jdwp.JDWPCommLink;
import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandException;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPIdSizes;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.jdwp.JDWPValueTag;
import cc.squirreljme.jdwp.host.event.JDWPHostCallStackStepping;
import cc.squirreljme.jdwp.host.event.JDWPHostFieldOnly;
import cc.squirreljme.jdwp.host.event.JDWPHostEventFilter;
import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTrip;
import cc.squirreljme.jdwp.host.views.JDWPView;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import cc.squirreljme.jdwp.host.views.JDWPViewHasInstance;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.multiphasicapps.collections.UnmodifiableMap;

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
	
	/** Attempts to instance capture. */
	private static final JDWPViewKind[] _INSTANCE_CAPTURE =
		new JDWPViewKind[]{JDWPViewKind.THREAD, JDWPViewKind.THREAD_GROUP,
			JDWPViewKind.TYPE};
	
	/** All the command handlers that are available. */
	private static final Map<JDWPCommand, JDWPCommandHandler> _HANDLERS; 
	
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
	private final Deque<JDWPHostValue> _freeValues =
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
	private volatile boolean _holdEvents;
	
	/** Is this closed? */
	private volatile boolean _closed;
	
	static
	{
		// Fill out handlers
		Map<JDWPCommand, JDWPCommandHandler> handlers =
			new LinkedHashMap<>();
		
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetArrayReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetClassLoader.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetClassObjectReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetClassType.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetEventRequest.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetMethod.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetObjectReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetReferenceType.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetStackFrame.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetStringReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetThreadGroupReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetThreadReference.values());
		JDWPHostController.__fillIn(handlers,
			JDWPHostCommandSetVirtualMachine.values());
			
		// Store for later usage
		_HANDLERS = UnmodifiableMap.of(handlers);
	}
	
	/**
	 * Fills in values for the map.
	 *
	 * @param __handlers The target handler mapping.
	 * @param __impls The implementation of the handlers.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	private static void __fillIn(
		Map<JDWPCommand, JDWPCommandHandler> __handlers,
		JDWPCommandHandler[] __impls)
		throws NullPointerException
	{
		if (__handlers == null || __impls == null)
			throw new NullPointerException("NARG");
		
		for (JDWPCommandHandler impl : __impls)
			__handlers.put(impl.command(), impl);
	}
	
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
		this.getCommLink().setIdSizes(new JDWPIdSizes(
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
			this.getCommLink().close();
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
	 * @since 2024/01/23
	 */
	public JDWPCommandHandler commandHandler(JDWPCommandSet __commandSet,
		int __command)
	{
		// No command set? We cannot handle it
		if (__commandSet == null)
			return null;
		
		return this.commandHandler(__commandSet.command(__command));
	}
	
	/**
	 * Returns the command handler for packets.
	 *
	 * @param __command The command used.
	 * @return The handler for commands.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	public JDWPCommandHandler commandHandler(JDWPCommand __command)
		throws NullPointerException
	{
		return JDWPHostController._HANDLERS.get(__command);
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
		
		JDWPPacket rv = this.getCommLink().request(64,
			100);
		
		// Write the single event header
		rv.writeByte(__policy.id);
		rv.writeInt(1);
		rv.writeByte(__kind.id);
		rv.writeInt(__responseId);
		
		return rv;
	}
	
	/**
	 * Returns the communication link.
	 *
	 * @return The communication link.
	 * @since 2024/01/23
	 */
	public JDWPCommLink getCommLink()
	{
		return this.commLink;
	}
	
	/**
	 * Returns the event manager.
	 *
	 * @return The event manager.
	 * @since 2024/01/23
	 */
	public JDWPHostEventManager getEventManager()
	{
		return this.eventManager;
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
	 * Tries to guess the type of value used.
	 * 
	 * @param __value The value type.
	 * @return The guessed value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/14
	 */
	public JDWPValueTag guessType(JDWPHostValue __value)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		// If not set, treat as void
		if (!__value.isSet())
			return JDWPValueTag.VOID;
		return this.guessTypeRaw(__value.get());
	}
	
	/**
	 * Tries to guess the type of value used.
	 * 
	 * @param __value The value type.
	 * @return The guessed value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/30
	 */
	public JDWPValueTag guessTypeRaw(Object __value)
		throws NullPointerException
	{
		// If null, assume an object type
		if (__value == null || this.viewObject().isNullObject(__value))
			return JDWPValueTag.OBJECT;
		
		// If this a valid object, try to get it from its type
		else if (this.viewObject().isValid(__value))
			return JDWPValueTag.fromSignature(this.viewType()
				.signature(this.viewObject().type(__value)));
		
		// Boxed typed?
		else if (__value instanceof Boolean)
			return JDWPValueTag.BOOLEAN;
		else if (__value instanceof Byte)
			return JDWPValueTag.BYTE;
		else if (__value instanceof Short)
			return JDWPValueTag.SHORT;
		else if (__value instanceof Character)
			return JDWPValueTag.CHARACTER;
		else if (__value instanceof Integer)
			return JDWPValueTag.INTEGER;
		else if (__value instanceof Long)
			return JDWPValueTag.LONG;
		else if (__value instanceof Float)
			return JDWPValueTag.FLOAT;
		else if (__value instanceof Double)
			return JDWPValueTag.DOUBLE;
		
		// Unknown, use void
		return JDWPValueTag.VOID;
	}
	
	/**
	 * Is the debugger holding events?
	 *
	 * @return If it is holding events.
	 * @since 2024/01/23
	 */
	public boolean isHoldingEvents()
	{
		synchronized (this)
		{
			return this._holdEvents;
		}
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
		for (JDWPCommLink commLink = this.getCommLink();;)
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
					if (this.isHoldingEvents())
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
						if (JDWPHostController._DEBUG)
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
	 * Reads the given array from the packet.
	 * 
	 * @param __packet The source packet.
	 * @param __nullable Can this be null?
	 * @return The array value.
	 * @throws JDWPException If this does not refer to a valid array.
	 * @since 2021/04/11
	 */
	public final Object readArray(JDWPPacket __packet,
		boolean __nullable)
		throws JDWPException
	{
		Object object = this.readObject(__packet, __nullable);
		
		// Is this an invalid array?
		if (this.viewObject().arrayLength(object) < 0)
		{
			if (__nullable && object == null)
				return null;
			
			// Fail with invalid thread
			throw JDWPErrorType.INVALID_ARRAY.toss(object,
				System.identityHashCode(object));
		}
		
		return object;
	}
	
	/**
	 * Reads the given frame from the packet.
	 * 
	 * @param __packet The source packet.
	 * @param __nullable Can this be null?
	 * @return The frame value.
	 * @throws JDWPException If this does not refer to a valid frame.
	 * @since 2021/04/11
	 */
	public Object readFrame(JDWPPacket __packet, boolean __nullable)
	{
		int id = __packet.readId();
		Object frame = this.getState().items.get(id);
		
		// Is this valid?
		if (!this.viewFrame().isValid(frame))
		{
			if (__nullable && frame == null)
				return null;
			
			// Fail with invalid thread
			throw JDWPErrorType.INVALID_FRAME_ID.toss(frame, id);
		}
		
		return frame;
	}
	
	/**
	 * Reads a location from the packet.
	 * 
	 * @param __packet The source packet.
	 * @return The given location.
	 * @throws JDWPException If the location is not valid.
	 * @since 2021/04/17
	 */
	public JDWPHostLocation readLocation(JDWPPacket __packet)
		throws JDWPException
	{
		synchronized (__packet)
		{
			// This identifies classes or interfaces except we do not need
			// this distinction, however for exception handlers locations can
			// be 0 for anything that is not handled.
			int tag = __packet.readByte();
			if (tag == 0)
				return JDWPHostLocation.BLANK;
			
			// Make sure the type and method are valid
			JDWPViewType viewType = this.viewType();
			Object type = this.readType(__packet, false);
			int methodDx = __packet.readId();
			if (!viewType.isValidMethod(type, methodDx))
				throw JDWPErrorType.INVALID_METHOD_ID.toss(type, methodDx,
					null);
			
			// Build location
			return new JDWPHostLocation(type, methodDx, __packet.readLong(),
				viewType.methodName(type, methodDx),
				viewType.methodSignature(type, methodDx));
		}
	}
	
	/**
	 * Reads the given object from the packet.
	 * 
	 * @param __packet The source packet.
	 * @param __nullable Can this be null?
	 * @return The object value.
	 * @throws JDWPException If this does not refer to a valid object.
	 * @since 2021/04/11
	 */
	public final Object readObject(JDWPPacket __packet,
		boolean __nullable)
		throws JDWPException
	{
		int id = __packet.readId();
		Object object = this.getState().items.get(id);
		
		// Is this not a valid object?
		if (!this.viewObject().isValid(object))
		{
			// If this is a valid thread, then bounce through to the thread's
			// instance object
			if (this.viewThread().isValid(object))
			{
				Object alt = this.viewThread().instance(object);
				if (this.viewObject().isValid(alt))
				{
					// Make sure it is registered
					this.getState().items.put(alt);
					return alt;
				}
			}
			
			// If this is a thread group, try to get the representative object
			// of it, this should be the task
			if (this.viewThreadGroup().isValid(object))
			{
				Object alt = this.viewThreadGroup().instance(object);
				if (this.viewObject().isValid(alt))
				{
					// Make sure it is registered
					this.getState().items.put(alt);
					return alt;
				}
			}
			
			// If this a valid class, bounce to the class instance object
			if (this.viewType().isValid(object))
			{
				// Double check if it is valid
				Object alt = this.viewType().instance(object);
				if (this.viewObject().isValid(alt))
				{
					// Make sure it is registered
					this.getState().items.put(alt);
					return alt;
				}
			}
			
			// Not valid?
			if (__nullable && object == null)
				return null;
			
			// Fail with invalid object
			throw JDWPErrorType.INVALID_OBJECT.toss(object, id);
		}
		
		return object;
	}
	
	/**
	 * Reads the given thread from the packet.
	 * 
	 * @param __packet The source packet.
	 * @return The object value.
	 * @throws JDWPException If this does not refer to a valid thread.
	 * @since 2021/04/11
	 */
	public final Object readThread(JDWPPacket __packet)
		throws JDWPException
	{
		JDWPViewObject viewObject = this.viewObject();
		JDWPViewThread viewThread = this.viewThread();
		JDWPViewType viewType = this.viewType();
		
		int id = __packet.readId();
		Object thread = this.getState().items.get(id);
		
		// Is this valid?
		if (!viewThread.isValid(thread))
		{
			// Threads may be aliased to objects, and as such if we try to
			// read a thread that is aliased by an object we need to get
			// the original thread back
			// Scan through all threads and see if we can find it again
			if (viewObject.isValid(thread))
			{
				// Try to find the actual owning thread
				for (Object check : this.allThreads(false))
					if (thread == viewThread.instance(check))
					{
						// Make sure it is registered
						this.getState().items.put(check);
						return check;
					}
				
				// Try to get the internal thread this represents if we were
				// unable to find an existing thread this is owned by... since
				// perhaps the thread terminated and no longer exists
				Object objType = viewObject.type(thread);
				if ("Ljava/lang/Thread;".equals(viewType.signature(objType)))
				{
					// Find the field for this
					int fieldId = JDWPHostUtils.findFieldId(viewType, objType,
						"_vmThread",
						"Lcc/squirreljme/jvm/mle/brackets/VMThreadBracket;");
					
					// Read from this field
					if (fieldId >= 0)
						try (JDWPHostValue value = this.value())
						{
							// If this is a valid object, then use it
							if (viewObject.readValue(thread, fieldId, value))
								return viewThread.fromBracket(value.get());
						}
				}
			}
			
			// Fail with invalid thread
			throw JDWPErrorType.INVALID_THREAD.toss(thread, id);
		}
		
		return thread;
	}
	
	/**
	 * Reads the given thread group from the packet.
	 * 
	 * @param __packet The source packet.
	 * @param __nullable Can this be null?
	 * @return The thread group.
	 * @throws JDWPException If this does not refer to a valid thread group.
	 * @since 2021/04/14
	 */
	public final Object readThreadGroup(JDWPPacket __packet,
		boolean __nullable)
		throws JDWPException
	{
		int id = __packet.readId();
		Object group = this.getState().items.get(id);
		
		// Is this valid?
		JDWPViewThreadGroup viewThreadGroup = this.viewThreadGroup();
		if (!this.viewThreadGroup().isValid(group))
		{
			// Groups may be aliased to Objects, so if this is not one then
			// we want to check all of our thread groups to see if we can
			// find a match accordingly
			if (this.viewObject().isValid(group))
				for (Object check : this.allThreadGroups())
					if (group == viewThreadGroup.instance(check))
					{
						// Make sure it is registered
						this.getState().items.put(check);
						return check;
					}
			
			if (__nullable && group == null)
				return null;
			
			// Fail with invalid thread
			throw JDWPErrorType.INVALID_THREAD.toss(group, id);
		}
		
		return group;
	}
	
	/**
	 * Reads the given type from the packet.
	 * 
	 * @param __packet The source packet.
	 * @param __nullable Can this be null?
	 * @return The type value.
	 * @throws JDWPException If this does not refer to a valid type.
	 * @since 2021/04/12
	 */
	public final Object readType(JDWPPacket __packet,
		boolean __nullable)
		throws JDWPException
	{
		int id = __packet.readId();
		Object object = this.getState().items.get(id);
		
		// Is this valid?
		if (!this.viewType().isValid(object))
		{
			// We may be trying to read the type of object, so we need to
			// alias to that
			if (this.viewObject().isValid(object))
			{
				Object alt = this.viewObject().type(object);
				if (this.viewType().isValid(alt))
				{
					// Make it is registered
					this.getState().items.put(alt);
					return alt;
				}
			}
			
			if (__nullable && object == null)
				return null;
			
			// Fail with invalid thread
			throw JDWPErrorType.INVALID_CLASS.toss(object, id);
		}
		
		return object;
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
		return this.getCommLink().reply(__id, __error);
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
			JDWPCommLink commLink = this.getCommLink();
			while (!commLink.isShutdown())
				this.poll();
		}
	}
	
	/**
	 * Set or unset event holding.
	 *
	 * @param __hold Should events be held?
	 * @since 2024/01/23
	 */
	public void setHoldingEvents(boolean __hold)
	{
		synchronized (this)
		{
			this._holdEvents = __hold;
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
	public boolean signal(Object __thread, JDWPEventKind __kind,
		Object... __args)
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
		for (JDWPHostEventRequest request : this.getEventManager().find(
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
				JDWPHostEventKindHandler.of(__kind.debuggerId())
					.write(this, __thread, packet, __args);
				
				// Are we holding events? Save this for later if so
				synchronized (this)
				{
					if (this.isHoldingEvents())
					{
						this._heldPackets.add(this.getCommLink()
							.getPacket().copyOf(packet));
						continue;
					}
				}
				
				// Send it away!
				this.getCommLink().send(packet);
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
					JDWPHostFieldOnly fieldOnly = filter.fieldOnly;
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
					JDWPHostCallStackStepping stepping =
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
	public final JDWPHostValue value()
	{
		Deque<JDWPHostValue> freeValues = this._freeValues;
		synchronized (this._freeValues)
		{
			// Use an existing free value for recycling?
			JDWPHostValue rv = freeValues.poll();
			if (rv != null)
				return rv.__resetToOpen();
			
			// Otherwise make a new one
			//noinspection resource
			return new JDWPHostValue(freeValues).__resetToOpen();
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
	
	/**
	 * Writes a location into the packet.
	 * 
	 * @param __packet The source packet. 
	 * @param __location The location used.
	 * @throws JDWPException If the packet could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/25
	 */
	public void writeLocation(JDWPPacket __packet,
		JDWPHostLocation __location)
		throws JDWPException, NullPointerException
	{
		// If this is the blank location, then write as blank
		if (JDWPHostLocation.BLANK.equals(__location))
		{
			__packet.writeByte(0);
			return;
		}
		
		// Otherwise forward
		this.writeLocation(__packet, __location.type,
			__location.methodDx, __location.codeDx);
	}
	
	/**
	 * Writes a location into the packet.
	 * 
	 * @param __packet The source packet. 
	 * @param __class The class to write.
	 * @param __atMethodIndex The method index.
	 * @param __atCodeIndex The code index.
	 * @throws JDWPException If the packet could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public void writeLocation(JDWPPacket __packet, Object __class,
		int __atMethodIndex, long __atCodeIndex)
		throws JDWPException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		synchronized (__packet)
		{
			// Write class located within
			this.writeTaggedId(__packet, __class);
			
			// Write the method ID and the special index (address)
			__packet.writeId(__atMethodIndex);
			
			// Where is this located? Note that the index is a long here
			// although such a high value should hopefully never be needed
			// in SquirrelJME
			__packet.writeLong(__atCodeIndex);
		}
	}
	
	/**
	 * Writes the object to the output.
	 * 
	 * @param __packet The source packet.
	 * @param __instance The instance of the object.
	 * @throws JDWPException If this is not an object.
	 * @throws NullPointerException If {@code __controller} is {@code null}.
	 * @since 2022/09/01
	 */
	public void writeObject(JDWPPacket __packet, Object __instance)
		throws JDWPException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		synchronized (__packet)
		{
			// If this is the null object, invalidate it
			JDWPViewObject viewObject = this.viewObject();
			if (__instance != null && viewObject.isNullObject(__instance))
				__instance = null;
			
			// Try to remap the object to an instance type if possible
			if (__instance != null)
			{
				// Try to capture the object instance of this?
				for (JDWPViewKind captureKind :
					JDWPHostController._INSTANCE_CAPTURE)
				{
					JDWPViewHasInstance viewInstance =
						this.view(JDWPViewHasInstance.class, captureKind);
					
					// Can only do this if it is valid to do it
					if (viewInstance.isValid(__instance))
					{
						// It is possible that there is no actual instance
						// type yet such as with types, so only replace if it
						// does not lead to null
						Object potential = viewInstance.instance(__instance);
						if (potential != null &&
							!viewObject.isNullObject(potential))
						{
							// We need to store both of these
							this.getState().items.put(__instance);
							this.getState().items.put(potential);
							
							// Use the new instance
							__instance = potential;
						
							// Stop now
							break;
						}
					}
				}
				
				// Not valid at all?
				if (!viewObject.isValid(__instance) &&
					!this.viewType().isValid(__instance) &&
					!this.viewFrame().isValid(__instance) &&
					!this.viewThread().isValid(__instance) &&
					!this.viewThreadGroup().isValid(__instance))
					throw JDWPErrorType.INVALID_OBJECT.toss(__instance,
						System.identityHashCode(__instance));
			}
			
			// Forward to write ID
			__packet.writeId(System.identityHashCode(__instance));
			
			// Store for later referencing, just in case
			if (__instance != null)
				this.getState().items.put(__instance);
		}
	}
	
	/**
	 * Writes a tagged object ID to the output.
	 * 
	 * @param __packet The source packet.
	 * @param __object The object to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2022/08/28
	 */
	public void writeTaggedId(JDWPPacket __packet, Object __object)
		throws JDWPException
	{
		synchronized (__packet)
		{
			__packet.writeByte(JDWPHostUtils.classType(
				this, __object).id);
			__packet.writeId(System.identityHashCode(__object));
		}
	}
	
	/**
	 * Writes a value to the output.
	 *
	 * @param __packet The source packet.
	 * @param __val The value to write.
	 * @param __context Context value which may adjust how the value is
	 * written, this may be {@code null}.
	 * @param __untag Untagged value?
	 * @throws JDWPException If it failed to write.
	 * @since 2021/04/11
	 */
	public void writeValue(JDWPPacket __packet, Object __val,
		JDWPValueTag __context, boolean __untag)
		throws JDWPException
	{
		// We really meant to write a value here
		if (__val instanceof JDWPHostValue)
		{
			this.writeValue(__packet,
				((JDWPHostValue)__val).get(), __context, __untag);
			return;
		}
		
		synchronized (__packet)
		{
			// Depends on the context
			switch (__context)
			{
					// Void type
				case VOID:
					__packet.writeByte('V');
					break;
				
					// Boolean value, may be untagged
				case BOOLEAN:
					if (!__untag)
						__packet.writeByte('Z');
					__packet.writeBoolean(((__val instanceof Boolean) ?
						(boolean)__val :
						((Number)__val).longValue() != 0));
					break;
					
					// Byte value, may be untagged
				case BYTE:
					if (!__untag)
						__packet.writeByte('B');
					__packet.writeByte(((Number)__val).byteValue());
					break;
					
					// Short value, may be untagged
				case SHORT:
					if (!__untag)
						__packet.writeByte('S');
					__packet.writeShort(((Number)__val).shortValue());
					break;
					
					// Character value, may be untagged
				case CHARACTER:
					if (!__untag)
						__packet.writeByte('C');
					__packet.writeShort(((__val instanceof Character) ?
						(char)__val :
						((Number)__val).shortValue()));
					break;
					
					// Integer value, may be untagged
				case INTEGER:
					if (!__untag)
						__packet.writeByte('I');
					__packet.writeInt(((Number)__val).intValue());
					break;
					
					// Long value, may be untagged
				case LONG:
					if (!__untag)
						__packet.writeByte('J');
					__packet.writeLong(((Number)__val).longValue());
					break;
					
					// Float value, may be untagged
				case FLOAT:
					if (!__untag)
						__packet.writeByte('F');
					__packet.writeInt(Float.floatToRawIntBits(
						((Number)__val).floatValue()));
					break;
					
					// Long value, may be untagged
				case DOUBLE:
					if (!__untag)
						__packet.writeByte('D');
					__packet.writeLong(Double.doubleToRawLongBits(
						((Number)__val).doubleValue()));
					break;
				
					// Objects are always tagged
				case ARRAY:
				case OBJECT:
				case CLASS_OBJECT:
				case CLASS_LOADER:
				case THREAD:
				case THREAD_GROUP:
				case STRING:
					// Write the tag
					switch (__context)
					{
						case ARRAY:
							__packet.writeByte('[');
							break;
						
						case CLASS_OBJECT:
							__packet.writeByte('c');
							break;
						
						case CLASS_LOADER:
							__packet.writeByte('l');
							break;
						
						case THREAD:
							__packet.writeByte('t');
							break;
						
						case THREAD_GROUP:
							__packet.writeByte('g');
							break;
						
						case STRING:
							__packet.writeByte('s');
							break;
						
						default:
							__packet.writeByte('L');
							break;
					}
					
					this.writeObject(__packet, __val);
					break;
				
				default:
					throw Debugging.oops(__context);
			}
		}
	}
}
