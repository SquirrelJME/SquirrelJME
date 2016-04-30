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

import java.io.IOException;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.circlebufs.CircularByteBuffer;

/**
 * This is a data processor which is given input bytes and performs
 * transformation of the input and provides an output.
 *
 * Data processors must be able to handle situations where partial information
 * and state is available, that is if there is not enough input available it
 * can continue when there is input.
 *
 * All data processors are initialized in the waiting state.
 *
 * @since 2016/03/11
 */
public abstract class DataPipe
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Data used for input to the data processor. */
	private final CircularByteBuffer _input =
		new CircularByteBuffer(lock);
	
	/** Data which has been output by the data processor. */
	private final CircularByteBuffer _output =
		new CircularByteBuffer(lock);
	
	/** Visible lock. */
	final Object _lock =
		lock;
	
	/**
	 * Is this finished? If so then no more input is accepted and the output
	 * must be processed.
	 */
	private volatile boolean _isfinished;
	
	/** Is this waiting? */
	private volatile boolean _iswaiting =
		true;
	
	/** Threw IOException during process. */
	private volatile boolean _threwioe;
	
	/**
	 * Processes some data.
	 *
	 * This method called from the {@link DataPipe} class will be done
	 * during a lock.
	 *
	 * @throws IOException On processing errors.
	 * @throws WaitingException When not enough input is available.
	 * @since 2016/03/11
	 */
	protected abstract void process()
		throws IOException, WaitingException;
	
	/**
	 * Signals that the end of the input has been reached and that processing
	 * should do as much as it can or fail, no more input is permitted after
	 * this.
	 *
	 * @return {@code this}.
	 * @throws IOException On processing errors.
	 * @since 2016/03/11
	 */
	public final DataPipe finish()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// if already finished, ignore
			if (_isfinished)
				return this;
			
			// Set
			_isfinished = true;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns {@code true} if the output buffer has bytes in it.
	 *
	 * @return {@code true} if output bytes still remain.
	 * @since 2016/03/11
	 */
	public final boolean hasRemainingOutput()
	{
		// Lock
		synchronized (lock)
		{
			return _output.hasAvailable();
		}
	}
	
	/**
	 * Returns {@code true} if this processor is in the finished state.
	 *
	 * @return {@code true} if in the finished state.
	 * @since 2016/03/11
	 */
	public final boolean isFinished()
	{
		// Lock
		synchronized (lock)
		{
			return _isfinished;
		}
	}
	
	/**
	 * Returns {@code true} if this processor is in the waiting for more data
	 * state.
	 *
	 * @return {@code true} if it is waiting for more data.
	 * @since 2016/03/11
	 */
	public final boolean isWaiting()
	{
		// Lock
		synchronized (lock)
		{
			return _iswaiting;
		}
	}
	
	/**
	 * Offers a single byte to the processor input at the end of its internal
	 * buffer.
	 *
	 * @param __b The byte to offer.
	 * @return {@code this}.
	 * @throws IllegalStateException If the processor is in the finish state.
	 * @throws IOException On processing errors.
	 * @since 2016/03/11
	 */
	public final DataPipe offer(byte __b)
		throws IllegalStateException, IOException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot offer if finished
			if (_isfinished)
				throw new IllegalStateException("XI0c");
			
			// Add byte to the input
			_input.offerLast(__b);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers the given byte array to the processor input.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws IllegalStateException If the processor is in the finish state.
	 * @throws IOException On processing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final DataPipe offer(byte... __b)
		throws IllegalStateException, IOException, NullPointerException
	{
		return offer(__b, 0, __b.length);
	}
	
	/**
	 * Offers bytes within the given range of the array to the processor
	 * input.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of bytes to offer.
	 * @return {@code this}.
	 * @throws IllegalStateException If the processor is in the finish state.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws IOException On processing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final DataPipe offer(byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot offer if finished
			if (_isfinished)
				throw new IllegalStateException("XI0c");
			
			// Add to the output
			_input.offerLast(__b, __o, __l);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Reads bytes which are waiting on the input side of the pipe.
	 *
	 * @param __b The output array to read input from.
	 * @param __o The base offset to start the output at.
	 * @param __l The maximum number of bytes to read from the input for
	 * placement onto the output.
	 * @return The number of input bytes which were removed.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the bounds of the input array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/29
	 */
	protected final int pipeInput(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n;
		if (__o < 0 || __l < 0 || (__o + __l) > (n = __b.length))
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (lock)
		{
			return _input.removeFirst(__b, __o, __l);
		}
	}
	
	/**
	 * Writes a single byte to the output of this pipe.
	 *
	 * @param __b The byte to write.
	 * @return {@code this}.
	 * @since 2016/04/30
	 */
	protected final DataPipe pipeOutput(byte __b)
	{
		// Lock
		synchronized (lock)
		{
			_output.offerLast(__b);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Writes bytes from the given array into the output of this pipe.
	 *
	 * @param __b The array containing bytes to place in the output.
	 * @param __o The offset of the input bytes.
	 * @param __l The number of bytes to output.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the bounds of the input array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/29 
	 */
	protected final DataPipe pipeOutput(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n;
		if (__o < 0 || __l < 0 || (__o + __l) > (n = __b.length))
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (lock)
		{
			_output.offerLast(__b, __o, __l);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Reads and removes the first available byte, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws IOException On processing errors.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public final byte remove()
		throws IOException, NoSuchElementException
	{
		synchronized (lock)
		{
			// Previous read threw an exception
			if (_threwioe)
				throw new IOException("XI0d");
			
			// Read until failure or a value is returned
			for (boolean fail = false;;)
			{
				// Try reading some output
				try
				{
					// Return an output byte
					return _output.removeFirst();
				}
			
				// No data is available
				catch (NoSuchElementException nsee)
				{
					// Happened twice, toss it
					if (fail)
						throw nsee;
					
					// Fail if this happens again
					fail = true;
					
					// Process some data
					try
					{
						process();
					}
					
					// Caught exception
					catch (IOException ioe)
					{
						// Mark it as failed so that a broken state is not
						// used
						_threwioe = true;
						
						// Rethrow it
						throw ioe;
					}
					
					// If waiting just fail here
					catch (WaitingException we)
					{
						throw nsee;
					}
				}
			}
		}
	}
	
	/**
	 * Reads and removes any available bytes and places them within the
	 * given array.
	 *
	 * @param __b The array to write byte values into.
	 * @return The number of bytes which were removed.
	 * @throws IOException On processing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final int remove(byte[] __b)
		throws IOException, NullPointerException
	{
		return remove(__b, 0, __b.length);
	}
	
	/**
	 * Reads and removes multiple bytes waiting for output up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write byte values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of bytes to remove.
	 * @return The number of removed bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws IOException On processing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final int remove(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Total
			int rc = 0;
			
			// Remove bytes
			for (int i = 0; i < __l; i++)
				try
				{
					__b[__o + i] = remove();
					rc++;
				}
				
				// Return the number of read bytes
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
	
	/**
	 * Sets the waiting state (if the processor is waiting for more bytes as
	 * input).
	 *
	 * If the waiting state is {@code false} and there no output data then
	 * {@code -1} will be returned from the read.
	 *
	 * @param __w If {@code true} then the waiting state is set, otherwise
	 * it is cleared.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	protected final DataPipe setWaiting(boolean __w)
	{
		// Lock
		synchronized (lock)
		{
			_iswaiting = __w;
		}
		
		// Self
		return this;
	}
	
	/**
	 * This is thrown when during the middle of processing there is not
	 * enough data to continue, that is there is not enough input for output
	 * to be written to.
	 *
	 * @since 2016/03/11
	 */
	public static final class WaitingException
		extends RuntimeException
	{
		/**
		 * Initializes the waiting exception with no message.
		 *
		 * @since 2016/03/17
		 */
		public WaitingException()
		{
		}
		
		/**
		 * Initializes the waiting exception with the given message.
		 *
		 * @param __m The message to use.
		 * @since 2016/03/17
		 */
		public WaitingException(String __m)
		{
			super(__m);
		}
	}
}

