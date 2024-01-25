// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.function.BiFunction;

/**
 * Stored a cached known value.
 *
 * @param <T> The type to store.
 * @since 2024/01/20
 */
public final class KnownValue<T>
{
	/** The type of value to store. */
	protected final Class<T> type;
	
	/** Is this an array? */
	protected final boolean isArray;
	
	/** The updater used for values. */
	protected final KnownValueUpdater<T> updater;
	
	/** Is the value known? */
	private volatile boolean _known;
	
	/** The current value. */
	private volatile T _value;
	
	/**
	 * Initializes the known value.
	 *
	 * @param __type The type used for the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public KnownValue(Class<T> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.isArray = __type.isArray();
		this.updater = null;
	}
	
	/**
	 * Initializes the known value with an update handler.
	 *
	 * @param __type The type used for the value.
	 * @param __updater The value updater.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public KnownValue(Class<T> __type, KnownValueUpdater<T> __updater)
		throws NullPointerException
	{
		if (__type == null || __updater == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.isArray = __type.isArray();
		this.updater = __updater;
	}
	
	/**
	 * Initializes the known value with an initial value.
	 *
	 * @param __type The type used for the value.
	 * @param __value The initial value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public KnownValue(Class<T> __type, T __value)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.isArray = __type.isArray();
		this.updater = null;
		
		// Set to this known value
		this._known = true;
		this._value = __value;
	}
	
	/**
	 * Gets the current value.
	 *
	 * @return The current value, will be {@code null} if not known.
	 * @since 2024/01/20
	 */
	public T get()
	{
		synchronized (this)
		{
			return this.__clone(this._value);
		}
	}
	
	/**
	 * Gets the type and casts it to the specified class.
	 *
	 * @param <X> The type to cast to.
	 * @param __type The type to cast to.
	 * @return The resultant value.
	 * @throws ClassCastException If the type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public <X> X get(Class<X> __type)
		throws ClassCastException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return __type.cast(this.get());
	}
	
	/**
	 * Gets the current value, or a default.
	 *
	 * @param __default The default value if there is no known one.
	 * @return The current value, will be {@code __default} if not known.
	 * @since 2024/01/20
	 */
	public T getOrDefault(T __default)
	{
		synchronized (this)
		{
			if (this._known)
				return this.__clone(this._value);
			return __default;
		}
	}
	
	/**
	 * Gets the given value or calls the updater if it is not known.
	 *
	 * @param __state The state to use.
	 * @return The known value or the value when updated.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public T getOrUpdate(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// If the value is already known, then get it
		if (this.isKnown())
			return this.get();
		
		// Otherwise call the updater
		return this.update(__state);
	}
	
	/**
	 * Gets or wait for the value to appear, using the default timeout value.
	 *
	 * @return The resultant value or {@code null} if not known due to timeout.
	 * @since 2024/01/22
	 */
	public T getOrWait()
	{
		return this.getOrWait(Utils.TIMEOUT);
	}
	
	/**
	 * Gets or wait for the value to appear.
	 *
	 * @param __waitMs The number of milliseconds to wait.
	 * @return The resultant value or {@code null} if not known due to timeout.
	 * @since 2024/01/22
	 */
	public T getOrWait(int __waitMs)
	{
		synchronized (this)
		{
			// Do we immediately know the value?
			if (this._known)
				return this.__clone(this._value);
			
			// Wait
			try
			{
				this.wait(__waitMs);
			}
			catch (InterruptedException ignored)
			{
			}
			
			// Return the value, assuming it is known
			if (this._known)
				return this.__clone(this._value);
			
			// Otherwise null
			return null;
		}
	}
	
	/**
	 * Is there a known value here?
	 *
	 * @return If the value is known.
	 * @since 2024/01/20
	 */
	public boolean isKnown()
	{
		synchronized (this)
		{
			return this._known;
		}
	}
	
	/**
	 * Sets the value of this.
	 *
	 * @param __v The value to set.
	 * @throws ClassCastException If the value type is not correct.
	 * @since 2024/01/20
	 */
	public void set(T __v)
		throws ClassCastException
	{
		synchronized (this)
		{
			this._value = this.__clone(this.type.cast(__v));
			this._known = true;
			
			// Notify on self for anything waiting on this
			this.notifyAll();
		}
	}
	
	/**
	 * Attempts to update the value and returns it.
	 *
	 * @param __state The state for packet control.
	 * @return The current and updated value, if it is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public T update(DebuggerState __state)
		throws NullPointerException
	{
		return this.update(__state, null);
	}
	
	/**
	 * Attempts to update the value and returns it.
	 *
	 * @param __state The state for packet control.
	 * @param __default The default value if not known.
	 * @return The current and updated value, if it is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public T update(DebuggerState __state, T __default)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// We need an actual updater
		KnownValueUpdater<T> updater = this.updater;
		if (updater == null)
			return this.get();
		
		// Call the updater
		updater.update(__state, this);
		
		// Return the value
		return this.getOrDefault(__default);
	}
	
	/**
	 * Potentially clones the value.
	 *
	 * @param __value The value to potentially clone.
	 * @return The resultant value.
	 * @since 2024/01/22
	 */
	@SuppressWarnings("unchecked")
	private T __clone(T __value)
	{
		if (__value == null || !this.isArray)
			return __value;
		
		return (T)((Object[])__value).clone();
	}
}
