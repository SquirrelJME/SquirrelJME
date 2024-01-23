// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Deque;

/**
 * Represents a value for storage.
 *
 * @since 2021/03/17
 */
public final class JDWPHostValue
	implements AutoCloseable
{
	/** Free value queue. */
	private final Reference<Deque<JDWPHostValue>> _freeValues;
	
	/** Is this open? */
	private volatile boolean _isOpen;
	
	/** The set value. */
	private volatile Object _value;
	
	/** Is this set? */
	private volatile boolean _isSet;
	
	/**
	 * Initializes the value storage.
	 * 
	 * @param __freeValues The free values queue, to go when closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/19
	 */
	JDWPHostValue(Deque<JDWPHostValue> __freeValues)
		throws NullPointerException
	{
		if (__freeValues == null)
			throw new NullPointerException("NARG");
		
		this._freeValues = new WeakReference<>(__freeValues);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/17
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	public void close()
	{
		if (this._isOpen)
		{
			// Clear to closed
			this._isOpen = true;
			this._isSet = false;
			this._value = null;
			
			// Recycle this back in the queue
			Deque<JDWPHostValue> queue = this._freeValues.get();
			if (queue != null)
				synchronized (queue)
				{
					queue.add(this);
				}
		}
	}
	
	/**
	 * Returns the value.
	 * 
	 * @return The value.
	 * @throws IllegalStateException If no value was set or this was closed.
	 * @since 2021/03/17
	 */
	public Object get()
		throws IllegalStateException
	{
		synchronized (this)
		{
			// Must be open
			this.__checkOpen();
			
			/* {@squirreljme.error AG0j Value not set.} */
			if (!this._isSet)
				throw new IllegalStateException("AG0j");
			
			return this._value;
		}
	}
	
	/**
	 * Checks if the value has been set.
	 * 
	 * @return If this is set.
	 * @throws IllegalStateException If this is not open.
	 * @since 2021/04/30
	 */
	public boolean isSet()
		throws IllegalStateException
	{
		synchronized (this)
		{
			// Must be open
			this.__checkOpen();
			
			return this._isSet;
		}
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param __val The value to set.
	 * @throws IllegalStateException If a value is already set or this is
	 * not open.
	 * @since 2021/03/19
	 */
	public void set(Object __val)
		throws IllegalStateException
	{
		synchronized (this)
		{
			// Must be open
			this.__checkOpen();
			
			/* {@squirreljme.error AG0k Value already set.} */
			if (this._isSet)
				throw new IllegalStateException("AG0k");
			
			this._value = __val;
			this._isSet = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/30
	 */
	@Override
	public final String toString()
	{
		synchronized (this)
		{
			if (!this._isSet)
				return "<UNSET>";
			return String.valueOf(this._value);
		}
	}
	
	/**
	 * Checks if the value is open.
	 * 
	 * @throws IllegalStateException If it is not open.
	 * @since 2021/03/19
	 */
	void __checkOpen()
		throws IllegalStateException
	{
		/* {@squirreljme.error AG0i Value not open.} */
		if (!this._isOpen)
			throw new IllegalStateException("AG0i");
	}
	
	/**
	 * Resets this value.
	 * 
	 * @return {@code this}.
	 * @since 2021/03/19
	 */
	final JDWPHostValue __resetToOpen()
	{
		synchronized (this)
		{
			// Set to open
			this._isOpen = true;
			this._isSet = false;
			this._value = null;
			
			return this;
		}
	}
}
