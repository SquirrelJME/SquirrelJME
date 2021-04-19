// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.trips.JDWPTrip;
import cc.squirreljme.jdwp.views.JDWPViewFrame;
import cc.squirreljme.jdwp.views.JDWPViewKind;
import cc.squirreljme.jdwp.views.JDWPViewObject;
import cc.squirreljme.jdwp.views.JDWPViewThread;
import cc.squirreljme.jdwp.views.JDWPViewThreadGroup;
import cc.squirreljme.jdwp.views.JDWPViewType;
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

/**
 * This class acts as the main controller interface for JDWP and acts as a kind
 * of polling system to interface with something.
 *
 * @since 2021/03/08
 */
public final class JDWPController
	implements Closeable, Runnable
{
	/** The event type. */
	private static final int _EVENT_TYPE =
		64;
	
	/** Composite event. */
	private static final int _COMPOSITE_COMMAND =
		100;
	
	/** The binding, which is called to perform any actions. */
	protected final JDWPBinding bind;
	
	/** The communication link. */
	protected final CommLink commLink;
	
	/** Debugger state. */
	protected final JDWPState state;
		
	/** The event manager. */
	protected final EventManager eventManager =
		new EventManager();
		
	/** The ID lock. */
	private final Object _nextIdMonitor =
		new Object();
	
	/** Value cache. */
	private final Deque<JDWPValue> _freeValues =
		new LinkedList<>();
	
	/** The global trips that are available. */
	private final JDWPTrip[] _trips =
		new JDWPTrip[JDWPGlobalTrip.values().length];
	
	/** Are events to the debugger being held? */
	protected volatile boolean _holdEvents;
	
	/** Next ID number. */
	private volatile int _nextId;
	
	/**
	 * Initializes the controller which manages the communication of JDWP.
	 * 
	 * @param __bind The binding to use.
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPController(JDWPBinding __bind, InputStream __in,
		OutputStream __out)
		throws NullPointerException
	{
		if (__bind == null || __in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.bind = __bind;
		this.state = new JDWPState(new WeakReference<>(__bind));
		this.commLink = new CommLink(__in, __out);
		
		// Setup Communication Link thread
		Thread thread = new Thread(this, "JDWPController");
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/08
	 */
	@Override
	public void close()
	{
		this.commLink.close();
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
		for (CommLink commLink = this.commLink;;)
			try (JDWPPacket packet = commLink.poll())
			{
				// No data?
				if (packet == null)
					break;
				
				// Debug
				Debugging.debugNote("JDWP: <- %s", packet);
				
				// Ignore any reply packet we received
				if (packet.isReply())
					continue;
				
				// Resultant packet, returned as a result
				JDWPPacket result;
				
				// Get the command and if it is unknown, ignore it
				JDWPCommand command = packet.commandSet()
					.command(packet.command());
				if (command == null)
					result = this.__reply(packet.id(),
						ErrorType.NOT_IMPLEMENTED);
				
				// Execute the command normally
				else
				{
					try
					{
						// Lock to make sure state does not get warped
						synchronized (this)
						{
							result = command.execute(this, packet);
						}
						
						// If a result is missing, assume nothing needed
						if (result == null)
							result = this.__reply(packet.id(),
								ErrorType.NO_ERROR);
					}
					
					// There was an error executing this command
					catch (JDWPCommandException e)
					{
						// Print a trace of it
						e.printStackTrace();
						
						// Use this result
						result = this.__reply(
							packet.id(), e.type);
					}
				}
				
				// Send the result to the debugger, close when done
				try (JDWPPacket ignored = result)
				{
					commLink.send(result);
				}
			}
		
		return true;
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
		try (JDWPController ignored = this)
		{
			CommLink commLink = this.commLink;
			while (!commLink._shutdown)
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
	 * @since 2021/03/16
	 */
	public void signal(Object __thread, EventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Go through all compatible events for this thread
		for (EventRequest request : this.eventManager.find(
			this, __thread, __kind, __args))
		{
			// Suspend all threads?
			if (request.suspendPolicy == SuspendPolicy.ALL)
				for (Object thread : this.__allThreads())
					this.viewThread().suspension(thread).suspend();
			
			// Suspend only a single thread?
			else if (request.suspendPolicy == SuspendPolicy.EVENT_THREAD)
				this.viewThread().suspension(__thread).suspend();
			
			// Send response to the VM of the event that just occurred
			try (JDWPPacket packet = this.__event(request.suspendPolicy,
				__kind, request.id))
			{
				// Write the signal event data
				__kind.write(this, __thread, packet, __args);
				
				// Send it away!
				this.commLink.send(packet);
			}
		}
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
		
		// Otherwise setup a new trip
		Reference<JDWPController> ref = new WeakReference<>(this);
		switch (__t)
		{
			case CLASS_STATUS:
				trip = new __TripClassStatus__(ref);
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
	 * Returns the frame viewer.
	 * 
	 * @return The frame viewer.
	 * @since 2021/04/11
	 */
	public final JDWPViewFrame viewFrame()
	{
		return this.state.view(JDWPViewFrame.class, JDWPViewKind.FRAME);
	}
	
	/**
	 * Returns the object viewer.
	 * 
	 * @return The object viewer.
	 * @since 2021/04/10
	 */
	public final JDWPViewObject viewObject()
	{
		return this.state.view(JDWPViewObject.class, JDWPViewKind.OBJECT);
	}
	
	/**
	 * Returns the thread viewer.
	 * 
	 * @return The thread viewer.
	 * @since 2021/04/10
	 */
	public final JDWPViewThread viewThread()
	{
		return this.state.view(JDWPViewThread.class, JDWPViewKind.THREAD);
	}
	
	/**
	 * Returns the viewer for thread groups.
	 * 
	 * @return The viewer for thread groups.
	 * @since 2021/04/10
	 */
	public final JDWPViewThreadGroup viewThreadGroup()
	{
		return this.state.view(JDWPViewThreadGroup.class,
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
		return this.state.view(JDWPViewType.class,
			JDWPViewKind.TYPE);
	}
	
	/**
	 * Returns all thread groups.
	 * 
	 * @return All thread groups.
	 * @since 2021/04/10
	 */
	final Object[] __allThreadGroups()
	{
		// Get all thread groups
		Object[] groups = this.bind.debuggerThreadGroups();
		
		// Register each one
		JDWPState state = this.state;
		for (Object group : groups)
			state.items.put(group);
		
		return groups;
	}
	
	/**
	 * Returns all threads.
	 * 
	 * @return All threads.
	 * @since 2021/04/10
	 */
	final Object[] __allThreads()
	{
		// Current state
		JDWPState state = this.state;
		
		// All available threads
		ArrayList<Object> allThreads = new ArrayList<>(); 
		
		// Start from the root thread group and get all of the threads
		// under them, since this is a machine to thread linkage
		JDWPViewThreadGroup view = state.view(
			JDWPViewThreadGroup.class, JDWPViewKind.THREAD_GROUP);
		for (Object group : this.bind.debuggerThreadGroups())
		{
			// Register thread group
			state.items.put(group);
			
			// Obtain all threads from this group
			Object[] threads = view.threads(group);
			
			// Register each thread
			for (Object thread : threads)
				state.items.put(thread);
			
			// Store into the list
			allThreads.ensureCapacity(
				allThreads.size() + threads.length);
			allThreads.addAll(Arrays.asList(threads));
		}
		
		return allThreads.toArray(new Object[allThreads.size()]);
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
	JDWPPacket __event(SuspendPolicy __policy, EventKind __kind,
		int __responseId)
		throws NullPointerException
	{
		if (__policy == null || __kind == null)
			throw new NullPointerException("NARG");
		
		JDWPPacket rv = this.commLink.__getPacket(true);
		
		// Composite command code
		rv._commandSet = 64;
		rv._command = 100;
		
		// Is just a normal event
		rv._id = this.__nextId();
		rv._errorCode = ErrorType.NO_ERROR;
		rv._flags = 0;
		
		// Write the single event header
		rv.writeByte(__policy.id);
		rv.writeInt(1);
		rv.writeByte(__kind.id);
		rv.writeInt(__responseId);
		
		return rv;
	}
	
	/**
	 * The next ID number.
	 * 
	 * @return Returns a new ID number.
	 * @since 2021/03/13
	 */
	final int __nextId()
	{
		synchronized (this._nextIdMonitor)
		{
			return ++this._nextId;
		}
	}
	
	/**
	 * Creates a reply packet.
	 * 
	 * @param __id The identifier.
	 * @param __error The error code.
	 * @return The packet used.
	 * @since 2021/03/12
	 */
	final JDWPPacket __reply(int __id, ErrorType __error)
	{
		JDWPPacket rv = this.commLink.__getPacket(true);
		
		rv._id = __id;
		rv._errorCode = __error;
		rv._flags = JDWPPacket.FLAG_REPLY;
		
		return rv;
	}
	
	/**
	 * Returns a value to store data in.
	 * 
	 * @return A value to store data in.
	 * @since 2021/03/19
	 */
	final JDWPValue __value()
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
}
