// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;

/**
 * Wrapper for keys and values within the map.
 *
 * @param <K> The key being wrapped.
 * @since 2023/02/09
 */
final class __WeakHashMapKey__<K>
{
	/** The reference used. */
	volatile WeakReference<K> _ref;
	
	/** The hash code, for bucket mapping. */
	final int _hashCode;
	
	/**
	 * Initializes the key wrapper.
	 *
	 * @param __key The key used.
	 * @since 2023/02/09
	 */
	__WeakHashMapKey__(K __key)
	{
		// Null keys are valid!
		if (__key == null)
		{
			this._ref = null;
			this._hashCode = 0;
		}
		
		// Has a value, so use that
		else
		{
			this._ref = new WeakReference<>(__key);
			this._hashCode = __key.hashCode();
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/02/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof __WeakHashMapKey__))
			return false;
		
		// Check if it is the same, compare the ref itself because although
		// for every reference used it will be null, otherwise both sides
		// must not be null, so we can cleanly deref without a NPE
		__WeakHashMapKey__<?> o = (__WeakHashMapKey__<?>)__o;
		return this._hashCode == o._hashCode && (this._ref == o._ref ||
			(this._ref != null && o._ref != null && Objects.equals(
			this._ref.get(), o._ref.get())));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/02/09
	 */
	@Override
	public int hashCode()
	{
		return this._hashCode;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/02/09
	 */
	@Override
	public String toString()
	{
		// Defaults to null essentially
		WeakReference<K> ref = this._ref;
		if (ref == null)
			return "null";
		
		// This should not occur!
		K k = ref.get();
		if (k == null)
			throw Debugging.oops("WHMRef");
		
		// Use the representation of this object instead
		return k.toString();
	}
}
