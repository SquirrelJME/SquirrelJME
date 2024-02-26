// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.OutputStream;

/**
 * An output stream which appends to a byte deque, the locking is done on
 * the byte deque itself.
 *
 * @see ByteDequeInputStream
 * @since 2024/01/19
 */
public class ByteDequeOutputStream
	extends OutputStream
{
	/** The byte deque to access. */
	protected final ByteDeque queue;
	
	/**
	 * Initializes the byte deque stream.
	 *
	 * @param __queue The queue to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public ByteDequeOutputStream(ByteDeque __queue)
		throws NullPointerException
	{
		if (__queue == null)
			throw new NullPointerException("NARG");
		
		this.queue = __queue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void flush()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void close()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void write(int __b)
	{
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			// Append the data
			queue.addLast((byte)__b);
			
			// Signal the other side that data exists
			queue.notifyAll();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void write(byte[] __b)
	{
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			// Append the data
			queue.addLast(__b);
			
			// Signal the other side that data exists
			queue.notifyAll();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
	{
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			// Append the data
			queue.addLast(__b, __o, __l);
			
			// Signal the other side that data exists
			queue.notifyAll();
		}
	}
}
