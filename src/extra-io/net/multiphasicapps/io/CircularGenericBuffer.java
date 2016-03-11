// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This provide a base circular buffer class which is extended by subclasses
 * which handle the given types as required.
 *
 * @param <T> The type of buffer to use.
 * @param <E> The erased type.
 * @since 2016/03/11
 */
public abstract class CircularGenericBuffer<T, E>
{
	/** Initial buffer size. */
	protected static final int INITIAL_SIZE =
		8;	
	
	/** Lock. */
	protected final Object lock;
	
	/** The internal buffer. */
	private volatile T _buffer;
	
	/** Head of the buffer. */
	private volatile int _head =
		-1;
	
	/** Tail of the buffer. */
	private volatile int _tail =
		-1;
	
	/**
	 * Initializes the base generic buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer()
	{
		this(null);
	}
	
	/**
	 * Initializes the base generic buffer with the given lock.
	 *
	 * @param __lock The locking object to use, if {@code null} then one is
	 * created.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer(Object __lock)
	{
		lock = (__lock != null ? __lock : new Object());
	}
}

