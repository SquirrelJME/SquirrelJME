// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datasink;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which when given bytes, writes them to the given
 * data sink.
 *
 * {@squirreljme.error AA0b Failed to write to the sink.}
 *
 * @since 2016/04/30
 */
public class DataSinkOutputStream
	extends OutputStream
{
	/** Internal lock. */
	protected final Object lock;
	
	/** The output sink. */
	protected final DataSink sink;
	
	/** Already has been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the output stream to the given data sink.
	 *
	 * @param __ds The data sink to write bytes to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public DataSinkOutputStream(DataSink __ds)
		throws NullPointerException
	{
		// Check
		if (__ds == null)
			throw new NullPointerException("NARG");
		
		// Set
		sink = __ds;
		lock = __ds._lock;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public void close()
		throws IOException
	{
		try
		{
			// Prevent race condition during close
			synchronized (lock)
			{
				// Set as complete
				boolean wascl = _closed;
				_closed = true;
			
				// If closed already, do nothing
				if (wascl)
					return;
				
				// Set as complee
				sink.setComplete();
			
				// Force all bytes to complete
				while (sink.waiting() > 0)
				{
					// Flush as many bytes as possible
					sink.flush();
				
					// Let other threads get a chance
					Thread.yield();
				}
			}
		}
		
		// Failed to close
		catch (CompleteSinkException|SinkProcessException e)
		{
			// {@squirreljme.error AA08 Caught sink error while closing the
			// output stream.}
			throw new IOException("AA08", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Check for close
			__checkClose();
		
			// Could fail
			try
			{
				sink.flush();
			}
		
			// Failed to flush
			catch (CompleteSinkException|SinkProcessException e)
			{
				// {@squirreljme.error AA0a Could not flush the stream}
				throw new IOException("AA0a", e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Check for close
			__checkClose();
		
			// Offer single byte
			try
			{
				sink.offer((byte)__b);
			}
		
			// Write failed
			catch (CompleteSinkException|SinkProcessException e)
			{
				throw new IOException("AA0b", e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Check for close
			__checkClose();
		
			// Offer multiple bytes
			try
			{
				sink.offer(__b, __o, __l);
			}
		
			// Write failed
			catch (CompleteSinkException|SinkProcessException e)
			{
				throw new IOException("AA0b", e);
			}
		}
	}
	
	/**
	 * Checks if the stream was closed.
	 *
	 * @throws IOException If it was closed.
	 * @since 2016/04/30
	 */
	private final void __checkClose()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AA09 Stream has been closed.}
			if (_closed)
				throw new IOException("AA09");
		}
	}
}

