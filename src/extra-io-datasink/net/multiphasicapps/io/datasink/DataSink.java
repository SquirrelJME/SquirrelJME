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

import java.io.Flushable;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.circlebufs.CircularByteBuffer;

/**
 * This is a data sink which consumes bytes.
 *
 * Bytes cannot be added to the sink while processing is being performed (that
 * is the processor cannot add more bytes).
 *
 * {@squirreljme.error AA01 Cannot add more bytes because the input has been
 * completed.}
 * {@squirreljme.error AA02 Cannot add more bytes because the sink is
 * currently processing data.}
 * {@squirreljme.error AA05 Cannot accept bytes when processing is not being
 * performed.}
 *
 * @since 2016/04/30
 */
public abstract class DataSink
	implements Flushable
{
	/** This is returned when there is no input and the source is complete. */
	public static final int COMPLETED =
		Integer.MIN_VALUE;
	
	/** This is returned when there is no input. */
	public static final int NO_INPUT =
		COMPLETED + 1;
	
	/** Lock. */
	protected final Object lock;
	
	/** Externally visible lock. */
	final Object _lock;
	
	/** Data used for input to the data processor. */
	private final CircularByteBuffer _input;
	
	/** Is the input complete? */
	private volatile boolean _complete;
	
	/** Is processing being performed? */
	private volatile boolean _inproc;
	
	/** Has failure occur? */
	private volatile boolean _didfail;
	
	/**
	 * Initializes the data sink.
	 *
	 * @since 2016/04/30
	 */
	public DataSink()
	{
		this(new Object());
	}
	
	/**
	 * Initializes the data sink and uses the specified object as a lock.
	 *
	 * @param __lk The locking object to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public DataSink(Object __lk)
		throws NullPointerException
	{
		// Check
		if (__lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		lock = __lk;
		_lock = __lk;
		
		// Setup input
		_input = new CircularByteBuffer(__lk);
	}
	
	/**
	 * This is implemented by sub-classes to indicate that there are bytes
	 * ready to be processed.
	 *
	 * @param __n The number of bytes available for processing.
	 * @throws SinkProcessException If there was an error processing the bytes.
	 * @since 2016/04/30
	 */
	protected abstract void process(int __n)
		throws SinkProcessException;
	
	/**
	 * Removes a single byte from the input queue.
	 *
	 * @return The read byte or {@link #COMPLETED} if completed.
	 * @throws NoSuchElementException If no data is available for reading.
	 * @throws SinkProcessException If there was an acceptance error.
	 * @since 2016/04/30
	 */
	protected final int accept()
		throws NoSuchElementException, SinkProcessException
	{
		// Lock
		synchronized (lock)
		{
			// Not being processed?
			if (!_inproc)
				throw new SinkProcessException("AA05");
			
			// Could have no data in the buffer
			try
			{
				return ((int)((byte)(_input.removeFirst()))) & 0xFF;
			}
			
			// No data read
			catch (NoSuchElementException nsee)
			{
				if (_complete)
					return COMPLETED;
				throw nsee;
			}
		}
	}
	
	/**
	 * Removes multiples bytes from the input queue.
	 *
	 * @param __b The output buffer to read bytes into.
	 * @return The number of read bytes.
	 * @throws NullPointerException On null arguments.
	 * @throws SinkProcessException If there was an acceptance error.
	 * @since 2016/04/30
	 */
	protected final int accept(byte[] __b)
		throws NullPointerException, SinkProcessException
	{
		return accept(__b, 0, __b.length);
	}
	
	/**
	 * Removes multiples bytes from the input queue.
	 *
	 * @param __b The output buffer to read bytes into.
	 * @param __o The starting offset to place bytes at.
	 * @param __l The number of bytes to add.
	 * @return The number of read bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws SinkProcessException If there was an acceptance error.
	 * @since 2016/04/30
	 */
	protected final int accept(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException,
			SinkProcessException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Not being processed?
			if (!_inproc)
				throw new SinkProcessException("AA05");
			
			// Read input bytes
			int rv = _input.removeFirst(__b, __o, __l);
			
			// Nothing read?
			if (rv <= 0)
				return (_complete ? COMPLETED : NO_INPUT);
			
			// Return the read count
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public final void flush()
	{
		// Lock
		synchronized (lock)
		{
			__process();
		}
	}
	
	/**
	 * Returns whether or not the input source bytes has ended.
	 *
	 * @return {@code true} if the bytes which remain in the input buffer are
	 * the last bytes to handle.
	 * @since 2016/04/30
	 */
	public final boolean isComplete()
	{
		// Lock
		synchronized (lock)
		{
			return _complete;
		}
	}
	
	/**
	 * Offers a single byte for processing.
	 *
	 * @param __b The byte to offer.
	 * @return {@code this}.
	 * @throws CompleteSinkException If the input source is complete.
	 * @throws SinkProcessException If bytes were attempted to be offered
	 * during processing.
	 * @since 2016/04/30
	 */
	public final DataSink offer(byte __b)
		throws CompleteSinkException, SinkProcessException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot add if complete
			if (_complete)
				throw new CompleteSinkException("AA01");
			
			// Cannot add if in process
			if (_inproc)
				throw new SinkProcessException("AA02");
			
			// Add to the input
			_input.offerLast(__b);
			
			// Process data
			__process();
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers multiple bytes for processing.
	 *
	 * @param __b The bytes to offer.
	 * @return {@code this}.
	 * @throws CompleteSinkException If the input source is complete.
	 * @throws NullPointerException On null arguments.
	 * @throws SinkProcessException If bytes were attempted to be offered
	 * during processing.
	 * @since 2016/04/30
	 */
	public final DataSink offer(byte[] __b)
		throws CompleteSinkException, NullPointerException,
			SinkProcessException
	{
		return offer(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple byte for processing.
	 *
	 * @param __b The bytes to offer.
	 * @param __o The starting offset to read bytes from.
	 * @param __l The number of bytes to buffer.
	 * @return {@code this}.
	 * @throws CompleteSinkException If the input source is complete.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws SinkProcessException If bytes were attempted to be offered
	 * during processing.
	 * @since 2016/04/30
	 */
	public final DataSink offer(byte[] __b, int __o, int __l)
		throws CompleteSinkException, IndexOutOfBoundsException,
			NullPointerException, SinkProcessException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot add if complete
			if (_complete)
				throw new CompleteSinkException("AA01");
			
			// Cannot add if in process
			if (_inproc)
				throw new SinkProcessException("AA02");
			
			// Add to the input
			_input.offerLast(__b, __o, __l);
			
			// Process data
			__process();
		}
		
		// Self
		return this;
	}
	
	/**
	 * Sets the flag which indicates that there will be no more bytes placed
	 * into the sink.
	 *
	 * @return {@code this}.
	 * @throws SinkProcessException If the sink is already complete.
	 * @since 2016/04/30
	 */
	public final DataSink setComplete()
		throws SinkProcessException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AA07 The data sink cannot be set as complete
			// during processing.}
			if (_inproc)
				throw new SinkProcessException("AA07");
			
			// {@squirreljme.error AA06 The data sink is already complete}
			if (_complete)
				throw new SinkProcessException("AA06");
			
			// Set
			_complete = true;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the number of bytes which are waiting to be processed in the
	 * input.
	 *
	 * @return The number of bytes waiting on input.
	 * @since 2016/04/30
	 */
	public final int waiting()
	{
		// Lock
		synchronized (lock)
		{
			return _input.available();
		}
	}
	
	/**
	 * Processes input bytes.
	 *
	 * @throws SinkProcessException If processing has failed.
	 * @since 2016/04/30
	 */
	private final void __process()
		throws SinkProcessException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AA03 The sink previously threw an
			// exception during processing.}
			if (_didfail)
				throw new SinkProcessException("AA03");
			
			// Obtain the number of available bytes
			int count = _input.available();
			
			// if no bytes to process, do nothing
			if (count <= 0)
				return;
			
			// {@squirreljme.error AA07 Double processing.}
			if (_inproc)
				throw new IllegalStateException("AA07");
			
			try
			{
				// Now processing
				_inproc = true;
				
				// Run processor
				process(count);
			}
			
			// Sink processor has some issues
			catch (Throwable e)
			{
				_didfail = true;
				
				// Rethrow it
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				else if (e instanceof Error)
					throw (Error)e;
				
				// {@squirreljme.error AA04 Caught another exception during
				// processing.}
				throw new SinkProcessException("AA04", e);
			}
			
			// Clear process bit
			finally
			{
				_inproc = false;
			}
		}
	}
}

