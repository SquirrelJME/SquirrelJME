// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is an input which tracks if it has been closed, if EOF is read from
 * a stream then it will automatically be closed.
 *
 * @since 2019/05/13
 */
public final class TrackedInputStream
	extends InputStream
{
	/** The tracker used. */
	protected final ConnectionStateTracker tracker;
	
	/** The wrapped stream. */
	protected final InputStream in;
	
	/**
	 * Initializes the tracked stream.
	 *
	 * @param __t The tracker.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public TrackedInputStream(ConnectionStateTracker __t, InputStream __in)
		throws NullPointerException
	{
		if (__t == null || __in == null)
			throw new NullPointerException("NARG");
		
		this.tracker = __t;
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int available()
		throws IOException
	{
		// {@squirreljme.error EC0r The input has been closed.}
		if (this.tracker._inclosed)
			throw new IOException("EC0r");
		
		return this.in.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Always close the input
		this.in.close();
		
		// Set stream as closed
		this.tracker._inclosed = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int read()
		throws IOException
	{
		// {@squirreljme.error EC0p The input has been closed.}
		if (this.tracker._inclosed)
			throw new IOException("EC0p");
		
		// Read data
		int rv = this.in.read();
		if (rv < 0)
			this.close();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int read(byte[] __b)
		throws IOException
	{
		// {@squirreljme.error EC0q The input has been closed.}
		if (this.tracker._inclosed)
			throw new IOException("EC0q");
		
		// Read data
		int rv = this.in.read(__b);
		if (rv < 0)
			this.close();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		// {@squirreljme.error EC0q The input has been closed.}
		if (this.tracker._inclosed)
			throw new IOException("EC0q");
		
		// Read data
		int rv = this.in.read(__b, __o, __l);
		if (rv < 0)
			this.close();
		return rv;
	}
}

