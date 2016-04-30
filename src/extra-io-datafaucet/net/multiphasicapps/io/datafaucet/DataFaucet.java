// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datafaucet;

import java.io.Flushable;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.circlebufs.CircularByteBuffer;

/**
 * This is a data faucet which generates bytes.
 *
 * {@squirreljme.error AB01 Cannot add bytes for draining when the output is
 * complete.}
 * {@squirreljme.error AB02 Cannot add bytes for draining when processing is
 * not being performed.}
 * {@squirreljme.error AB05 Cannot drain bytes during processing.}
 *
 * @since 2016/04/30
 */
public abstract class DataFaucet
	implements Flushable
{
	/** The internal lock. */
	protected final Object lock;
	
	/** The output temporary buffer. */
	private final CircularByteBuffer _output;
	
	/** Is the faucet complete? */
	private volatile boolean _complete;
	
	/** Processing is being performed. */
	private volatile boolean _inproc;
	
	/** Did processing fail. */
	private volatile boolean _didfail;
	
	/**
	 * Initializes the data faucet.
	 *
	 * @since 2016/04/30
	 */
	public DataFaucet()
	{
		this(new Object());
	}
	
	/**
	 * Initializes the data faucet with the given lock.
	 *
	 * @param __lk The data lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public DataFaucet(Object __lk)
		throws NullPointerException
	{
		// Check
		if (__lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		lock = __lk;
		
		// Setup output buffer
		_output = new CircularByteBuffer(__lk);
	}
	
	/**
	 * Processes data for output to be drained in the faucet.
	 *
	 * @throws FaucetProcessException On processing errors.
	 * @since 2016/04/30
	 */
	protected abstract void process()
		throws FaucetProcessException;
	
	/**
	 * Drains a single byte from the faucet.
	 *
	 * @return The read byte value or {@code -1} if the end has been reached.
	 * @throws FaucetProcessException If could not process bytes for draining.
	 * @throws NoSuchElementException If the faucet is not yet complete and
	 * there is no byte which is available.
	 * @since 2016/04/30
	 */
	public final int drain()
		throws FaucetProcessException, NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot drain during processing
			if (_inproc)
				throw new FaucetProcessException("AB05");
			
			// Process
			__process();
			
			// Try to read a single byte
			try
			{
				return ((int)_output.removeFirst()) & 0xFF;
			}
			
			catch (NoSuchElementException e)
			{
				// If complete end it
				if (_complete)
					return -1;
				
				// Otherwise, rethrow
				throw e;
			}
		}
	}
	
	/**
	 * Drains multiple bytes from the faucet.
	 *
	 * @param __b The byte array to write to.
	 * @return The number of drained bytes or {@code -1} if the end has been
	 * reached.
	 * @throws FaucetProcessException If could not process bytes for draining.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public final int drain(byte[] __b)
		throws FaucetProcessException, NullPointerException
	{
		return drain(__b, 0, __b.length);
	}
	
	/**
	 * Drains multiple bytes from the faucet.
	 *
	 * @param __b The byte array to write to.
	 * @param __o The offset to start the write at.
	 * @param __l The number of bytes to write.
	 * @return The number of drained bytes or {@code -1} if the end has been
	 * reached.
	 * @throws FaucetProcessException If could not process bytes for draining.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public final int drain(byte[] __b, int __o, int __l)
		throws FaucetProcessException, IndexOutOfBoundsException,
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
			// Cannot drain during processing
			if (_inproc)
				throw new FaucetProcessException("AB05");
			
			// Process
			__process();
			
			// Read many bytes
			int rv = _output.removeFirst(__b, __o, __l);
			
			// No bytes read?
			if (rv <= 0)
				return (_complete ? -1 : 0);
			
			// Return the read count
			return rv;
		}
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The single byte to add.
	 * @return {@code this}.
	 * @throws CompleteFaucetException If the faucet is complete.
	 * @throws FaucetProcessException If filling is not called during
	 * processing.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte __b)
		throws CompleteFaucetException, FaucetProcessException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot fill when already complete
			if (_complete)
				throw new CompleteFaucetException("AB01");
			
			// Must be processing
			if (!_inproc)
				throw new FaucetProcessException("AB02");
			
			// Send in
			_output.offerLast(__b);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The bytes to add.
	 * @return {@code this}.
	 * @throws CompleteFaucetException If the faucet is complete.
	 * @throws FaucetProcessException If filling is not called during
	 * processing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte[] __b)
		throws CompleteFaucetException, FaucetProcessException,
			NullPointerException
	{
		return fill(__b, 0, __b.length);
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The bytes to add.
	 * @return {@code this}.
	 * @throws CompleteFaucetException If the faucet is complete.
	 * @throws FaucetProcessException If filling is not called during
	 * processing.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte[] __b, int __o, int __l)
		throws CompleteFaucetException, FaucetProcessException,
			IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot fill when already complete
			if (_complete)
				throw new FaucetProcessException("AB01");
			
			// Must be processing
			if (!_inproc)
				throw new FaucetProcessException("AB02");
			
			// Send in
			_output.offerLast(__b, __o, __l);
		}
		
		// Self
		return this;
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
	 * Returns {@code true} if there is no more output to be generated from
	 * this faucet.
	 *
	 * @return {@code true} if the faucet has no more data to generate.
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
	 * Processes the faucet to determine if there are more bytes for input.
	 *
	 * @throws FaucetProcessException If processing failed.
	 * @since 2016/04/30
	 */
	private final void __process()
		throws FaucetProcessException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AB03 The faucet previously threw an
			// exception during processing.}
			if (_didfail)
				throw new FaucetProcessException("AB03");
			
			// {@squirreljme.error AB07 Double processing.}
			if (_inproc)
				throw new IllegalStateException("AB07");
			
			// Already complete, there will be no more bytes
			if (_complete)
				return;
			
			// Could fail
			try
			{
				// Set
				_inproc = true;
				
				// Call processor
				process();
			}
			
			// Failed
			catch (Throwable t)
			{
				// Set failure
				_didfail = true;
				
				// Throw as is
				if (t instanceof RuntimeException)
					throw (RuntimeException)t;
				else if (t instanceof Error)
					throw (Error)t;
				
				// {@squirreljme.error AB04 Caught another exception while
				// processing the faucet.}
				throw new FaucetProcessException("AB04", t);
			}
			
			// Not in a processor run
			finally
			{
				_inproc = false;
			}
		}
	}
}

