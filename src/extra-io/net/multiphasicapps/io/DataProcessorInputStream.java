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

import java.io.InputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This is {@link InputStream} which uses a given {@link DataProcessor} with
 * bytes that are read from a wrapped stream to provide output for usage.
 *
 * @since 2016/03/11
 */
public class DataProcessorInputStream
	extends InputStream
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Input source. */
	protected final InputStream in;
	
	/** The data processor which handles input byte sources. */
	protected final DataProcessor processor;
	
	/** Threw some kind of exception? */
	private volatile boolean _threwexception;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the pipe from the given input stream to the given processor.
	 *
	 * @param __in Input stream to read source bytes from.
	 * @param __dp The data processor for handling data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public DataProcessorInputStream(InputStream __in, DataProcessor __dp)
		throws NullPointerException
	{
		// Check
		if (__in == null || __dp == null)
			throw new NullPointerException();
		
		// Set
		in = __in;
		processor = __dp;
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
			// Failed so just stop
			if (_threwexception)
				throw new IOException();
			
			// If closed then read nothing
			if (_closed)
				return -1;
			
			// Also lock on the processor lock also to prevent from using the
			// processor while this input stream is running.
			synchronized (processor._lock)
			{
				// Could fail
				try
				{
					for (;;)
					{
						// Attempt to grab bytes from the output
						try
						{
							// Try to grab a byte from the output
							byte rv = processor.remove();
					
							// If one was grabbed then return it
							return ((int)rv) & 0xFF;
						}
				
						// There are no bytes waiting
						catch (NoSuchElementException nsee)
						{
						}
				
						// If the processor is waiting then give it some bytes
						if (processor.isWaiting())
						{
							// Read input byte
							int val = in.read();
						
							// EOF? then finish
							if (val < 0)
								processor.finish();
						
							// Otherwise offer it
							else
								processor.offer((byte)val);
						}
					
						// Otherwise if no more output bytes are available,
						// then end it
						else if (!processor.hasRemainingOutput())
							return -1;
					}
				}
					
				// Read/write/process error
				catch (IOException ioe)
				{
					// Set failure state
					_threwexception = true;
				
					// Rethrow it
					throw ioe;
				}
			}
		}
	}
}

