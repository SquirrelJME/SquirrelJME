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
 * @since 2016/05/15
 */
public class EventQueue
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The event queue. */
	private volatile int[] _queue;
	
	/** The read position in the queue. */
	private volatile int _read;
	
	/** The write position in the queue. */
	private volatile int _write;
}

