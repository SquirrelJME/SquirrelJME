// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.Arrays;

/**
 * Tally tracker.
 *
 * @since 2024/01/19
 */
public final class TallyTracker
{
	/** Tally listeners. */
	private volatile TallyListener[] _listeners;
	
	/** The current value. */
	private volatile int _value;
	
	/**
	 * Adds a tally listener.
	 *
	 * @param __listener The listener to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public void addListener(TallyListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Expand array?
			TallyListener[] current = this._listeners;
			if (current != null)
				current = Arrays.copyOf(current, current.length + 1);
			else
				current = new TallyListener[1];
			
			// Set last to the new listener
			current[current.length - 1] = __listener;
			
			// Store for future usage
			this._listeners = current;
		}
	}
	
	/**
	 * Decrements the tally by one.
	 *
	 * @since 2024/01/26
	 */
	public void decrement()
	{
		// The changed values
		int oldValue;
		int newValue;
		
		// Increment it
		TallyListener[] listeners;
		synchronized (this)
		{
			// Get listeners to use
			listeners = this._listeners;
			
			// Get old and then increment to the new
			oldValue = this._value;
			if (oldValue > 0)
				newValue = oldValue - 1;
			else
				newValue = 0;
			
			// Store the new value
			this._value = newValue;
		}
		
		// Send to all listeners
		this.__notify(listeners, oldValue, newValue);
	}
	
	/**
	 * Returns the current value.
	 *
	 * @return The current tally value.
	 * @since 2024/01/19
	 */
	public int get()
	{
		synchronized (this)
		{
			return this._value;
		}
	}
	
	/**
	 * Increments the tally by one.
	 *
	 * @since 2024/01/19
	 */
	public void increment()
	{
		// The changed values
		int oldValue;
		int newValue;
		
		// Increment it
		TallyListener[] listeners;
		synchronized (this)
		{
			// Get listeners to use
			listeners = this._listeners;
			
			// Get old and then increment to the new
			oldValue = this._value;
			newValue = oldValue + 1;
			
			// Store the new value
			this._value = newValue;
		}
		
		// Send to all listeners
		this.__notify(listeners, oldValue, newValue);
	}
	
	/**
	 * Sets to the given value.
	 *
	 * @param __value The value to set to.
	 * @since 2024/01/26
	 */
	public void set(int __value)
	{
		// The changed values
		int oldValue;
		int newValue;
		
		// Increment it
		TallyListener[] listeners;
		synchronized (this)
		{
			// Get listeners to use
			listeners = this._listeners;
			
			// Get old and then increment to the new
			oldValue = this._value;
			newValue = (__value >= 0 ? __value : 0);
			
			// Store the new value
			this._value = newValue;
		}
		
		// Send to all listeners
		this.__notify(listeners, oldValue, newValue);
	}
	
	/**
	 * Notifies the given listeners.
	 *
	 * @param __listeners The listeners to notify.
	 * @param __oldValue The old value.
	 * @param __newValue The new value.
	 * @since 2024/01/26
	 */
	private void __notify(TallyListener[] __listeners,
		int __oldValue, int __newValue)
	{
		// Send to all listeners
		if (__listeners != null && __oldValue != __newValue)
			for (TallyListener listener : __listeners)
				if (listener != null)
					listener.updateTally(this, __oldValue, __newValue);
	}
}
