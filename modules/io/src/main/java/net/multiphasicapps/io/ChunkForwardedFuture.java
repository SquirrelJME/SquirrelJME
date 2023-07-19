// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This is a future that is a forward of another future.
 *
 * @since 2020/11/29
 */
public final class ChunkForwardedFuture
	implements ChunkFuture
{
	/** Initialized to zero? */
	protected final boolean zeroInit;
	
	/** The future to get the value from. */
	private volatile ChunkFuture _future;
	
	/** An offset, which may be a future. */
	private volatile ChunkFuture _offset;
	
	/**
	 * Initializes the future with nothing set.
	 * 
	 * @since 2021/01/20
	 */
	public ChunkForwardedFuture()
	{
		this(false);
	}
	
	/**
	 * Initialize the future to a value of zero?
	 * 
	 * @param __zeroInit Is this initialized to zero?
	 * @since 2021/09/06
	 */
	public ChunkForwardedFuture(boolean __zeroInit)
	{
		this.zeroInit = __zeroInit;
	}
	
	/**
	 * Initializes the future with values optionally set.
	 * 
	 * @param __future The future value.
	 * @param __offset The potential offset.
	 * @since 2021/01/20
	 */
	public ChunkForwardedFuture(ChunkFuture __future, ChunkFuture __offset)
	{
		this._future = __future;
		this._offset = __offset;
		this.zeroInit = false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/29
	 */
	@Override
	public int get()
	{
		synchronized (this)
		{
			// Ensure the future is actually set
			ChunkFuture future = this._future;
			if (future == null)
			{
				// If zero initializing, ignore any offset and just return
				// zero
				if (this.zeroInit)
					return 0;
					
				/* {@squirreljme.error BD05 A future was never set.} */
				throw new IllegalStateException("BD05");
			}
			
			// Future with optional offset applied?
			ChunkFuture offset = this._offset;
			return future.get() + (offset == null ? 0 : offset.get());
		}
	}
	
	/**
	 * Checks if this future is set or not.
	 * 
	 * @return If this is set or not.
	 * @since 2020/12/06
	 */
	public boolean isSet()
	{
		synchronized (this)
		{
			return null != this._future;
		}
	}
	
	/**
	 * Sets the future to read a value from.
	 * 
	 * @param __future The future to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	public void set(ChunkFuture __future)
		throws NullPointerException
	{
		this.set(__future, null);
	}
	
	/**
	 * Sets the future to read a value from.
	 * 
	 * @param __future The future to set.
	 * @param __off Offset for the future.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/17
	 */
	public void set(ChunkFuture __future, int __off)
		throws NullPointerException
	{
		this.set(__future, new ChunkFutureInteger(__off));
	}
	
	/**
	 * Sets the future to read a value from.
	 * 
	 * @param __future The future to set.
	 * @param __off Optional offset for the future.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/17
	 */
	public void set(ChunkFuture __future, ChunkFuture __off)
		throws NullPointerException
	{
		if (__future == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._future = __future;
			this._offset = __off;
		}
	}
	
	/**
	 * Sets the future to this integer value.
	 * 
	 * @param __value The value to set.
	 * @return The generated future.
	 * @since 2020/12/06
	 */
	public ChunkFutureInteger setInt(int __value)
	{
		ChunkFutureInteger future = new ChunkFutureInteger(__value);
		
		this.set(future, null);
		
		return future;
	}
}
