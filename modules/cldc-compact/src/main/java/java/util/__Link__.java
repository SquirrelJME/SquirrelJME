// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Represents a single link in the linked list. This is just a basic
 * structure like object with public fields for simple access.
 *
 * @param <E> The type to store.
 * @since 2018/10/29
 */
final class __Link__<E>
{
	/** The previous link. */
	__Link__<E> _prev;
	
	/** The next link. */
	__Link__<E> _next;
	
	/** The value to store. */
	E _value;
	
	/**
	 * Initializes the new link and links into the chain.
	 *
	 * @param __prev The previous link to link in.
	 * @param __v The value to use.
	 * @param __next The next link to link in.
	 * @since 2018/10/29
	 */
	__Link__(__Link__<E> __prev, E __v, __Link__<E> __next)
	{
		// Set value first
		this._value = __v;
		
		// Link into previous chain
		this._prev = __prev;
		if (__prev != null)
			__prev._next = this;
		
		// Link into next chain
		this._next = __next;
		if (__next != null)
			__next._prev = this;
	}
}

