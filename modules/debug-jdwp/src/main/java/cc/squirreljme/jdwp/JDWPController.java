// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	protected final JDWPState state =
		new JDWPState();
		
	/** The event manager. */
	protected final EventManager eventManager =
		new EventManager();
	
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
	 * Updates the state as needed for debugging.
	 * 
	 * @param __what What gets updated?
	 * @return The debugger state.
	 * @since 2021/03/13
	 */
	public JDWPState debuggerUpdate(JDWPUpdateWhat... __what)
	{
		JDWPState state = this.state;
		
		this.bind.debuggerUpdate(state, __what);
		
		return state;
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
				
				// Debug
				Debugging.debugNote("JDWP: -> %s", result);
				
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
		try (JDWPController self = this)
		{
			CommLink commLink = this.commLink;
			while (!commLink._shutdown)
				this.poll();
		}
	}
	
	/**
	 * Signals that the given thread has started.
	 * 
	 * @param __thread The thread that started.
	 * @param __started Was this thread started?
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public void signalThreadState(JDWPThread __thread, boolean __started)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Register this thread for later use
		this.state.threads.put(__thread);
		
		// Which event is being sent?
		EventKind kind = (__started ? EventKind.THREAD_START :
			EventKind.THREAD_DEATH);
		
		// Send if requested
		EventRequest request = this.eventManager.get(kind,
			(__started ? null :
			new ThreadModifierMatcher(__thread)));
		if (request != null)
			try (JDWPPacket event = this.__event())
			{
				// No threads got suspended
				event.writeByte(SuspendPolicy.NONE.id);
				
				// Only a single event
				event.writeInt(1);
				
				// Signal of thread start
				event.writeByte(kind.id);
				event.writeInt(request.id);
				event.writeId(__thread);
				
				// Send it away!
				this.commLink.send(event);
			}
	}
	
	/**
	 * Signals that the thread suspend.
	 * 
	 * @param __thread The thread to be suspended or resumed.
	 * @param __suspend If the thread is to be suspended.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public void signalThreadSuspend(JDWPThread __thread, boolean __suspend)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Nothing needs to be done here...
	}
	
	/**
	 * Creates an event packet.
	 * 
	 * @return The event packet.
	 * @since 2021/03/14
	 */
	private JDWPPacket __event()
	{
		JDWPPacket rv = this.commLink.__getPacket(true);
		
		// Composite command code
		rv._commandSet = 64;
		rv._command = 100;
		
		// Is just a normal event
		rv._id = this.__nextId();
		rv._errorCode = ErrorType.NO_ERROR;
		rv._flags = 0;
		
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
		synchronized (this)
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
	JDWPPacket __reply(int __id, ErrorType __error)
	{
		JDWPPacket rv = this.commLink.__getPacket(true);
		
		rv._id = __id;
		rv._errorCode = __error;
		rv._flags = JDWPPacket.FLAG_REPLY;
		
		return rv;
	}
}
