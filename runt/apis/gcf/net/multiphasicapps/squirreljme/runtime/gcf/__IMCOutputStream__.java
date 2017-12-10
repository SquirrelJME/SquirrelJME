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

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelMailBoxException;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemMailBoxConnection;

/**
 * This wraps the mailbox datagram connection for output.
 *
 * @since 2016/10/13
 */
class __IMCOutputStream__
	extends OutputStream
{
	/** The buffer size for IMC connections. */
	static final int _BUFFER_SIZE =
		256;
	
	/** The mailbox descriptor. */
	private final SystemMailBoxConnection _fd;
	
	/** IMC buffer size. */
	private final byte[] _buffer =
		new byte[_BUFFER_SIZE];
	
	/** Current buffer position. */
	private volatile int _at;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __fd The descriptor to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	__IMCOutputStream__(SystemMailBoxConnection __fd)
		throws NullPointerException
	{
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		this._fd = __fd;
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
		
		// Flush before close
		flush();
		this._closed = true;
		
		// Close it
		try
		{
			this._fd.close();
		}
		
		// {@squirreljme.error EC0q Could not close the mailbox for the output
		// stream. (The descriptor)}
		catch (KernelMailBoxException e)
		{
			throw new IOException(String.format("EC0q %d", this._fd), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void flush()
		throws IOException
	{
		// If closed, do nothing
		if (this._closed)
			return;
		
		// Are there bytes to be flushed
		int at = this._at;
		if (at > 0)
		{
			// Send buffer to mailbox
			byte[] buffer = this._buffer;
			try
			{
				this._fd.send(0, buffer, 0, at);
			}
			
			// {@squirreljme.error EC0r Could not flush the output mailbox.
			// (The descriptor)}
			catch (KernelMailBoxException e)
			{
				throw new IOException(String.format("EC0r %d", this._fd), e);
			}
			
			// Clear
			this._at = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// {@squirreljme.error EC0s Cannot write single byte after close.}
		if (this._closed)
			throw new IOException("EC0s");
		
		// May need to flush depending on the new position
		int at = this._at;
		
		// Write single byte
		this._buffer[at++] = (byte)__b;
		
		// Flush?
		if (at >= _BUFFER_SIZE)
			flush();
		else
			this._at = at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// {@squirreljme.error EC0t Cannot write multiple bytes after close.}
		if (this._closed)
			throw new IOException("EC0t");
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// Write all bytes into the buffer
		int at = this._at, end = __o + __l;
		byte[] buffer = this._buffer;
		for (int i = __o; i < end; i++)
		{
			// Write byte to position
			buffer[at++] = __b[i];
			
			// Buffer size reached?
			if (at >= _BUFFER_SIZE)
			{
				flush();
				at = 0;
			}
		}
		
		// Set new location
		this._at = at;
	}
}

