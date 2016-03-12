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

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This is a data processor which handles RFC 1951 deflate streams.
 *
 * @since 2016/03/11
 */
public class DeflateDataProcessor
	extends DataProcessor
{
	/**
	 * Required non-finished bits in the queue, this is for optimal processing
	 * so that partial states are simpler.
	 */
	protected static final int REQUIRED_BITS =
		48;
	
	/** No compression. */
	protected static final int TYPE_NO_COMPRESSION =
		0b00;
	
	/** Fixed huffman table compression. */
	protected static final int TYPE_FIXED_HUFFMAN =
		0b01;
	
	/** Dynamic huffman table compression. */
	protected static final int TYPE_DYNAMIC_HUFFMAN =
		0b10;
	
	/** An error. */
	protected static final int TYPE_ERROR =
		0b11;
	
	/** Input bits. */
	protected final CircularBooleanBuffer inputbits =
		new CircularBooleanBuffer();
	
	/** The bit compactor for queing added bits. */
	protected final BitCompactor compactor =
		new BitCompactor(new BitCompactor.Callback()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/11
				 */
				@Override
				public void ready(byte __v)
				{
					System.err.printf(">> %02x%n", __v);
					
					// Give it to the output data
					output.offerLast(__v);
				}
			});
	
	/** Current decoding task. */
	private volatile Task _task =
		Task.READ_HEADER;
	
	/** Was the final block hit? */
	private volatile boolean _finalhit;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected void process()
		throws IOException, WaitingException
	{
		// Take all bytes which are available to the input and add them to the
		// input bit buffer
		while (input.hasAvailable())
			inputbits.offerLastInt(((int)input.removeFirst()) & 0xFF, 0xFF,
				true);
		
		// Processing loop
		for (;;)
		{
			// Require more available bytes if not finished
			if (!isFinished() && inputbits.available() < REQUIRED_BITS)
				throw new WaitingException();
		
			// Perform work
			try
			{
				// Depends on the action
				switch (_task)
				{
						// Read the deflate header
					case READ_HEADER:
						if (!__readHeader())
							return;
						break;
					
						// Unknown
					default:
						throw new IllegalStateException(_task.name());
				}
			}
		
			// Short read
			catch (NoSuchElementException nsee)
			{
				throw new IOException(nsee);
			}
		}
	}
	
	/**
	 * Reads the deflate header.
	 *
	 * @throws IOException On null arguments.
	 * @since 2016/03/11
	 */
	private boolean __readHeader()
		throws IOException
	{
		// If the final block was hit then just stop
		if (_finalhit)
		{
			setWaiting(false);
			return false;
		}
		
		// Read final bit
		_finalhit |= inputbits.removeFirst();
		
		// Read type
		int type = inputbits.removeFirstInt(2);
		
		// Depends on the type to read
		switch (type)
		{
				// None
			case TYPE_NO_COMPRESSION:
				if (true)
					throw new Error("TODO");
				break;
				
				// Fixed huffman
			case TYPE_FIXED_HUFFMAN:
				if (true)
					throw new Error("TODO");
				break;
				
				// Dynamic huffman
			case TYPE_DYNAMIC_HUFFMAN:
				if (true)
					throw new Error("TODO");
				break;
			
				// Error or unknown
			case TYPE_ERROR:
			default:
				throw new InflaterException.HeaderErrorTypeException();
		}
		
		// Continue
		return true;
	}
	
	/**
	 * The current task to perform when decoding input code.
	 *
	 * @since 2016/03/11
	 */
	private static enum Task
	{
		/** Read the deflate header. */
		READ_HEADER,
		
		/** End. */
		;
	}
}

