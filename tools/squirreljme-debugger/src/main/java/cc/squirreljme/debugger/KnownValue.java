// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

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
	 * @param __updater The updater for values.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public KnownValue(Class<T> __type, T __value,
		KnownValueUpdater<T> __updater)
		throws NullPointerException
	{
		if (__type == null || __updater == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.isArray = __type.isArray();
		this.updater = __updater;
		
		// Set to this known value, assuming null was not passed
		this._known = (__value != null);
		this._value = __value;
	}
	
	/**
	 * Drop whatever values are stored here.
	 *
	 * @since 2024/01/26
	 */
	public void drop()
	{
		synchronized (this)
		{
			this._value = null;
			this._known = false;
			
			// Notify the monitor
			this.notifyAll();
		}
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
	 * Gets the current value, if it is not yet know then updates the value
	 * synchronously, this should be used sparingly!
	 *
	 * @param __state The state to update within.
	 * @return The resultant value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/27
	 */
	public T getOrUpdateSync(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// Check to see if the value is known first
		synchronized (this)
		{
			if (this._known)
				return this._value;
		}
		
		// Run the update
		ForkJoinPool pool = ForkJoinPool.commonPool();
		ForkJoinTask<?> task = pool.submit(() -> {
				this.update(__state, (__ignored1, __ignored2) -> {});
			});
		
		// Wait for the task to complete
		task.join();
		
		// Try again!
		synchronized (this)
		{
			// Can we quickly tell it was set?
			if (this._known)
				return this._value;
			
			// Not set or known
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
	 * @param __sync The callback to execute on completion.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public void update(DebuggerState __state, KnownValueCallback<T> __sync)
		throws NullPointerException
	{
		if (__state == null || __sync == null)
			throw new NullPointerException("NARG");
		
		// We need an actual updater
		KnownValueUpdater<T> updater = this.updater;
		if (updater == null)
		{
			__sync.sync(__state, this);
			return;
		}
		
		// Call the updater
		updater.update(__state, this, __sync);
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
