// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.event;

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
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The event queue. */
	private volatile int[] _queue;
	
	/** The read position in the queue. */
	private volatile int _read;
	
	/** The write position in the queue. */
	private volatile int _write;
	
	/**
	 * Returns the next raw event in the queue.
	 *
	 * @return The next raw event.
	 * @since 2016/05/15
	 */
	public int nextRaw()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Submits a raw event.
	 *
	 * @param __r The raw event to post.
	 * @since 2016/05/15
	 */
	public void postRaw(int __r)
	{
		System.err.printf("%08x%n", __r);
		//throw new Error("TODO");
	}
	
	/**
	 * Posts a key pressed event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @since 2016/05/15
	 */
	public void postKeyPressed(int __port, char __v)
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		postRaw((EventKind.KEY_PRESSED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
	
	/**
	 * Posts a key release event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @since 2016/05/15
	 */
	public void postKeyReleased(int __port, char __v)
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		postRaw((EventKind.KEY_RELEASED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
	
	/**
	 * Posts a key typed event.
	 *
	 * @param __port The controller port.
	 * @param __v The key which was pressed, if the value is {@code 0} then the
	 * event is not posted.
	 * @since 2016/05/15
	 */
	public void postKeyTyped(int __port, char __v)
	{
		// Do not post?
		if (__v == 0)
			return;
		
		// Post
		postRaw((EventKind.KEY_TYPED.ordinal() << EVENT_KIND_SHIFT) |
			((__port & CONTROLLER_PORT_VALUE_MASK) << CONTROLLER_PORT_SHIFT) |
			((__v & KEY_CHARACTER_VALUE_MASK) << KEY_CHARACTER_SHIFT));
	}
}

