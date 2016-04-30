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

import java.io.Flushable;
import java.io.IOException;
import java.util.NoSuchElementException;
import net.multiphasicapps.io.datafaucet.CompleteFaucetException;
import net.multiphasicapps.io.datafaucet.DataFaucet;
import net.multiphasicapps.io.datafaucet.FaucetProcessException;
import net.multiphasicapps.io.datasink.CompleteSinkException;
import net.multiphasicapps.io.datasink.DataSink;
import net.multiphasicapps.io.datasink.SinkProcessException;
import net.multiphasicapps.util.circlebufs.CircularByteBuffer;

/**
 * This is a data processor which is given input bytes and performs
 * transformation of the input and provides an output.
 *
 * Data processors must be able to handle situations where partial information
 * and state is available, that is if there is not enough input available it
 * can continue when there is input.
 *
 * {@squirreljme.error AC01 The input end of the pipe is closed.}
 * {@squirreljme.error AC02 Cannot offer or remove pipe bytes during
 * processing.}
 *
 * @since 2016/03/11
 */
public abstract class DataPipe
	implements Flushable
{
	/** Lock. */
	protected final Object lock;
	
	/** Visible lock. */
	final Object _lock;
	
	/** The sink of input data. */
	private final __Sink__ _input;
	
	/** The faucet of output data. */
	private final __Faucet__ _output;
	
	/** Is processing being performed? */
	private volatile boolean _inproc;
	
	/**
	 * Initializes the data pipe with a default lock.
	 *
	 * @since 2016/04/30
	 */
	public DataPipe()
	{
		this(new Object());
	}
	
	/**
	 * Initializes the data pipe with the given lock.
	 *
	 * @param __lk The object to use for locking.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public DataPipe(Object __lk)
		throws NullPointerException
	{
		// Check
		if (__lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		lock = __lk;
		_lock = __lk;
		
		// Setup input and output
		_input = new __Sink__();
		_output = new __Faucet__();
	}
	
	/**
	 * Processes some data.
	 *
	 * This method called from the {@link DataPipe} class will be done
	 * during a lock.
	 *
	 * @throws PipeProcessException On processing errors.
	 * @throws PipeStalledException When not enough input is available.
	 * @since 2016/03/11
	 */
	protected abstract void process()
		throws PipeProcessException, PipeStalledException;
	
	/**
	 * Closes the input end of the pipe indicating that no more bytes are
	 * available for input.
	 *
	 * @return {@code this}.
	 * @throws PipeInputClosedException If the input of the pipe is already
	 * closed.
	 * @throws PipeProcessException If this is called during processing.
	 * @since 2016/04/30
	 */
	public final DataPipe closeInput()
		throws PipeInputClosedException, PipeProcessException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AC03 Cannot close the pipe input during
			// processing.}
			if (_inproc)
				throw new PipeProcessException("AC03");
			
			// Close the input
			try
			{
				_input.setComplete();
			}
			
			// Could not close it
			catch (CompleteSinkException e)
			{
				throw new PipeInputClosedException(e);
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Removes a single byte from the output.
	 *
	 * @return The read byte value or {@code -1} if the processing is
	 * complete.
	 * @throws PipeProcessException If there was an error processing bytes
	 * for draining.
	 * @throws PipeStalledException If a single byte is not available for
	 * output.
	 * @since 2016/04/30
	 */
	public final int drain()
		throws PipeProcessException, PipeStalledException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot be processing
			if (_inproc)
				throw new PipeProcessException("AC02");
			
			// Process bytes
			__process();
			
			// Drain single byte
			try
			{
				return _output.drain();
			}
			
			// Stalled
			catch (NoSuchElementException e)
			{
				throw new PipeStalledException(e);
			}
		}
	}
	
	/**
	 * Removes multiple bytes from the output.
	 *
	 * @param __b The array to write drained bytes into.
	 * @return The number of bytes read or {@code -1} if the processor end
	 * has been reached.
	 * @throws NullPointerException On null arguments.
	 * @throws PipeProcessException If there was an error processing bytes
	 * for draining.
	 * @since 2016/04/30
	 */
	public final int drain(byte[] __b)
		throws NullPointerException, PipeProcessException
	{
		return drain(__b, 0, __b.length);
	}
	
	/**
	 * Removes multiple bytes from the output.
	 *
	 * @param __b The array to write drained bytes into.
	 * @param __o The starting offset in the array to write at.
	 * @param __l The number of bytes to drain.
	 * @return The number of bytes read or {@code -1} if the processor end
	 * has been reached.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws PipeProcessException If there was an error processing bytes
	 * for draining.
	 * @since 2016/04/30
	 */
	public final int drain(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException,
			PipeProcessException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot be processing
			if (_inproc)
				throw new PipeProcessException("AC02");
			
			// Process bytes
			__process();
			
			// Drain multiple bytes
			return _output.drain(__b, __o, __l);
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
			// Flush input and outputs
			_input.flush();
			_output.flush();
		}
	}
	
	/**
	 * Offers a single byte to the pipe input.
	 *
	 * @param __b The byte to offer to the input.
	 * @return {@code this}.
	 * @throws PipeInputClosedException If the input side of the pipe is
	 * closed.
	 * @throws PipeProcessException If bytes are offered during processing.
	 * @since 2016/04/30
	 */
	public final DataPipe offer(byte __b)
		throws PipeInputClosedException, PipeProcessException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot be processing
			if (_inproc)
				throw new PipeProcessException("AC02");
			
			// Offer input bytes
			try
			{
				// Add bytes
				_input.offer(__b);
				
				// Process those bytes
				__process();
			}
			
			// The input is closed
			catch (CompleteSinkException e)
			{
				throw new PipeInputClosedException("AC01", e);
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers multiple bytes to the pipe input.
	 *
	 * @param __b The bytes to offer to the input.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @throws PipeInputClosedException If the input side of the pipe is
	 * closed.
	 * @throws PipeProcessException If bytes are offered during processing.
	 * @since 2016/04/30
	 */
	public final DataPipe offer(byte[] __b)
		throws NullPointerException, PipeInputClosedException,
			PipeProcessException
	{
		return offer(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bytes to the pipe input.
	 *
	 * @param __b The bytes to offer to the input.
	 * @param __o The starting offset of the bytes to offer.
	 * @param __l The number of bytes to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws PipeInputClosedException If the input side of the pipe is
	 * closed.
	 * @throws PipeProcessException If bytes are offered during processing.
	 * @since 2016/04/30
	 */
	public final DataPipe offer(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException,
			PipeInputClosedException, PipeProcessException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot be processing
			if (_inproc)
				throw new PipeProcessException("AC02");
			
			// Offer input bytes
			try
			{
				// Add bytes
				_input.offer(__b, __o, __l);
				
				// Process those bytes
				__process();
			}
			
			// The input is closed
			catch (CompleteSinkException e)
			{
				throw new PipeInputClosedException("AC01", e);
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Processes pipe bytes.
	 *
	 * @since 2016/04/30
	 */
	private final void __process()
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Internal faucet for processing.
	 *
	 * @since 2016/04/30
	 */
	private final class __Faucet__
		extends DataFaucet
	{
		/**
		 * Initializes the faucet.
		 *
		 * @since 2016/04/30
		 */
		private __Faucet__()
		{
			super(DataPipe.this.lock);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/30
		 */
		@Override
		protected final void process()
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	}
	
	/**
	 * Internal sink for processing.
	 *
	 * @since 2016/04/30
	 */
	private final class __Sink__
		extends DataSink
	{
		/**
		 * Initializes the sink.
		 *
		 * @since 2016/04/30
		 */
		private __Sink__()
		{
			super(DataPipe.this.lock);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/30
		 */
		@Override
		protected final void process(int __n)
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	}
}

