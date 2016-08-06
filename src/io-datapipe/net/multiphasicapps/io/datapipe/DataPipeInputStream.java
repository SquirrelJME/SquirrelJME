// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datapipe;

import java.io.Flushable;
import java.io.InputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import net.multiphasicapps.io.datafaucet.FaucetProcessException;
import net.multiphasicapps.io.datasink.SinkProcessException;

/**
 * This is {@link InputStream} which uses a given {@link DataPipe} with
 * bytes that are read from a wrapped stream to provide output for usage.
 *
 * This class is not thread safe.
 *
 * @since 2016/03/11
 */
public class DataPipeInputStream
	extends InputStream
	implements Flushable
{
	/**
	 * {@squirreljme.property net.multiphasicapps.io.datapipe.drainsize=n
	 * When there is not enough data within the data pipe, then these many
	 * bytes are attempted to be processed and placed into the output
	 * processor.}
	 */
	private static final int _DRAIN_SIZE =
		Math.max(8, Integer.getInteger(
			"net.multiphasicapps.io.datapipe.drainsize", 512));
	
	/** Input source. */
	protected final InputStream in;
	
	/** The data processor which handles input byte sources. */
	protected final DataPipe processor;
	
	/** Single byte operation. */
	private final byte[] _solo =
		new byte[1];
	
	/** Drain array (so every drain does not new an array). */
	private final byte[] _drain =
		new byte[_DRAIN_SIZE];
	
	/** Threw some kind of exception? */
	private volatile boolean _failed;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/** Finished? */
	private volatile boolean _done;
	
	/** Suppressed failing exception. */
	private volatile Throwable _suppressed;
	
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
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public void close()
		throws IOException
	{
		// Set closed
		_closed = true;
		
		// Close the wrapped stream
		in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/16
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Flush the pipe
		this.processor.flush();
		
		// If the input stream supports, flushing then flush it
		InputStream in = this.in;
		if (in instanceof Flushable)
			((Flushable)in).flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public int read()
		throws IOException
	{
		// Constantly try reading a single byte
		byte[] solo = this._solo;
		for (;;)
		{
			int rv = this.read(solo, 0, 1);
			
			// EOF?
			if (rv < 0)
				return -1;
			
			// Read a byte, return it
			if (rv == 1)
				break;
		}
		
		// Return the read value
		return (solo[0] & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/05
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("BAOB");
		
		// {@squirreljme.error AC04 Previous read failed.}
		if (_failed)
			throw new IOException("AC04", _suppressed);
		
		// If closed then read nothing
		if (_closed)
			return -1;
		
		// Could fail
		try
		{
			DataPipe processor = this.processor;
			
			// Keep trying to drain bytes
			int at = __o;
			int rt = 0;
			for (;;)
			{
				//Drain bytes
				int dc = processor.drain(__b, at, __l);
				
				// Nothing left to drain
				if (dc < 0)
					return -1;
				
				// Read something potentially, do not overwrite
				at += dc;
				rt += dc;
				
				// Stalled
				if (dc == 0)
				{
					// If done, not going to read anything
					if (_done)
						continue;
				
					// Setup buffer
					byte[] drain = this._drain;
				
					// Read some input
					int rc;
					try
					{
						// Drain
						rc = in.read(drain);
						
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
						processor.offer(drain, 0, rc);
					
						// Try again
						continue;
					}
				
					// Failed to read some input
					catch (IOException f)
					{
						// Mark it
						_failed = true;
					
						// Rethrow
						this._suppressed = f;
						throw f;
					}
				}
				
				// Return the total number of drained bytes
				return rt;
			}
		}
		
		// Failed pipe read
		catch (PipeProcessException|FaucetProcessException|
			SinkProcessException e)
		{
			// Failed read
			_failed = true; 
			
			// {@squirreljme.error AC05 Failed to drain from the pipe.}
			IOException tt = new IOException("AC05", e);
			this._suppressed = tt;
			throw tt;
		}
	}
}

