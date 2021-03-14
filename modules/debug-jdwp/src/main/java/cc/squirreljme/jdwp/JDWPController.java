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
import java.io.IOException;
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
	implements Closeable
{
	/** The binding, which is called to perform any actions. */
	protected final JDWPBinding bind;
	
	/** The communication link. */
	protected final CommLink commLink;
	
	/** The thread containing the communication link. */
	protected final Thread commLinkThread;
	
	/** Debugger state. */
	protected final JDWPState state =
		new JDWPState();
	
	/** Event mappings by Kind. */
	private final Map<EventKind, List<EventRequest>> _eventByKind =
		new EnumTypeMap<EventKind, List<EventRequest>>(
			EventKind.class, EventKind.values());
	
	/** Event mapping by Id. */
	private final Map<Integer, EventRequest> _eventById =
		new LinkedHashMap<>();
	
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
		
		CommLink commLink = new CommLink(__in, __out);
		this.commLink = commLink;
		
		// Setup Communication Link thread
		Thread thread = new Thread(commLink, "SquirrelJME-JDWPCommLink");
		thread.start();
		
		this.commLinkThread = thread;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/08
	 */
	@Override
	public void close()
		throws IOException
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
	 * Adds an event request for later event handling.
	 * 
	 * @param __request The request to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	void __addEventRequest(EventRequest __request)
		throws NullPointerException
	{
		if (__request == null)
			throw new NullPointerException("NARG");
		
		Map<EventKind, List<EventRequest>> eventByKind = this._eventByKind;
		synchronized (this)
		{
			// Get list of the event
			List<EventRequest> list = eventByKind.get(__request.eventKind);
			if (list == null)
				eventByKind.put(__request.eventKind,
					(list = new LinkedList<>()));
			
			// Map events
			list.add(__request);
			this._eventById.put(__request.debuggerId(), __request);
		}
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
