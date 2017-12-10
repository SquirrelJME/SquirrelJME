// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.gcf;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelMailBoxException;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemMailBoxConnection;

/**
 * This wraps the mailbox datagram connection for input.
 *
 * @since 2016/10/13
 */
class __IMCInputStream__
	extends InputStream
{
	/** Interrupt read operations? */
	protected final boolean interrupt;
	
	/** The mailbox descriptor. */
	private final SystemMailBoxConnection _fd;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** The working buffer. */
	private volatile byte[] _work =
		new byte[__IMCOutputStream__._BUFFER_SIZE];
	
	/** The current read position. */
	private volatile int _at;
	
	/** The length of the current read. */
	private volatile int _end;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/** Was the EOF reached? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __fd The descriptor to read from.
	 * @param __int Are interrupts to be generated?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	__IMCInputStream__(SystemMailBoxConnection __fd, boolean __int)
		throws NullPointerException
	{
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		this._fd = __fd;
		this.interrupt = __int;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
		throws IOException
	{
		// Only close once
		if (this._closed)
			return;
		this._closed = true;
		
		// Close it
		try
		{
			this._fd.close();
		}
		
		// {@squirreljme.error EC0n Could not close the mailbox for the
		// input stream. (The descriptor)}
		catch (KernelMailBoxException e)
		{
			throw new IOException(String.format("EC0n %d", this._fd), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public int read()
		throws IOException
	{
		// Need to read a single byte always
		for (;;)
		{
			// Read single byte
			byte[] solo = this._solo;
			int rc = read(solo, 0, 1);
		
			// EOF?
			if (rc < 0)
				return -1;
			
			// If a single byte was read, then use it
			if (rc == 1)
				return solo[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = (__o + __l);
		if (__o < 0 || __l < 0 || n > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// EOF reached, stop
		if (this._eof)
			return -1;
		
		// Initial arguments
		byte[] work = this._work;
		int at = this._at, end = this._end;
		SystemMailBoxConnection fd = this._fd;
		boolean interrupt = this.interrupt;
		int[] chan = null;
		
		// Try constantly filling the buffer
		try
		{
			// Try filling the buffer
			int count = 0;
			for (int i = 0; i < n; i++)
			{
				// Need to read in a new datagram?
				if (at >= end)
				{
					// Allocate channel if it is missing
					if (chan == null)
						chan = new int[]{0};
					
					// Keep trying
					for (;;)
						try
						{
							// Read in datagram
							int rc = fd.receive(chan,
								work, 0, work.length, true);
							
							// EOF? Return read bytes or EOF
							if (rc < 0)
							{
								this._eof = true;
								return (count == 0 ? -1 : count);
							}
							
							// Read success, use the buffer data
							end = rc;
							this._end = end;
							
							// Reset read position
							at = 0;
							
							// Stop trying
							break;
						}
						
						// {@squirreljme.error EC0o Could not read from the
						// remote destination. (The descriptor)}
						catch (KernelMailBoxException e)
						{
							throw new IOException(String.format("EC0o %d", fd),
								e);
						}
						
						// The work buffer is too small
						catch (ArrayStoreException e)
						{
							work = new byte[Math.max(work.length, chan[0])];
							this._work = work;
						}
						
						// No datagrams available for read
						// If interrupted, just stop reading and return the
						// number of read bytes
						catch (InterruptedException|NoSuchElementException e)
						{
							// {@squirreljme.error EC0p Read interrupt and no
							// data was read.}
							if (interrupt && count == 0)
								throw new InterruptedIOException("EC0p");
							return count;
						}
				}
				
				// Copy single byte
				__b[i] = work[at++];
				count++;
			}
			
			// Return the number of requested bytes
			return n;
		}
		
		// At position may have changed, update it always
		finally
		{
			this._at = at;
		}
	}
}

