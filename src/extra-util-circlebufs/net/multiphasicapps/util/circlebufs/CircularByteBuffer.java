// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.circlebufs;

import java.util.NoSuchElementException;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This is a circular buffer which provides bytes for input and output as a
 * queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * @since 2016/03/11
 */
public class CircularByteBuffer
{
	/** The lock to use. */
	protected final Object lock;
	
	/** The buffer to base off. */
	protected final DynamicByteBuffer base =
		new DynamicByteBuffer();
	
	/**
	 * Initializes a circular byte buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularByteBuffer()
	{
		this(new Object());
	}
	
	/**
	 * Initializes a circular byte buffer with the given lock object.
	 *
	 * @param __lock The lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer(Object __lock)
		throws NullPointerException
	{
		// Check
		if (__lock == null)
			throw new NullPointerException("NARG");
		
		// Set
		lock = __lock;
	}
}

