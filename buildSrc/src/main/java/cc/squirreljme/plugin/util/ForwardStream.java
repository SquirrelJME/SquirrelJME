// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Base class for any stream that forwards.
 *
 * @since 2024/02/04
 */
public abstract class ForwardStream
	implements Closeable, Runnable
{
	/** The stream to read from. */
	protected final Closeable from;
	
	/** The stream to write to. */
	protected final Closeable to;
	
	/** The thread that is doing work, if one exists. */
	private volatile Thread _thread;
	
	/**
	 * Initializes the base forwarder.
	 *
	 * @param __from Where to read from.
	 * @param __to Where to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/04
	 */
	public ForwardStream(Closeable __from, Closeable __to)
		throws NullPointerException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		this.from = __from;
		this.to = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void close()
		throws IOException
	{
		// Interrupt thread so it stops working
		Thread thread = this._thread;
		if (thread != null)
			thread.interrupt();
		
		// Close streams
		this.from.close();
		this.to.close();
	}
	
	/**
	 * Runs the given forwarding stream within its own thread.
	 *
	 * @param __name The name of the thread.
	 * @return The resultant thread.
	 * @throws IllegalStateException If a thread has already been started.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/04
	 */
	public final Thread runThread(String __name)
		throws IllegalStateException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Cannot start the thread twice
		Thread thread = this._thread;
		if (thread != null)
			throw new IllegalStateException("Thread already started.");
		
		// Startup a new thread
		thread = new Thread(this, __name);
		this._thread = thread;
		thread.start();
		
		// Return the resultant thread
		return thread;
	}
}
