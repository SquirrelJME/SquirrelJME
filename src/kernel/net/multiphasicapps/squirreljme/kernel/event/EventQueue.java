// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.event;

import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is the event queue.
 *
 * For reduced memory, the internal event loop is represented by integers which
 * store the event data. If the queue size is exceeded then it is attempted to
 * be increased in size, otherwise if that fails then events are dropped.
 *
 * All events are 32-bit integers so that they keep space reduced and do not
 * require a large multiple of objects to be allocated which may definitely
 * strain low memory/speed systems especially when garbage is collected.
 *
 * The upper-most 4 bits are the event kind, {@code 0xFFFF} is reserved for
 * special handling.
 *
 * @since 2016/05/15
 */
public class EventQueue
{
	/** Initial event queue size. */
	public static final int INITIAL_SIZE =
		8;
	
	/** The event kind shifted mask. */
	public static final int EVENT_KIND_SHIFT_MASK =
		0b1111_0000__0000_0000__0000_0000__0000_0000;
	
	/** The event kind value mask. */
	public static final int EVENT_KIND_VALUE_MASK =
		0b1111;
	
	/** The event kind shift. */
	public static final int EVENT_KIND_SHIFT =
		28;
	
	/** The controller port shifted mask. */
	public static final int CONTROLLER_PORT_SHIFT_MASK =
		0b0000_1100__0000_0000__0000_0000__0000_0000;
	
	/** The controller port value mask. */
	public static final int CONTROLLER_PORT_VALUE_MASK =
		0b11;
	
	/** The controller port shift. */
	public static final int CONTROLLER_PORT_SHIFT =
		26;
	
	/** The key character shifted mask. */
	public static final int KEY_CHARACTER_SHIFT_MASK =
		0b0000_0000__0000_0000__1111_1111__1111_1111;
	
	/** The key character value mask. */
	public static final int KEY_CHARACTER_VALUE_MASK =
		0b1111_1111__1111_1111;
	
	/** The key character shift. */
	public static final int KEY_CHARACTER_SHIFT =
		0;
	
	/** No event array. */
	private static final EventHandler[] _NO_EVENTS =
		new EventHandler[0];
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The owning process. */
	protected final KernelProcess owner;
	
	/** Is this attached to a process? */
	protected final boolean attached;
	
	/** The event queue. */
	private volatile int[] _queue;
	
	/** The read position in the queue. */
	private volatile int _read;
	
	/** The write position in the queue. */
	private volatile int _write;
	
	/**
	 * Initializes an event queue which is not attached to a process.
	 *
	 * @since 2016/05/16
	 */
	public EventQueue()
	{
		owner = null;
		attached = false;
	}
	
	/**
	 * Initializes the event queue for the given process.
	 *
	 * @param __kp The process which owns the event queue
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	public EventQueue(KernelProcess __kp)
		throws NullPointerException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __kp;
		attached = true;
	}
	
	/**
	 * Adds an event to the queue and throws an exception if it was not
	 * added.
	 *
	 * @param __r The event to add.
	 * @throws IllegalStateException If the event was not added.
	 * @throws SecurityException If the current thread is not permitted to
	 * write events to this queue.
	 * @since 2016/05/17
	 */
	public final void addRaw(int __r)
		throws IllegalStateException, SecurityException
	{
		// {@squirreljme.error AY0a Could not add the raw event.}
		if (!offerRaw(__r))
			throw new IllegalStateException("AY0a");
	}
	
	/**
	 * Obtains the next event but does not remove it, an exception is thrown
	 * if there is no event available.
	 *
	 * @return The next event.
	 * @throws NoSuchElementException If there are no events.
	 * @throws SecurityException If the current thread cannot peek events.
	 * @since 2016/05/17
	 */
	public final int getRaw()
		throws NoSuchElementException, SecurityException
	{
		// Get
		int rv = peekRaw();
		
		// {@squirreljme.error AY08 No events available to peek.}
		if (rv == 0)
			throw new NoSuchElementException("AY08");
		
		// Return it
		return rv;
	}
	
	/**
	 * Parses input events and passes them to the given event handlers.
	 *
	 * @param __eh Event handlers which are used to handle events with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/15
	 */
	public final void handleEvents(EventHandler __eh)
		throws NullPointerException
	{
		handleEvents(__eh, _NO_EVENTS);
	}
	
	/**
	 * Parses input events and passes them to the given event handlers.
	 *
	 * @param __eh Event handlers which are used to handle events with.
	 * @param __rest The remaining event handlers which may get events.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If reading events is not permitted.
	 * @since 2016/05/15
	 */
	public final void handleEvents(EventHandler __eh, EventHandler... __rest)
		throws NullPointerException, SecurityException
	{
		// Check
		if (__eh == null || __rest == null)
			throw new NullPointerException("NARG");
		
		// Count handlers
		int nh = __rest.length;
		
		// Event handling loop
		for (;;)
		{
			// Get next event
			int v = pollRaw();
			
			// No more events?
			if (v == 0)
				return;
			
			// Go through all handlers, start at -1 for first argument
			for (int i = -1; i < nh; i++)
			{
				// Get handler
				EventHandler eh = (i < 0 ? __eh : __rest[i]);
				
				throw new Error("TODO");
			}
		}
	}
	
	/**
	 * Posts a key pressed event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @throws SecurityException If events cannot be posted.
	 * @since 2016/05/15
	 */
	public final void offerKeyPressed(int __port, char __v)
		throws SecurityException
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		offerRaw((EventKind.KEY_PRESSED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
	
	/**
	 * Posts a key release event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @throws SecurityException If events cannot be posted.
	 * @since 2016/05/15
	 */
	public final void offerKeyReleased(int __port, char __v)
		throws SecurityException
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		offerRaw((EventKind.KEY_RELEASED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
	
	/**
	 * Posts a key typed event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @throws SecurityException If events cannot be posted.
	 * @since 2016/05/15
	 */
	public final void offerKeyTyped(int __port, char __v)
		throws SecurityException
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		offerRaw((EventKind.KEY_TYPED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
	
	/**
	 * Submits a raw event. The event
	 *
	 * @param __r The raw event to post, the value of {@code 0} is never
	 * @return {@code true} if the event was posted, otherwise {@code false}
	 * will be returned if for example there is not enough memory remaining or
	 * the event is not valid.
	 * posted (because that would cause all event handling to halt).
	 * @throws SecurityException If posting threads is denied.
	 * @since 2016/05/15
	 */
	public boolean offerRaw(int __r)
		throws SecurityException
	{
		// Must be permitted
		if (this.attached)
			owner.permissions().eventWrite(this);
		
		// Can never be zero
		if (__r == 0)
			return false;
		
		// Lock
		synchronized (lock)
		{
			// Get the queue
			int[] queue = this._queue;
			
			// Need to allocate?
			if (queue == null)
				try
				{
					queue = new int[INITIAL_SIZE];
					this._queue = queue;
				}
			
				// Drop the event
				catch (OutOfMemoryError e)
				{
					return false;
				}
			
			// Get the positions
			int n = queue.length;
			int read = this._read;
			int write = this._write;
			
			// Calculate the next write position
			int next = write + 1;
			if (next >= n)
				next = 0;
			
			// No room in the queue?
			if (next == read)
				try
				{
					// Allocate new array, add double elements
					int nl = n << 1;
					
					// The new size overflows, drop event
					if (nl <= 0)
						return false;
					
					// Allocate
					int[] into = new int[nl];
					
					// Copy items into it
					write = 0;
					for (int copy = pollRaw(); copy != 0; copy = pollRaw())
						into[write++] = copy;
					
					// Set new details
					read = 0;
					this._read = read;
					this._write = write;
					
					// Use new queue
					queue = into;
					this._queue = queue;
					
					// The next write position
					next = write + 1;
				}
				
				// Drop the event
				catch (OutOfMemoryError e)
				{
					return false;
				}
			
			// Write in the queue
			queue[write] = __r;
			
			// Set next write
			this._write = next;
		}
		
		// Posted
		return true;
	}
	
	/**
	 * Peeks the next raw event in the queue.
	 *
	 * @return The next raw event, or {@code 0} if there none remaining.
	 * @throws SecurityException If event peeking is denied.
	 * @since 2016/05/15
	 */
	public int peekRaw()
		throws SecurityException
	{
		// Must be permitted
		if (this.attached)
			owner.permissions().eventRead(this);
		
		// Lock
		synchronized (lock)
		{
			// Obtain the queue details
			int[] queue = this._queue;
			
			// No queue?
			if (queue == null)
				return 0;
			
			// Get read/write positions
			int n = queue.length;
			int read = this._read;
			int write = this._write;
			
			// If the read head is at the write head then there is no data
			// in the buffer
			if (read == write)
				return 0;
			
			// Return it
			return queue[read];
		}
	}
	
	/**
	 * Returns the next raw event in the queue.
	 *
	 * @return The next raw event, or {@code 0} if there none remaining.
	 * @throws SecurityException If event removal is denied.
	 * @since 2016/05/15
	 */
	public int pollRaw()
		throws SecurityException
	{
		// Must be permitted
		if (this.attached)
			owner.permissions().eventRead(this);
		
		// Lock
		synchronized (lock)
		{
			// Obtain the queue details
			int[] queue = this._queue;
			
			// No queue?
			if (queue == null)
				return 0;
			
			// Get read/write positions
			int n = queue.length;
			int read = this._read;
			int write = this._write;
			
			// If the read head is at the write head then there is no data
			// in the buffer
			if (read == write)
				return 0;
			
			// Read value
			int rv = queue[read];
			
			// Clear value
			queue[read] = 0;
			
			// Increment
			int next = read + 1;
			if (next >= n)
				next = 0;
			_read = next;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Removes the next event waiting in the queue, an exception is thrown
	 * if there are no events in the queue.
	 *
	 * @return The raw event.
	 * @throws NoSuchElementException If there are no available events.
	 * @throws SecurityException If events are not permitted to be removed.
	 * @since 2016/05/17
	 */
	public final int removeRaw()
		throws NoSuchElementException, SecurityException
	{
		// Remove event
		int rv = pollRaw();
		
		// {@squirreljme.error AY09 No event was polled.}
		if (rv == 0)
			throw new NoSuchElementException("AY09");
		
		// Return it
		return rv;
	}
}

