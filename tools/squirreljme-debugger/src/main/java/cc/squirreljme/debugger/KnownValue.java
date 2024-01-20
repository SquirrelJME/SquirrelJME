// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

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
			return this._value;
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
			this._value = this.type.cast(__v);
			this._known = true;
		}
	}
}
