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
public class InflateDataProcessor
	extends DataProcessor
{
	/**
	 * Required non-finished bits in the queue, this is for optimal processing
	 * so that partial states are simpler.
	 */
	protected static final int REQUIRED_BITS =
		48;
	
	/** The size of the sliding window. */
	protected static final int SLIDING_WINDOW_SIZE =
		32768;
	
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
	
	/** The sliding byte window. */
	protected final SlidingByteWindow window =
		new SlidingByteWindow(SLIDING_WINDOW_SIZE);
	
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
					// Give it to the output data
					output.offerLast(__v);
					
					// Also give it to the sliding window
					window.append(__v);
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
			inputbits.offerLastInt(((int)input.removeFirst()) & 0xFF, 0xFF);
		
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
						
						// Read fixed huffman table
					case FIXED_HUFFMAN:
						__readFixedHuffman();
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
	 * Handles a single huffman code.
	 *
	 * @param __c The input code.
	 * @throws IOException On read/write error.s
	 * @since 2016/03/12
	 */
	private void __handleCode(int __c)
		throws IOException
	{
		// Debug
		System.err.println("DEBUG -- c " + __c + " " + (char)__c);
		System.err.flush();
		
		// Literal byte value
		if (__c >= 0 && __c <= 256)
			compactor.add(__c & 0xFF, 0xFF);
		
		// Stop processing, use the waiting exception to break out of the state
		// and go back to the header handler
		else if (__c == 256)
		{
			_task = Task.READ_HEADER;
			throw new WaitingException();
		}
		
		// Window based result
		else if (__c >= 257 && __c <= 285)
		{
			// Read the distance
			int dist = __handleDistance(__c);
			System.err.printf("DEBUG -- Distance %d%n", dist);
			
			// Read the length
			int lent = __handleLength();
			System.err.printf("DEBUG -- Length %d%n", lent);
			
			// Create a byte array from the sliding window data
			byte[] winb = new byte[lent];
			try
			{
				window.get(dist, winb, 0, lent);
			}
			
			// Bad window read
			catch (IndexOutOfBoundsException ioobe)
			{
				throw new InflaterException(ioobe);
			}
			
			// Add those bytes to the output
			for (int i = 0; i < lent; i++)
				compactor.add(winb[i] & 0xFF, 0xFF);
		}
		
		// Error
		else
			throw new InflaterException.IllegalSequence();
	}
	
	/**
	 * Handles distance codes.
	 *
	 * @param __c The distance code.
	 * @return The distance to actually use
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleDistance(int __c)
		throws IOException
	{
		// If the code is 285 then the distance will be that
		if (__c == 285)
			return 285;
		
		// Get the base code
		int base = __c - 257;
		
		// Calculate the required distance to use
		int rv = 3;
		for (int i = 0; i < base; i++)
		{
			// Determine how many groups of 4 the code is away. Since zero
			// appears as items then subtract 1 to make it longer. However
			// after the first 8 it goes up in a standard pattern.
			rv += (1 << Math.max(0, (i / 4) - 1));
		}
		
		// Calculate the number of extra bits to read
		int extrabits = Math.max(0, (base / 4) - 1);
		
		// Read in those bits, if applicable
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, true);
		
		// Return the distance
		return rv;
	}
	
	/**
	 * Reads length codes from the input.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleLength()
		throws IOException
	{
		// Read 5 bits of length input
		int code = inputbits.removeFirstInt(5, true);
		
		// Error if above 29
		if (code > 29)
			throw new InflaterException.IllegalSequence();
		
		// Calculate the required length to use
		int rv = 0;
		for (int i = 0; i < code; i++)
		{
			// This uses a similar pattern to the distance code, however the
			// division is half the size (so there are groups of 2 now).
			rv += (1 << Math.max(0, (i / 2) - 1));
		}
		
		// Determine the number of extra bits
		int extrabits = Math.max(0, (code / 2) - 1);
		
		// If there are bits to read then read them in
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, true);
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads the fixed huffman based input.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/11
	 */
	private void __readFixedHuffman()
		throws IOException
	{
		// If there are at least 9 available bits for input, read them.
		// Alternatively if this is the final block allow 7 because this
		// could be the final stop code.
		while (inputbits.available() >= 9 ||
			(_finalhit && inputbits.available() >= 7))
		{
			// Read single code
			int code = DeflateFixedHuffman.read(inputbits);
			
			// Handle the code
			__handleCode(code);
		}
	}
	
	/**
	 * Reads the deflate header.
	 *
	 * @return {@code true} if the calling loop should terminate.
	 * @throws IOException On read/write errors.
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
		
		// Debug
		System.err.printf("DEBUG -- fn: %s%n", _finalhit);
		System.err.printf("DEBUG -- ty: %d%n", type);
		
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
				_task = Task.FIXED_HUFFMAN;
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
		
		/** Read fixed huffman table. */
		FIXED_HUFFMAN,
		
		/** End. */
		;
	}
}

