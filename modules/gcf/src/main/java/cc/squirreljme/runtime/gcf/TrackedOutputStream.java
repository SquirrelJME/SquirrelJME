// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which keeps track if the connection has been
 * closed or not.
 *
 * @since 2019/05/13
 */
public final class TrackedOutputStream
	extends OutputStream
{
	/** The tracker used. */
	protected final ConnectionStateTracker tracker;
	
	/** The wrapped stream. */
	protected final OutputStream out;
	
	/**
	 * Initializes the tracked stream.
	 *
	 * @param __t The tracker.
	 * @param __out The output.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public TrackedOutputStream(ConnectionStateTracker __t, OutputStream __out)
		throws NullPointerException
	{
		if (__t == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.tracker = __t;
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Always close the output
		this.out.close();
		
		// Set stream as closed
		this.tracker._outclosed = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void flush()
		throws IOException
	{
		/* {@squirreljme.error EC0v The output has been closed.} */
		if (this.tracker._outclosed)
			throw new IOException("EC0v");
		
		// Forward
		this.out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		/* {@squirreljme.error EC0w The output has been closed.} */
		if (this.tracker._outclosed)
			throw new IOException("EC0w");
		
		// Forward
		this.out.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void write(byte[] __b)
		throws IOException, NullPointerException
	{
		/* {@squirreljme.error EC0x The output has been closed.} */
		if (this.tracker._outclosed)
			throw new IOException("EC0x");
		
		// Forward
		this.out.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		/* {@squirreljme.error EC0y The output has been closed.} */
		if (this.tracker._outclosed)
			throw new IOException("EC0y");
		
		// Forward
		this.out.write(__b, __o, __l);
	}
}

