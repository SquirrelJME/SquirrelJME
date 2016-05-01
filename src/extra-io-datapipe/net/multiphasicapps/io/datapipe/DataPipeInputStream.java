// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datapipe;

import java.io.InputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import net.multiphasicapps.io.datafaucet.FaucetProcessException;
import net.multiphasicapps.io.datasink.SinkProcessException;

/**
 * This is {@link InputStream} which uses a given {@link DataPipe} with
 * bytes that are read from a wrapped stream to provide output for usage.
 *
 * @since 2016/03/11
 */
public class DataPipeInputStream
	extends InputStream
{
	/** Lock. */
	protected final Object lock;
	
	/** Input source. */
	protected final InputStream in;
	
	/** The data processor which handles input byte sources. */
	protected final DataPipe processor;
	
	/** Threw some kind of exception? */
	private volatile boolean _failed;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/** Finished? */
	private volatile boolean _done;
	
	/**
	 * Initializes the pipe from the given input stream to the given processor.
	 *
	 * @param __in Input stream to read source bytes from.
	 * @param __dp The data processor for handling data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public DataPipeInputStream(InputStream __in, DataPipe __dp)
		throws NullPointerException
	{
		// Check
		if (__in == null || __dp == null)
			throw new NullPointerException("NARG");
		
		// Set
		in = __in;
		processor = __dp;
		lock = __dp._lock;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Set closed
			_closed = true;
			
			// Close the wrapped stream
			in.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AC04 Previous read failed.}
			if (_failed)
				throw new IOException("AC04");
			
			// If closed then read nothing
			if (_closed)
				return -1;
			
			// Constantly read input
			for (;;)
			{
				// Try to read bytes from the output
				try
				{
					int rv = processor.drain();
					
					// Nothing left?
					if (rv < 0)
						return -1;
					
					// Return it
					return rv;
				}
				
				// Failed pipe read
				catch (PipeProcessException|FaucetProcessException|
					SinkProcessException e)
				{
					// Failed read
					_failed = true; 
					
					// {@squirreljme.error AC05 Failed to drain from the pipe.}
					throw new IOException("AC05", e);
				}
				
				// Output stalled, add more bytes to the input
				catch (PipeStalledException e)
				{
					// If done, not going to read anything
					if (_done)
						continue;
					
					// Setup buffer
					int ADD = 32;
					byte[] bb = new byte[ADD];
					
					// Read some input
					int rc;
					try
					{
						rc = in.read(bb);
					}
					
					// Failed to read some input
					catch (IOException f)
					{
						// Mark it
						_failed = true;
						
						// Rethrow
						throw f;
					}
					
					// EOF reached?
					if (rc < 0)
					{
						// Mark done and complete the input
						processor.completeInput();
						_done = true;
						
						// Try draining
						continue;
					}
					
					// Add to the input queue
					processor.offer(bb, 0, rc);
				}
			}
		}
	}
}

